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
public abstract class RetrofitProvider {


    public static final String TAG = "======";

    protected static Retrofit retrofit;

    protected RetrofitProvider() {
        retrofit = new Retrofit.Builder()
                .baseUrl(getBASE_URL())
                .client(client())
                //.serializeNulls  解决 gson  null值不进行转换问题
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().serializeNulls().create()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }


    public Retrofit getRetorfit() {
        return retrofit;
    }


    private OkHttpClient client() {
        //缓存路径
        File file = FileUtils.getHttpCacheFile();
        Cache cache = new Cache(file, 20 * 1024 * 1024);

        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        if (isAutoCache()) {
            //本地缓存拦截器
            builder.addInterceptor(new RewriteCacheControlInterceptor());
            builder.addNetworkInterceptor(new RewriteCacheControlInterceptor());
        }
        if (isShowLog()) {
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
        BasicParamsInterceptor basicParamsInterceptor = addBasicParams();
        if (null != basicParamsInterceptor) {
            builder.addNetworkInterceptor(basicParamsInterceptor);
        }


        OkHttpClient client = builder(builder)
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

    /**
     * Base URL
     *
     * @return
     */
    @NonNull
    protected abstract String getBASE_URL();

    /**
     * 公共参数
     *
     * @return
     */
    protected abstract BasicParamsInterceptor addBasicParams();

    /**
     * 其他配置
     *
     * @param builder
     * @return
     */
    @NonNull
    protected OkHttpClient.Builder builder(OkHttpClient.Builder builder) {
        return builder;
    }

    /**
     * 自动缓存
     *
     * @return
     */
    protected boolean isAutoCache() {
        return true;
    }

    /**
     * 是否显示日志
     *
     * @return
     */
    protected boolean isShowLog() {
        return true;
    }

}