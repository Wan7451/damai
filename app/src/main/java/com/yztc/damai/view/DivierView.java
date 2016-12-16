package com.yztc.damai.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.yztc.damai.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wanggang on 2016/12/15.
 */

public class DivierView extends LinearLayout {


    @BindView(R.id.divider_line)
    View dividerLine;
    @BindView(R.id.divider_rect)
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
        ButterKnife.bind(this,this);
    }
}
