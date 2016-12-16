package com.yztc.damai.ui.recommend;

import android.content.Context;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.google.gson.Gson;
import com.yztc.damai.net.NetResponse;
import com.yztc.damai.net.NetUtils;
import com.yztc.damai.utils.DensityUtil;
import com.yztc.damai.view.FullyLinearLayoutManager;
import com.yztc.damai.view.NoScrollListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by wanggang on 2016/12/15.
 */

public class Type11View extends TypeContainerView {
    public Type11View(Context context) {
        super(context);
    }

    public Type11View(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void setData(TypeViewBean data) {
        super.setData(data);
    }

    @Override
    protected void fillTypeView(final TypeViewBean data) {

        NoScrollListView listView = new NoScrollListView(getContext());

        final ArrayList<GuessLikeBean> list = new ArrayList();
        final GuessLikeAdapter adapter = new GuessLikeAdapter(getContext(), list);
        listView.setAdapter(adapter);
        listView.setDividerHeight(DensityUtil.dip2px(getContext(),1));
        addView(listView);

        HashMap<String, String> maps = new HashMap<>();
        maps.put("cityId", "852");
        maps.put("lon", "116.391");
        maps.put("lat", "39.907");
        maps.put("visitorId", "WDBSUDg%2FcM8DADI8fZp%2FGofv");
        NetUtils.getInstance().get("proj/Intelligence/index.aspx", maps, new NetResponse() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);
                    Gson gson = new Gson();
                    for (int i = 0; i < array.length(); i++) {
                        list.add(gson.fromJson(array.getString(i), GuessLikeBean.class));
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
