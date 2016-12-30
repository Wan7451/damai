package com.yztc.core.net;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.GsonBuilder;
import com.yztc.core.utils.FileUtils;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by wanggang on 2016/11/2.
 * <p>
 * 初始化 Retrofit  Okhttp
 */
public class RetrofitProvider {


    public static final String TAG = "======";

    protected static Retrofit retrofit;

    private RetrofitProvider() {
    }

    private RetrofitProvider(Builder builder) {
        if (retrofit != null)
            return;
        retrofit = new Retrofit.Builder()
                .baseUrl(builder.baseUrl)
                .client(client(builder))
                //.serializeNulls  解决 gson  null值不进行转换问题
                .addConverterFactory(GsonConverterFactory.create(
                        new GsonBuilder()
                                .serializeNulls()
                                .setDateFormat("yyyy-MM-dd hh:mm:ss")
                                .create()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }


    public Retrofit getRetorfit() {
        return retrofit;
    }


    private OkHttpClient client(Builder options) {
        //缓存路径
        File file = FileUtils.getHttpCacheFile();
        Cache cache = new Cache(file, 20 * 1024 * 1024);

        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        if (options.isGetAutoCache) {
            //本地缓存拦截器
            builder.addInterceptor(new RewriteCacheControlInterceptor());
            builder.addNetworkInterceptor(new RewriteCacheControlInterceptor());
        }
        if (options.isShowLog) {
            //打印网络日志
            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                @Override
                public void log(String message) {
                    Log.i(TAG, message);
                }
            });
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            //打印Log 拦截器
            builder.addInterceptor(httpLoggingInterceptor);
        }

        //公共参数

        if (null != options.interceptor) {
            builder.addNetworkInterceptor(options.interceptor);
        }

        OkHttpClient client = builder
                //SSLSocket
//                .sslSocketFactory(
//                        SSLHelper.getSSLSocketFactory(App.getContext()),
//                        SSLHelper.getTrustManager())
                //hostname 验证
//                .hostnameVerifier(SSLHelper.getHostnameVerifier())
                //baseparams 拦截器
                //.addInterceptor(basicParamsInterceptor)

                .cache(cache)
                .connectTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();

        return client;
    }


    public static class Builder {
        private boolean isGetAutoCache = true;
        private boolean isShowLog = true;
        private String baseUrl;
        private BasicParamsInterceptor interceptor;

        public Builder isGetAutoCache(boolean isGetAutoCache) {
            this.isGetAutoCache = isGetAutoCache;
            return this;
        }

        public Builder isShowLog(boolean isShowLog) {
            this.isShowLog = isShowLog;
            return this;
        }

        public Builder basicParamsInterceptor(BasicParamsInterceptor interceptor) {
            this.interceptor = interceptor;
            return this;
        }

        public Builder baseUrl(@NonNull String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public RetrofitProvider build() {
            RetrofitProvider retrofit = new RetrofitProvider(this);
            return retrofit;
        }

    }


}