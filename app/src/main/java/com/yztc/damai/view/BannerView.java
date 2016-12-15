package com.yztc.damai.view;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.yztc.damai.R;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by wanggang on 2016/12/14.
 *
 * 广告条
 * 无限轮播
 * 点击
 * 触摸不轮播
 * 支持 少于4条数据的情况
 */

public class BannerView extends FrameLayout {

    //轮播时间
    private static final int AUTO_SCROLL_TIME=3000;

    //代码中使用
    public BannerView(Context context) {
        super(context);
        init();
    }
    //XML 使用
    public BannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private BannerViewPager viewPager;
    private RadioGroup indicate;
    private Timer timer;
    private Handler handler;
    private BannerViewAdapter adapter;
    private ArrayList<String> data;

    private boolean isGoNext=true;

    private void init() {
        //设置布局
        LayoutInflater inflater = LayoutInflater.from(getContext());
        inflater.inflate(R.layout.view_banner,this,true);

        //v 的宽高,与 parent的宽高一致   Fragment
        //View v= inflater.inflate(R.layout.view_banner,parent,false);

        //v的高  wrap_content    getView()
        //View v= inflater.inflate(R.layout.view_banner,null);

        //直接将v  add到Parent中    自定义Vew
        //inflater.inflate(R.layout.view_banner,parent,true);


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

    public void setOnBannerViewClick(final BannerViewPager.OnBannerViewClick l) {
        if (viewPager != null && l!=null) {
            viewPager.setOnBannerViewClick(new BannerViewPager.OnBannerViewClick() {
                @Override
                public void onBannerViewClick(int position) {
                    if (data != null && data.size() > 0){
                        l.onBannerViewClick(position % data.size());
                    }
                }
            });
        }
    }

    /**
     * 设置显示的效果
     * @param data  图片的地址
     */
    public void setData(final ArrayList<String> data) {
        this.data=data;
        adapter = new BannerViewAdapter(getContext(), data);
        viewPager.setAdapter(adapter);

        //数据只有1条
        if(data.size()==1){
            viewPager.setScrollEnable(false);
            return;
        }

        viewPager.addOnPageChangeListener(new PagerListener());
//        ViewPager.PageTransformer pageTransformer=new ViewPager.PageTransformer() {
//            @Override
//            public void transformPage(View page, float position) {
//                page.setAnimation(AnimationUtils.loadAnimation(getContext(), android.R.anim.slide_out_right));
//            }
//        };
//        viewPager.setPageTransformer(true, pageTransformer);

        // 设置滑动速度
        FixedSpeedScroller mScroller = null;
        try {
            Field mField = null;

            mField = ViewPager.class.getDeclaredField("mScroller");
            mField.setAccessible(true);

            mScroller = new FixedSpeedScroller(
                    viewPager.getContext(),
                    new AccelerateInterpolator());
            mScroller.setmDuration(800); // 2000ms
            mField.set(viewPager, mScroller);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // /设置每次加载时第一页在MAX_VALUE / 2 - Extra 页，
        //造成用户无限轮播的错觉
        // 6  100%6   100-4   96
        int startPage = 100;
        int extra = startPage % data.size();
        startPage = startPage - extra-1;
        //position 起点
        viewPager.setCurrentItem(startPage);

        //添加指示器
        int count = data.size();
        for (int i = 0; i < count; i++) {
            indicate.addView(generateIndicateView(i));
        }

        //定时器 自定滚动
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //是否进行轮播
                        if(!isGoNext)
                            return;
                        int position = viewPager.getCurrentItem();
                        viewPager.setCurrentItem(position + 1);
                    }
                });
            }
        }, 0, AUTO_SCROLL_TIME);

    }

    private View generateIndicateView(int id) {
        RadioButton btn = new RadioButton(getContext());
        if(id==0){
            btn.setChecked(true);
        }
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


     class PagerListener implements ViewPager.OnPageChangeListener{


        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            RadioButton child = (RadioButton) indicate.getChildAt(position % data.size());
            if (child != null)
                child.setChecked(true);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

}
