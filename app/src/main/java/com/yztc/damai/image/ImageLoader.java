package com.yztc.damai.image;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.util.LruCache;
import android.view.View;
import android.widget.ImageView;

import com.yztc.damai.App;
import com.yztc.damai.utils.FileUtils;
import com.yztc.damai.utils.MD5Utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by wanggang on 2016/12/12.
 */

public class ImageLoader {


    private static final int SOFT_CACHE_SIZE = 15;
    private static final int DISK_CACHE_SIZE = 20 * 1024 * 1024;
    private static final int MAX_THREAD_NUM = 6;

    private LruCache<String, Bitmap> mLruCache;
    private LinkedHashMap<String, SoftReference<Bitmap>> mSoftCache;
    private DiskLruCache mDiskCache;

    //下载队列
    private Set<ImageDownloadTask> loadingQueue;
    // 等待下载队列
    private List<ImageDownloadTask> waitingQueue;
    //正在进行下载任务的imageview
    private LinkedHashMap<String, View> imageViewManager;

    private static ImageLoader instance = null;

    public static ImageLoader getInstance() {
        if (instance == null) {
            synchronized (ImageLoader.class) {
                if (instance == null)
                    instance = new ImageLoader();
            }
        }
        return instance;
    }


    /**
     * 加载图片
     *
     * @param imgView  View
     * @param imageUrl 图片地址
     * @param priority 优先级
     */
    public void loadImages(ImageView imgView, String imageUrl, boolean priority) {

        Bitmap bitmap = getBitmapFromCache(imageUrl);
        if (bitmap == null) {
            imageViewManager.put(imageUrl, imgView);
            ImageDownloadTask task = new ImageDownloadTask(imageUrl);
            addTaskToQueue(task, priority);
        } else {
            if (imgView != null && bitmap != null) {
                imgView.setImageBitmap(bitmap);
            }
        }
    }

    private void addTaskToQueue(ImageDownloadTask task, boolean priority) {

        if (loadingQueue.size() < MAX_THREAD_NUM) {
            //添加到下载队列
            loadingQueue.add(task);
            task.execute();
        } else {
            //添加到等待队列
            if (priority)
                waitingQueue.add(0, task);
            else
                waitingQueue.add(task);
        }
    }


    private Bitmap getBitmapFromCache(String imageUrl) {
        Bitmap bitmap = mLruCache.get(imageUrl);
        if (bitmap != null) {
            return bitmap;
        }

        SoftReference<Bitmap> softReference =
                mSoftCache.get(imageUrl);
        if (softReference != null) {
            bitmap = softReference.get();
            if (bitmap != null) {
                mLruCache.put(imageUrl, bitmap);
                mSoftCache.remove(imageUrl);
                return bitmap;
            } else {
                mSoftCache.remove(imageUrl);
            }
        }

        return null;
    }


    private ImageLoader() {
        //Lru
        initLruCache();
        //Soft
        initSoftCache();
        //disk
        initLocalCache();
        //正在下载的队列  只能存储不重复的对象
        loadingQueue = new HashSet<>();
        //等待下载的队列
        waitingQueue = new ArrayList<>();
        //imageView  url 绑定
        imageViewManager = new LinkedHashMap<>();
    }

    private void initLocalCache() {
        try {
            //1  缓存路径
            //2  app版本  当版本变化时，会清除之前的数据
            //3  key-value  1   1key对应1value个值
            //4  缓存空间大小
            mDiskCache = DiskLruCache.open(
                    FileUtils.getImageCacheFloder(),
                    getAppVersion(),
                    1,
                    DISK_CACHE_SIZE
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //SoftCache
    private void initSoftCache() {
        //true  LRU算法
        mSoftCache = new LinkedHashMap<String, SoftReference<Bitmap>>(
                SOFT_CACHE_SIZE, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Entry eldest) {
                //限制 链表的长度
                if (size() > SOFT_CACHE_SIZE) {
                    //在添加新 item时，移除最不常用的 item
                    return true;
                }
                return false;
            }
        };
    }

    //LruCache
    private void initLruCache() {
        //系统为App 分配的最大内存
        long maxMemory = Runtime.getRuntime().maxMemory();
        mLruCache = new LruCache<String, Bitmap>((int) (maxMemory / 8)) {
            //计算每个元素占用空间的大小
            @Override
            protected int sizeOf(String key, Bitmap value) {
                //png  宽*高*32
                if (value != null)
                    return value.getRowBytes() * value.getHeight();
                return 0;
            }

            //当空间满的时候，元素被挤出来
            @Override
            protected void entryRemoved(boolean evicted, String key, Bitmap oldValue, Bitmap newValue) {
                super.entryRemoved(evicted, key, oldValue, newValue);
                if (oldValue != null) {
                    mSoftCache.put(key, new SoftReference<Bitmap>(oldValue));
                }
            }
        };
    }


    private int getAppVersion() {
        try {
            PackageManager manager = App.context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(App.context.getPackageName(), 0);
            return info.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }
    }


    /**
     * 加载本地图片
     * 网络图片
     */
    class ImageDownloadTask extends AsyncTask<String, Void, Bitmap> {

        private String imageUrl;

        public ImageDownloadTask(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        @Override
        protected Bitmap doInBackground(String... strings) {

            InputStream inputStream = null;
            DiskLruCache.Snapshot snapShot = null;
            //文件名字
            String key = MD5Utils.hashKeyForDisk(imageUrl);
            try {
                //从本地取图片
                snapShot = mDiskCache.get(key);
                //没有缓存过图片，从网上下载图片
                if (snapShot == null) {
                    DiskLruCache.Editor edit = mDiskCache.edit(key);
                    if (edit != null) {
                        OutputStream outputStream = edit.newOutputStream(0);
                        if (downloadImageToStream(imageUrl, outputStream)) {
                            //保存图片到本地，文件的名称是key
                            edit.commit();
                        } else {
                            edit.abort();
                        }
                    }
                    //再次加载
                    snapShot = mDiskCache.get(key);
                }


                if (snapShot != null) {
                    inputStream = snapShot.getInputStream(0);
                    Bitmap bitmap = null;
                    if (inputStream != null) {
                        bitmap = BitmapFactory.decodeStream(inputStream);
                    }
                    if (bitmap != null) {
                        mLruCache.put(imageUrl, bitmap);
                    }
                    return bitmap;
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            ImageView view = (ImageView)
                    imageViewManager.get(imageUrl);

            if (view != null && bitmap != null) {
                view.setImageBitmap(bitmap);
            }

            if (loadingQueue != null)
                loadingQueue.remove(this);

            waitToLoadStrategy();

            //将缓存记录同步到journal文件中。
            if (mDiskCache != null) {
                try {
                    mDiskCache.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //从等待队列中取任务继续执行
    private synchronized void waitToLoadStrategy() {
        //可以继续执行任务的个数
        int index = MAX_THREAD_NUM - loadingQueue.size();

        for (int i = 0; i < index; i++) {
            if (waitingQueue.size() > 0) {
                ImageDownloadTask task = waitingQueue.get(0);
                waitingQueue.remove(task);
                loadingQueue.add(task);
                task.execute();
            } else {
                break;
            }
        }
    }

    private boolean downloadImageToStream(String imageUrl, OutputStream outputStream) {
        HttpURLConnection urlConnection = null;
        BufferedOutputStream out = null;
        BufferedInputStream in = null;
        try {
            final URL url = new URL(imageUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            in = new BufferedInputStream(urlConnection.getInputStream(),
                    8 * 1024);
            out = new BufferedOutputStream(outputStream, 8 * 1024);
            int b;
            while ((b = in.read()) != -1) {
                out.write(b);
            }
            return true;
        } catch (final IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 清理缓存
     */
    public void onDestroy() {
        cancelAllTasks();
        mLruCache = null;
        if (mSoftCache != null) {
            mSoftCache.clear();
            mSoftCache = null;
        }
        loadingQueue = null;
        waitingQueue = null;

        imageViewManager = null;
        instance = null;
        try {
            mDiskCache.close();
            mDiskCache = null;
        } catch (IOException e) {
        }
    }

    /**
     * 取消所有正在下载或等待下载的任务。
     */
    public void cancelAllTasks() {
        if (loadingQueue != null) {
            for (ImageDownloadTask task : loadingQueue) {
                task.cancel(false);
            }
            loadingQueue.clear();
        }
        if (waitingQueue != null) {
            for (ImageDownloadTask task : waitingQueue) {
                task.cancel(false);
            }
            waitingQueue.clear();
        }
        if (imageViewManager != null) {
            imageViewManager.clear();
        }
    }
}
