package com.yztc.niuniu.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.yztc.core.utils.ScreenUtils;
import com.yztc.niuniu.App;
import com.yztc.niuniu.R;
import com.yztc.niuniu.db.ImageSize;
import com.yztc.niuniu.db.ImageSizeDao;
import com.yztc.niuniu.imageloader.ProgressImageView;
import com.yztc.niuniu.imageloader.ProgressModelLoader;

import java.util.List;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by wanggang on 2017/2/14.
 */

public class ImageLoager {


    private static int screen_width;

    private static ImageSizeDao imageSizeDao;

    public static void load(final Context context,
                            final String path,
                            final ImageView imageView) {
        if (imageSizeDao == null) {
            App app = (App) context.getApplicationContext();
            imageSizeDao = app.getImageSizeDao();
        }

        if (screen_width == 0) {
            screen_width = ScreenUtils.getScreenWidth(context);
        }

        List<ImageSize> imageSizeList = imageSizeDao.queryBuilder().where(ImageSizeDao.Properties.Url.eq(path)).build().list();
        if (imageSizeList != null &&
                imageSizeList.size() > 0) {
            ImageSize size = imageSizeList.get(0);
            ViewGroup.LayoutParams lp = imageView.getLayoutParams();
            lp.width = size.getWidth();
            lp.height = size.getHeight();
            imageView.setLayoutParams(lp);
            Glide.with(context)

                    .load(path)
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .placeholder(R.mipmap.ic_launcher)
                    .into(imageView);
        } else {
            ViewGroup.LayoutParams lp = imageView.getLayoutParams();
            lp.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            imageView.setLayoutParams(lp);
            imageView.setImageResource(R.mipmap.ic_launcher);

            Glide.with(context)
                    .load(path)
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .placeholder(R.mipmap.ic_launcher)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap bitmap,
                                                    GlideAnimation<? super Bitmap> glideAnimation) {
                            int width = bitmap.getWidth();
                            int height = bitmap.getHeight();
                            ViewGroup.LayoutParams lp = imageView.getLayoutParams();
                            lp.width = screen_width / 2;
                            float tempHeight = height * ((float) lp.width / width);
                            lp.height = (int) tempHeight;
                            imageView.setLayoutParams(lp);
                            imageView.setImageBitmap(bitmap);

                            ImageSize size = new ImageSize();
                            size.setHeight(lp.height);
                            size.setWidth(lp.width);
                            size.setUrl(path);
                            imageSizeDao.insertOrReplace(size);
                        }
                    });
        }


    }


    public static void load(final Context context,
                            final String path,
                            final ProgressImageView imageView) {
        if (imageSizeDao == null) {
            App app = (App) context.getApplicationContext();
            imageSizeDao = app.getImageSizeDao();
        }

        if (screen_width == 0) {
            screen_width = ScreenUtils.getScreenWidth(context);
        }

        List<ImageSize> imageSizeList = imageSizeDao.queryBuilder().where(ImageSizeDao.Properties.Url.eq(path)).build().list();
        if (imageSizeList != null &&
                imageSizeList.size() > 0) {
            ImageSize size = imageSizeList.get(0);
            ViewGroup.LayoutParams lp = imageView.getLayoutParams();
            lp.width = size.getWidth();
            lp.height = size.getHeight();
            imageView.setLayoutParams(lp);
            Glide.with(context)

                    .load(path)
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .placeholder(R.mipmap.ic_launcher)
                    .into(imageView.getImageView());
        } else {
            ViewGroup.LayoutParams lp = imageView.getLayoutParams();
            lp.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            imageView.setLayoutParams(lp);
            imageView.getImageView().setImageResource(R.mipmap.ic_launcher);

            Glide.with(context)
                    .using(new ProgressModelLoader(new ProgressHandler(imageView)))
                    .load(path)
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .placeholder(R.mipmap.ic_launcher)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap bitmap,
                                                    GlideAnimation<? super Bitmap> glideAnimation) {
                            int width = bitmap.getWidth();
                            int height = bitmap.getHeight();
                            ViewGroup.LayoutParams lp = imageView.getLayoutParams();
                            lp.width = screen_width / 2;
                            float tempHeight = height * ((float) lp.width / width);
                            lp.height = (int) tempHeight;
                            imageView.setLayoutParams(lp);
                            imageView.getImageView().setImageBitmap(bitmap);

                            ImageSize size = new ImageSize();
                            size.setHeight(lp.height);
                            size.setWidth(lp.width);
                            size.setUrl(path);
                            imageSizeDao.insertOrReplace(size);
                        }
                    });
        }


    }

    public static void loadNoResize(Context context,
                                    String path,
                                    ImageView imageView) {
        Glide.with(context)
                .load(path)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .placeholder(R.mipmap.ic_launcher)
                .fitCenter()
                .into(imageView);
    }

    public static void loadZoom(Context context,
                                String path,
                                final ImageView imageView) {
        final PhotoViewAttacher mAttacher = new PhotoViewAttacher(imageView);
        Glide.with(context)
                .load(path)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .placeholder(R.mipmap.ic_launcher)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap bitmap,
                                                GlideAnimation<? super Bitmap> glideAnimation) {
                        imageView.setImageBitmap(bitmap);
                        mAttacher.update();
                    }
                });

    }

    private static class ProgressHandler extends Handler {

        private final ProgressImageView mProgressImageView;

        public ProgressHandler(ProgressImageView progressImageView) {
            super(Looper.getMainLooper());
            mProgressImageView = progressImageView;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    int percent = msg.arg1 * 100 / msg.arg2;
                    mProgressImageView.setProgress(percent);
                    break;
                default:
                    break;
            }
        }
    }
}
