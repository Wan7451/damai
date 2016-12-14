package com.yztc.damai.view;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yztc.damai.image.ImageLoader;

import java.util.ArrayList;

/**
 * Created by wanggang on 2016/12/14.
 */

public class BannerViewAdapter extends PagerAdapter {

    private ArrayList<String> datas;
    private ArrayList<ImageView> views;
    private Context context;
    private ImageLoader imageLoader;

    public BannerViewAdapter(Context context, ArrayList<String> data) {
        this.context = context;
        this.imageLoader = ImageLoader.getInstance();
        views = new ArrayList<>();
        datas = new ArrayList<>();
        addBannerViews(data);
    }


    private void addBannerViews(ArrayList<String> data) {
        views.clear();
        datas.addAll(data);
        int len = datas.size();

        if (len == 1) {
            views.add(generateView());
            return;
        }

        //确保views的数量大于5
        while (len < 5) {
            len = len * 2;
            datas.addAll(data);
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
        imageLoader.loadImages(view, url, true);
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
