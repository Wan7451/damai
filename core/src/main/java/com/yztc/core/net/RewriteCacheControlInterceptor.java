package com.yztc.core.net;


import com.yztc.core.utils.NetUtils;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by wanggang on 2016/11/2.
 * <p>
 * 缓存网络数据的 拦截器
 * POST方法没有缓存；
 * 头部Cache-Control设为max-age=0时则不会使用缓存
 * 而请求服务器；
 * 为if-only-cache时只查询缓存而不会请求服务器，
 * max-stale可以配合设置缓存失效时间
 */

public class RewriteCacheControlInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (!NetUtils.isConnected()) {
            request = request.newBuilder()
                    //强制本地取缓存
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
        }
        Response originalResponse = chain.proceed(request);
        if (NetUtils.isConnected()) {
            //有网的时候读接口上的@Headers里的配置，你可以在这里进行统一的设置
            return originalResponse.newBuilder()
                    .removeHeader("Pragma")
                    .removeHeader("Cache-Control")
                    .header("Cache-Control", "public, max-age=3600")
                    .build();
        } else {
            int maxStale = 60 * 60;
            return originalResponse.newBuilder()
                    .removeHeader("Pragma")
                    .removeHeader("Cache-Control")
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                    .build();
        }

    }
}
