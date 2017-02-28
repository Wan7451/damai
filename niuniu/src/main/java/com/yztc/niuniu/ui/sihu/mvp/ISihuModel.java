package com.yztc.niuniu.ui.sihu.mvp;

import rx.Observable;

/**
 * Created by wanggang on 2017/2/14.
 */

public interface ISihuModel {
    Observable<String> loadlist(String page);

    Observable<String> loadImages(String path);
}
