package com.yztc.core.glide;

import android.content.Context;

import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.GenericLoaderFactory;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;

import java.io.InputStream;

import okhttp3.Call;
import okhttp3.OkHttpClient;


//进行加载图片
public class OkHttpUrlLoader implements ModelLoader<GlideUrl, InputStream> {

    private final Call.Factory client;

    private OkHttpUrlLoader(Call.Factory client) {
        this.client = client;
    }


    @Override
    public DataFetcher<InputStream> getResourceFetcher(GlideUrl model, int width, int height) {
        //OkHttpStreamFetcher  进行下载
        return new OkHttpStreamFetcher(client, model);
    }

    /**
     * The default factory for {@link OkHttpUrlLoader}s.
     */
    //ModelLoaderFactory   用于生成ModelLoader
    public static class Factory implements ModelLoaderFactory<GlideUrl, InputStream> {
        private static volatile Call.Factory internalClient;
        private Call.Factory client;

        private static Call.Factory getInternalClient() {
            if (internalClient == null) {
                synchronized (Factory.class) {
                    if (internalClient == null) {
                        internalClient = new OkHttpClient.Builder()
                                .build();

                    }
                }
            }
            return internalClient;
        }

        public Factory() {
            this(getInternalClient());
        }

        private Factory(Call.Factory client) {
            this.client = client;
        }


        //用于创建 ModelLoader  加载图片的时候进程调用
        @Override
        public ModelLoader<GlideUrl, InputStream> build(Context context, GenericLoaderFactory factories) {
            return new OkHttpUrlLoader(client);
        }

        @Override
        public void teardown() {
            // Do nothing, this instance doesn't own the client.
        }
    }
}
