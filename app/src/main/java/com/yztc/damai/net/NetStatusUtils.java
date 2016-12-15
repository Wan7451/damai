package com.yztc.damai.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.support.v4.net.ConnectivityManagerCompat;

import com.yztc.damai.App;

/**
 * Created by wanggang on 2016/12/12.
 */

public class NetStatusUtils {

    public static boolean isConnect(){


        ConnectivityManager mgr= (ConnectivityManager)
             App.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = mgr.getActiveNetworkInfo();
        if(netInfo!=null){
            return netInfo.isConnected();
        }

        return false;
    }
}
