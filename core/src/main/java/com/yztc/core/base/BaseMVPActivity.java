package com.yztc.core.base;

import android.os.Bundle;

/**
 * Created by wanggang on 2016/12/18.
 */

public abstract class BaseMVPActivity<V extends BaseView, T extends BasePresenter<V>> extends BaseActivity {

    protected abstract T setPresenter();

    protected T mPresenter;


    @Override
    protected void initPreData(Bundle savedInstanceState) {
        super.initPreData(savedInstanceState);
        mPresenter = setPresenter();
        mPresenter.attachView((V) this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null)
            mPresenter.detachView();
    }


}
