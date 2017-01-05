package com.yztc.core.glide;

import android.util.Log;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.util.ContentLengthInputStream;

import java.io.InputStream;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Fetches an {@link InputStream} using the okhttp library.
 */
public class OkHttpStreamFetcher implements DataFetcher<InputStream> {
    private static final String TAG = "OkHttpFetcher";
    private final Call.Factory client;
    private final GlideUrl url;
    private ResponseBody responseBody;
    private volatile Call call;

    public OkHttpStreamFetcher(Call.Factory client, GlideUrl url) {
        this.client = client;
        this.url = url;
    }


    //用于下载
    @Override
    public InputStream loadData(Priority priority) throws Exception {

        Request.Builder requestBuilder = new Request.Builder().url(url.toStringUrl());
        for (Map.Entry<String, String> headerEntry : url.getHeaders().entrySet()) {
            String key = headerEntry.getKey();
            requestBuilder.addHeader(key, headerEntry.getValue());
        }
        Request request = requestBuilder.build();

        call = client.newCall(request);
        Response response = call.execute(); //同步

        responseBody = response.body();
        if (response.isSuccessful()) {
            long contentLength = response.body().contentLength();
            return ContentLengthInputStream.obtain(responseBody.byteStream(), contentLength);
        } else if (Log.isLoggable(TAG, Log.DEBUG)) {
            Log.d(TAG, "OkHttp got error response: " + response.code() + ", " + response.message());
        }
        return null;
    }

    @Override
    public String getId() {
        return url.getCacheKey();
    }

    @Override
    public void cleanup() {
        if (responseBody != null) {
            responseBody.close();
        }
    }

    @Override
    public void cancel() {
        Call local = call;
        if (local != null) {
            local.cancel();
        }
    }


}
