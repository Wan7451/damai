package com.yztc.damai.ui.recommend;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.yztc.damai.R;
import com.yztc.damai.image.ImageLoader;
import com.yztc.damai.utils.DensityUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

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
                ImageLoader.getInstance().loadImages(img, dataList.get(i).getPicUrl(), false);
                addView(img);
            }
        }
    }

}

