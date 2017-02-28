package com.yztc.niuniu.ui.sihu;

import android.support.v7.widget.RecyclerView;

import com.yztc.core.base.BaseLazyListFragment;
import com.yztc.niuniu.ui.detail.DetailActivity;
import com.yztc.niuniu.ui.detail.OpenDetailListener;
import com.yztc.niuniu.ui.sihu.mvp.ISihuPresenter;
import com.yztc.niuniu.ui.sihu.mvp.ISihulView;
import com.yztc.niuniu.ui.sihu.mvp.SihuPresenterImpl;

import java.util.ArrayList;

/**
 * Created by wanggang on 2017/2/20.
 */

public class SihuFragment extends BaseLazyListFragment implements ISihulView {


    private ArrayList<SihuBean> data = new ArrayList<>();
    private ISihuPresenter presenter;


    @Override
    protected void onViewInited() {
        super.onViewInited();
        presenter = new SihuPresenterImpl(this);
    }


    @Override
    protected RecyclerView.Adapter getAdapter() {
        SihuAdapter adapter = new SihuAdapter(getContext(), data);
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
        presenter.loadData();
    }

    @Override
    protected void addData() {
        presenter.addData();
    }

    @Override
    public void showContent(ArrayList<SihuBean> data, boolean isload) {
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
