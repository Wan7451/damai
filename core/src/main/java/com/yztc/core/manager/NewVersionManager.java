package com.yztc.core.manager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.WindowManager;

import com.yztc.core.utils.AppUtils;
import com.yztc.core.utils.NetUtils;
import com.yztc.core.utils.SPUtils;

import java.io.File;

/**
 * Created by wanggang on 2016/12/22.
 * <p>
 * 用于管理欢迎页面的图片
 */

public class NewVersionManager {

    public static final String SP_NEW_VERSION = "new_version";


    public static void checkViersion(final Context context, final int version, String url) {
        if (version == 0 || TextUtils.isEmpty(url)) {
            return;
        }

        int currVersion = AppUtils.getAppVersion();
        DownLoadFileManager mgr = DownLoadFileManager.getInstance();
        //版本相同不处理
        if (version == currVersion) {
            //版本相同，删除之前的安装包
            if (mgr.isHasFile(url)) {
                mgr.remove(url);
            }
            return;
        }
        //本地apk文件的版本
        int lastVersion = (int) SPUtils.get(context, SP_NEW_VERSION, "");

        //版本不同 文件存在，不下载,已经下载好的版本
        if (mgr.isHasFile(url)) {
            //缓存的文件版本  >  目前APP版本
            if (lastVersion > currVersion) {
                showInstanllDialog(context, mgr, url);
            }
        } else {
            if (NetUtils.isWifi(context)) {
                //下载完成
                mgr.downLoadFile(url, new DownLoadFileManager.OnDownListener() {
                    @Override
                    public void onOk() {
                        SPUtils.put(context, SP_NEW_VERSION, version);
                    }

                    @Override
                    public void onError() {

                    }
                });

            }
        }

    }

    private static void showInstanllDialog(final Context context, final DownLoadFileManager mgr, final String url) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setMessage("已在WiFi环境中下载好最新版本的安装包，是否安装?");
        alertDialog.setPositiveButton("否", null);
        alertDialog.setNegativeButton("是", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                installAPK(context, mgr, url);
            }
        });

        AlertDialog ad = alertDialog.create();

        ad.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        ad.setCanceledOnTouchOutside(false);//点击外面区域不会让dialog消失
        ad.show();
    }

    private static void installAPK(Context context, DownLoadFileManager mgr, String url) {
        File file = mgr.getFile(url);
        if (!file.exists()) return;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.parse("file://" + file.toString());
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        //在服务中开启activity必须设置flag
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

}
