package com.cadyd.app.model;

import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

/**
 * 城市二级列表
 *
 * @author wangchaoyong
 */
public class CityInfo implements Serializable {
    public String id = "";
    public String name = "";
    public String areaid = "";
    public String areacode;

    public String longitude;//经度
    public String latitude;//纬度

    public CityInfo(String id, String name, String areaid) {
        this.id = id;
        this.name = name;
        this.areaid = areaid;
    }

    public CityInfo() {
    }

    public static Type getType() {
        Type type = new TypeToken<List<CityInfo>>() {
        }.getType();
        return type;
    }
}
