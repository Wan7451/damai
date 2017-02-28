package com.yztc.niuniu.dickcache;

import rx.functions.Func1;

/**
 * Created by wanggang on 2017/2/26.
 */

public class DickCacheFunc implements Func1<String, String> {

    private String key;

    public DickCacheFunc(String key) {
        this.key = key;
    }

    @Override
    public String call(String s) {
        DiskCache.getInstance().putString(key, s);
        return s;
    }
}
