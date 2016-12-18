package com.yztc.core.utils;

import android.widget.Toast;

import com.yztc.core.App;


/**
 * Created by wanggang on 2016/12/12.
 *
 * 解决Toast重叠问题
 *
 */

public class ToastUtils {


    private static  Toast toast=null;


    public static void show(String text){
        init();
        toast.setText(text);
        toast.show();
    }

    public static void showLong(String text){
        init();
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setText(text);
        toast.show();
    }

    private ToastUtils(){}

    private static Toast init(){
        if(toast==null){
            synchronized (ToastUtils.class){
                if(toast==null){
                    toast=Toast.makeText(App.getContext(),"",Toast.LENGTH_SHORT);
                }
            }
        }
        return toast;
    }
}
