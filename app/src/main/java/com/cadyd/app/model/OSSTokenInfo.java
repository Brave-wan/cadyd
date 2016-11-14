package com.cadyd.app.model;

/**
 * Created by Administrator on 2016/10/17.
 */
public class OSSTokenInfo {
    String AccessKeyId;
    String AccessKeySecret;
    String SecurityToken;
    String Expiration;
    String OSS_BucketName;
    String OSS_Url;
    String OSS_Callback;
    String OSS_ImgPath;

    public String getAccessKeyId() {
        return AccessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        AccessKeyId = accessKeyId;
    }

    public String getAccessKeySecret() {
        return AccessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        AccessKeySecret = accessKeySecret;
    }

    public String getSecurityToken() {
        return SecurityToken;
    }

    public void setSecurityToken(String securityToken) {
        SecurityToken = securityToken;
    }

    public String getExpiration() {
        return Expiration;
    }

    public void setExpiration(String expiration) {
        Expiration = expiration;
    }

    public String getOSS_BucketName() {
        return OSS_BucketName;
    }

    public void setOSS_BucketName(String OSS_BucketName) {
        this.OSS_BucketName = OSS_BucketName;
    }

    public String getOSS_Url() {
        return OSS_Url;
    }

    public void setOSS_Url(String OSS_Url) {
        this.OSS_Url = OSS_Url;
    }

    public String getOSS_Callback() {
        return OSS_Callback;
    }

    public void setOSS_Callback(String OSS_Callback) {
        this.OSS_Callback = OSS_Callback;
    }

    public String getOSS_ImgPath() {
        return OSS_ImgPath;
    }

    public void setOSS_ImgPath(String OSS_ImgPath) {
        this.OSS_ImgPath = OSS_ImgPath;
    }

    @Override
    public String toString() {
        return "OSSTokenInfo{" +
                "AccessKeyId='" + AccessKeyId + '\'' +
                ", AccessKeySecret='" + AccessKeySecret + '\'' +
                ", SecurityToken='" + SecurityToken + '\'' +
                ", Expiration='" + Expiration + '\'' +
                ", OSS_BucketName='" + OSS_BucketName + '\'' +
                ", OSS_Url='" + OSS_Url + '\'' +
                ", OSS_Callback='" + OSS_Callback + '\'' +
                ", OSS_ImgPath='" + OSS_ImgPath + '\'' +
                '}';
    }
}
