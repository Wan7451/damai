package com.yztc.core.base;

/**
 * Created by wanggang on 2016/12/12.
 */
public abstract class BasePresenter<T extends BaseView> {

    private T mView;

    public void attachView(T mvpView) {
        this.mView = mvpView;
    }

    public abstract void onStart();

    public void detachView() {
        this.mView = null;
    }

    public boolean isViewBind() {
        return mView != null;
    }

    public T getView() {
        return mView;
    }

}
