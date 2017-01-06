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
 * Created by wanggang on 2017/1/6.
 */

public class MyPreloadModleProvider implements ListPreloader.PreloadModelProvider<ClassBean> {

    private List<ClassBean> mData;
    private RequestManager mRequestManager;

    public MyPreloadModleProvider(RequestManager requestManager, List<ClassBean> data) {
        this.mData = data;
        this.mRequestManager = requestManager;
    }


    //预加载的数据
    @Override
    public List<ClassBean> getPreloadItems(int position) {
        return Collections.singletonList(mData.get(position));
    }

    //进行加载
    @Override
    public GenericRequestBuilder getPreloadRequestBuilder(ClassBean item) {

        Log.i("===========", "预加载");

        String i = String.valueOf(item.getI());
        String imageURI = NetConfig.BASR_IMG + i.substring(0, i.length() - 2) + "/" + i + "_n.jpg";

        StringSignature signature = new StringSignature(imageURI);

        return mRequestManager.load(imageURI).signature(signature);
    }
}
