package com.yztc.core.manager.usericon;

import android.app.Activity;
import android.support.v4.app.Fragment;

/**
 * Created by wanggang on 2017/1/2.
 */

public class UserIconHanlderFactory {


    public static UserIconHanlder create(Activity activity) {
        return new ActivityHandler(activity);
    }

    public static UserIconHanlder create(Fragment fragment) {
        return new FragmentHandler(fragment);
    }


}
