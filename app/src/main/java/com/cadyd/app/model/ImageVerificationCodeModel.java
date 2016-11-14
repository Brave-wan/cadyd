package com.cadyd.app.model;

/**
 * Created by xiongmao on 2016/10/24.
 */

public class ImageVerificationCodeModel {

    public String smsCode;
    public String tel_token;
    public String tel_sessionid;

    public String getSmsCode() {
        return smsCode;
    }

    public void setSmsCode(String smsCode) {
        this.smsCode = smsCode;
    }

    public String getTel_token() {
        return tel_token;
    }

    public void setTel_token(String tel_token) {
        this.tel_token = tel_token;
    }

    public String getTel_sessionid() {
        return tel_sessionid;
    }

    public void setTel_sessionid(String tel_sessionid) {
        this.tel_sessionid = tel_sessionid;
    }

    @Override
    public String toString() {
        return "ImageVerificationCodeModel{" +
                "smsCode='" + smsCode + '\'' +
                ", tel_token='" + tel_token + '\'' +
                ", tel_sessionid='" + tel_sessionid + '\'' +
                '}';
    }
}
