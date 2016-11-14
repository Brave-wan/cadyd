package com.cadyd.app.model;

/**
 * Created by Administrator on 2016/10/13.
 */
public class LiveEndInfo {

    /**
     * headImageUrl : http://schimages.img-cn-hangzhou.aliyuncs.com/images/groupbuy_img/2016/10/13/d1002ff8-7815-4ac3-8a9b-15761b25546a.png@!100-100.jpg
     * scoreIncome : 0
     * visitCount : 0
     */

    private String headImageUrl;
    private int scoreIncome;
    private int visitCount;
    private int liveMinutes;

    public int getLiveMinutes() {
        return liveMinutes;
    }

    public void setLiveMinutes(int liveMinutes) {
        this.liveMinutes = liveMinutes;
    }

    public String getHeadImageUrl() {
        return headImageUrl;
    }

    public void setHeadImageUrl(String headImageUrl) {
        this.headImageUrl = headImageUrl;
    }

    public int getScoreIncome() {
        return scoreIncome;
    }

    public void setScoreIncome(int scoreIncome) {
        this.scoreIncome = scoreIncome;
    }

    public int getVisitCount() {
        return visitCount;
    }

    public void setVisitCount(int visitCount) {
        this.visitCount = visitCount;
    }
}
