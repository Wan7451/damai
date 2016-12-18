package com.yztc.core.adapter.abslistview.base;


import android.view.View;

import com.yztc.core.adapter.abslistview.ViewHolder;

/**
 * Created by wanggang on 2016/12/18.
 */
public interface ItemViewDelegate<T> {

    public abstract int getItemViewLayoutId();

    public abstract boolean isForViewType(T item, int position);

    public abstract void convert(ViewHolder holder, T t, int position);


}
