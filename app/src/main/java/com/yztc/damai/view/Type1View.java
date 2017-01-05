package com.yztc.damai.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yztc.damai.ui.recommend.TypeViewBean;
import com.yztc.damai.ui.recommend.TypeViewDataBean;

import java.util.List;

/**
 * Created by wanggang on 2016/12/15.
 */

public class Type1View extends TypeContainerView {


    public Type1View(Context context) {
        super(context);
    }

    public Type1View(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setData(TypeViewBean data) {
        super.setData(data);
    }

    @Override
    protected void fillTypeView(TypeViewBean data) {
        List<TypeViewDataBean> dataList = data.getList();
        if (dataList.size() > 0) {
            for (int i = 0; i < dataList.size(); i++) {
                ImageView img = new TypeViewImage(getContext());
                Glide.with(getContext()).load(dataList.get(i).getPicUrl()).into(img);
                addView(img);
            }
        }
    }

}

