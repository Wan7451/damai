package com.yztc.damai.ui.recommend;

/**
 * Created by wanggang on 2016/12/16.
 */

public class RecommendBean {


    private int ProjectID;
    private String Name;
    private String PriceStr;
    private String priceName;
    private String ShowTime;
    private int SiteStatus;
    private String cityname;
    private String VenName;
    private int VenId;
    private int IsXuanZuo;
    private int openSum;

    public int getProjectID() {
        return ProjectID;
    }

    public void setProjectID(int ProjectID) {
        this.ProjectID = ProjectID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getPriceStr() {
        return PriceStr;
    }

    public void setPriceStr(String PriceStr) {
        this.PriceStr = PriceStr;
    }

    public String getPriceName() {
        return priceName;
    }

    public void setPriceName(String priceName) {
        this.priceName = priceName;
    }

    public String getShowTime() {
        return ShowTime;
    }

    public void setShowTime(String ShowTime) {
        this.ShowTime = ShowTime;
    }

    public int getSiteStatus() {
        return SiteStatus;
    }

    public void setSiteStatus(int SiteStatus) {
        this.SiteStatus = SiteStatus;
    }

    public String getCityname() {
        return cityname;
    }

    public void setCityname(String cityname) {
        this.cityname = cityname;
    }

    public String getVenName() {
        return VenName;
    }

    public void setVenName(String VenName) {
        this.VenName = VenName;
    }

    public int getVenId() {
        return VenId;
    }

    public void setVenId(int VenId) {
        this.VenId = VenId;
    }

    public int getIsXuanZuo() {
        return IsXuanZuo;
    }

    public void setIsXuanZuo(int IsXuanZuo) {
        this.IsXuanZuo = IsXuanZuo;
    }

    public int getOpenSum() {
        return openSum;
    }

    public void setOpenSum(int openSum) {
        this.openSum = openSum;
    }
}
