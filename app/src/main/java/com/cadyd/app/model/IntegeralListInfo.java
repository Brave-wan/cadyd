package com.cadyd.app.model;

import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by SCH-1 on 2016/8/8.
 */
public class IntegeralListInfo implements Serializable {

    private String date;

    private List<IntegralDetailInfo> detailIntegral;

    public IntegeralListInfo() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<IntegralDetailInfo> getDetailIntegral() {
        return detailIntegral;
    }

    public void setDetailIntegral(List<IntegralDetailInfo> detailIntegral) {
        this.detailIntegral = detailIntegral;
    }

    public static Type getType() {
        Type type = new TypeToken<List<IntegeralListInfo>>() {
        }.getType();
        return type;
    }
}
