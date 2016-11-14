package com.cadyd.app.model;


import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by xiongmao on 2016/7/1.
 * 积分商城商品model
 */
public class IntegralGoodsModel {

//             "bought": 0,
//            "id": "fc42c2cdaa8147ff8f2843593dc8e62d",
//            "integral": 50,
//            "thumb": "http://114.55.58.18:8087/sch/images/goods/20160111/thumb/1452496800319.jpg",
//            "title": "123132"


    public int bought;
    public String id;
    public int integral;//积分
    public String thumb;
    public String title;
    public String price;

    public static Type getType() {
        Type type = new TypeToken<List<IntegralGoodsModel>>() {
        }.getType();
        return type;
    }
}
