package com.yztc.core.views;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * Created by wanggang on 2016/12/21.
 * <p>
 * 一旦用户屏蔽了 App 的通知权限，则 Toast 也不会显示了！
 * <p>
 * 通过 WindowManager 来处理 ToastView. WindowManager
 * 管理着每个窗口的前后顺序，如果我们把 ToastView 添加到
 * WindowManager 里面，则就可以一直显示在屏幕上
 */

public class Toast {
    public static final int LENGTH_SHORT = 0;
    public static final int LENGTH_LONG = 1;
    private static final int LONG_DELAY = 3500;
    private static final int SHORT_DELAY = 2000;
    private static Context context;
    private static WindowManager windowManager;

    public static void init(Context context) {
        Toast.context = context.getApplicationContext();
        windowManager = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
    }

    public static void show(String text) {
        show(text, LENGTH_SHORT);
    }

    public static void show(final String text, int length) {
        final TextView textView = new TextView(context);
        textView.setBackgroundResource(android.R.drawable.toast_frame); //设置成官方原生的 Toast 背景
        textView.setText(text);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.type = WindowManager.LayoutParams.TYPE_TOAST;
        lp.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE; //设置这个 window 不可点击，不会获取焦点，这样可以不干扰背后的 Activity 的交互。
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.format = PixelFormat.TRANSLUCENT; //这样可以保证 Window 的背景是透明的，不然背景可能是黑色或者白色。
        lp.windowAnimations = android.R.style.Animation_Toast; //使用官方原生的 Toast 动画效果

        windowManager.addView(textView, lp);
        textView.postDelayed(new Runnable() { // 指定时间后，取消 Toast 显示
            @Override
            public void run() {
                windowManager.removeView(textView);
            }
        }, length == LENGTH_SHORT ? SHORT_DELAY : LONG_DELAY);
    }

}

