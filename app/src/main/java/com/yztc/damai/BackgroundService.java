package com.yztc.damai;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.yztc.core.manager.NewVersionManager;
import com.yztc.core.manager.WelcomePicManager;
import com.yztc.core.utils.SPUtils;
import com.yztc.damai.help.Constant;
import com.yztc.damai.net.NetResponse;
import com.yztc.damai.net.NetUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by wanggang on 2016/12/22.
 * 跟新欢迎面图片服务
 */

public class BackgroundService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //获得图片下载地址
        dowmWelcomePic();

        checkNewVersion();

        return START_STICKY_COMPATIBILITY;
    }

    private void checkNewVersion() {
        NetUtils.getInstance().get(
                "/Update/getAndroidVersion.aspx",
                null,
                new NetResponse() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        UpBean upBean = gson.fromJson(response, UpBean.class);
                        NewVersionManager.checkViersion(BackgroundService.this,
                                upBean.version,
                                upBean.download,
                                upBean.message,
                                upBean.isForcedUpdate);

                    }

                    @Override
                    public void onError(String erroe) {

                    }
                });
    }


    private void dowmWelcomePic() {
        HashMap<String, String> maps = new HashMap<>();
        maps.put("type", "720_1280");
        int cityId = (int) SPUtils.get(this, Constant.SP_CURR_CITY, 852);
        maps.put("cityid", cityId + "");
        maps.put("version", "50609");
        NetUtils.getInstance().get(
                "Other/WelcomePagePicv1.aspx",
                maps,
                new NetResponse() {
                    @Override
                    public void onResponse(String response) {
                        downImage(response);
                    }

                    @Override
                    public void onError(String erroe) {

                    }
                });
    }

    private void downImage(String response) {
        String pic = null;
        //工作线程
        try {
            JSONObject object = new JSONObject(response);
            pic = object.getString("pic");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //下载图片
        if (!TextUtils.isEmpty(pic)) {
            WelcomePicManager.savePic(BackgroundService.this, pic);
        }
    }

    public static void start(Context context) {
        Intent i = new Intent();
        i.setClass(context, BackgroundService.class);
        context.startService(i);
    }

    public static void stop(Context context) {
        Intent i = new Intent();
        i.setClass(context, BackgroundService.class);
        context.stopService(i);
    }


    static class UpBean {

        private int version;
        private String download;
        private String message;
        private int isForcedUpdate;

        public int getVersion() {
            return version;
        }

        public void setVersion(int version) {
            this.version = version;
        }

        public String getDownload() {
            return download;
        }

        public void setDownload(String download) {
            this.download = download;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public int getIsForcedUpdate() {
            return isForcedUpdate;
        }

        public void setIsForcedUpdate(int isForcedUpdate) {
            this.isForcedUpdate = isForcedUpdate;
        }
    }
}
