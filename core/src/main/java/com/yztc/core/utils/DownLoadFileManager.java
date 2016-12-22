package com.yztc.core.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by wanggang on 2016/12/13.
 */

/**
 * 文件缓存
 */
public class DownLoadFileManager {

    private static DownLoadFileManager instance;

    private static final int CACHE_SIZE = 100 * 1024 * 1024;

    private DiskLruCache mDiskLruCache;

    private static final boolean DEBUG = true;
    private static final String TAG = "==CACHE==>";


    public static DownLoadFileManager getInstance() {
        if (instance == null) {
            synchronized (DownLoadFileManager.class) {
                if (instance == null) {
                    instance = new DownLoadFileManager();
                }
            }
        }

        return instance;
    }


    private DownLoadFileManager() {
        try {
            mDiskLruCache = DiskLruCache.open(
                    FileUtils.getFileCacheFloder(),
                    AppUtils.getAppVersion(),
                    1,
                    CACHE_SIZE
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 缓存数据
     *
     * @param key
     * @param in
     * @return
     */
    public boolean put(String key, InputStream in) {
        String keyForDisk = getKey(key);
        DiskLruCache.Editor edit = null;
        BufferedOutputStream bw = null;
        BufferedInputStream br = null;

        try {
            edit = mDiskLruCache.edit(keyForDisk);
            if (edit == null) return false;
            OutputStream os = edit.newOutputStream(0);
            bw = new BufferedOutputStream(os);
            br = new BufferedInputStream(in);

            byte[] b = new byte[1024 * 5];
            int l = 0;
            while ((l = br.read(b)) != -1) {
                bw.write(b, 0, l);
            }
            bw.flush();
            edit.commit();//write CLEAN
            return true;
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
                if (br != null)
                    br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;

    }


    public boolean isHasFile(String key) {
        DiskLruCache.Snapshot snapshot = null;
        try {
            snapshot = mDiskLruCache.get(getKey(key));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return snapshot != null;
    }


    /**
     * 获取缓存的图片
     *
     * @param key
     * @return
     */
    public Bitmap getAsBitmap(String key) {
        InputStream inputStream = null;
        try {
            //write READ
            inputStream = get(key);
            if (inputStream == null) return null;

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            return BitmapFactory.decodeStream(inputStream, null, options);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
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

    /**
     * 下载文件
     *
     * @param urlPath
     */
    public void downLoad(String urlPath) {
        InputStream stream = null;
        HttpURLConnection conn = null;
        try {
            URL url = new URL(urlPath);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(30 * 1000);
            conn.setReadTimeout(30 * 1000);
            int code = conn.getResponseCode();
            if (code == 200) {
                stream = conn.getInputStream();
                //缓存
                if (put(urlPath, stream)) {
                    LogUtils.i(TAG, "下载完成");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    /**
     * 移除文件
     *
     * @param key
     * @return
     */
    public boolean remove(String key) {
        try {
            key = MD5Utils.hashKeyForDisk(key);
            return mDiskLruCache.remove(key);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
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
