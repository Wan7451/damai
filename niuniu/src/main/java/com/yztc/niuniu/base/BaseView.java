package com.yztc.niuniu.base;

/**
 * Created by wanggang on 2017/2/27.
 */
public interface BaseView {
    void showLoading();

    void hideLoading();

    void showError(String message);
}
