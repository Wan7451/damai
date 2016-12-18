package com.yztc.damai.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yztc.damai.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wanggang on 2016/12/16.
 */

public class TypeViewTitle extends LinearLayout {
    @BindView(R.id.type_title)
    TextView typeTitle;
    @BindView(R.id.type_subtitle)
    TextView typeSubtitle;
    @BindView(R.id.title_container)
    LinearLayout titleContainer;

    public TypeViewTitle(Context context) {
        super(context);
        init();
    }

    public TypeViewTitle(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext())
                .inflate(R.layout.view_type_text, this, true);
        ButterKnife.bind(this,this);
    }

    public void setData(String title,String sunTitle){
        if (TextUtils.isEmpty(title)) {
            titleContainer.setVisibility(GONE);
        } else {
            titleContainer.setVisibility(VISIBLE);
            typeTitle.setText(title);

            if (TextUtils.isEmpty(sunTitle)) {
                typeSubtitle.setVisibility(GONE);
            } else {
                typeSubtitle.setVisibility(VISIBLE);
                typeSubtitle.setText(sunTitle);
            }
        }
    }


}
