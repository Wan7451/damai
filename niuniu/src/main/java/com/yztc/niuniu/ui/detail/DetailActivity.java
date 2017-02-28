package com.yztc.niuniu.ui.detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.yztc.niuniu.R;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        getSupportActionBar().hide();

        FragmentManager manager = getSupportFragmentManager();
        DetailFragment fragment = new DetailFragment();
        fragment.setArguments(getIntent().getExtras());
        manager.beginTransaction().add(R.id.activity_detail, fragment).commit();
    }

    public static void start(Context context, ArrayList<String> data) {
        Intent i = new Intent();
        i.setClass(context, DetailActivity.class);
        i.putExtra("imgs", data);
        context.startActivity(i);
    }

}
