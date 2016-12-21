package com.yztc.core.utils;


import com.yztc.core.App;
import com.yztc.core.views.Toast;


/**
 * Created by wanggang on 2016/12/12.
 *
 * 解决Toast重叠问题
 *
 */

public class ToastUtils {




    public static void show(String text){
        Toast.init(App.getContext());
        Toast.show(text);
    }

    public static void showLong(String text){
        Toast.init(App.getContext());
        Toast.show(text, Toast.LENGTH_LONG);
    }

    private ToastUtils(){}

}
