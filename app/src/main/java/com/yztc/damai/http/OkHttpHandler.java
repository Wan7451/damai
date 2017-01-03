package com.yztc.damai.http;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.yztc.core.net.HttpLoggingInterceptor;
import com.yztc.core.net.RewriteCacheControlInterceptor;
import com.yztc.core.utils.FileUtils;
import com.yztc.damai.config.NetConfig;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by wanggang on 2017/1/3.
 */

public class OkHttpHandler extends HttpHandler {


    //测试服务器
    //线上服务器

    private static OkHttpHandler instance;
    private OkHttpClient client;
    private Handler handler;

    public static OkHttpHandler getInstance() {
        if (instance == null) {
            synchronized (OkHttpHandler.class) {
                if (instance == null) {
                    instance = new OkHttpHandler();
                }
            }
        }
        return instance;
    }


    private OkHttpHandler() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.i("=============", message);
            }
        });
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        RewriteCacheControlInterceptor cacheInterceptor = new RewriteCacheControlInterceptor();


//        BasicParamsInterceptor basicInterceptor = new BasicParamsInterceptor.Builder()
//                .addHeaderParam("Connection", "Keep-Alive")
//                .addHeaderParam("Accept-Encoding", "gzip")
//                .addQueryParam("osType", NetConfig.osType)
//                .addQueryParam("channel_from", NetConfig.channel_from)
//                .addQueryParam("source", NetConfig.source)
//                .addQueryParam("version", NetConfig.version)
//                .addQueryParam("appType", NetConfig.appType)
//                .build();


        File cacheFile = FileUtils.getHttpCacheFile();

        final Cache cache = new Cache(cacheFile, 1024 * 1024 * 10);

        client = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .addInterceptor(interceptor)//Log拦截器
                .addInterceptor(cacheInterceptor)//缓存拦截器
                .addNetworkInterceptor(cacheInterceptor)
//                .addNetworkInterceptor(basicInterceptor)
                .cache(cache)//配置缓存路径
                .build();
        //保证回调在主线程执行
        handler = new Handler(Looper.getMainLooper());

    }

    @Override
    public void get(String path, HashMap<String, String> params, NetResponse callback) {
        get(NetConfig.BASE_URL, path, params, callback);
    }


    @Override
    public void get(String baseUrl, String path, HashMap<String, String> params, final NetResponse callback) {
        StringBuilder url = new StringBuilder();
        url.append(baseUrl);
        url.append(path);
        url.append("?");
        url.append(buildParams(params));

        final Request request = new Request.Builder()
                .url(url.toString())
//                .addHeader("Connection", "Keep-Alive")
//                .addHeader("Accept-Encoding", "gzip")
                .build();

        //下载  同步
        //普通请求  异步
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (callback != null) {
                            callback.onError(e.getMessage());
                        }
                    }
                });
            }

            @Override
            public void onResponse(final Call call, final okhttp3.Response response) throws IOException {

                final String data = new String(response.body().bytes(), "UTF-8");
                Log.i(">>>>>>>>>", call.request().url().toString());
                Log.i(">>>>>>>>>", data);

                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        if (response.isSuccessful()) {
                            if (callback != null) {
                                //成功
                                callback.onResponse(data);
                            }
                        } else {
                            if (callback != null) {
                                //失败
                                callback.onError(response.message());
                            }
                        }

                    }
                });
            }
        });
    }

    @Override
    public void destory() {
        client.dispatcher().cancelAll();
    }
}
