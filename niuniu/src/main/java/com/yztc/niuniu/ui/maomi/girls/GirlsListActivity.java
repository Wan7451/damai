package com.yztc.niuniu.ui.maomi.girls;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.yztc.niuniu.R;

public class GirlsListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_girls_list);
        getSupportActionBar().hide();

        GirlsListFragment fragment = new GirlsListFragment();
        fragment.setArguments(getIntent().getExtras());

        FragmentManager manager = getSupportFragmentManager();

        manager.beginTransaction()
                .add(R.id.activity_girls_list, fragment)
                .commit();
    }

    public static void start(Context context, String path) {
        Intent i = new Intent();
        i.setClass(context, GirlsListActivity.class);
        i.putExtra("path", path);
        context.startActivity(i);
    }

}
