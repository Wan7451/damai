package com.yztc.damai.ui.mine;


import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.yztc.core.manager.up_user_icon.UserIconHandlerListener;
import com.yztc.core.manager.up_user_icon.UserIconManager;
import com.yztc.core.utils.ToastUtils;
import com.yztc.damai.R;
import com.yztc.damai.databinding.FragmentMineBinding;

import java.io.File;

/**
 * A simple {@link Fragment} subclass.
 */
public class MineFragment extends Fragment {


    public MineFragment() {
        // Required empty public constructor
    }

    private FragmentMineBinding binding;
    private UserIconManager userIconManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_mine, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userIconManager = UserIconManager.getInstance(this);
        binding.setListener(new BtnClick(userIconManager));

        binding.getRoot().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP
                        && keyCode == KeyEvent.KEYCODE_BACK) {
                    ToastUtils.getInstance().showToast("BACK CLICK!");
                    return true;
                }
                return false;
            }
        });

        userIconManager.setIsAutoUp2Server(true);
        userIconManager.setUserIconHandlerListener(new UserIconHandlerListener() {
            @Override
            public void onCropOk(File f) {
                Bitmap bitmap = BitmapFactory.decodeFile(f.getPath());
                binding.userIcon.setImageBitmap(bitmap);
            }

            @Override
            public void onUp2ServerOk(String url) {
                Log.i("==========>>", url);
                Glide.with(MineFragment.this).load(url).into(binding.userIcon);
            }

            @Override
            public void onError(String error) {

            }
        });

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        userIconManager.onActivityResult(requestCode, resultCode, data);
    }

    public static class BtnClick {

        private UserIconManager userIconManager;

        public BtnClick(UserIconManager userIconManager) {
            this.userIconManager = userIconManager;
        }

        public void onSetIconClick(View v) {
            userIconManager.showChoiceDialog();
        }
    }

}
