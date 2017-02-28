package com.yztc.niuniu;

import android.app.Application;

import com.yztc.niuniu.db.DBManager;
import com.yztc.niuniu.db.ImageSizeDao;

/**
 * Created by wanggang on 2017/2/19.
 */

public class App extends Application {

    private static App context;
    private ImageSizeDao imageSizeDao;

    @Override
    public void onCreate() {
        super.onCreate();
        imageSizeDao = DBManager.getInstace(this).getImageSizeDao();
        context = this;
    }

    public ImageSizeDao getImageSizeDao() {
        return imageSizeDao;
    }

    public static App getContext() {
        return context;
    }
}
