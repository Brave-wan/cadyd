package com.cadyd.app.model;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by xiongmao on 2016/7/28.
 */
public class HomeShopModel {
//    "id": "b1a6ec29d5e411e5955b00163f000345",
//            "logo": "http://114.55.58.18:8087/sch/images/putaway/1457156026952.jpg",
//            "story": "",
//            "distance": "6.4km",
//            "name": "正宗重庆火锅",
//            "temp": 0.0581702418939421,
//            "longitude": 106.515461,
//            "latitude": 29.526774

    public String id;
    public String logo;
    public String story;
    public String distance;
    public String name;
    public String temp;
    public String longitude;
    public String latitude;

    public static Type getType() {
        Type type = new TypeToken<List<HomeShopModel>>() {
        }.getType();
        return type;
    }

}
