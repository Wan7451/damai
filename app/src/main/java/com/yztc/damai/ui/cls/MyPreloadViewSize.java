package com.yztc.damai.ui.cls;

import com.bumptech.glide.ListPreloader;

/**
 * Created by wanggang on 2017/1/6.
 */

public class MyPreloadViewSize implements ListPreloader.PreloadSizeProvider<ClassBean> {
    @Override
    public int[] getPreloadSize(ClassBean item, int adapterPosition, int perItemPosition) {
        return new int[]{100, 100};
    }
}
