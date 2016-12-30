package com.yztc.core.base;

import android.os.Bundle;

/**
 * Created by wanggang on 2016/12/12.
 */

public abstract class BaseMVPFragment<V extends BaseView, T extends BasePresenter<V>> extends BaseFragment {

    protected T mPresenter;

    protected abstract T initPresenter();

    @Override
    protected void initPreData(Bundle savedInstanceState) {
        super.initPreData(savedInstanceState);
        mPresenter = initPresenter();
        mPresenter.attachView((V) this);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null)
            mPresenter.detachView();
    }


}
