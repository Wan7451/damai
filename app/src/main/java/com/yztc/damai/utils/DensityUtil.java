package com.yztc.damai.utils;

import android.content.Context;

/**
 * Created by wanggang on 2016/12/15.
 */

public class DensityUtil {

    public static int dip2px(Context context,float dipValue){
        final float scale=context.getResources().getDisplayMetrics().densityDpi;
        return (int)(dipValue*(scale/160)+0.5f);
    }

    public static int px2dp(Context context,float pxValue){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)((pxValue*160)/scale+0.5f);
    }
}
