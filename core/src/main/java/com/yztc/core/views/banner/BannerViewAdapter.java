package com.yztc.core.views.banner;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;

import java.util.ArrayList;

/**
 * Created by wanggang on 2016/12/14.
 *
 *
 * 无限循环   ViewPager
 */

public class BannerViewAdapter extends PagerAdapter {

    private ArrayList<String> datas;
    private ArrayList<ImageView> views;
    private Context context;

    public BannerViewAdapter(Context context, ArrayList<String> data) {
        this.context = context;
        views = new ArrayList<>();  //所有的要显示的组件
        datas = new ArrayList<>();  //要显示的数据
        addBannerViews(data);       //生成 views
    }


    private void addBannerViews(ArrayList<String> data) {
        views.clear();
        datas.clear();

        //数据集合
        datas.addAll(data);

        int len = datas.size();

        if (len == 1) {
            views.add(generateView());
            return;
        }

        //当数据集合长度小于5，适配器出问题
        //要避免这个问题，可以增加数据集合的长度
        // 确保views的数量大于5
        while (len < 5) {
            datas.addAll(data);
            len=datas.size();
        }

        for (int i = 0; i < len; i++) {
            views.add(generateView());
        }
    }

    private ImageView generateView() {
        ImageView view = new ImageView(context);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(lp);
        view.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return view;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        ImageView view = views.get(position % datas.size());
        String url = datas.get(position % datas.size());
        Glide.with(context).load(url).priority(Priority.IMMEDIATE).into(view);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(views.get(position % datas.size()));
    }

    @Override
    public int getCount() {
        if (datas != null) {
            if (datas.size() == 1)
                return 1;
            return Integer.MAX_VALUE;
        }
        return 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


}
