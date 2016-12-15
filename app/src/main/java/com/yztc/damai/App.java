package com.yztc.damai;

import android.app.Application;
import android.content.Context;

import com.yztc.damai.image.ImageLoader;
import com.yztc.damai.net.NetDataCache;

/**
 * Created by wanggang on 2016/12/12.
 */

public class App extends Application {

    //App  Context
    //Activity、Service Context
    //1 全局
    //2 生命周期长
    private static App context;

    @Override
    public void onCreate() {
        super.onCreate();
        context=this;
    }

    public static App getContext(){
        return context;
    }

    //结束
    public void onDestory(){
        ImageLoader.getInstance().onDestroy();
        NetDataCache.getInstance().onDestroy();
    }
}
