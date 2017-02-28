package com.yztc.core.glide;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.GlideModule;

import java.io.File;
import java.io.InputStream;

/**
 * Created by wanggang on 2017/1/5.
 */

public class CustomGlideModel implements GlideModule {

    //在Glide单例创建之前应用所有的选项配置，该方法每次实现只会被调用一次。
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {

        //MemorySizeCalculator会根据给定屏幕大小可用内存算出合适的缓存大小，
        // 这也是推荐的缓存大小，我们可以根据这个推荐大小做出调整
        MemorySizeCalculator calculator = new MemorySizeCalculator(context);
        int defaultMemoryCacheSize = calculator.getMemoryCacheSize();
        int defaultBitmapPoolSize = calculator.getBitmapPoolSize();

        int customMemoryCacheSize = 2 * defaultMemoryCacheSize;
        int customBitmapPoolSize = 2 * defaultBitmapPoolSize;


        //设置Glide内存缓存大小
        //MemoryCache用来把resources 缓存在内存里，以便能马上能拿出来显示。
        // 默认情况下Glide使用LruResourceCache
        builder.setMemoryCache(new LruResourceCache(customMemoryCacheSize));


        //设置BitmapPool缓存内存大小

        //Bitmap池用来允许不同尺寸的Bitmap被重用，这可以显著地减少因为图片解码像素数
        //组分配内存而引发的垃圾回收。默认情况下Glide使用LruBitmapPool作为Bitmap池，
        //LruBitmapPool采用LRU算法保存最近使用的尺寸的Bitmap。
        builder.setBitmapPool(new LruBitmapPool(customBitmapPoolSize));

        File cacheDir = context.getExternalCacheDir();//指定的是数据的缓存地址
        int diskCacheSize = 1024 * 1024 * 300;//最多可以缓存多少字节的数据
        //设置磁盘缓存大小
        //设置一个用来存储Resource数据和缩略图的DiskCache实现。
        builder.setDiskCache(new DiskLruCacheFactory(cacheDir.getPath(), "glide", diskCacheSize));

        //设置图片解码格式
        //为所有的默认解码器设置解码格式。如DecodeFormat.PREFER_ARGB_8888。
        //默认是DecodeFormat.PREFER_RGB_565，因为相对于ARGB_8888的4字节/像素
        // 可以节省一半的内存，但不支持透明度且某些图片会出现条带。
        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);

        //设置磁盘缓存线程池
        //   builder.setDiskCacheService(Executors.newFixedThreadPool(3));

        //设置计算大小的线程池
        //  builder.setResizeService(Executors.newFixedThreadPool(3));

    }

    //用来在Glide单例创建之后但请求发起之前注册组件，该方法每次实现只会被调用一次。
    //通常在该方法中注册ModelLoader。
    @Override
    public void registerComponents(Context context, Glide glide) {
        //ModelLoader接口可以提供给我们要载入图片的View的尺寸，并允许我们
        // 通过这个尺寸选择合适的url下载一个合适尺寸的图片，使用合适大小的图片
        // 可以节省带宽和设备存储空间，也可以提升app的表现。

        //使用OkhttpLoader 进行网络加载
        //根据GlideUrl 获取  IntputStream
        //  glide.register(GlideUrl.class, InputStream.class,new HttpUrlGlideUrlLoader.Factory());
        glide.register(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory());

    }
}
