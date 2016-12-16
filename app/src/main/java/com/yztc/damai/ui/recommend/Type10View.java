package com.yztc.damai.ui.recommend;

import android.content.Context;
import android.support.v7.widget.GridLayout;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;

import com.yztc.damai.image.ImageLoader;
import com.yztc.damai.utils.DensityUtil;
import com.yztc.damai.view.DivierView;

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
        image.getLayoutParams().height=DensityUtil.dip2px(getContext(),100);
        ImageLoader.getInstance().loadImages(image,list.get(i).getPicUrl(),false);
        addView(image);
        }
    }
}
