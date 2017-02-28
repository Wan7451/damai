package com.yztc.niuniu.dickcache;

import rx.functions.Func1;

/**
 * Created by wanggang on 2017/2/26.
 */

public class DickCacheNoTimeOutFunc implements Func1<String, String> {

    private String key;

    public DickCacheNoTimeOutFunc(String key) {
        this.key = key;
    }

    @Override
    public String call(String s) {
        DiskCache.getInstance().getStringNoTimeOut(key);
        return s;
    }
}
