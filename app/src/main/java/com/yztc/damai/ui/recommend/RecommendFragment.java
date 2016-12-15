package com.yztc.damai.ui.recommend;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.yztc.damai.R;
import com.yztc.damai.net.NetConfig;
import com.yztc.damai.net.NetResponse;
import com.yztc.damai.net.NetUtils;
import com.yztc.damai.utils.ToastUtils;
import com.yztc.damai.view.BannerView;
import com.yztc.damai.view.BannerViewPager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecommendFragment extends Fragment {

    @BindView(R.id.bannerView)
    BannerView bannerView;
    @BindView(R.id.classifyView)
    ClassifyView classifyView;
    private ArrayList<BannerBean> banners = new ArrayList<>();
    private ArrayList<String> bannerStr = new ArrayList<>();

    private  ArrayList<ClassifyBean>  classifys=new ArrayList<ClassifyBean>();
    private ArrayList<HeadLineBean> headLines=new ArrayList<>();

    private Gson gson=new Gson();

    public RecommendFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recommend, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    private NetUtils netUtils;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        netUtils = NetUtils.getInstance();
        initBannerView(view);
        loadBanner();
    }


    private void initBannerView(View v) {
        bannerView.setOnBannerViewClick(new BannerViewPager.OnBannerViewClick() {
            @Override
            public void onBannerViewClick(int position) {
                ToastUtils.show(banners.get(position).getName());
            }
        });
    }

    private void loadData(){
        netUtils.get("Proj/Panev3.aspx", null, new NetResponse() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object=new JSONObject(response);

                    JSONArray btnCtx = object.getJSONArray("btnCtx");
                    classifys.clear();
                    for (int i = 0,len=btnCtx.length(); i <len ; i++) {
                        classifys.add(gson.fromJson(btnCtx.getString(i),ClassifyBean.class));
                    }
                    classifyView.setClassifys(classifys);

                    headLines.clear();
                    JSONArray headline = object.getJSONArray("headline");
                    for (int i = 0,len=headline.length(); i < len ; i++) {
                        headLines.add(gson.fromJson(headline.getString(i),HeadLineBean.class));
                    }
                    classifyView.setHeadLines(headLines);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(String erroe) {

            }
        });
    }



    private void loadBanner() {
        HashMap<String, String> maps = new HashMap<>();
        maps.put("cityId", "852");

        netUtils.get(NetConfig.BASE_URL2,
                "index/banner/13/list.json",
                maps, new NetResponse() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            banners.clear();
                            bannerStr.clear();
                            for (int i = 0, len = array.length(); i < len; i++) {
                                banners.add(gson.fromJson(array.getString(i), BannerBean.class));
                                bannerStr.add(array.getJSONObject(i).getString("Pic"));
                            }
                            bannerView.setData(bannerStr);

                            loadData();

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
