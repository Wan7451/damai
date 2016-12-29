package com.yztc.core.manager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

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

    private static final String SP_NEW_VERSION = "new_version";

    private static String currDownUrl = "";  //用于防止多次下载

    /**
     * 进行版本跟新
     *
     * @param context
     * @param version        新的版本
     * @param url            下载地址
     * @param message        版本跟新信息
     * @param isForcedUpdate 是否强制更新
     */
    public static void checkViersion(final Context context, final int version, String url, String message, int isForcedUpdate) {
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
        int lastVersion = (int) SPUtils.get(context, SP_NEW_VERSION, 0);

        //版本不同 文件存在，不下载,已经下载好的版本
        if (mgr.isHasFile(url)) {
            //缓存的文件版本  >  目前APP版本
            if (lastVersion > currVersion) {

                File file = mgr.getFile(url);
                DialogActivity.startDialogActivity(context,
                        message, file.getPath(), isForcedUpdate);
            }
        } else {
            if (NetUtils.isWifi(context)) {
                //防止多次下载
                if (currDownUrl.equals(url)) {
                    return;
                }
                currDownUrl = url;
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


    /**
     * 显示安装的对话框
     */
    public static class DialogActivity extends AppCompatActivity {
        private Context mContext = this;
        private String filePath = "";
        private String message = "";
        private int isForcedUpdate = 0;

        public static void startDialogActivity(Context context,
                                               String message,
                                               String filePath,
                                               int isForcedUpdate) {
            Intent i = new Intent();
            i.setClass(context, DialogActivity.class);
            i.putExtra("message", message);
            i.putExtra("filePath", filePath);
            i.putExtra("isForcedUpdate", isForcedUpdate);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }

        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            filePath = getIntent().getStringExtra("filePath");
            message = getIntent().getStringExtra("message");
            isForcedUpdate = getIntent().getIntExtra("isForcedUpdate", 0);

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
            alertDialog.setTitle("已在WiFi环境中下载好最新版本的安装包，是否安装?");
            alertDialog.setMessage(message);
            if (isForcedUpdate == 0) {
                alertDialog.setPositiveButton("下次再说", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
            }
            alertDialog.setNegativeButton("安装", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    installAPK(DialogActivity.this, filePath);
                }
            });

            AlertDialog ad = alertDialog.create();

            ad.setCanceledOnTouchOutside(false);//点击外面区域不会让dialog消失
            ad.show();
        }

        private static void installAPK(Context context, String filePath) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri uri = Uri.parse("file://" + filePath);
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
            //在服务中开启activity必须设置flag
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }


    }


}
