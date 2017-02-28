package com.yztc.niuniu.ui.av;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.yztc.core.base.BaseLazyListFragment;
import com.yztc.niuniu.ui.av.mvp.AvPresenterImpl;
import com.yztc.niuniu.ui.av.mvp.IAvView;
import com.yztc.niuniu.ui.detail.DetailActivity;
import com.yztc.niuniu.ui.detail.OpenDetailListener;

import java.util.ArrayList;

/**
 * Created by wanggang on 2017/2/13.
 */

public class AvFragment extends BaseLazyListFragment implements IAvView {
    private ArrayList<AvBean> data = new ArrayList<>();
    private AvPresenterImpl listPresenter;


    @Override
    protected void onViewInited() {
        super.onViewInited();
        listPresenter = new AvPresenterImpl(this);
    }


    @Override
    protected void onPrepareInitData(Bundle savedInstanceState) {
        super.onPrepareInitData(savedInstanceState);
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(
                2,
                StaggeredGridLayoutManager.VERTICAL
        );
        mRecycleView.setLayoutManager(manager);
    }


    @Override
    protected RecyclerView.Adapter getAdapter() {
        AvAdapter adapter = new AvAdapter(getContext(), data);
        adapter.setIOpenDetailListener(new OpenDetailListener() {
            @Override
            public void openDetailWithPosition(int position) {
                listPresenter.loadData(data.get(position).getUrl());
            }
        });
        return adapter;
    }

    @Override
    protected void loadData() {
        listPresenter.loadData();
    }

    @Override
    protected void addData() {
        listPresenter.addData();
    }


    @Override
    public void showContent(ArrayList<AvBean> data, boolean isload) {
        if (isload) {
            this.data.clear();
        }
        if (data != null)
            this.data.addAll(data);
        showContent();
        loadfinish();
    }

    @Override
    public void startDetail(ArrayList<String> data) {
        DetailActivity.start(getContext(), data);
    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showLoading() {

    }
}
