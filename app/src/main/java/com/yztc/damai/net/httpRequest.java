package com.yztc.damai.net;

import com.yztc.core.net.HttpBase;

import retrofit2.Retrofit;

/**
 * Created by wanggang on 2016/12/19.
 */

public class HttpRequest extends HttpBase {

    private Retrofit retorfit;
    private DaMaiApi daMaiApi;

    static HttpRequest instance = null;

    private HttpRequest() {
        DMRetrofitHepler hepler = new DMRetrofitHepler();
        retorfit = hepler.getRetorfit();
    }

    public static HttpRequest getInstance() {
        if (instance == null) {
            synchronized (HttpRequest.class) {
                if (instance == null) {
                    instance = new HttpRequest();
                }
            }
        }
        return instance;
    }


    public DaMaiApi getApi() {
        if (daMaiApi == null)
            daMaiApi = retorfit.create(DaMaiApi.class);
        return daMaiApi;
    }


//    public Observable loadData(String userId) {
//        return daMaiApi.loadData("userId")
//                .delay(1, TimeUnit.SECONDS)//延迟1S执行网络操作
//                .compose(schedulersTransformer())//线程调度
//                .compose(transformer());//处理 baseResponse
//    }

}
