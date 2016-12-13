package com.yztc.damai.net;

import android.util.Log;

import com.yztc.damai.utils.DiskLruCache;
import com.yztc.damai.utils.FileUtils;
import com.yztc.damai.utils.MD5Utils;
import com.yztc.damai.utils.AppUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.nio.Buffer;
import java.nio.charset.Charset;

/**
 * Created by wanggang on 2016/12/13.
 */

public class NetDataCache {

    private static NetDataCache instance;

    private static final int CACHE_SIZE = 10 * 1024 * 1024;
    private static final int CACHE_TIMEOUT = 10 * 60 * 1000;

    private DiskLruCache mDiskLruCache;


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

        StringBuilder builder=new StringBuilder();
        builder.append("<! TIME_OUT:");
        builder.append(System.currentTimeMillis()+CACHE_TIMEOUT);
        builder.append(" !>\n");
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
            BufferedReader reader=new BufferedReader(new InputStreamReader(inputStream,"utf-8"));
            String temp=null;
            while ((temp = reader.readLine()) != null) {
                sb.append(temp);
            }
            String data= sb.toString();

            if(data.startsWith("<!")) {
                String time = data.substring(12, 25);
                Long t=Long.parseLong(time);
                if(System.currentTimeMillis()-t>CACHE_TIMEOUT){
                    //超时
                    mDiskLruCache.remove(getKey(key));
                    return "";
                }
                String endTag="!>\n";
                int indexOf = data.indexOf(endTag);
                data=data.substring(indexOf+endTag.length(),data.length());
            }

            return data;

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
            DiskLruCache.Snapshot snapshot = mDiskLruCache.get(getKey(key));
            if (snapshot == null) {
                //not find entry , or entry.readable = false
                return null;
            }
            //write READ
            return snapshot.getInputStream(0);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getKey(String key){
        return MD5Utils.hashKeyForDisk(key);
    }
}
