package com.yztc.damai;

import android.app.Application;
import android.content.Context;

/**
 * Created by wanggang on 2016/12/12.
 */

public class App extends Application {

    //App  Context
    //Activity、Service Context
    //1 全局
    //2 生命周期长
    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context=this;
    }
}
