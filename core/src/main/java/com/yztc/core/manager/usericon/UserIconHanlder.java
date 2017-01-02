package com.yztc.core.manager.usericon;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import com.yztc.core.manager.QiNiuUpManager;
import com.yztc.core.utils.FileUtils;

import java.io.File;

/**
 * Created by wanggang on 2017/1/2.
 */

public abstract class UserIconHanlder {

    protected static final int PHOTO_REQUEST_PICK = 0x101;
    protected static final int PHOTO_REQUEST_CAMERA = 0x102;
    protected static final int PHOTO_REQUEST_CUT = 0x103;

    protected static final String PHOTO_FILE_NAME = "icon.jpg";
    protected static final String CROP_FILE_NAME = "crop.jpg";
    private UserIconHandlerListener userIconHandlerListener;
    private boolean isAutoUp2Server;

    protected Intent getCropIntent(Uri uri, Uri image) {
        //裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        //裁剪框的比例  1:1
        intent.putExtra("X", 1);
        intent.putExtra("Y", 1);
        //裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 250);
        //图片格式
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);//取消人脸识别
//        intent.putExtra("return-data",true); //true:不返回uri,false:返回uri
        intent.putExtra("return-data", false); //true:不返回uri,false:返回uri
        intent.putExtra(MediaStore.EXTRA_OUTPUT, image);
        return intent;
    }


    protected Intent getPickIntent() {
        //激活系统图库,选择一张图片
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        return intent;
    }

    protected Intent getCaptureIntent(File tempFile) {
        //激活相机
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        //判断存储卡是否可用,可用进行存储
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
        return intent;
    }


    protected File getTempFile(String fileName) {
        File tempCacheFile = FileUtils.getTempCacheFile();
        return new File(tempCacheFile, fileName);
    }


    public void setUserIconHandlerListener(UserIconHandlerListener l) {
        this.userIconHandlerListener = l;
    }

    public void setIsAutoUp2Server(boolean isAutoUp2Server) {
        this.isAutoUp2Server = isAutoUp2Server;
    }


    protected void cropOk(File cropFile) {
        if (userIconHandlerListener != null) {
            userIconHandlerListener.onCropOk(cropFile);
        }

        if (isAutoUp2Server) {
            String key = System.currentTimeMillis() + "userIcon.jpg";
            QiNiuUpManager.upLoadFile(getContext(), cropFile, key, true, new QiNiuUpManager.OnUpLoadLintener() {
                @Override
                public void onUpSuccess(String url) {
                    if (userIconHandlerListener != null) {
                        userIconHandlerListener.onUp2ServerOk(url);
                    }
                }

                @Override
                public void onUpError(String msg) {
                    if (userIconHandlerListener != null) {
                        userIconHandlerListener.onError(msg);
                    }
                }
            });
        }
    }

    //开始选择图片
    public abstract void onChoicePick();

    //开始打开相册
    public abstract void onChoiceCapture();

    //获取Context
    public abstract Context getContext();

    //回调
    public abstract void onActivityResult(int requestCode, int resultCode, Intent data);


}
