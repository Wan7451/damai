package com.yztc.core.base;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.yztc.core.R;
import com.yztc.core.views.StatusViewLayout;

/**
 * Created by wanggang on 2017/1/16.
 */

public abstract class BaseListFragment extends BaseFragment {

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
    protected void onInitView(Bundle savedInstanceState) {
        mRecycleView = (RecyclerView) mRootView.findViewById(R.id.recycleView);
        mSwipeRefreshLayout = (SwipeRefreshLayout) mRootView.findViewById(R.id.swipeRefreshView);
        mStatusView = (StatusViewLayout) mRootView.findViewById(R.id.statusView);

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

                int last = getLastVisiblePosition();
                if (last != -1) {
                    lastVisibleItem = last;
                }
            }
        });

    }


    /**
     * 获取第一条展示的位置
     *
     * @return
     */
    private int getFirstVisiblePosition() {
        int position;
        RecyclerView.LayoutManager manager = mRecycleView.getLayoutManager();
        if (manager instanceof LinearLayoutManager) {
            position = ((LinearLayoutManager) manager).findFirstVisibleItemPosition();
        } else if (manager instanceof GridLayoutManager) {
            position = ((GridLayoutManager) manager).findFirstVisibleItemPosition();
        } else if (manager instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) manager;
            int[] lastPositions = layoutManager.findFirstVisibleItemPositions(new int[layoutManager.getSpanCount()]);
            position = getMinPositions(lastPositions);
        } else {
            position = 0;
        }
        return position;
    }

    /**
     * 获得当前展示最小的position
     *
     * @param positions
     * @return
     */
    private int getMinPositions(int[] positions) {
        int size = positions.length;
        int minPosition = Integer.MAX_VALUE;
        for (int i = 0; i < size; i++) {
            minPosition = Math.min(minPosition, positions[i]);
        }
        return minPosition;
    }

    /**
     * 获取最后一条展示的位置
     *
     * @return
     */
    private int getLastVisiblePosition() {
        int position;
        RecyclerView.LayoutManager manager = mRecycleView.getLayoutManager();
        if (manager instanceof LinearLayoutManager) {
            position = ((LinearLayoutManager) manager).findLastVisibleItemPosition();
        } else if (manager instanceof GridLayoutManager) {
            position = ((GridLayoutManager) manager).findLastVisibleItemPosition();
        } else if (manager instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) manager;
            int[] lastPositions = layoutManager.findLastVisibleItemPositions(new int[layoutManager.getSpanCount()]);
            position = getMaxPosition(lastPositions);
        } else {
            position = manager.getItemCount() - 1;
        }
        return position;
    }

    /**
     * 获得最大的位置
     *
     * @param positions
     * @return
     */
    private int getMaxPosition(int[] positions) {
        int size = positions.length;
        int maxPosition = Integer.MIN_VALUE;
        for (int i = 0; i < size; i++) {
            maxPosition = Math.max(maxPosition, positions[i]);
        }
        return maxPosition;
    }

    @Override
    protected void onInitData() {
        mSwipeRefreshLayout.setRefreshing(true);
        loadData();
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
