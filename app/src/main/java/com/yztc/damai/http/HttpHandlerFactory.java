package com.yztc.damai.http;

/**
 * Created by wanggang on 2017/1/3.
 */

public class HttpHandlerFactory {

    public static HttpHandler createOkhttpClient() {
        return OkHttpHandler.getInstance();
    }


    public static HttpHandler creataHttpUrlClient() {
        return HttpUrlConnectionHandler.getInstance();
    }


    public static HttpHandler getHttpHandler() {
        return createOkhttpClient();
    }

}
