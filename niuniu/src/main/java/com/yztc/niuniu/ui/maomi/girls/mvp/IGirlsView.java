package com.yztc.niuniu.ui.maomi.girls.mvp;

import com.yztc.niuniu.ui.maomi.girls.GirlsBean;

import java.util.ArrayList;

/**
 * Created by wanggang on 2017/2/14.
 */

public interface IGirlsView {
    void showContent(ArrayList<GirlsBean> data, boolean isload);

    void showLoading();

    void showError(String msg);

    void startDetail(ArrayList<String> strings);
}
