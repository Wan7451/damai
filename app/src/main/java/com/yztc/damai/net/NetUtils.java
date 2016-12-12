package com.yztc.damai.net;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;

import com.yztc.damai.utils.ToastUtils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by wanggang on 2016/12/12.
 */

public class NetUtils {

    //同时线程数量
    private static final int THREAD_NUM = 3;

    private static final int CONNECT_TIMEOUT = 20 * 1000;
    private static final int READ_TIMEOUT = 20 * 1000;

    private static final String TAG="==NET=>";

    private static final boolean DEBUG=true;

    private static NetUtils instance = null;

    private ExecutorService threadPool;
    private Handler handler;

    private NetUtils() {
        threadPool =
                Executors.newFixedThreadPool(THREAD_NUM);
         handler=new Handler();
    }

    public static NetUtils getInstance() {
        if (instance == null) {
            synchronized (NetUtils.class) {
                if (instance == null) {
                    instance = new NetUtils();
                }
            }
        }
        return instance;
    }


    public void get(final String path, final HashMap<String, String> maps, final NetResponse response) {



        if(!NetStatusUtils.isConnect()){
            ToastUtils.show("网络无连接");
            return;
        }


        threadPool.execute(new Runnable() {
            @Override
            public void run() {

                StringBuilder builder = new StringBuilder(NetConfig.BASE_URL);
                builder.append(path);
                if (maps != null && maps.size() > 0) {
                    builder.append("?");
                    builder.append(buildParams(maps));
                }
                if(DEBUG){
                    Log.i(TAG,builder.toString());
                }

                InputStream stream = null;
                HttpURLConnection conn = null;
                try {
                    URL url = new URL(builder.toString());
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(CONNECT_TIMEOUT);
                    conn.setReadTimeout(READ_TIMEOUT);
                    int code = conn.getResponseCode();
                    if (code == 200) {
                        stream = conn.getInputStream();
                        BufferedReader reader =
                                new BufferedReader(new InputStreamReader(stream));
                        final StringBuilder resut = new StringBuilder();
                        String temp;
                        while ((temp = reader.readLine()) != null) {
                            resut.append(temp);
                        }
                        if (response != null) {
                            handleSuccess(response,resut.toString());
                        }
                    } else if (code > 400) {
                        if (response != null) {
                            handleError(response,"网络连接异常");
                        }
                    } else if (code > 500) {
                        if (response != null) {
                            handleError(response,"后台服务异常");
                        }
                    } else {
                        if (response != null) {
                            handleError(response,"未知异常");
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    if (response != null) {
                        handleError(response,e.getMessage());
                    }
                } finally {
                    if (stream != null) {
                        try {
                            stream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (conn != null) {
                        conn.disconnect();
                    }
                }
            }
        });
    }




    public void post(String path, HashMap<String, String> maps) {

    }


    private void handleError(final NetResponse response, final String error) {
        if(DEBUG)
            Log.i(TAG,error);
        handler.post(new Runnable() {
            @Override
            public void run() {
                response.onError(error);
            }
        });
    }

    private void handleSuccess(final NetResponse response, final String result) {
        if(DEBUG)
            Log.i(TAG,result);
        handler.post(new Runnable() {
            @Override
            public void run() {
                response.onResponse(result);
            }
        });
    }

    private HashMap<String,String> getBasicParams(){
        HashMap<String,String> basic=new HashMap<>();
        basic.put("osType",NetConfig.osType);
        basic.put("channel_from",NetConfig.channel_from);
        basic.put("source",NetConfig.source);
        basic.put("version",NetConfig.version);
        basic.put("appType",NetConfig.appType);

        return basic;
    }

    private String buildParams(@NonNull HashMap<String, String> maps) {
        maps.putAll(getBasicParams());
        StringBuilder builder = new StringBuilder();
        Iterator<Map.Entry<String, String>> iterator = maps.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> next = iterator.next();
            builder.append(next.getKey());
            builder.append("=");
            builder.append(next.getValue());
            builder.append("&");
        }
        if (builder.length() > 0) {
            builder.setLength(builder.length() - 1);
        }

        return builder.toString();
    }


}
