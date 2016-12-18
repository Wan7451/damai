package com.yztc.damai.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yztc.core.utils.DensityUtil;

/**
 * Created by wanggang on 2016/12/16.
 */

public class TypeViewImage extends ImageView {
    public TypeViewImage(Context context) {
        super(context);
        init();
    }

    public TypeViewImage(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    private void init(){
        ViewGroup.LayoutParams lp=new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                DensityUtil.dip2px(getContext(),120)
        );
        setScaleType(ImageView.ScaleType.CENTER_CROP);
        setLayoutParams(lp);
    }
}
