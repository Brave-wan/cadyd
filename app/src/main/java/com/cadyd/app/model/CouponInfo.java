package com.cadyd.app.model;

import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by SCH-1 on 2016/8/9.
 */
public class CouponInfo implements Serializable {

    private String id;

    private String title;

    private String goodsid;

    private String invalid;

    private double money;

    private int typeId;

    private int preCondition;

    public CouponInfo() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGoodsid() {
        return goodsid;
    }

    public void setGoodsid(String goodsid) {
        this.goodsid = goodsid;
    }

    public String getInvalid() {
        return invalid;
    }

    public void setInvalid(String invalid) {
        this.invalid = invalid;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public int getPreCondition() {
        return preCondition;
    }

    public void setPreCondition(int preCondition) {
        this.preCondition = preCondition;
    }

    public static Type getType() {
        Type type = new TypeToken<List<CouponInfo>>() {
        }.getType();
        return type;
    }
}
