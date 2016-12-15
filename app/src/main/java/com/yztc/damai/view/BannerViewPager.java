package com.yztc.damai.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by wanggang on 2016/12/14.
 *
 * 点击
 * 触摸
 *
 */

public class BannerViewPager extends ViewPager {


    public BannerViewPager(Context context) {
        super(context);
    }

    public BannerViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //是否可以滑动
    private boolean scrollEnable=true;
    private float x, y;
    private OnBannerViewClick l;
    private OnAutoSlideEnableListner enable;

    public void setOnBannerViewClick(OnBannerViewClick l) {
        this.l = l;
    }

    public void setOnAutoSlideEnableListner(OnAutoSlideEnableListner enable) {
        this.enable = enable;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(!scrollEnable){
            //不处理滑动事件
            return false;
        }
        return super.onInterceptTouchEvent(ev);
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:

                x = ev.getX();
                y = ev.getY();
                //不能进行轮播
                if(enable!=null){
                    enable.onAutoSlideEnable(false);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                if (x == ev.getX()
                        && y == ev.getY()
                        && l != null) {
                    //点击事件
                    l.onBannerViewClick(getCurrentItem());
                }
                //进行轮播
                if(enable!=null){
                    enable.onAutoSlideEnable(true);
                }
                break;
        }

        return super.onTouchEvent(ev);
    }

    // 只有1个item  是否可以滑动
    public void setScrollEnable(boolean scrollEnable) {
        this.scrollEnable = scrollEnable;
    }


    //点击事件
    public interface OnBannerViewClick {
        void onBannerViewClick(int position);
    }

    //是否可以轮播
    public interface OnAutoSlideEnableListner{
        void onAutoSlideEnable(boolean isEnadle);
    }
}
