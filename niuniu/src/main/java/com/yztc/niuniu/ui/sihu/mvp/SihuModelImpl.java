package com.yztc.niuniu.ui.sihu.mvp;

import com.yztc.niuniu.net.NetRequest;

import rx.Observable;

/**
 * Created by wanggang on 2017/2/14.
 */

public class SihuModelImpl implements ISihuModel {
    @Override
    public Observable<String> loadlist(String page) {
        return NetRequest.getInstance().getSihuApi().list(page);
    }

    @Override
    public Observable<String> loadImages(String path) {
        return NetRequest.getInstance().getSihuApi().images(path);
    }
}
