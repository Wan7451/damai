package com.yztc.niuniu.ui.av;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by wanggang on 2017/2/13.
 */

@Entity
public class AvBean {

    private Long id;
    private Long pageId;
    private String title;
    private String url;
    private String img;


    @Generated(hash = 568692207)
    public AvBean(Long id, Long pageId, String title, String url, String img) {
        this.id = id;
        this.pageId = pageId;
        this.title = title;
        this.url = url;
        this.img = img;
    }

    @Generated(hash = 1495532620)
    public AvBean() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPageId() {
        return pageId;
    }

    public void setPageId(Long pageId) {
        this.pageId = pageId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
