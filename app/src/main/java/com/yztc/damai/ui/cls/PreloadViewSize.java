package com.yztc.damai.ui.cls;

import android.util.Log;

import com.bumptech.glide.ListPreloader;

/**
 * Created by wanggang on 2017/1/5.
 */


public abstract class PreloadViewSize implements ListPreloader.PreloadSizeProvider<ClassBean> {


    public abstract int[] getViewSize();


    @Override
    public int[] getPreloadSize(ClassBean item, int adapterPosition, int perItemPosition) {
        Log.i("==========", "adapterPosition:" + adapterPosition + ",perItemPosition:" + perItemPosition);
        return getViewSize();
    }
}
