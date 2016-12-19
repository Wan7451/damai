package com.yztc.core.net;

/**
 * Created by wanggang on 2016/11/4.
 * 服务器返回数据的基类
 */

public class BaseResponse<T> {

    // {status:1,result:{},msg:""}

    private int status;
    private T result;
    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public boolean isOk() {
        return status == 1;
    }

    public T getData() {
        return result;
    }
}
