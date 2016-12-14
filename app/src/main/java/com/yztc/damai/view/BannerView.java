package com.yztc.damai.view;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.yztc.damai.R;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by wanggang on 2016/12/14.
 */

public class BannerView extends FrameLayout {

    private static final int AUTO_SCROLL_TIME=3000;

    public BannerView(Context context) {
        super(context);
        init();
    }

    public BannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private BannerViewPager viewPager;
    private RadioGroup indicate;
    private Timer timer;
    private Handler handler;
    private BannerViewAdapter adapter;

    private boolean isGoNext=true;

    private void init() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        inflater.inflate(R.layout.view_banner, this, true);

        viewPager = (BannerViewPager) findViewById(R.id.view_pager);
        indicate = (RadioGroup) findViewById(R.id.view_indicate);
        timer = new Timer();
        handler = new Handler(Looper.getMainLooper());
        viewPager.setOnAutoSlideEnableListner(new BannerViewPager.OnAutoSlideEnableListner() {
            @Override
            public void onAutoSlideEnable(boolean isEnadle) {
                isGoNext=isEnadle;
            }
        });
    }

    public void setOnBannerViewClick(BannerViewPager.OnBannerViewClick l) {
        if (viewPager != null) {
            viewPager.setOnBannerViewClick(l);
        }
    }

    public void setData(final ArrayList<String> data) {
        adapter = new BannerViewAdapter(getContext(), data);
        viewPager.setAdapter(adapter);

        if(data.size()==1){
            viewPager.setScrollEnable(false);
            return;
        }

        //设置每次加载时第一页在MAX_VALUE / 2 - Extra 页，
        //造成用户无限轮播的错觉
        int startPage = 500;
        int extra = startPage % data.size();
        startPage = startPage - extra;
        viewPager.setCurrentItem(startPage);

        int count = data.size();
        for (int i = 0; i < count; i++) {
            indicate.addView(generateIndicateView(i));
        }

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        if(!isGoNext)
                            return;

                        int position = viewPager.getCurrentItem();
                        viewPager.setCurrentItem(position + 1);
                        RadioButton child = (RadioButton) indicate.getChildAt(position % data.size());
                        if (child != null)
                            child.setChecked(true);
                    }
                });
            }
        }, 0, AUTO_SCROLL_TIME);

    }

    private View generateIndicateView(int id) {
        RadioButton btn = new RadioButton(getContext());
        btn.setId(id + 23423);
        RadioGroup.LayoutParams lp = new RadioGroup.LayoutParams(
                RadioGroup.LayoutParams.WRAP_CONTENT,
                RadioGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 0, 25, 0);
        btn.setLayoutParams(lp);
        btn.setButtonDrawable(R.drawable.indicate_bg);
        return btn;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (timer != null)
            timer.cancel();
    }


}
