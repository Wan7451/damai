package com.yztc.damai.http;

import android.support.annotation.NonNull;

import com.yztc.damai.config.NetConfig;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by wanggang on 2017/1/3.
 */

public abstract class HttpHandler {


    private HashMap<String, String> getBasicParams() {
        HashMap<String, String> basic = new HashMap<>();
        basic.put("osType", NetConfig.osType);
        basic.put("channel_from", NetConfig.channel_from);
        basic.put("source", NetConfig.source);
        basic.put("version", NetConfig.version);
        basic.put("appType", NetConfig.appType);

        return basic;
    }

    protected String buildParams(@NonNull HashMap<String, String> maps) {
        //公共参数
        HashMap<String, String> basicParams = getBasicParams();
        //业务参数
        if (maps != null && maps.size() > 0) {
            basicParams.putAll(maps);
        }
        StringBuilder builder = new StringBuilder();
        Iterator<Map.Entry<String, String>> iterator =
                basicParams.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> next = iterator.next();
            builder.append(next.getKey());
            builder.append("=");
            builder.append(next.getValue());
            builder.append("&");
        }
        if (builder.length() > 0) {
            builder.setLength(builder.length() - 1);
        }

        return builder.toString();
    }


    public abstract void get(String path, HashMap<String, String> params, NetResponse callback);

    public abstract void get(String baseUrl, String path, HashMap<String, String> params, NetResponse callback);

    public abstract void destory();
}
