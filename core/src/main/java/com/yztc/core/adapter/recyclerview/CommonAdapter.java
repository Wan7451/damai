package com.yztc.core.adapter.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.yztc.core.adapter.recyclerview.base.ItemViewDelegate;
import com.yztc.core.adapter.recyclerview.base.ViewHolder;
import com.yztc.core.adapter.recyclerview.base.ItemViewDelegate;

import java.util.List;

/**
 * Created by wanggang on 2016/12/18.
 */
public abstract class CommonAdapter<T> extends MultiItemTypeAdapter<T> {
    protected Context mContext;
    protected int mLayoutId;
    protected List<T> mDatas;
    protected LayoutInflater mInflater;

    public CommonAdapter(final Context context, final int layoutId, List<T> datas) {
        super(context, datas);
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mLayoutId = layoutId;
        mDatas = datas;

        addItemViewDelegate(new ItemViewDelegate<T>() {
            @Override
            public int getItemViewLayoutId() {
                return layoutId;
            }

            @Override
            public boolean isForViewType(T item, int position) {
                return true;
            }

            @Override
            public void convert(ViewHolder holder, T t, int position) {
                CommonAdapter.this.convert(holder, t, position);
            }
        });
    }

    protected abstract void convert(ViewHolder holder, T t, int position);


}
