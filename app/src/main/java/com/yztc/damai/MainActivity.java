package com.yztc.damai;

import android.support.annotation.RequiresPermission;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.yztc.damai.image.ImageLoader;
import com.yztc.damai.net.NetDataCache;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    long curr;
    @Override
    public void onBackPressed() {
        if(System.currentTimeMillis()-curr>2000){
            curr=System.currentTimeMillis();
        }else {
            App.getContext().onDestory();
            super.onBackPressed();
        }
    }
}
