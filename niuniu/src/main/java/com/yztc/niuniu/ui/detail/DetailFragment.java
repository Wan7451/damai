package com.yztc.niuniu.ui.detail;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.yztc.core.base.BaseListFragment;

import java.util.ArrayList;

/**
 * Created by wanggang on 2017/2/14.
 */

public class DetailFragment extends BaseListFragment {
    private ArrayList<String> data = new ArrayList<>();

    @Override
    protected void onPrepareInitData(Bundle savedInstanceState) {
        super.onPrepareInitData(savedInstanceState);
        data.clear();
        data.addAll(getArguments().getStringArrayList("imgs"));

        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(
                2,
                StaggeredGridLayoutManager.VERTICAL
        );
        mRecycleView.setLayoutManager(manager);


    }

    @Override
    protected boolean enableRefresh() {
        return false;
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return new DetailAdapter(getContext(), data);
    }

    @Override
    protected void loadData() {
        loadfinish();
        showContent();
    }

    @Override
    protected void addData() {

    }

}
