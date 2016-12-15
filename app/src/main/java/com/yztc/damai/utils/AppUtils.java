package com.yztc.damai.utils;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.yztc.damai.App;

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
}
