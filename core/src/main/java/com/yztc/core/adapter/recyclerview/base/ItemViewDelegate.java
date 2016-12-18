package com.yztc.core.adapter.recyclerview.base;


import android.view.View;

/**
 * Created by wanggang on 2016/12/18.
 *
 * 适配器 不同ItemView 的 封装
 *
 */
public interface ItemViewDelegate<T> {

    int getItemViewLayoutId();

    boolean isForViewType(T item, int position);

    void convert(ViewHolder holder, T t, int position);

}
