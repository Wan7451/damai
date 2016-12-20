package com.yztc.damai.ui.cls;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yztc.core.base.BaseFragment;
import com.yztc.core.utils.SPUtils;
import com.yztc.damai.R;
import com.yztc.damai.help.Constant;
import com.yztc.damai.help.Event;
import com.yztc.damai.ui.others.ChoiceCityActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class ClassFragment extends BaseFragment {


    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.choiceCity)
    TextView choiceCity;

    private ArrayList<ClassItemFragment> fragments;

    private String[] titles = {"全部分类", "演唱会", "音乐会", "话题歌剧", "舞蹈芭蕾", "曲苑杂坛", "体育比赛", "休闲度假", "周边商品", "儿童亲子", "动漫"};
    private String cityName;

    public ClassFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_class, container, false);
        ButterKnife.bind(this, root);
        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        cityName = (String) SPUtils.get(getContext(), Constant.SP_CURR_CITY_N, "北京");
        choiceCity.setText(cityName);

        fragments = new ArrayList<>();
        for (int i = 0, len = titles.length; i < len; i++) {
            fragments.add(ClassItemFragment.newInstance(i));
        }


        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        ClassFragmentAdapter adapter = new ClassFragmentAdapter(getChildFragmentManager(), fragments, titles);
        //预加载界面的个数
        viewPager.setOffscreenPageLimit(fragments.size());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
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
                cityName = (String) SPUtils.get(getContext(), Constant.SP_CURR_CITY_N, "北京");
                choiceCity.setText(cityName);
                ClassItemFragment currFrag = fragments.get(viewPager.getCurrentItem());
                currFrag.refreshData();


                break;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        EventBus.getDefault().unregister(this);
    }

    @OnClick(R.id.choiceCity)
    public void onClick() {
        ChoiceCityActivity.start(getContext());
    }
}
