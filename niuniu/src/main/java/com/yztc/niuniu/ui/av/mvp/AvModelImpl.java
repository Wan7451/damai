package com.yztc.niuniu.ui.av.mvp;

import com.yztc.niuniu.net.NetRequest;

import rx.Observable;

/**
 * Created by wanggang on 2017/2/14.
 */

public class AvModelImpl implements IAvModel {
    @Override
    public Observable<String> loadList(String page) {
        return NetRequest.getInstance().getApi().list(page);
    }

    @Override
    public Observable<String> loadDetail(String path) {
        return NetRequest.getInstance().getApi().detail(path);
    }
}
