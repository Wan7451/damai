package com.yztc.damai;

import com.yztc.damai.http.HttpHandlerFactory;

/**
 * Created by wanggang on 2016/12/12.
 */

public class App extends com.yztc.core.App {


    @Override
    public void onDestory() {
        super.onDestory();
        HttpHandlerFactory.getHttpHandler().destory();
    }
}
