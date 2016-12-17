package com.yztc.damai.ui.recommend;

import android.content.Context;
import android.support.v7.widget.GridLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.widget.ImageView;

import com.yztc.damai.image.ImageLoader;
import com.yztc.damai.utils.DensityUtil;

import java.util.List;

/**
 * Created by wanggang on 2016/12/15.
 */

public class Type3View extends TypeContainerView {

    GridLayout typeGridContainer;

    public Type3View(Context context) {
        super(context);
    }

    public Type3View(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setData(TypeViewBean data) {
        super.setData(data);
    }

    @Override
    protected void fillTypeView(TypeViewBean data) {
        typeGridContainer = new GridLayout(getContext());

        addView(typeGridContainer);

        int widthPixels = getResources().getDisplayMetrics().widthPixels;

        typeGridContainer.setColumnCount(2);

        List<TypeViewDataBean> dataList = data.getList();
        if (dataList.size() > 0) {
            int x = 0;
            int y = 0;
            for (int i = 0; i < dataList.size(); i++) {
                ImageView img = new ImageView(getContext());


                ImageLoader.getInstance().loadImages(img, dataList.get(i).getPicUrl(), false);
                GridLayout.Spec columnSpec = GridLayout.spec(y);
                GridLayout.Spec rowSpec;
                //设置跨行
                if (i % 3 == 0) {
                    rowSpec = GridLayout.spec(x, 2);     //设置它的行和列
                } else {
                    rowSpec = GridLayout.spec(x);     //设置它的行和列
                }
                GridLayout.LayoutParams params = new GridLayout.LayoutParams(rowSpec, columnSpec);
                //设置高度
                if (i % 3 == 0) {
                    params.height = DensityUtil.dip2px(getContext(), 180);
                } else {
                    params.height = DensityUtil.dip2px(getContext(), 90);
                }
                //设置宽度
                params.width = widthPixels / 2;
                params.setGravity(Gravity.CENTER);
                typeGridContainer.addView(img, params);


                //计算下标
                if (y != typeGridContainer.getColumnCount() - 1) {
                    y++;
                } else {
                    if (i % 3 == 2) {
                        x++;
                        y = 0;
                    } else {
                        x++;
                    }
                }
            }
        }
    }
}
