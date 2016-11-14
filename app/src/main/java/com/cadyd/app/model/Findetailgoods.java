package com.cadyd.app.model;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * 商品图片详情规格参数信息
 * Created by wcy on 2016/5/14.
 */
public class Findetailgoods {
    public String id;//
    public String mixfield;// "正装高帮皮鞋";//
    public String attrname;//"fashion";//
    public String attrdes;// "款式";//
    public static Type getType() {
        Type type = new TypeToken<List<Findetailgoods>>() {
        }.getType();
        return type;
    }
}
