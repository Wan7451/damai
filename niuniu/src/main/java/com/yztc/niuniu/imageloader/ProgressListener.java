package com.yztc.niuniu.imageloader;

public interface ProgressListener {

    void progress(long bytesRead, long contentLength, boolean done);

}
