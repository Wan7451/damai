package com.yztc.core.net;

import android.util.Log;


import com.yztc.core.utils.AppUtils;
import com.yztc.core.utils.DiskLruCache;
import com.yztc.core.utils.FileUtils;
import com.yztc.core.utils.MD5Utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Date;

/**
 * Created by wanggang on 2016/12/13.
 */

/**
 * 数据缓存
 * 数据缓存时间
 */
public class NetDataCache {

    private static NetDataCache instance;

    private static final int CACHE_SIZE = 10 * 1024 * 1024;
    private static final int CACHE_TIMEOUT = 30 * 60 * 1000;//30分钟

    private DiskLruCache mDiskLruCache;

    private static final boolean DEBUG=true;
    private static final String TAG="==CACHE==>";


    public static NetDataCache getInstance() {
        if (instance == null) {
            synchronized (NetDataCache.class) {
                if (instance == null) {
                    instance = new NetDataCache();
                }
            }
        }

        return instance;
    }


    private NetDataCache() {
        try {
            mDiskLruCache = DiskLruCache.open(
                    FileUtils.getUrlCacheFloder(),
                    AppUtils.getAppVersion(),
                    1,
                    CACHE_SIZE
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void put(String key, String data) {
        String keyForDisk = getKey(key);
        DiskLruCache.Editor edit = null;
        BufferedWriter bw = null;
        //生成超时时间
        StringBuilder builder = new StringBuilder();
        builder.append("<! TIME_OUT:");
        builder.append(System.currentTimeMillis() + CACHE_TIMEOUT);
        builder.append(" !>\n");
        //加入 文件中
        builder.append(data);

        try {
            edit = mDiskLruCache.edit(keyForDisk);
            if (edit == null) return;
            OutputStream os = edit.newOutputStream(0);
            bw = new BufferedWriter(new OutputStreamWriter(os, "utf-8"));
            bw.write(builder.toString());
            edit.commit();//write CLEAN
        } catch (IOException e) {
            e.printStackTrace();
            try {
                edit.abort();//write REMOVE
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } finally {
            try {
                if (bw != null)
                    bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }


    public String getAsString(String key) {
        InputStream inputStream = null;
        try {
            //write READ
            inputStream = get(key);
            if (inputStream == null) return null;
            StringBuilder sb = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
            String temp = null;
            while ((temp = reader.readLine()) != null) {
                if (temp.startsWith("<! TIME_OUT:")) {
                    //读取超时时间
                    String time = temp.substring(12, 25);
                    Long t = Long.parseLong(time);
                    if(DEBUG)
                    Log.i(TAG,"CACHE_TIME_OUT=="+new Date(t).toString());
                    if (System.currentTimeMillis() - t > 0) {
                        if(DEBUG) {
                            Log.i(TAG,"CACHE_TIME_OUT");
                        }
                        //超时
                        inputStream.close();
                        mDiskLruCache.remove(getKey(key));
                        return "";
                    }
                } else {
                    sb.append(temp);
                }
            }
            return sb.toString();

        } catch (IOException e) {
            e.printStackTrace();
            if (inputStream != null)
                try {
                    inputStream.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
        }
        return null;
    }

    private InputStream get(String key) {
        try {
            DiskLruCache.Snapshot snapshot =
                    mDiskLruCache.get(getKey(key));
            if (snapshot == null) {
                return null;
            }
            return snapshot.getInputStream(0);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getKey(String key) {
        return MD5Utils.hashKeyForDisk(key);
    }

    public void onDestroy() {
        instance = null;
        try {
            mDiskLruCache.close();
            mDiskLruCache = null;
        } catch (IOException e) {
        }
    }
}
