package com.yztc.damai.ui.cls;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewStub;
import android.view.ViewTreeObserver;

import com.google.gson.Gson;
import com.yztc.core.base.LazyFragment;
import com.yztc.core.utils.ToastUtils;
import com.yztc.damai.R;
import com.yztc.damai.help.Event;
import com.yztc.damai.net.NetResponse;
import com.yztc.damai.net.NetUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class ClassItemFragment extends LazyFragment {


    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private ViewStub stubView;
    private View emptyView;

    public ClassItemFragment() {
    }


    private ArrayList<ClassBean> data = new ArrayList<>();
    private ClassAdapter adapter;

    private int type;
    private int page = 1;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_item_class;
    }

    @Override
    protected void init(final View v) {

        type = getArguments().getInt("type");
        switch (type) {
            case 9:
                type = 100;
                break;
            case 10:
                type = 200;
                break;
        }

        refreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipeRefreshView);
        recyclerView = (RecyclerView) v.findViewById(R.id.recycleView);
        stubView = (ViewStub) v.findViewById(R.id.emptyView);
        refreshLayout.setColorSchemeColors(
                getResources().getColor(R.color.red),
                getResources().getColor(R.color.green),
                getResources().getColor(R.color.yellow)
        );

        DividerItemDecoration decoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(decoration);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ClassAdapter(getContext(), data);
        recyclerView.setAdapter(adapter);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });


        //自动加载
        root.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //在布局加载完成之后，才能执行，不然看不到效果
                refreshLayout.setRefreshing(true);
                loadData();
                root.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });


        //上拉加载
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastVisibleItem;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE &&
                        lastVisibleItem + 1 == adapter.getItemCount()) {
                    //滑动到底部，加载新的数据
                    if (!refreshLayout.isRefreshing()) {
                        refreshLayout.setRefreshing(true);
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
    }

    private void addData() {
        HashMap<String, String> params = new HashMap<>();
        params.put("cc", "0");
        params.put("ps", "20");
        params.put("mc", type + "");
        params.put("ot", "0");
        params.put("v", "0");
        params.put("p", ++page + "");
        params.put("cityId", "852");

        NetUtils.getInstance().get("ProjLst.aspx", params, new NetResponse() {
            @Override
            public void onResponse(String response) {
                refreshLayout.setRefreshing(false);
                try {
                    JSONObject object = new JSONObject(response);
                    if (!object.has("l")) {
                        // 数据全部加载
                        showErrorHint();
                        return;
                    }
                    JSONArray array = object.getJSONArray("l");
                    Gson gson = new Gson();
                    for (int i = 0, size = array.length(); i < size; i++) {
                        String string = array.getString(i);
                        data.add(gson.fromJson(string, ClassBean.class));
                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(String erroe) {
                refreshLayout.setRefreshing(false);
            }
        });
    }

    private void showErrorHint() {
        ToastUtils.showLong("已经到底啦~~");
    }

    private void loadData() {
        page = 1;
        HashMap<String, String> params = new HashMap<>();
        params.put("cc", "0");
        params.put("ps", "20");
        params.put("mc", type + "");
        params.put("ot", "0");
        params.put("v", "0");
        params.put("p", page + "");
        params.put("cityId", "852");

        NetUtils.getInstance().get("ProjLst.aspx", params, new NetResponse() {
            @Override
            public void onResponse(String response) {
                refreshLayout.setRefreshing(false);
                try {
                    JSONObject object = new JSONObject(response);
                    if (!object.has("l")) {
                        showErrorView();
                        return;
                    }
                    JSONArray array = object.getJSONArray("l");
                    data.clear();
                    Gson gson = new Gson();
                    for (int i = 0, size = array.length(); i < size; i++) {
                        String string = array.getString(i);
                        data.add(gson.fromJson(string, ClassBean.class));
                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(String erroe) {
                refreshLayout.setRefreshing(false);
            }
        });
    }

    //显示数据空 的效果
    private void showErrorView() {
        if (emptyView == null) {
            //ViewStub 只能加载一次
            emptyView = stubView.inflate();
        }
        emptyView.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    private void hintErrorView() {
        if (emptyView == null) {
            emptyView = stubView.inflate();
        }
        emptyView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    public static Fragment newInstance(int i) {
        ClassItemFragment f = new ClassItemFragment();
        Bundle args = new Bundle();
        args.putInt("type", i);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        EventBus.getDefault().register(this);
    }

    @Subscribe
    public void onChange(Event event) {
        switch (event.getType()) {
            case Event.EVENT_CITY_CHANGE:
                isInited = false;
                break;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        EventBus.getDefault().unregister(this);
    }
}
