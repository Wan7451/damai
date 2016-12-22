package com.yztc.core.manager;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;

import com.yztc.core.utils.SPUtils;

/**
 * Created by wanggang on 2016/12/22.
 * <p>
 * 用于管理欢迎页面的图片
 */

public class NewVersionManager {

    public static final String SP_WELCOME = "welcome_pic";


    public static void savePic(Context context, String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        //文件存在，不下载
        if (DownLoadFileManager.getInstance().isHasFile(url)) {
            return;
        }
        //之前的图片
        String lastUrl = (String) SPUtils.get(context, SP_WELCOME, "");
        //判断是否一致，不同的话，删除之前的图片
        if (!TextUtils.isEmpty(lastUrl) && !TextUtils.equals(lastUrl, url)) {
            if (DownLoadFileManager.getInstance().isHasFile(lastUrl)) {
                DownLoadFileManager.getInstance().remove(lastUrl);
            }
        }
        //记录图片的名字
        SPUtils.put(context, SP_WELCOME, url);
        //进行下载
        DownLoadFileManager.getInstance().downLoad(url);
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
