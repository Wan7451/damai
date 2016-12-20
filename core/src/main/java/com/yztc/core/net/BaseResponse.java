package com.yztc.core.net;

/**
 * Created by wanggang on 2016/11/4.
 * 服务器返回数据的基类
 */

public interface BaseResponse<T> {
    boolean isOk();

    T getData();

    String erroeMsg();

}
