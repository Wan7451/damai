package com.yztc.damai;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;

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

//        HandlerThread thread = new HandlerThread("UpWelcomePicService");
//        thread.start();
//        Handler handler = new Handler(thread.getLooper());

        //获得图片下载地址
        dowmWelcomePic();

        return START_STICKY_COMPATIBILITY;
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
}
