package com.yztc.core.manager.usericon;

import java.io.File;

/**
 * Created by wanggang on 2017/1/2.
 */

public interface UserIconHandlerListener {
    void onCropOk(File f);

    void onUp2ServerOk(String url);

    void onError(String error);
}
