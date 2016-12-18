package com.yztc.damai.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.yztc.core.utils.DensityUtil;
import com.yztc.core.views.DivierView;
import com.yztc.damai.ui.recommend.TypeViewBean;

/**
 * Created by wanggang on 2016/12/16.
 */

public abstract class TypeContainerView extends LinearLayout {

    public TypeContainerView(Context context) {
        super(context);
    }

    public TypeContainerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected void setData(TypeViewBean data){
        setOrientation(VERTICAL);
        int px = DensityUtil.dip2px(getContext(), 5);
        setPadding(0,px,0,px);

        if(!TextUtils.isEmpty(data.getTitle())){
            TypeViewTitle title=new TypeViewTitle(getContext());
            title.setData(data.getTitle(),data.getSubTitle());
            addView(new DivierView(getContext()));
            addView(title);
        }
        //内容
        fillTypeView(data);

    }

    protected abstract void fillTypeView(TypeViewBean data);
}
