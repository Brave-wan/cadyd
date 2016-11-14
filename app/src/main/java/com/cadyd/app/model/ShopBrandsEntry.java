package com.cadyd.app.model;

import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by SCH-1 on 2016/8/4.
 */
public class ShopBrandsEntry implements Serializable {

    private String logo;

    private String name;

    private String brandId;

    public ShopBrandsEntry() {
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public static Type getType() {
        Type type = new TypeToken<List<ShopBrandsEntry>>() {
        }.getType();
        return type;
    }
}
