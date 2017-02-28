package com.yztc.niuniu.ui.sihu.mvp;

import com.yztc.niuniu.ui.sihu.SihuBean;

import java.util.ArrayList;

/**
 * Created by wanggang on 2017/2/14.
 */

public interface ISihulView {
    void showContent(ArrayList<SihuBean> data, boolean isload);

    void showLoading();

    void showError(String msg);

    void startDetail(ArrayList<String> strings);
}
