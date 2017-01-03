package com.yztc.damai.http;

import java.util.HashMap;

/**
 * Created by wanggang on 2017/1/3.
 */

public abstract class HttpHandler {

    public abstract void get(String path, HashMap<String, String> params, NetResponse callback);

    public abstract void get(String baseUrl, String path, HashMap<String, String> params, NetResponse callback);

    public abstract void destory();
}
