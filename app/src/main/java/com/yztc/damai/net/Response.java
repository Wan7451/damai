package com.yztc.damai.net;

import com.yztc.core.net.BaseResponse;

/**
 * Created by wanggang on 2016/12/20.
 */

public class Response<T> implements BaseResponse {
    @Override
    public boolean isOk() {
        return false;
    }

    @Override
    public T getData() {
        return null;
    }

    @Override
    public String erroeMsg() {
        return null;
    }
}
