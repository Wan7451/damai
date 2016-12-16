package com.yztc.damai.ui.recommend;

import java.util.List;

/**
 * Created by wanggang on 2016/12/15.
 */

public class TypeViewBean {

    private int type;
    private String title;
    private String subTitle;
    private List<TypeViewDataBean> list;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public List<TypeViewDataBean> getList() {
        return list;
    }

    public void setList(List<TypeViewDataBean> list) {
        this.list = list;
    }

}
