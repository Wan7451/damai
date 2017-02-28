package com.yztc.niuniu.net;


import com.yztc.niuniu.util.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

import okhttp3.Cache;
import okhttp3.Dns;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by wanggang on 2017/2/26.
 */

public class HttpDns implements Dns {


    private static OkHttpClient httpDnsclient;

    @Override
    public List<InetAddress> lookup(String hostname) throws UnknownHostException {
        if (hostname == null) throw new UnknownHostException("hostname == null");
        HttpUrl httpUrl = new HttpUrl.Builder().scheme("http")
                .host("119.29.29.29")
                .addPathSegment("d")
                .addQueryParameter("dn", hostname)
                .build();
        Request dnsRequest = new Request.Builder().url(httpUrl).get().build();
        try {
            String s = getHTTPDnsClient().newCall(dnsRequest).execute().body().string();
            //free server may down, will return loopback ip address
            if (!s.matches("\\b(?:\\d{1,3}\\.){3}\\d{1,3}\\b")) {
                return Dns.SYSTEM.lookup(hostname);
            }
            return Arrays.asList(InetAddress.getAllByName(s));
        } catch (IOException e) {
            return Dns.SYSTEM.lookup(hostname);
        }
    }


    static synchronized OkHttpClient getHTTPDnsClient() {
        if (httpDnsclient == null) {

            File cache = new File(FileUtils.getHttpCacheFile(), "httpdns");

            httpDnsclient = new OkHttpClient.Builder()
                    .addNetworkInterceptor(new Interceptor() {
                        //REWRITE_CACHE_CONTROL_INTERCEPTOR
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Response originalResponse = chain.proceed(chain.request());
                            return originalResponse.newBuilder()
                                    //dns default cache time
                                    .header("Cache-Control", "max-age=600").build();
                        }
                    }).cache(new Cache(cache, 5 * 1024 * 1024)).build();
        }
        return httpDnsclient;
    }

}
