package com.yztc.core.manager;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;

import com.qiniu.android.common.Zone;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;
import com.qiniu.android.utils.UrlSafeBase64;

import org.json.JSONObject;

import java.io.File;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by wanggang on 2016/12/28.
 */

public class QiNiuUpManager {


    private static final String sk = "y9jZpT-uA6BTr9O7N5bpYrBjieeQFgBt602HGADX";
    private static final String ak = "nlliSHOYD9Lz6K0PugV1HrA3gZ0CH9eD2JUV9_e2";

    private static final String MAC_NAME = "HmacSHA1";
    private static final String ENCODING = "UTF-8";

    private static UploadManager uploadManager;

    public static UploadManager getUploadManager() {

        if (uploadManager != null) {
            return uploadManager;
        }
        Configuration config = new Configuration.Builder()
                .chunkSize(256 * 1024)  //分片上传时，每片的大小。 默认256K
                .putThreshhold(512 * 1024)  // 启用分片上传阀值。默认512K
                .connectTimeout(10) // 链接超时。默认10秒
                .responseTimeout(60) // 服务器响应超时。默认60秒
                //.recorder(recorder)  // recorder分片上传时，已上传片记录器。默认null
                //.recorder(recorder, keyGen)  // keyGen 分片上传时，生成标识符，用于片记录器区分是那个文件的上传记录
                .zone(Zone.zone0) // 设置区域，指定不同区域的上传域名、备用域名、备用IP。
                .build();
        // 重用uploadManager。一般地，只需要创建一个uploadManager对象
        uploadManager = new UploadManager(config);

        return uploadManager;
    }


    /**
     * 上传单个文件
     *
     * @param context
     * @param upFile       上传的文件
     * @param key          文件在服务器上的名称
     * @param isDeleteFile 上传完成是否删除
     */
    public static void upLoadFile(Context context, final File upFile, String key, final boolean isDeleteFile, final OnUpLoadLintener l) {

        if (upFile == null || !upFile.exists()) {
            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("正在上传......");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.show();

        //上传进度
        final UpProgressHandler handler = new UpProgressHandler() {
            @Override
            public void progress(String key, double percent) {
                progressDialog.setMessage(percent * 100 + "%");
                progressDialog.setProgress((int) (percent * 100));

            }
        };
        UploadOptions options = new UploadOptions(null, null, false, handler, null);

        String uploadToken = getUploadToken();


        uploadManager = getUploadManager();

        uploadManager.put(upFile, key, uploadToken, new UpCompletionHandler() {
            @Override
            public void complete(String key, ResponseInfo info, JSONObject response) {
                if (info.isOK()) {
                    //上传完成
                    progressDialog.dismiss();
                    if (l != null) {
                        String path = "http://oh0vbg8a6.bkt.clouddn.com/" + key;
                        l.onUpSuccess(path);
                    }
                    //是否删除
                    if (isDeleteFile) {
                        upFile.delete();
                    }

                } else {
                    if (l != null) {
                        l.onUpError(info.error);
                    }
                }
            }
        }, options);
    }


    public interface OnUpLoadLintener {
        void onUpSuccess(String url);

        void onUpError(String msg);
    }


    @NonNull
    private static String getUploadToken() {
        try {
            // 1 构造上传策略
            JSONObject _json = new JSONObject();
            long _dataline = System.currentTimeMillis() / 1000 + 3600;
            _json.put("deadline", _dataline);// 有效时间为一个小时
            _json.put("scope", "yztc");
            String _encodedPutPolicy = UrlSafeBase64.encodeToString(_json
                    .toString().getBytes());
            byte[] _sign = HmacSHA1Encrypt(_encodedPutPolicy, sk);
            String _encodedSign = UrlSafeBase64.encodeToString(_sign);
            return ak + ':' + _encodedSign + ':'
                    + _encodedPutPolicy;
        } catch (Exception e) {

        }
        return null;
    }


    /**
     * @param encryptText 被签名的字符串
     * @param encryptKey  密钥
     * @return
     * @throws Exception
     */
    private static byte[] HmacSHA1Encrypt(String encryptText, String encryptKey)
            throws Exception {
        byte[] data = encryptKey.getBytes(ENCODING);
        // 根据给定的字节数组构造一个密钥,第二参数指定一个密钥算法的名称
        SecretKey secretKey = new SecretKeySpec(data, MAC_NAME);
        // 生成一个指定 Mac 算法 的 Mac 对象
        Mac mac = Mac.getInstance(MAC_NAME);
        // 用给定密钥初始化 Mac 对象
        mac.init(secretKey);
        byte[] text = encryptText.getBytes(ENCODING);
        // 完成 Mac 操作
        return mac.doFinal(text);
    }
}
