package com.yztc.damai.help;

/**
 * Created by wanggang on 2016/12/19.
 */

public class Event {

    public static final int EVENT_CITY_CHANGE = 111;


    private Event() {
    }

    public Event(int type, Object data) {
        this.type = type;
        this.data = data;
    }

    private int type;

    private Object data;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
