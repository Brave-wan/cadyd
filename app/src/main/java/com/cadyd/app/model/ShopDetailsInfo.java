package com.cadyd.app.model;

/**
 * Created by root on 16-9-3.
 */
public class ShopDetailsInfo {


    /**
     * state : 0
     * data : {"followCount":1,"shopid":"","onlineCount":2,"photo":"http://schimages.img-cn-hangzhou.aliyuncs.com/images/groupbuy_img/2016/08/31/00bfe94a-c9f1-47bc-8984-98ea6f80351c.jpg@!100-100.jpg","nickname":"å\u0090\u008cå­¦","followState":1}
     */

    private int state;
    /**
     * followCount : 1
     * shopid :
     * onlineCount : 2
     * photo : http://schimages.img-cn-hangzhou.aliyuncs.com/images/groupbuy_img/2016/08/31/00bfe94a-c9f1-47bc-8984-98ea6f80351c.jpg@!100-100.jpg
     * nickname : åå­¦
     * followState : 1
     */

    private DataBean data;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private int followCount;
        private String shopid;
        private int onlineCount;
        private String photo;
        private String nickname;
        private int followState;

        public int getFollowCount() {
            return followCount;
        }

        public void setFollowCount(int followCount) {
            this.followCount = followCount;
        }

        public String getShopid() {
            return shopid;
        }

        public void setShopid(String shopid) {
            this.shopid = shopid;
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

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public int getFollowState() {
            return followState;
        }

        public void setFollowState(int followState) {
            this.followState = followState;
        }
    }
}
