package com.cadyd.app.model;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * 旗舰店
 * Created by wcy on 2016/5/5.
 */
public class FlagshipStore {
    public String id;//18268ebda31711e5b7a414dda925d83c", //店铺id
    public String logo;// "http://www.cadyd.com/images/merchant/logo/d99d5bbbc0d711e5941e14dda925d83c.jpg", //店铺logo
    public int num;// 1, //收藏数
    public String shopname;// "好又来店铺" //店铺名称


    public String distance;//距离
    public int collectNum;//人气
    public int salenum;//销量
    public String name;

    public static Type getType() {
        Type type = new TypeToken<List<FlagshipStore>>() {
        }.getType();
        return type;
    }
}
