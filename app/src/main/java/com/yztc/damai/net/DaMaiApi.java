package com.yztc.damai.net;

import com.yztc.core.net.BaseResponse;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by wanggang on 2016/12/19.
 */

public interface DaMaiApi {

    @FormUrlEncoded
    @POST("getList")
    Observable<BaseResponse<String>> loadData(
            @Field("userID") String userID
    );
}
