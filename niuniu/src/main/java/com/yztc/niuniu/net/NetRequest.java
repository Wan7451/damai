package com.yztc.niuniu.net;

import android.util.Log;

import com.yztc.core.net.HttpLoggingInterceptor;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by wanggang on 2017/2/14.
 */

public class NetRequest {

    private static NetRequest instance;

    private OkHttpClient client;
    private IAvApi api;
    private ISihuApi sihuApi;
    private IMaomiApi maomiApi;

    public static NetRequest getInstance() {
        if (instance == null) {
            synchronized (NetRequest.class) {
                if (instance == null) {
                    instance = new NetRequest();
                }
            }
        }
        return instance;
    }

    private NetRequest() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.i("===========>", message);
            }
        });
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .dns(new HttpDns())
                .build();
    }


    public IAvApi getApi() {
        if (api == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .client(client)
                    .baseUrl(IAvApi.BASE_URL)
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build();
            api = retrofit.create(IAvApi.class);
        }
        return api;
    }

    public ISihuApi getSihuApi() {
        if (sihuApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .client(client)
                    .baseUrl(ISihuApi.BASE_URL)
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build();
            sihuApi = retrofit.create(ISihuApi.class);
        }
        return sihuApi;
    }

    public IMaomiApi getMaomiApi() {
        if (maomiApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .client(client)
                    .baseUrl(IMaomiApi.BASE_URL)
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build();

            maomiApi = retrofit.create(IMaomiApi.class);
        }
        return maomiApi;
    }

}
