package com.yztc.core.utils;

import android.os.Environment;

import com.yztc.core.App;

import java.io.File;

/**
 * Created by wanggang on 2016/12/12.
 */

public class FileUtils {

    public static File getCacheFloder(){
        File root;
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            // sdcard/android/data/包名/cache
            root= App.getContext().getExternalCacheDir();
            //Environment.getExternalStorageDirectory()
        }else {
            // /data/data/包名/cache
            root=App.getContext().getCacheDir();
        }
        return root;
    }

    public static File getImageCacheFloder(){
        File cache=new File(getCacheFloder(),"imgs");
        if(!cache.exists()){
            cache.mkdirs();
        }
        return cache;
    }

    public static File getUrlCacheFloder(){
        File cache=new File(getCacheFloder(),"urls");
        if (!cache.exists()) {
            cache.mkdirs();
        }
        return cache;
    }


    public static File getFileCacheFloder() {
        File cache = new File(getCacheFloder(), "file");
        if (!cache.exists()) {
            cache.mkdirs();
        }
        return cache;
    }

    public static File getHttpCacheFile() {
        File cache = new File(getCacheFloder(), "http");
        if(!cache.exists()){
            cache.mkdirs();
        }
        return cache;
    }

    public static File getTempCacheFile() {
        File cache = new File(getCacheFloder(), "temp");
        if (!cache.exists()) {
            cache.mkdirs();
        }
        return cache;
    }

}
