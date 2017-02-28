package com.yztc.niuniu.ui.maomi.girls;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.yztc.core.base.BaseLazyListFragment;
import com.yztc.niuniu.ui.detail.OpenDetailListener;
import com.yztc.niuniu.ui.maomi.girls.mvp.GirlsPresenterImpl;
import com.yztc.niuniu.ui.maomi.girls.mvp.IGirlsPresenter;
import com.yztc.niuniu.ui.maomi.girls.mvp.IGirlsView;

import java.util.ArrayList;

/**
 * Created by wanggang on 2017/2/20.
 */

public class GirlsFragment extends BaseLazyListFragment implements IGirlsView {


    private ArrayList<GirlsBean> data = new ArrayList<>();
    private IGirlsPresenter presenter;


    @Override
    protected boolean enableRefresh() {
        return false;
    }

    @Override
    protected void onViewInited() {
        presenter = new GirlsPresenterImpl(this);

        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(
                2,
                StaggeredGridLayoutManager.VERTICAL
        );
        mRecycleView.setLayoutManager(manager);
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        GirlsAdapter adapter = new GirlsAdapter(getContext(), data);
        adapter.setIOpenDetailListener(new OpenDetailListener() {
            @Override
            public void openDetailWithPosition(int position) {
                GirlsListActivity.start(getContext(), data.get(position).getUrl());
            }
        });
        return adapter;
    }

    @Override
    protected void loadData() {
        presenter.loadData();
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
    public void startDetail(ArrayList<String> strings) {

    }


}
