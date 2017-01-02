package com.yztc.core.manager;

import android.app.Activity;

import java.util.Stack;

/**
 * Created by wanggang on 2017/1/2.
 * <p>
 * <p>
 * Activity 栈管理
 */

public class ActivityStackManager {

    private static Stack<Activity> activityStack;
    private static ActivityStackManager instance;

    private ActivityStackManager() {
    }

    public static ActivityStackManager getInstance() {
        if (instance == null) {
            instance = new ActivityStackManager();
        }
        return instance;
    }

    public void popActivity() {
        Activity activity = activityStack.lastElement();
        if (activity != null) {
            activity.finish();
            activity = null;
        }
    }

    public void popActivity(Activity activity) {
        if (activity != null) {
            activity.finish();
            activityStack.remove(activity);
            activity = null;
        }
    }

    public Activity currentActivity() {
        Activity activity = activityStack.lastElement();
        return activity;
    }

    public void pushActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
    }

    public void popAllActivityExceptOne(Class cls) {
        while (true) {
            Activity activity = currentActivity();
            if (activity == null) {
                break;
            }
            if (activity.getClass().equals(cls)) {
                break;
            }
            popActivity(activity);
        }
    }

    public void onDestory() {
        while (!activityStack.empty()) {
            instance.popActivity();
        }
        activityStack = null;
        instance = null;
    }
}
