package com.yztc.niuniu.ui.av.mvp;

import com.yztc.niuniu.base.BaseView;
import com.yztc.niuniu.ui.av.AvBean;

import java.util.ArrayList;

/**
 * Created by wanggang on 2017/2/14.
 */

public interface IAvView extends BaseView {
    void showContent(ArrayList<AvBean> data, boolean isload);

    void startDetail(ArrayList<String> data);
}
