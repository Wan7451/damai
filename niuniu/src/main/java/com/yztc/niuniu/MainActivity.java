package com.yztc.niuniu;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.yztc.niuniu.ui.av.AvFragment;
import com.yztc.niuniu.ui.main.MainAdapter;
import com.yztc.niuniu.ui.maomi.girls.GirlsFragment;
import com.yztc.niuniu.ui.sihu.SihuFragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new AvFragment());
        fragments.add(new SihuFragment());
        fragments.add(new GirlsFragment());


        ArrayList<String> titles = new ArrayList<>();
        titles.add("av");
        titles.add("sihu");
        titles.add("猫咪美女");

        MainAdapter adapter = new MainAdapter(getSupportFragmentManager(),
                fragments, titles);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setOffscreenPageLimit(fragments.size());
    }
}
