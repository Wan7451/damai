package com.yztc.niuniu.net;


import com.yztc.niuniu.App;
import com.yztc.niuniu.util.NetUtils;

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


    //设缓存有效期为两天
    protected static final long CACHE_STALE_SEC = 60 * 60 * 24 * 2;
    //查询缓存的Cache-Control设置，为if-only-cache时只查询缓存而不会请求服务器，max-stale可以配合设置缓存失效时间
    protected static final String CACHE_CONTROL_CACHE = "only-if-cached, max-stale=" + CACHE_STALE_SEC;
    //查询网络的Cache-Control设置，头部Cache-Control设为max-age=0时则不会使用缓存而请求服务器
    protected static final String CACHE_CONTROL_NETWORK = "max-age=0";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (!NetUtils.isConnected(App.getContext())) {
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE).build();
        }
        Response originalResponse = chain.proceed(request);
        if (NetUtils.isConnected(App.getContext())) {
            //有网的时候读接口上的@Headers里的配置，你可以在这里进行统一的设置
            String cacheControl = request.cacheControl().toString();
            return originalResponse.newBuilder()
                    .header("Cache-Control", cacheControl)
                    .removeHeader("Pragma").build();
        } else {
            return originalResponse.newBuilder().header("Cache-Control",
                    "public, only-if-cached," + CACHE_STALE_SEC)
                    .removeHeader("Pragma").build();
        }


    }
}
