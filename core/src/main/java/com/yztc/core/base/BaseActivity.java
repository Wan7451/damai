package com.yztc.core.base;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.yztc.core.manager.ActivityStackManager;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by wanggang on 2016/12/18.
 */

public abstract class BaseActivity extends AppCompatActivity {

    protected abstract int getLayoutResource();

    protected abstract void onInitView(Bundle bundle);

    protected abstract void onInitData();


    /**
     * 权限授权回掉
     */
    private PermissionListener mPermissionListener;
    private Context mContext;

    /***
     * 用于在初始化View之前做一些事
     */
    protected void initPre(Bundle savedInstanceState) {

    }

    /***
     * 用于在初始化Data之前做一些事
     */
    protected void initPreData(Bundle savedInstanceState) {

    }

    /***
     * 是否需要使用EventBus
     */
    protected boolean isNeedInitEventBus() {
        return false;
    }

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        mContext = getBaseContext();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        initPre(savedInstanceState);
        if (getLayoutResource() != 0)
            setContentView(getLayoutResource());
        ButterKnife.bind(this);
        this.onInitView(savedInstanceState);
        initPreData(savedInstanceState);

        if (isNeedInitEventBus()) {
            EventBus.getDefault().register(this);
        }
        //入栈
        ActivityStackManager.getInstance().pushActivity(this);
    }

    private boolean isFrist = true;

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && isFrist) {
            isFrist = false;
            //页面加载完成，加载数据
            this.onInitData();
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //出栈
        ActivityStackManager.getInstance().popActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isNeedInitEventBus()) {
            EventBus.getDefault().unregister(this);
        }
    }

    public Context getContext() {
        return mContext;
    }


    protected void startActivityWithoutExtras(Class<?> clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }

    protected void startActivityWithExtras(Class<?> clazz, Bundle extras) {
        Intent intent = new Intent(this, clazz);
        intent.putExtras(extras);
        startActivity(intent);

    }

    protected void addFragment(int id, Fragment fragment) {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        supportFragmentManager.beginTransaction().add(id, fragment).commit();
    }

    protected void replaceFragment(int id, Fragment fragment) {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        supportFragmentManager.beginTransaction().replace(id, fragment).commit();
    }


    /**
     * 权限申请
     *
     * @param permissions
     * @param listener
     */
    protected void requestRunTimePermission(String[] permissions, PermissionListener listener) {

        //todo 获取栈顶activity，如果null。return；
        this.mPermissionListener = listener;

        List<String> permissionList = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(permission);
            }
        }
        if (!permissionList.isEmpty()) {
            ActivityCompat.requestPermissions(this, permissionList.toArray(new String[permissionList.size()]), 1);
        } else {
            listener.onGranted();
        }
    }

    /**
     * 申请结果
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    List<String> deniedPermissions = new ArrayList<>();
                    List<String> grantedPermissions = new ArrayList<>();
                    for (int i = 0; i < grantResults.length; i++) {
                        int grantResult = grantResults[i];
                        if (grantResult != PackageManager.PERMISSION_GRANTED) {
                            String permission = permissions[i];
                            deniedPermissions.add(permission);
                        } else {
                            String permission = permissions[i];
                            grantedPermissions.add(permission);
                        }
                    }

                    if (deniedPermissions.isEmpty()) {
                        mPermissionListener.onGranted();
                    } else {
                        mPermissionListener.onDenied(deniedPermissions);
                        mPermissionListener.onGranted(grantedPermissions);
                    }
                }
                break;
        }
    }

}
