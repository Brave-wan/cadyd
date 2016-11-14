package com.cadyd.app.model;

import java.io.Serializable;

/**
 * Created by root on 16-9-2.
 */

public class SocketInfo implements Serializable {


    /**
     * content : 欢迎  同学!
     * followCount : 1
     * followState : 1
     * operation : 16777217
     * playNum : 2
     * shopid :
     */

    private DataBean data;
    /**
     * data : {"content":"欢迎  同学!","followCount":1,"followState":1,"operation":"16777217","playNum":2,"shopid":""}
     * state : 0
     */

    private int state;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public static class DataBean implements Serializable {
        private String content;
        private int followCount;
        private int followState;
        private String operation;
        private int playNum;
        private String shopid;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

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

        public String getOperation() {
            return operation;
        }

        public void setOperation(String operation) {
            this.operation = operation;
        }

        public int getPlayNum() {
            return playNum;
        }

        public void setPlayNum(int playNum) {
            this.playNum = playNum;
        }

        public String getShopid() {
            return shopid;
        }

        public void setShopid(String shopid) {
            this.shopid = shopid;
        }
    }
}
