package com.yztc.damai.utils;

import android.widget.Toast;

import com.yztc.damai.App;

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

    private ToastUtils(){}

    private static Toast init(){
        if(toast==null){
            synchronized (ToastUtils.class){
                if(toast==null){
                    toast=Toast.makeText(App.context,"",Toast.LENGTH_SHORT);
                }
            }
        }
        return toast;
    }
}
