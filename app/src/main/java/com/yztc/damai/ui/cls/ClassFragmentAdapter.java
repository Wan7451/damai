package com.yztc.damai.ui.cls;

import android.print.PageRange;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by wanggang on 2016/12/12.
 */

public class ClassFragmentAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> fragments;

    public ClassFragmentAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public int getCount() {
        return fragments!=null?fragments.size():0;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "aaa"+position;
    }
}
