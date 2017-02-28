package com.yztc.niuniu.db;

import android.content.Context;

import org.greenrobot.greendao.database.Database;


/**
 * Created by wanggang on 2017/2/19.
 */

public class DBManager {


    private static DBManager instance;

    private final DaoMaster daoMaster;
    private final DaoSession daoSession;

    private DBManager(Context context) {
        DaoMaster.DevOpenHelper openHelper = new DaoMaster.DevOpenHelper(context.getApplicationContext(), "data,db");
        Database db = openHelper.getWritableDb();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }


    public static DBManager getInstace(Context context) {
        if (instance == null) {
            synchronized (DBManager.class) {
                if (instance == null) {
                    instance = new DBManager(context);
                }
            }
        }
        return instance;
    }


    public DaoSession getDaoSession() {
        return daoSession;
    }

    public ImageSizeDao getImageSizeDao() {
        return daoSession.getImageSizeDao();
    }

//    public PageBeanDao getPageBeanDao(){
//        return daoSession.getPageBeanDao();
//    }
//
//    public ListItemBeanDao getListItemBeanDao(){
//        return daoSession.getListItemBeanDao();
//    }
}
