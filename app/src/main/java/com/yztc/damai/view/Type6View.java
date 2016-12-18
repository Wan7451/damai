package com.yztc.damai.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.yztc.damai.net.NetResponse;
import com.yztc.damai.net.NetUtils;
import com.yztc.damai.ui.recommend.RecommendAdapter;
import com.yztc.damai.ui.recommend.RecommendBean;
import com.yztc.damai.ui.recommend.TypeViewBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by wanggang on 2016/12/15.
 */

public class Type6View extends TypeContainerView {
    public Type6View(Context context) {
        super(context);
    }

    public Type6View(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setData(TypeViewBean data) {
        super.setData(data);
    }

    @Override
    protected void fillTypeView(final TypeViewBean data) {
        RecyclerView recycler=new RecyclerView(getContext());
        recycler.setNestedScrollingEnabled(false);
        LinearLayoutManager mgr=new LinearLayoutManager(getContext());
        mgr.setOrientation(LinearLayoutManager.HORIZONTAL);
        recycler.setLayoutManager(mgr);
        final ArrayList<RecommendBean> list=new ArrayList();
        final RecommendAdapter adapter=new RecommendAdapter(getContext(),list);
        recycler.setAdapter(adapter);
        addView(recycler);

        HashMap<String,String> maps=new HashMap<>();
        maps.put("cityId","852");
        NetUtils.getInstance().get("proj/HotProjV1.aspx", maps, new NetResponse() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object=new JSONObject(response);
                    JSONArray array = object.getJSONArray("list");
                    Gson gson=new Gson();
                    for (int i = 0; i < array.length() ; i++) {
                        list.add(gson.fromJson(array.getString(i),RecommendBean.class));
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
