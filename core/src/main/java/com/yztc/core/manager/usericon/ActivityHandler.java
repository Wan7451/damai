package com.yztc.core.manager.usericon;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.io.File;

/**
 * Created by wanggang on 2017/1/2.
 */

public class ActivityHandler extends UserIconHanlder {

    private Activity activity;
    private File tempFile;
    private File cropFile;

    public ActivityHandler(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onChoicePick() {
        activity.startActivityForResult(getPickIntent(), PHOTO_REQUEST_PICK);
    }

    @Override
    public void onChoiceCapture() {
        tempFile = getTempFile(System.currentTimeMillis() + PHOTO_FILE_NAME);
        activity.startActivityForResult(getCaptureIntent(tempFile), PHOTO_REQUEST_CAMERA);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Intent intent;
        switch (requestCode) {
            case PHOTO_REQUEST_PICK:
                Uri uri = data.getData();
                //公开   SD卡
                cropFile = getTempFile(System.currentTimeMillis() + CROP_FILE_NAME);
                intent = getCropIntent(uri, Uri.fromFile(cropFile));
                activity.startActivityForResult(intent, PHOTO_REQUEST_CUT);
                break;
            case PHOTO_REQUEST_CAMERA:
                cropFile = getTempFile(System.currentTimeMillis() + CROP_FILE_NAME);
                intent = getCropIntent(Uri.fromFile(tempFile), Uri.fromFile(cropFile));
                activity.startActivityForResult(intent, PHOTO_REQUEST_CUT);
                break;
            case PHOTO_REQUEST_CUT:
                cropOk(cropFile);
                break;
        }

    }

    @Override
    public Context getContext() {
        return activity;
    }
}