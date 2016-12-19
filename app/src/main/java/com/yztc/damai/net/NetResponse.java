package com.yztc.damai.net;

/**
 * Created by wanggang on 2016/12/12.
 */

public interface NetResponse {
     void onResponse(String response);
     void onError(String erroe);
}
