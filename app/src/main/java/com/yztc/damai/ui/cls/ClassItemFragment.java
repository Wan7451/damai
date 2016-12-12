package com.yztc.damai.ui.cls;


import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.*;
import android.view.View;

import com.google.gson.Gson;
import com.yztc.damai.R;
import com.yztc.damai.base.LazyFragment;
import com.yztc.damai.net.NetResponse;
import com.yztc.damai.net.NetUtils;

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

    public ClassItemFragment() {
    }


    private ArrayList<ClassBean> data = new ArrayList<>();
    private ClassAdapter adapter;



    @Override
    protected int getLayoutId() {
        return R.layout.fragment_item_class;
    }
    @Override
    protected void init(View v) {
        refreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipeRefreshView);
        recyclerView = (RecyclerView) v.findViewById(R.id.recycleView);

        DividerItemDecoration decoration=new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL);
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

                refreshLayout.setRefreshing(true);
                loadData();



//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            int lastVisibleItem;
//
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == adapter.getItemCount()) {
//                    if(!refreshLayout.isRefreshing())
//                    refreshLayout.setRefreshing(true);
//                }
//            }
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//
//                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
//                if(layoutManager instanceof LinearLayoutManager){
//
//                    int last = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
//                    if(last!=-1){
//                        lastVisibleItem=last;
//                    }
//                }
//
//
//            }
//        });
    }

    private void loadData() {
        HashMap<String, String> params = new HashMap<>();
        params.put("cc", "0");
        params.put("ps", "20");
        params.put("mc", "0");
        params.put("ot", "0");
        params.put("v", "0");
        params.put("p", "1");
        params.put("cityId", "852");

        NetUtils.getInstance().get("ProjLst.aspx", params, new NetResponse() {
            @Override
            public void onResponse(String response) {
                refreshLayout.setRefreshing(false);
                try {
                    JSONObject object = new JSONObject(response);
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

            }
        });
    }
}
