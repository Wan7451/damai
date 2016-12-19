package com.yztc.damai.net;

import android.support.annotation.NonNull;

import com.yztc.core.net.BasicParamsInterceptor;
import com.yztc.core.net.RetrofitProvider;
import com.yztc.damai.config.NetConfig;

/**
 * Created by wanggang on 2016/12/19.
 */

public class DMRetrofitHepler extends RetrofitProvider {

    DMRetrofitHepler() {
        super();
    }

    @NonNull
    @Override
    protected String getBASE_URL() {
        return NetConfig.BASE_URL;
    }

    @Override
    protected BasicParamsInterceptor addBasicParams() {
        //公共参数
        return new BasicParamsInterceptor.Builder()
                //.addParam("key1", "value1")
                .build();
    }
}
