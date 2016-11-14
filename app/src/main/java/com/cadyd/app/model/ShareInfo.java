package com.cadyd.app.model;

/**
 * Created by Administrator on 2016/10/12.
 */
public class ShareInfo {

    /**
     * clickUrl : http://xxxx/xxxx/?roomcode=f1006c019507462d967ad315402bd70e
     * content : 几天不见，有没有想我？我正在第一点直播！快来看。。。
     * key : 76eb35ff-9eba-4863-8a4b-36ff75cfef08
     * refCode : f1006c019507462d967ad315402bd70e
     * refType : 1
     * title :
     */

    private String clickUrl;
    private String pictureUrl;
    private String content;
    private String key;
    private String refCode;
    private int refType;
    private String title;

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getClickUrl() {
        return clickUrl;
    }

    public void setClickUrl(String clickUrl) {
        this.clickUrl = clickUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getRefCode() {
        return refCode;
    }

    public void setRefCode(String refCode) {
        this.refCode = refCode;
    }

    public int getRefType() {
        return refType;
    }

    public void setRefType(int refType) {
        this.refType = refType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
