package com.cadyd.app.model;

import java.io.Serializable;

public class UserInfo implements Serializable {
    private String userIcon;
    private String userName;
    private String userGender;
    private String token;
    private String platformName;

    public String getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(String userIcon) {
        this.userIcon = userIcon;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserGender() {
        return userGender;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "userIcon='" + userIcon + '\'' +
                ", userName='" + userName + '\'' +
                ", userGender=" + userGender +
                ", token='" + token + '\'' +
                ", platformName='" + platformName + '\'' +
                '}';
    }
}
