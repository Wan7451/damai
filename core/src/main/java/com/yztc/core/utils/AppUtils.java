package com.yztc.core.utils;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.yztc.core.App;


/**
 * Created by wanggang on 2016/12/13.
 */

public class AppUtils {

    public static int getAppVersion() {
        try {
            PackageManager manager = App.getContext().getPackageManager();
            PackageInfo info = manager.getPackageInfo(App.getContext().getPackageName(), 0);
            return info.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }
    }

    /**
     * 获取应用程序名称
     */
    public static String getAppName() {
        try {
            PackageManager packageManager = App.getContext().getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    App.getContext().getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return App.getContext().getResources().getString(labelRes);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


}
