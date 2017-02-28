package com.yztc.niuniu.ui.maomi.girls.mvp;

import com.yztc.niuniu.net.NetRequest;

import rx.Observable;

/**
 * Created by wanggang on 2017/2/14.
 */

public class GirlsModelImpl implements IGirlsModel {
    @Override
    public Observable<String> loadlist(String page) {
        return NetRequest.getInstance().getMaomiApi().list();
    }

    @Override
    public Observable<String> imagesList(String path) {
        return NetRequest.getInstance().getMaomiApi().imagesList(path);
    }

    @Override
    public Observable<String> loadImages(String path) {
        return NetRequest.getInstance().getMaomiApi().images(path);
    }
}
