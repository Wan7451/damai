package com.yztc.core.manager.up_user_icon;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;

import com.yztc.core.R;
import com.yztc.core.databinding.DialogChoiceIconBinding;

/**
 * Created by wanggang on 2017/1/2.
 */

public class UserIconManager {


    /**
     * 显示 选择裁剪图片 方式 对话框
     */
    public void showChoiceDialog() {

        LayoutInflater inflater = LayoutInflater.from(hanlder.getContext());
        DialogChoiceIconBinding binding = DataBindingUtil.inflate(inflater, R.layout.dialog_choice_icon, null, false);

        BottomSheetDialog dialog = new BottomSheetDialog(hanlder.getContext());
        dialog.setContentView(binding.getRoot());
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

        binding.setListener(new UserIconListener(hanlder, dialog));
    }

    /**
     * 回调
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        hanlder.onActivityResult(requestCode, resultCode, data);
    }


    /**
     * 界面点击事件
     */
    public static class UserIconListener {

        private BottomSheetDialog dialog;
        private UserIconHanlder hanlder;

        public UserIconListener(UserIconHanlder hanlder, BottomSheetDialog dialog) {
            this.dialog = dialog;
            this.hanlder = hanlder;
        }

        public void onChoicePick(View v) {
            hanlder.onChoicePick();
            dialog.cancel();
        }

        public void onChoiceCapture(View v) {
            hanlder.onChoiceCapture();
            dialog.cancel();
        }

        public void onCancle(View v) {
            dialog.cancel();
        }
    }


    public void setUserIconHandlerListener(UserIconHandlerListener l) {
        hanlder.setUserIconHandlerListener(l);
    }

    public void setIsAutoUp2Server(boolean autoUp) {
        hanlder.setIsAutoUp2Server(autoUp);
    }


    private static UserIconHanlder hanlder;
    private static UserIconManager instance;

    public static UserIconManager getInstance(Activity activity) {
        if (instance != null && hanlder != null && hanlder.getContext() == activity) {
            return instance;
        } else {
            instance = new UserIconManager();
            hanlder = UserIconHanlderFactory.create(activity);
        }
        return instance;
    }

    public static UserIconManager getInstance(Fragment fragment) {
        if (instance != null && hanlder != null && hanlder.getContext() == fragment.getContext()) {
            return instance;
        } else {
            instance = new UserIconManager();
            hanlder = UserIconHanlderFactory.create(fragment);
        }
        return instance;
    }


}
