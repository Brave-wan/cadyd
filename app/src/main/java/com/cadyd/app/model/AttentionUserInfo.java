package com.cadyd.app.model;

/**
 * @Description: 关注用户
 * 作者：Jerry
 * 邮箱：185214487@qq.com
 */
public class AttentionUserInfo {
    /**
     * 用户Id
     */
    private String userId;
    /**
     * 头像
     */
    private String headImageUrl;
    /**
     * 昵称
     */
    private String nickName;
    /**
     * 关注状态（1=已关注，2=未关注）
     */
    private int focusStatus;

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

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getFocusStatus() {
        return focusStatus;
    }

    public void setFocusStatus(int focusStatus) {
        this.focusStatus = focusStatus;
    }
}
