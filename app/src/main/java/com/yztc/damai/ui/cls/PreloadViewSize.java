package com.yztc.damai.ui.cls;

import com.bumptech.glide.ListPreloader;

/**
 * Created by wanggang on 2017/1/5.
 */


public abstract class PreloadViewSize implements ListPreloader.PreloadSizeProvider<ClassBean> {


    public abstract int[] getViewSize();


    @Override
    public int[] getPreloadSize(ClassBean item, int adapterPosition, int perItemPosition) {
        return getViewSize();
    }
}
