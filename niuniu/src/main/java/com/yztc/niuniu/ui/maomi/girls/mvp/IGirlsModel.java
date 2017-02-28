package com.yztc.niuniu.ui.maomi.girls.mvp;

import rx.Observable;

/**
 * Created by wanggang on 2017/2/14.
 */

public interface IGirlsModel {
    Observable<String> loadlist(String page);

    Observable<String> imagesList(String path);

    Observable<String> loadImages(String path);
}
