package com.cadyd.app.model;

/**
 * Created by xiongmao on 2016/10/25.
 */

public class ThreeWayBindingModel {
    private int bindStatus;//绑定状态（1-已绑定；2-未绑定）
    private int usertype;//绑定状态（1-已绑定；2-未绑定）
    private String nickname;//

    public int getBindStatus() {
        return bindStatus;
    }

    public void setBindStatus(int bindStatus) {
        this.bindStatus = bindStatus;
    }

    public int getUsertype() {
        return usertype;
    }

    public void setUsertype(int usertype) {
        this.usertype = usertype;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public String toString() {
        return "ThreeWayBindingModel{" +
                "bindStatus=" + bindStatus +
                ", usertype=" + usertype +
                ", nickname='" + nickname + '\'' +
                '}';
    }
}
