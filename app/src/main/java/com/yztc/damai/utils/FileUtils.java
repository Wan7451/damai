package com.yztc.damai.utils;

import android.os.Environment;

import com.yztc.damai.App;

import java.io.File;

/**
 * Created by wanggang on 2016/12/12.
 */

public class FileUtils {

    public static File getCacheFloder(){
        File root;
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            root=App.context.getExternalCacheDir();
        }else {
            root=App.context.getCacheDir();
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
        if(!cache.exists()){
            cache.mkdirs();
        }
        return cache;
    }

}
