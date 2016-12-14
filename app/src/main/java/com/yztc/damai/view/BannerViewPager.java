package com.yztc.damai.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by wanggang on 2016/12/14.
 */

public class BannerViewPager extends ViewPager {
    private boolean scrollEnable=true;

    public BannerViewPager(Context context) {
        super(context);
    }

    public BannerViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


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
                    l.onBannerViewClick(getCurrentItem());
                }
                if(enable!=null){
                    enable.onAutoSlideEnable(true);
                }
                break;
        }

        return super.onTouchEvent(ev);
    }

    public void setScrollEnable(boolean scrollEnable) {
        this.scrollEnable = scrollEnable;
    }


    public interface OnBannerViewClick {
        void onBannerViewClick(int position);
    }

    public interface OnAutoSlideEnableListner{
        void onAutoSlideEnable(boolean isEnadle);
    }
}
