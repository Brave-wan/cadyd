package com.cadyd.app.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: ${todo}(这里用一句话描述这个方法的作用)}$
 * 作者：Jerry
 * 邮箱：185214487@qq.com
 */
public class UserCenterInfo {
    /**
     * 个人基本信息
     */
    private BasicInfo userBasicInfo;

    /**
     * 视频数
     */
    private int videoCount;
    /**
     * 粉丝数
     */
    private int fansCount;
    /**
     * 关注数量
     */
    private int focusCount;
    /**
     * 关注状态 （当前操作用户相对于该用户的关注，1=已关注，2=未关注，3=不支持）
     */
    private int focusStatus;

    /**
     * 最近访客
     */
    private List<Visitors> lastVisitors;

    public BasicInfo getUserBasicInfo() {
        if (userBasicInfo == null)
            userBasicInfo = new BasicInfo();
        return userBasicInfo;
    }

    public void setUserBasicInfo(BasicInfo userBasicInfo) {
        this.userBasicInfo = userBasicInfo;
    }

    public int getVideoCount() {
        return videoCount;
    }

    public void setVideoCount(int videoCount) {
        this.videoCount = videoCount;
    }

    public int getFansCount() {
        return fansCount;
    }

    public void setFansCount(int fansCount) {
        this.fansCount = fansCount;
    }

    public int getFocusCount() {
        return focusCount;
    }

    public void setFocusCount(int focusCount) {
        this.focusCount = focusCount;
    }

    public int getFocusStatus() {
        return focusStatus;
    }

    public void setFocusStatus(int focusStatus) {
        this.focusStatus = focusStatus;
    }

    public List<Visitors> getLastVisitors() {
        return lastVisitors;
    }

    public void setLastVisitors(ArrayList<Visitors> lastVisitors) {
        this.lastVisitors = lastVisitors;
    }

    public static class Visitors {
        /**
         * 用户Id
         */
        private String userId;
        /**
         * 头像地址
         */
        private String headImageUrl;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getHeadImageUrl() {
            return headImageUrl;
        }

        public void setHeadImageUrl(String headImageUrl) {
            this.headImageUrl = headImageUrl;
        }

        @Override
        public String toString() {
            return "Visitors{" +
                    "userId='" + userId + '\'' +
                    ", headImageUrl='" + headImageUrl + '\'' +
                    '}';
        }
    }


    public static class BasicInfo {

        /**
         * 用户Id
         */
        private String userId;

        /**
         * 用户等级 （沿用以前的样子）
         */
        private String level;
        /**
         * 头像地址
         */
        private String headImageUrl;
        /**
         * 昵称
         */
        private String nickName;
        /**
         * 描述
         */
        private String des;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public String getHeadImageUrl() {
            return headImageUrl;
        }

        public void setHeadImageUrl(String headImageUrl) {
            this.headImageUrl = headImageUrl;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getDes() {
            return des;
        }

        public void setDes(String des) {
            this.des = des;
        }

        @Override
        public String toString() {
            return "BasicInfo{" +
                    "userId='" + userId + '\'' +
                    ", level='" + level + '\'' +
                    ", headImageUrl='" + headImageUrl + '\'' +
                    ", nickName='" + nickName + '\'' +
                    ", description='" + des + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "UserCenterInfo{" +
                "userBasicInfo=" + userBasicInfo +
                ", videoCount=" + videoCount +
                ", fansCount=" + fansCount +
                ", focusCount=" + focusCount +
                ", focusStatus=" + focusStatus +
                ", astVisitors=" + lastVisitors +
                '}';
    }
}
