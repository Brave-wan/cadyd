package com.cadyd.app.model;

/**
 * Created by root on 16-9-2.
 */

public class AttactionInfo {

    /**
     * @interface LivePersonModel : SCHBaseModel
    __decl_model_string__ *followCount;
    __decl_model_string__ *followState;
    __decl_model_string__ *nickname;
    __decl_model_string__ *onlineCount;
    __decl_model_string__ *photo;
    __decl_model_string__ *shopid;
     @end
     */

    private int  followCount;
    private int  followState;
    private String  nickname;
    private int  onlineCount;
    private String  photo;
    private String  shopid;


    public int getFollowCount() {
        return followCount;
    }

    public void setFollowCount(int followCount) {
        this.followCount = followCount;
    }

    public int getFollowState() {
        return followState;
    }

    public void setFollowState(int followState) {
        this.followState = followState;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getOnlineCount() {
        return onlineCount;
    }

    public void setOnlineCount(int onlineCount) {
        this.onlineCount = onlineCount;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getShopid() {
        return shopid;
    }

    public void setShopid(String shopid) {
        this.shopid = shopid;
    }
}
