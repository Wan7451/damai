package com.yztc.core.net;

import android.content.Context;

import com.yztc.core.utils.DialogHelper;
import com.yztc.core.utils.NetUtils;

import rx.Subscriber;

/**
 * 处理异常的Subscriber
 * Created by wanggang on 2016/11/4.
 */

public abstract class BaseSubscriber<T> extends Subscriber<T> {

    private Context context;

    public BaseSubscriber(Context context) {
        this.context = context;
    }

    //失败的时候回调
    @Override
    public void onError(Throwable e) {
        //统一处理异常
        if (e instanceof ExceptionHandle.ResponeThrowable) {
            //回调给Activity
            onError((ExceptionHandle.ResponeThrowable) e);
        } else {
            onError(new ExceptionHandle.ResponeThrowable(e, ExceptionHandle.ERROR.UNKNOWN));
        }
    }


    //开始的时候回调
    @Override
    public void onStart() {
        super.onStart();
        if (!NetUtils.isConnected(context)) {
            onError(
                    new ExceptionHandle.ResponeThrowable(
                            new Throwable("网络没有连接,请打开网路"),
                            ExceptionHandle.ERROR.NETWORD_ERROR));

        } else {
            //网络操作，显示对话框
            DialogHelper.showProgressDlg(context, "网络加载中....");
        }
    }

    //完成是回调
    @Override
    public void onCompleted() {
        //隐藏对话框
        DialogHelper.stopProgressDlg();
    }

    //出现错误直接调用 自定义的 onError
    public abstract void onError(ExceptionHandle.ResponeThrowable e);

}