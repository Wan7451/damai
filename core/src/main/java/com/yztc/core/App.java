package com.yztc.core;

import android.app.ActivityManager;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.support.multidex.MultiDex;
import android.text.TextUtils;

import com.bumptech.glide.Glide;
import com.yztc.core.base.LoadResActivity;
import com.yztc.core.manager.ActivityStackManager;
import com.yztc.core.manager.DownLoadFileManager;
import com.yztc.core.manager.LimitCacheManager;
import com.yztc.core.utils.AppUtils;
import com.yztc.core.utils.LogUtils;

import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

/**
 * Created by wanggang on 2016/12/12.
 */

public class App extends Application {

    //App  Context
    //Activity、Service Context
    //1 全局
    //2 生命周期长
    private static App context;


    @Override
    public void onCreate() {
        super.onCreate();
        if (quickStart()) {
            return;
        }

        context=this;
    }

    public static App getContext(){
        return context;
    }

    //结束
    public void onDestory(){
        Glide.get(this).clearMemory();
        ActivityStackManager.getInstance().onDestory();
        LimitCacheManager.getInstance().onDestroy();
        DownLoadFileManager.getInstance().onDestroy();
    }

    public static final String KEY_DEX2_SHA1 = "dex2-SHA1-Digest";

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        LogUtils.d("loadDex", "App attachBaseContext ");
        if (!quickStart() && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {//>=5.0的系统默认对dex进行oat优化
            if (needWait(base)) {
                waitForDexopt(base);
            }
            MultiDex.install(this);
        } else {
            return;
        }
    }

    public boolean quickStart() {
        if (getCurProcessName(this).contains(":mini")) {
            LogUtils.d("loadDex", ":mini start!");
            return true;
        }
        return false;
    }

    //neead wait for dexopt ?
    private boolean needWait(Context context) {
        String flag = get2thDexSHA1(context);
        if (TextUtils.isEmpty(flag)) {
            return false;
        }
        LogUtils.d("loadDex", "dex2-sha1 " + flag);
        SharedPreferences sp = context.getSharedPreferences(
                AppUtils.getPackageInfo(context).versionName, MODE_MULTI_PROCESS);
        String saveValue = sp.getString(KEY_DEX2_SHA1, "");
        return !flag.equals(saveValue);
    }

    /**
     * Get classes.dex file signature
     *
     * @param context
     * @return
     */
    private String get2thDexSHA1(Context context) {
        ApplicationInfo ai = context.getApplicationInfo();
        String source = ai.sourceDir;
        try {
            JarFile jar = new JarFile(source);
            Manifest mf = jar.getManifest();
            Map<String, Attributes> map = mf.getEntries();
            Attributes a = map.get("classes2.dex");
            return a.getValue("SHA1-Digest");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // optDex finish
    public void installFinish(Context context) {
        SharedPreferences sp = context.getSharedPreferences(
                AppUtils.getPackageInfo(context).versionName, MODE_MULTI_PROCESS);
        sp.edit().putString(KEY_DEX2_SHA1, get2thDexSHA1(context)).apply();
    }

    public static String getCurProcessName(Context context) {
        try {
            int pid = android.os.Process.myPid();
            ActivityManager mActivityManager = (ActivityManager) context
                    .getSystemService(Context.ACTIVITY_SERVICE);
            for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager
                    .getRunningAppProcesses()) {
                if (appProcess.pid == pid) {
                    return appProcess.processName;
                }
            }
        } catch (Exception e) {
            // ignore
        }
        return null;
    }

    public void waitForDexopt(Context base) {
        Intent intent = new Intent();
        ComponentName componentName = new
                ComponentName("com.yztc.core.base", LoadResActivity.class.getName());
        intent.setComponent(componentName);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        base.startActivity(intent);
        long startWait = System.currentTimeMillis();
        long waitTime = 10 * 1000;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR1) {
            waitTime = 20 * 1000;//实测发现某些场景下有些2.3版本有可能10s都不能完成optdex
        }
        while (needWait(base)) {
            try {
                long nowWait = System.currentTimeMillis() - startWait;
                LogUtils.d("loadDex", "wait ms :" + nowWait);
                if (nowWait >= waitTime) {
                    return;
                }
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
