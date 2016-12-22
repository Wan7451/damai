package com.yztc.core.utils;

import android.util.Log;

/**
 * Created by wanggang on 2016/12/21.
 */

public class LogUtils {

    public static final boolean IS_DEBUG = true;
    private static final String TAG = ">>>>>>>>";

    public static void d(String tag, String msg) {
        if (IS_DEBUG)
            Log.d(tag, msg);
    }

    public static void i(String tag, String msg) {
        if (IS_DEBUG)
            Log.i(tag, msg);
    }

    public static void e(String tag, String msg) {
        if (IS_DEBUG)
            Log.e(tag, msg);
    }


    public static void d(String msg) {
        if (IS_DEBUG)
            Log.d(TAG, msg);
    }

    public static void i(String msg) {
        if (IS_DEBUG)
            Log.i(TAG, msg);
    }

    public static void e(String msg) {
        if (IS_DEBUG)
            Log.e(TAG, msg);
    }

}
