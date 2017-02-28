package com.yztc.niuniu.ui.av.mvp;

import rx.Observable;

/**
 * Created by wanggang on 2017/2/14.
 */

public interface IAvModel {

    Observable<String> loadList(String page);

    Observable<String> loadDetail(String path);

}
