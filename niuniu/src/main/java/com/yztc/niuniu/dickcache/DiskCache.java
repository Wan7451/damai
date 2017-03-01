package com.yztc.niuniu.dickcache;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.yztc.core.disklrucache.DiskLruCache;
import com.yztc.core.utils.MD5Utils;
import com.yztc.niuniu.App;
import com.yztc.niuniu.util.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by wanggang on 2017/2/26.
 */

public class DiskCache {


    private static final int CACHE_TIME_OUT = 24 * 60 * 60 * 1000;  //1天
    private DiskLruCache mDiskLruCache = null;
    private static DiskCache instance;

    private DiskCache() {
        try {
            File cacheDir = FileUtils.getHttpCacheFile();
            mDiskLruCache = DiskLruCache.open(
                    cacheDir,   //缓存路径
                    getAppVersion(),  //app 版本
                    2,  //键值对保存数据   key   缓存数据、获取数据的时间  1key2values
                    300 * 1024 * 1024);//缓存空间大小
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static DiskCache getInstance() {
        if (instance == null) {
            synchronized (DiskCache.class) {
                if (instance == null) {
                    instance = new DiskCache();
                }
            }
        }
        return instance;
    }

    public String getString(String key) {
        //DiskLruCache  key  就是文件的名字

        DiskLruCache.Snapshot snapshot = null;
        try {
            snapshot = mDiskLruCache.get(getLruKey(key));
            if (snapshot == null) //没有缓存过数据
                return "";
            return snapshot.getString(1);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (snapshot != null)
                snapshot.close();
        }
        return "";
    }


    public void putString(String key, String cache) {
        DiskLruCache.Editor edit = null;
        try {
            edit = mDiskLruCache.edit(getLruKey(key));
            edit.set(0, putTime());
            edit.set(1, cache);
            edit.commit();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                mDiskLruCache.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    public boolean isTimeOut(String key, long timeOut) {
        DiskLruCache.Snapshot snapshot = null;
        try {
            snapshot = mDiskLruCache.get(getLruKey(key));
            if (snapshot == null)
                return true;
            String timeStr = snapshot.getString(0);
            //当时获取数据的时间
            long time = Long.parseLong(timeStr);
            return System.currentTimeMillis() - time > timeOut;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (snapshot != null)
                snapshot.close();
        }
        return true;
    }


    public boolean isTimeOut(String key) {
        return isTimeOut(key, CACHE_TIME_OUT);
    }

    private String getLruKey(String key) {
        return MD5Utils.hashKeyForDisk(key);
    }


    private String putTime() {
        return System.currentTimeMillis() + "";
    }


//    public String getStringNoTimeOut(String key) {
//        return getString(key, System.currentTimeMillis() * 2);
//    }
//
//
//    public String getString(String key, long timeOut) {
//        DiskLruCache.Snapshot snapshot = null;
//        try {
//            snapshot = mDiskLruCache.get(getLruKey(key));
//            if (snapshot == null)
//                return "";
//            if (isTimeOut(key, timeOut)) {
//                return "";
//            }
//            return snapshot.getString(1);
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (snapshot != null)
//                snapshot.close();
//        }
//        return "";
//    }

    private int getAppVersion() {
        try {
            PackageInfo info = App.getContext().getPackageManager().getPackageInfo(App.getContext().getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }

}
