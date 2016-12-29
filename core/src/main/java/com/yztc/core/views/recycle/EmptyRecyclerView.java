package com.yztc.core.views.recycle;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by wanggang on 2016/12/27.
 */

public class EmptyRecyclerView extends RecyclerView {

    private View mEmptyView;

    private View mErrorView;

    private boolean isError;

    private int mVisibility;

    final private AdapterDataObserver mObserver = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            updateEmptyView();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            updateEmptyView();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            updateEmptyView();
        }
    };

    public EmptyRecyclerView(Context context) {
        super(context);
        mVisibility = getVisibility();
    }

    public EmptyRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mVisibility = getVisibility();
    }

    public EmptyRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mVisibility = getVisibility();
    }

    @Override
    public void setAdapter(Adapter adapter) {
        Adapter oldAdapter = getAdapter();
        if (oldAdapter != null) {
            oldAdapter.unregisterAdapterDataObserver(mObserver);
        }
        super.setAdapter(adapter);
        if (adapter != null) {
            adapter.registerAdapterDataObserver(mObserver);
        }
        updateEmptyView();
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        mVisibility = visibility;
        updateErrorView();
        updateEmptyView();
    }

    private void updateEmptyView() {
        if (mEmptyView != null && getAdapter() != null) {
            boolean isShowEmptyView = getAdapter().getItemCount() == 0;
            mEmptyView.setVisibility(isShowEmptyView && !shouldShowErrorView() && mVisibility == VISIBLE ? VISIBLE : GONE);
            super.setVisibility(!isShowEmptyView && !shouldShowErrorView() && mVisibility == VISIBLE ? VISIBLE : GONE);
        }
    }

    private void updateErrorView() {
        if (mErrorView != null) {
            mErrorView.setVisibility(shouldShowErrorView() && mVisibility == VISIBLE ? VISIBLE : GONE);
        }
    }

    private boolean shouldShowErrorView() {
        return mErrorView != null && isError;
    }

    public void setEmptyView(View emptyView) {
        mEmptyView = emptyView;
        updateEmptyView();
    }

    public void setErrorView(View errorView) {
        mErrorView = errorView;
        updateErrorView();
        updateEmptyView();
    }

    public void showErrorView() {
        isError = true;
        updateErrorView();
        updateEmptyView();
    }

    public void hideErrorView() {
        isError = false;
        updateErrorView();
        updateEmptyView();
    }
/**
 * <me.henrytao.sharewifi.widget.RecycleEmptyErrorView
 android:id="@+id/list"
 android:layout_width="match_parent"
 android:layout_height="match_parent"
 android:scrollbars="vertical"
 style="@style/MdList" />

 <RelativeLayout
 android:id="@+id/empty_view"
 android:layout_width="match_parent"
 android:layout_height="wrap_content">
 <include layout="@layout/view_empty_wifi_list" />
 </RelativeLayout>

 <RelativeLayout
 android:id="@+id/error_view"
 android:layout_width="match_parent"
 android:layout_height="wrap_content">
 <include layout="@layout/view_error_wifi_list" />
 </RelativeLayout>
 mRecyclerView.setHasFixedSize(true);
 mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
 mRecyclerView.setAdapter(adapter);
 mRecyclerView.setEmptyView(mEmptyView);
 mRecyclerView.setErrorView(mErrorView);
 mRecyclerView.showErrorView();
 mRecyclerView.hideErrorView();
 */
}
