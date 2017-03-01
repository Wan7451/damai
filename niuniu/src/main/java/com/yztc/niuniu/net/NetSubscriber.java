package com.yztc.niuniu.net;

import android.content.Context;
import android.util.Log;

import com.yztc.niuniu.dialog.LoadingDialog;

import rx.Subscriber;

/**
 * Created by wanggang on 2017/2/22.
 */

public abstract class NetSubscriber<T> extends Subscriber<T> {


    private Context context;
    private LoadingDialog loadingDialog;

    public NetSubscriber(Context context) {
        this.context = context;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (ishowLoading()) {
            loadingDialog = new LoadingDialog(context);
            loadingDialog.show();
        }
    }

    @Override
    public void onCompleted() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }

    @Override
    public void onError(Throwable e) {
        Log.i("======", e.getMessage());
        StackTraceElement[] stackTrace = e.getStackTrace();
        for (StackTraceElement trace : stackTrace) {
            Log.i("======", trace.toString());
        }

        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }

        ExceptionHandle.ResponeThrowable throwable;
        //后台逻辑执行失败，抛出的异常
        if (e instanceof ExceptionHandle.ResponeThrowable) {
            throwable = (ExceptionHandle.ResponeThrowable) e;
        } else {
            //网络操作执行的异常
            throwable = ExceptionHandle.handleException(e);
        }
        onError(throwable);
    }

    protected boolean ishowLoading() {
        return false;
    }

    public abstract void onError(ExceptionHandle.ResponeThrowable throwable);

}
