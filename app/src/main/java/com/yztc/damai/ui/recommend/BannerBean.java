package com.yztc.damai.ui.recommend;

/**
 * Created by wanggang on 2016/12/14.
 */

public class BannerBean {

    private String Name;
    private int ProjectID;
    private String Pic;
    private int ProjType;
    private String Url;
    private String Summary;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getProjectID() {
        return ProjectID;
    }

    public void setProjectID(int projectID) {
        ProjectID = projectID;
    }

    public String getPic() {
        return Pic;
    }

    public void setPic(String pic) {
        Pic = pic;
    }

    public int getProjType() {
        return ProjType;
    }

    public void setProjType(int projType) {
        ProjType = projType;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public String getSummary() {
        return Summary;
    }

    public void setSummary(String summary) {
        Summary = summary;
    }
}
