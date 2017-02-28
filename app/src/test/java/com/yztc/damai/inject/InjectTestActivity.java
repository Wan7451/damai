package com.yztc.damai.inject;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.TextView;

import com.yztc.damai.R;

/**
 * Created by wanggang on 2017/1/6.
 */

@LayouyInject(R.layout.activity_main)
public class InjectTestActivity extends InjectActivity {


    @ViewInject(R.id.alignBounds)
    private TextView text;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }
}
