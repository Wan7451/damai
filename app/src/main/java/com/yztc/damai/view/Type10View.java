package com.yztc.damai.view;

import android.content.Context;
import android.util.AttributeSet;

import com.yztc.core.image.ImageLoader;
import com.yztc.core.utils.DensityUtil;
import com.yztc.damai.ui.recommend.TypeViewBean;
import com.yztc.damai.ui.recommend.TypeViewDataBean;

import java.util.List;

/**
 * Created by wanggang on 2016/12/15.
 */

public class Type10View extends TypeContainerView {


    public Type10View(Context context) {
        super(context);
    }

    public Type10View(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setData(TypeViewBean data) {
        super.setData(data);
    }

    @Override
    protected void fillTypeView(TypeViewBean data) {

        List<TypeViewDataBean> list = data.getList();

        for (int i = 0; i < list.size(); i++) {
        TypeViewImage image=new TypeViewImage(getContext());
        image.getLayoutParams().height= DensityUtil.dip2px(getContext(),100);
        ImageLoader.getInstance().loadImages(image,list.get(i).getPicUrl(),false);
        addView(image);
        }
    }
}
