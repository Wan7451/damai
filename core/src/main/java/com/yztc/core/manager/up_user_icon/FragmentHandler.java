package com.yztc.core.manager.up_user_icon;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;

import java.io.File;

/**
 * Created by wanggang on 2017/1/2.
 */

public class FragmentHandler extends UserIconHanlder {

    private Fragment fragment;
    private File tempFile;
    private File cropFile;

    public FragmentHandler(Fragment fragment) {
        this.fragment = fragment;
    }

    @Override
    public void onChoicePick() {
        fragment.startActivityForResult(getPickIntent(), PHOTO_REQUEST_PICK);
    }

    @Override
    public void onChoiceCapture() {
        tempFile = getTempFile(System.currentTimeMillis() + PHOTO_FILE_NAME);
        fragment.startActivityForResult(getCaptureIntent(tempFile), PHOTO_REQUEST_CAMERA);
    }

    @Override
    public Context getContext() {
        return fragment.getContext();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Intent intent;
        switch (requestCode) {
            case PHOTO_REQUEST_PICK:
                Uri uri = data.getData();
                //公开   SD卡
                cropFile = getTempFile(System.currentTimeMillis() + CROP_FILE_NAME);
                intent = getCropIntent(uri, Uri.fromFile(cropFile));
                fragment.startActivityForResult(intent, PHOTO_REQUEST_CUT);
                break;
            case PHOTO_REQUEST_CAMERA:
                cropFile = getTempFile(System.currentTimeMillis() + CROP_FILE_NAME);
                intent = getCropIntent(Uri.fromFile(tempFile), Uri.fromFile(cropFile));
                fragment.startActivityForResult(intent, PHOTO_REQUEST_CUT);
                break;
            case PHOTO_REQUEST_CUT:
                cropOk(cropFile);
                break;
        }

    }
}