package com.cadyd.app.model;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * 广告位查询
 * Created by wcy on 2016/5/8.
 */
public class Findadver {
    public String url;//店铺id
    public String id;// "20bb16478ac941e7b69a5b41bd8096a7", //广告的Id
    public String positionid;// "G101", 广告位置
    public int isshop;// 0, //是否跳转店铺，0：是，1：否
    public int typeid;// 1, //广告类型（先暂时不理会）
    public String name;// "购物中心title", //名称(如果是文字广告，则使用此名称)
    public String target;// "c7cfe235b91211e5b44e14dda925d83c", //店铺的id或者链接地址
    public int isdirect;// 1, //广告方向：0：网页，1：app//图片
    public String imgurl;// http://115.29.151.27:8080/images/advertisement/1453951936720.jpg
    public static Type getType() {
        Type type = new TypeToken<List<Findadver>>() {
        }.getType();
        return type;
    }
}
