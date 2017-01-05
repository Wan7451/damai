package com.yztc.damai.ui.cls;

import android.util.Log;

import com.bumptech.glide.GenericRequestBuilder;
import com.bumptech.glide.ListPreloader;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.signature.StringSignature;
import com.yztc.damai.config.NetConfig;

import java.util.Collections;
import java.util.List;

/**
 * Created by wanggang on 2017/1/5.
 */

public class PreloadMProvider implements
        ListPreloader.PreloadModelProvider<ClassBean> {

    private final List<ClassBean> data;
    private final RequestManager requestManager;


    public PreloadMProvider(RequestManager requestManager, List<ClassBean> data) {
        this.data = data;
        this.requestManager = requestManager;
    }

    @Override
    public List<ClassBean> getPreloadItems(int position) {

        Log.i("==========", position + "");

        //返回一个含指定的对象 的不可变的列表。列表是可序列化的。
        return Collections.singletonList(data.get(position));
    }


    @Override
    public GenericRequestBuilder getPreloadRequestBuilder(ClassBean item) {

        Log.i("==========", "getPreloadRequestBuilder");

        String i = String.valueOf(item.getI());
        String imageURI = NetConfig.BASR_IMG + i.substring(0, i.length() - 2) + "/" + i + "_n.jpg";

        StringSignature signature = new StringSignature(imageURI);

        return requestManager.load(imageURI).signature(signature);
    }
}
