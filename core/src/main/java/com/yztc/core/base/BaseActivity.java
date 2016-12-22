package com.yztc.core.base;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by wanggang on 2016/12/18.
 */

public class BaseActivity extends AppCompatActivity {

    private Context context;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        context = getBaseContext();
    }

    public Context getContext() {
        return context;
    }
}
