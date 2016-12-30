package com.yztc.core.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;

/**
 * Created by wanggang on 2016/12/12.
 */

public abstract class BaseFragment extends Fragment {

    protected View rootView;
    protected Context mContext = null;//context

    protected abstract int getLayoutResource();

    protected abstract void onInitView(Bundle savedInstanceState);

    protected abstract void onInitData();

    /***
     * 用于在初始化Data之前做一些事
     */
    protected void initPreData(Bundle savedInstanceState) {

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
            rootView = inflater.inflate(getLayoutResource(), null);
        } else {
            rootView = super.onCreateView(inflater, container, savedInstanceState);
        }
        ButterKnife.bind(this, rootView);
        return rootView;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (isNeedInitEventBus()) {
            EventBus.getDefault().register(this);
        }
        this.onInitView(savedInstanceState);
        initPreData(savedInstanceState);
        this.onInitData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (isNeedInitEventBus()) {
            EventBus.getDefault().unregister(this);
        }
    }
}
