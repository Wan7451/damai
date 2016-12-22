package com.yztc.damai.net;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.yztc.core.manager.LimitCacheManager;
import com.yztc.damai.config.NetConfig;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by wanggang on 2016/12/12.
 */

public class NetUtils {

    //同时线程数量
    private static final int THREAD_NUM = 4;

    private static final int CONNECT_TIMEOUT = 30 * 1000;
    private static final int READ_TIMEOUT = 30 * 1000;

    private static final String TAG = "==NET=>";

    public static final boolean DEBUG = true;

    private static NetUtils instance = null;

    private ExecutorService threadPool;
    private Handler mHandler;

    private NetUtils() {
        //线程池
        threadPool =
                Executors.newFixedThreadPool(THREAD_NUM);
        mHandler = new Handler(Looper.getMainLooper());
    }

    public static NetUtils getInstance() {
        //防止创建多个对象
        if (instance == null) {
            synchronized (NetUtils.class) {
                if (instance == null) {
                    instance = new NetUtils();
                }
            }
        }
        return instance;
    }


    public void get(String path,
                    HashMap<String, String> maps,
                    NetResponse response) {
        get(NetConfig.BASE_URL, path, maps, response);
    }

    public void get(String path,
                    HashMap<String, String> maps,
                    NetResponse response,
                    Handler handler) {
        get(NetConfig.BASE_URL, path, maps, response, handler);
    }


    public void get(String baseUrl,
                    String path,
                    HashMap<String, String> maps,
                    NetResponse response) {
        get(baseUrl, path, maps, response, mHandler);
    }


    public void get(final String baseUrl,
                    final String path,
                    final HashMap<String, String> maps,
                    final NetResponse response,
                    final Handler handler) {
        threadPool.execute(new Runnable() {
            @Override
            public void run() {

                //拼接URL地址
                StringBuilder builder = new StringBuilder(baseUrl);
                builder.append(path);
                //添加参数
                builder.append("?");
                builder.append(buildParams(maps));
                String urlPath = builder.toString();

                //从本地缓存取数据
                //30分钟   缓存超时   返回空
                String cache = LimitCacheManager.getInstance().getString(urlPath);

                if (!TextUtils.isEmpty(cache)) {
                    if (DEBUG) {
                        Log.i(TAG, "state: from Cache");
                    }
                    handleSuccess(response, cache, handler);
                    return;
                }

                if (!NetStatusUtils.isConnect()) {
                    handleError(response, "网络没有连接", handler);
                    return;
                }

                //查看URL
                if (DEBUG) {
                    Log.i(TAG, urlPath);
                }

                InputStream stream = null;
                HttpURLConnection conn = null;
                try {
                    URL url = new URL(urlPath);
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(CONNECT_TIMEOUT);
                    conn.setReadTimeout(READ_TIMEOUT);
                    int code = conn.getResponseCode();
                    if (DEBUG) {
                        Log.i(TAG, "state:" + code);
                    }
                    if (code == 200) {
                        stream = conn.getInputStream();
                        BufferedReader reader =
                                new BufferedReader(new InputStreamReader(stream));
                        StringBuilder result = new StringBuilder();
                        String temp;
                        while ((temp = reader.readLine()) != null) {
                            result.append(temp);
                        }
                        //缓存JSO
                        LimitCacheManager.getInstance().put(urlPath, result.toString());
                        handleSuccess(response, result.toString(), handler);
                    } else if (code > 500) {
                        handleError(response, "后台服务异常", handler);
                    } else if (code > 400) {
                        handleError(response, "网络连接异常", handler);
                    } else {
                        handleError(response, "未知异常", handler);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    if (response != null) {
                        handleError(response, e.getMessage(), handler);
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


    private void handleError(final NetResponse response, final String error, Handler handler) {
        if (DEBUG && !TextUtils.isEmpty(error))
            Log.i(TAG, error);

        if (response == null)
            return;

        handler.post(new Runnable() {
            @Override
            public void run() {
                response.onError(error);
            }
        });
    }

    private void handleSuccess(final NetResponse response, final String result, Handler handler) {
        if (DEBUG)
            logResponse(result);

        if (response == null)
            return;

        handler.post(new Runnable() {
            @Override
            public void run() {
                response.onResponse(result);
            }
        });
    }

    private HashMap<String, String> getBasicParams() {
        HashMap<String, String> basic = new HashMap<>();
        basic.put("osType", NetConfig.osType);
        basic.put("channel_from", NetConfig.channel_from);
        basic.put("source", NetConfig.source);
        basic.put("version", NetConfig.version);
        basic.put("appType", NetConfig.appType);

        return basic;
    }

    private String buildParams(@NonNull HashMap<String, String> maps) {
        //公共参数
        HashMap<String, String> basicParams = getBasicParams();
        //业务参数
        if (maps != null && maps.size() > 0) {
            basicParams.putAll(maps);
        }
        StringBuilder builder = new StringBuilder();
        Iterator<Map.Entry<String, String>> iterator =
                basicParams.entrySet().iterator();
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


    private void logResponse(String response) {
        int division = 500;
        if (response.length() > division) {
            int chunkCount = response.length() / division;
            // integer division
            for (int i = 0; i <= chunkCount; i++) {
                int max = division * (i + 1);
                if (max >= response.length()) {
                    Log.i(TAG, response.substring(division * i));
                } else {
                    Log.i(TAG, response.substring(division * i, max));
                }
            }
        } else {
            Log.i(TAG, response.toString());
        }
    }

}
