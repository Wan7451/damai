package com.yztc.damai;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.yztc.damai.utils.ToastUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    long curr;
    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - curr > 2000) {
            curr = System.currentTimeMillis();
            ToastUtils.show("再按一次退出");
        } else {
            App.getContext().onDestory();
            super.onBackPressed();
        }
    }

}
