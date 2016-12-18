package com.yztc.core.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.yztc.core.R;


/**
 * Created by wanggang on 2016/12/15.
 */

public class DivierView extends LinearLayout {


    View dividerLine;
    View dividerRect;

    public DivierView(Context context) {
        super(context);
        init();
    }

    public DivierView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext())
                .inflate(R.layout.view_divider, this, true);
    }
}
