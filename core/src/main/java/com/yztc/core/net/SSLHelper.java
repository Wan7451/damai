package com.yztc.core.net;

import android.content.Context;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 * Created by wanggang on 2016/11/2.
 * <p>
 * <p>
 * Https  加载证书
 */

public class SSLHelper {

    public static SSLSocketFactory getSSLSocketFactory(Context context) {
        SSLSocketFactory sslSocketFactory = null;
        try {

            //context.getResources().openRawResource()
            InputStream open = context.getAssets().open("tomcat.cer");
            //数字证书的格式遵循X.509标准。
            CertificateFactory factory =
                    CertificateFactory.getInstance("X.509");
            //生成证书
            Certificate certificate = factory.generateCertificate(open);
            //密钥
            KeyStore keyStore =
                    KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);
            keyStore.setCertificateEntry("tomcat", certificate);
            //信任管理器
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(
                    TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();

            X509TrustManager trustManager = (X509TrustManager) trustManagers[0];
            //指定协议（一般是TLS）获取SSL上下文（SSLContext）实例。
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{trustManager}, new SecureRandom());
            sslSocketFactory = sslContext.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sslSocketFactory;
    }


    public static X509TrustManager getTrustManager() {

        return new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] certificates, String s) throws CertificateException {

            }

            @Override
            public void checkServerTrusted(X509Certificate[] certificates, String s) throws CertificateException {

            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        };
    }

    public static HostnameVerifier getHostnameVerifier() {

        return new HostnameVerifier() {
            @Override
            public boolean verify(String s, SSLSession session) {
                //添加域名验证,这里是默认所有的都信任
                return true;
            }
        };
    }
}
