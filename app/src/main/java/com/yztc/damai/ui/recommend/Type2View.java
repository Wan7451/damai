package com.yztc.damai.ui.recommend;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.GridLayout;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.yztc.damai.image.ImageLoader;
import com.yztc.damai.utils.DensityUtil;

import java.util.List;

/**
 * Created by wanggang on 2016/12/15.
 */

public class Type2View extends TypeContainerView {

    GridLayout typeGridContainer;

    public Type2View(Context context) {
        super(context);
    }

    public Type2View(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setData(TypeViewBean data) {
        super.setData(data);
    }

    @Override
    protected void fillTypeView(TypeViewBean data) {
        typeGridContainer=new GridLayout(getContext());

        addView(typeGridContainer);

        int widthPixels = getResources().getDisplayMetrics().widthPixels;


        typeGridContainer.setColumnCount(2);
        List<TypeViewDataBean> dataList = data.getList();
        if (dataList.size() > 0) {
            int x = 0;
            int y = 0;
            for (int i = 0; i < dataList.size(); i++) {

                ImageView img=new ImageView(getContext());

                ImageLoader.getInstance().loadImages(img, dataList.get(i).getPicUrl(), false);

                GridLayout.Spec rowSpec = GridLayout.spec(x);     //设置它的行和列
                GridLayout.Spec columnSpec = GridLayout.spec(y);

                GridLayout.LayoutParams params = new GridLayout.LayoutParams(rowSpec, columnSpec);
                params.height=DensityUtil.dip2px(getContext(),80);
                params.width=widthPixels/2;

                params.setGravity(Gravity.CENTER);
                typeGridContainer.addView(img, params);
                x++;
                if (x == typeGridContainer.getRowCount()) {
                    y++;
                    x = 0;
                }
            }
        }
    }
}
