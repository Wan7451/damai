package com.yztc.core.base;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yztc.core.R;
import com.yztc.core.views.StatusViewLayout;

/**
 * Created by wanggang on 2017/1/16.
 */

public abstract class BaseLazyListFragment extends LazyFragment {

    protected RecyclerView mRecycleView;
    protected SwipeRefreshLayout mSwipeRefreshLayout;
    protected StatusViewLayout mStatusView;

    protected RecyclerView.Adapter mAdapter;

    /**
     * 设置显示的Adapter
     *
     * @return RecycleView 显示的Adapter
     */
    protected abstract RecyclerView.Adapter getAdapter();

    /**
     * 下拉刷新，加载数据
     */
    protected abstract void loadData();

    /**
     * 上拉加载，加载数据
     */
    protected abstract void addData();


    protected void onViewInited() {
    }

    /**
     * 加载完成，取消加载状态
     */
    public void loadfinish() {
        mAdapter.notifyDataSetChanged();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    protected boolean enableRefresh() {
        return true;
    }

    protected boolean isAddItemDecoration() {
        return true;
    }

    @Override
    protected void onInitLazyView(View v) {

        mRecycleView = (RecyclerView) v.findViewById(R.id.recycleView);
        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipeRefreshView);
        mStatusView = (StatusViewLayout) v.findViewById(R.id.statusView);

        mSwipeRefreshLayout.setColorSchemeColors(
                getResources().getColor(android.R.color.holo_red_light),
                getResources().getColor(android.R.color.holo_green_light),
                getResources().getColor(android.R.color.holo_orange_light),
                getResources().getColor(android.R.color.holo_blue_light)
        );

        if (isAddItemDecoration()) {
            DividerItemDecoration decoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
            mRecycleView.addItemDecoration(decoration);
        }
        mRecycleView.setHasFixedSize(true);
        mRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = getAdapter();
        if (mAdapter != null)
            mRecycleView.setAdapter(mAdapter);

        onViewInited();

        if (!enableRefresh()) {
            mSwipeRefreshLayout.setEnabled(false);
            loadData();
            return;
        }

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });


        mRecycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastVisibleItem;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE &&
                        lastVisibleItem + 1 == mAdapter.getItemCount()) {
                    //滑动到底部，加载新的数据
                    if (!mSwipeRefreshLayout.isRefreshing()) {
                        mSwipeRefreshLayout.setRefreshing(true);
                        addData();
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                if (layoutManager instanceof LinearLayoutManager) {
                    int last = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                    if (last != -1) {
                        lastVisibleItem = last;
                    }
                }
            }
        });
        mSwipeRefreshLayout.setRefreshing(true);
        loadData();
    }

    @Override
    protected void onInitData() {

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_base_list;
    }


    public void showLoading() {
        mStatusView.showLoading();
    }

    public void showContent() {
        mStatusView.showContent();
    }

    public void showError(String msg) {
        mStatusView.showError();
    }

}
