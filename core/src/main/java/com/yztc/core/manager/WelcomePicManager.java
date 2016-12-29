package com.yztc.core.manager;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;

import com.yztc.core.utils.SPUtils;

/**
 * Created by wanggang on 2016/12/22.
 *
 * 用于管理欢迎页面的图片
 */

public class WelcomePicManager {

    private static final String SP_WELCOME = "welcome_pic";
    private static String currDownUrl = "";  //用于防止多次下载


    public static void savePic(final Context context, final String url) {

        if (TextUtils.isEmpty(url)) {
            return;
        }
        DownLoadFileManager mgr = DownLoadFileManager.getInstance();

        //文件存在，不下载
        if (mgr.isHasFile(url)) {
            return;
        }
        //之前的图片
        String lastUrl = (String) SPUtils.get(context, SP_WELCOME, "");
        //判断是否一致，不同的话，删除之前的图片
        if (!TextUtils.isEmpty(lastUrl) && !TextUtils.equals(lastUrl, url)) {
            if (mgr.isHasFile(lastUrl)) {
                mgr.remove(lastUrl);
            }
        }

        //防止一张图片下载多次
        if (currDownUrl.equals(url)) {
            return;
        }
        currDownUrl = url;

        //进行下载
        mgr.downLoadFile(url, new DownLoadFileManager.OnDownListener() {
            @Override
            public void onOk() {
                //记录图片的名字
                SPUtils.put(context, SP_WELCOME, url);
            }

            @Override
            public void onError() {

            }
        });
    }

    public static Bitmap getPic(Context context) {
        String key = (String) SPUtils.get(context, SP_WELCOME, "");
        if (!TextUtils.isEmpty(key)) {
            //已经下载过
            return DownLoadFileManager.getInstance().getAsBitmap(key);
        }
        return null;
    }
}
