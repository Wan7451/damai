package com.yztc.core;

import android.app.Application;

import com.yztc.core.image.ImageLoader;
import com.yztc.core.net.NetDataCache;

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
