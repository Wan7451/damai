package com.yztc.niuniu.ui.maomi.girls;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.yztc.core.base.BaseListFragment;
import com.yztc.niuniu.ui.detail.DetailActivity;
import com.yztc.niuniu.ui.detail.OpenDetailListener;
import com.yztc.niuniu.ui.maomi.girls.mvp.GirlsPresenterImpl;
import com.yztc.niuniu.ui.maomi.girls.mvp.IGirlsPresenter;
import com.yztc.niuniu.ui.maomi.girls.mvp.IGirlsView;

import java.util.ArrayList;

/**
 * Created by wanggang on 2017/2/24.
 */

public class GirlsListFragment extends BaseListFragment implements IGirlsView {

    private ArrayList<GirlsBean> data = new ArrayList<>();
    private IGirlsPresenter presenter;
    private String path;

    @Override
    protected void onViewInited() {
        presenter = new GirlsPresenterImpl(getContext(), this);

        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(
                2,
                StaggeredGridLayoutManager.VERTICAL
        );
        mRecycleView.setLayoutManager(manager);

        this.path = getArguments().getString("path");
    }

    @Override
    protected boolean enableRefresh() {
        return false;
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        GirlsAdapter adapter = new GirlsAdapter(getContext(), data);
        adapter.setIOpenDetailListener(new OpenDetailListener() {
            @Override
            public void openDetailWithPosition(int position) {
                presenter.loadImages(data.get(position).getUrl());
            }
        });
        return adapter;
    }

    @Override
    protected void loadData() {
        presenter.loadImagesList(path);
    }

    @Override
    protected void addData() {
    }

    @Override
    public void showContent(ArrayList<GirlsBean> data, boolean isload) {
        if (isload) {
            this.data.clear();
        }
        this.data.addAll(data);
        showContent();
        loadfinish();
    }

    @Override
    public void startDetail(ArrayList<String> data) {
        DetailActivity.start(getContext(), data);
    }

}
