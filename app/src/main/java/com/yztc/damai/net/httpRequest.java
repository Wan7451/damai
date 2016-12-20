package com.yztc.damai.net;

import com.yztc.core.net.BasicParamsInterceptor;
import com.yztc.core.net.HttpBase;
import com.yztc.core.net.RetrofitProvider;
import com.yztc.damai.config.NetConfig;

import java.util.concurrent.TimeUnit;

import retrofit2.Retrofit;
import rx.Observable;

/**
 * Created by wanggang on 2016/12/19.
 */

public class HttpRequest extends HttpBase {

    private Retrofit retorfit;
    private DaMaiApi daMaiApi;

    static HttpRequest instance = null;

    private HttpRequest() {

        BasicParamsInterceptor interceptor = new
                BasicParamsInterceptor.Builder()
                .addParam("key1", "v1")
                .build();

        RetrofitProvider hepler = new RetrofitProvider.Builder()
                .baseUrl(NetConfig.BASE_URL)
                .isGetAutoCache(true)
                .isShowLog(true)
                .basicParamsInterceptor(interceptor)
                .build();
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


    public Observable loadData(String userId) {
        return daMaiApi.loadData("userId")
                .delay(1, TimeUnit.SECONDS)//延迟1S执行网络操作
                .compose(schedulersTransformer())//线程调度
                .compose(transformer());//处理 baseResponse
    }

}
