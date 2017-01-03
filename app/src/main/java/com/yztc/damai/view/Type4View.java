package com.yztc.damai.view;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.GridLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.yztc.core.image.ImageLoader;
import com.yztc.core.utils.DensityUtil;
import com.yztc.damai.ui.recommend.TypeViewBean;
import com.yztc.damai.ui.recommend.TypeViewDataBean;

import java.util.List;

/**
 * Created by wanggang on 2016/12/15.
 */

public class Type4View extends TypeContainerView {

    GridLayout typeGridContainer;

    public Type4View(Context context) {
        super(context);
    }

    public Type4View(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setData(TypeViewBean data) {
        super.setData(data);
    }

    int widthPixels;

    @Override
    protected void fillTypeView(TypeViewBean data) {
        typeGridContainer = new GridLayout(getContext());
        addView(typeGridContainer);
        widthPixels = getResources().getDisplayMetrics().widthPixels;

        typeGridContainer.setColumnCount(3);

        List<TypeViewDataBean> dataList = data.getList();
        if (dataList.size() > 0) {
            int x = 0;
            int y = 0;
            int count = 0;
            int i = 0;
            while (count < dataList.size()) {

                if (i % 3 == 1) {
                    View v = new View(getContext());
                    GridLayout.Spec rowSpec = GridLayout.spec(x);     //设置它的行和列
                    GridLayout.Spec columnSpec = GridLayout.spec(y);
                    v.setBackgroundColor(Color.parseColor("#EEEEEE"));
                    GridLayout.LayoutParams params = new GridLayout.LayoutParams(rowSpec, columnSpec);
                    params.height = DensityUtil.dip2px(getContext(), 80);
                    params.width = 1;
                    typeGridContainer.addView(v, params);
                } else {
                    ImageView img = new ImageView(getContext());
                    int pd = DensityUtil.dip2px(getContext(), 5);
                    img.setPadding(pd, pd, pd, pd);
                    img.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    ImageLoader.getInstance().loadImages(img, dataList.get(count).getPicUrl(), false);
                    GridLayout.Spec rowSpec = GridLayout.spec(x);     //设置它的行和列
                    GridLayout.Spec columnSpec = GridLayout.spec(y);
                    GridLayout.LayoutParams params = new GridLayout.LayoutParams(rowSpec, columnSpec);
                    params.height = DensityUtil.dip2px(getContext(), 80);
                    params.width = widthPixels / 2;
                    typeGridContainer.addView(img, params);
                    count++;
                }

                switch (i % 3) {
                    case 0:
                    case 1:
                        y++;
                        break;
                    case 2:
                        x++;
                        y = 0;
                        addDivider(x, y);
                        x++;
                        break;
                }
                i++;
            }
        }

        //移除最后一个View
        typeGridContainer.removeViewAt(typeGridContainer.getChildCount() - 1);
    }

    private void addDivider(int x, int y) {

        GridLayout.Spec rowSpec = GridLayout.spec(x);     //设置它的行和列
        GridLayout.Spec columnSpec = GridLayout.spec(0, 3);
        GridLayout.LayoutParams params = new GridLayout.LayoutParams(
                rowSpec, columnSpec);
        params.height = 1;
        params.width = widthPixels;

        View v = new View(getContext());
        v.setBackgroundColor(Color.parseColor("#EEEEEE"));

        typeGridContainer.addView(v, params);

    }
}
