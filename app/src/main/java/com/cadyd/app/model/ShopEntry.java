package com.cadyd.app.model;

import java.io.Serializable;

/**
 * Created by SCH-1 on 2016/8/3.
 */
public class ShopEntry implements Serializable {

    private String qrcodepath;
    private String id;
    private String logo;
    private String distance;
    private String phone;
    private String address;
    private String name;
    private int fansNum;
    private double longitude;
    private double latitude;
    private String thumb;

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    private boolean collect;

    private String merchantId;

    public ShopEntry() {
    }

    public int getFansNum() {
        return fansNum;
    }

    public void setFansNum(int fansNum) {
        this.fansNum = fansNum;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getQrcodepath() {
        return qrcodepath;
    }

    public void setQrcodepath(String qrcodepath) {
        this.qrcodepath = qrcodepath;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isCollect() {
        return collect;
    }

    public void setCollect(boolean collect) {
        this.collect = collect;
    }

    @Override
    public String toString() {
        return "ShopEntry{" +
                "qrcodepath='" + qrcodepath + '\'' +
                ", id='" + id + '\'' +
                ", logo='" + logo + '\'' +
                ", distance='" + distance + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", name='" + name + '\'' +
                ", fansNum=" + fansNum +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", thumb='" + thumb + '\'' +
                '}';
    }
}
