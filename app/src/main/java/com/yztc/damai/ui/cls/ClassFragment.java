package com.yztc.damai.ui.cls;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yztc.damai.R;
import com.yztc.damai.base.BaseFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class ClassFragment extends BaseFragment {


    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;

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

        ArrayList<Fragment> fragments=new ArrayList<>();
        fragments.add(new ClassItemFragment());
        fragments.add(new ClassItemFragment());
        fragments.add(new ClassItemFragment());
        fragments.add(new ClassItemFragment());

        ClassFragmentAdapter adapter=new ClassFragmentAdapter(getChildFragmentManager(),fragments);
        viewPager.setOffscreenPageLimit(fragments.size());
        viewPager.setAdapter(adapter );
        tabLayout.setupWithViewPager(viewPager);
    }
}
