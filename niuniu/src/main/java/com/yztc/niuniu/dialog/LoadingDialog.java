package com.yztc.niuniu.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yztc.niuniu.R;


/**
 * Created by wanggang on 2016/12/29.
 * <p>
 * <p>
 * 仿微信加载对话框
 */

public class LoadingDialog extends Dialog {

    private TextView tv;

    public LoadingDialog(Context context) {
        super(context, R.style.loadingDialogStyle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loading);
        tv = (TextView) findViewById(R.id.tv);
        tv.setVisibility(View.GONE);
        LinearLayout linearLayout = (LinearLayout) this.findViewById(R.id.LinearLayout);
        linearLayout.getBackground().setAlpha(210);
    }

    public void setMessage(String message) {
        tv.setText(message);
        tv.setVisibility(View.VISIBLE);
    }

}

