package com.yztc.core.utils;


import android.widget.Toast;

import com.yztc.core.App;
import com.yztc.core.views.toastcompat.ToastCompat;


/**
 * Created by wanggang on 2016/12/12.
 *
 * 解决Toast重叠问题
 *
 */

public class ToastUtils {




    public static void show(String text){
        ToastCompat.makeText(App.getContext(), text,
                Toast.LENGTH_SHORT).show();
    }

    public static void showLong(String text){
        ToastCompat.makeText(App.getContext(), text,
                Toast.LENGTH_LONG).show();
    }

    private ToastUtils(){}

}
