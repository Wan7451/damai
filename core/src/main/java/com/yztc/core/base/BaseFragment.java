package com.yztc.core.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.bumptech.glide.Glide;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;

/**
 * Created by wanggang on 2016/12/12.
 */

public abstract class BaseFragment extends Fragment {

    protected View mRootView;
    protected Context mContext = null;//context

    @LayoutRes
    protected abstract int getLayoutResource();

    protected abstract void onInitView(Bundle savedInstanceState);

    protected abstract void onInitData();

    /***
     * 用于在初始化Data之前做一些事
     */
    protected void onPrepareInitData(Bundle savedInstanceState) {
    }

    /***
     * 是否需要使用EventBus
     */
    protected boolean isNeedInitEventBus() {
        return false;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (getLayoutResource() != 0) {
            mRootView = inflater.inflate(getLayoutResource(), null);
        } else {
            mRootView = super.onCreateView(inflater, container, savedInstanceState);
        }
        ButterKnife.bind(this, mRootView);
        return mRootView;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (isNeedInitEventBus()) {
            EventBus.getDefault().register(this);
        }
        this.onInitView(savedInstanceState);
        onPrepareInitData(savedInstanceState);
        //自动加载
        mRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //在布局加载完成之后，才能执行，不然看不到效果
                onInitData();
                mRootView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (isNeedInitEventBus()) {
            EventBus.getDefault().unregister(this);
        }
    }


    //在系统内存不足，所有后台程序（优先级为background的进程，
    // 不是指后台运行的进程）都被杀死时，系统会调用OnLowMemory。
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Glide.get(getContext()).clearMemory();
    }

}
