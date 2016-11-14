package com.cadyd.app.model;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * 商品类型
 * Created by wcy on 2016/5/10.
 */
public class GoodType {
    public String id;
    public String name;//名称
    public String parentid;//父id
    public Integer isnext;//是否父级：0：是，1：否
    public Integer seq;// 序号
    public String type;//菜单类型：001：商城菜单，002：全球购菜单，003：一乡一物
    public String icon = "";//菜单的图片
    public List<Good> chilgoods;

    public List<Good> goodsList;//店铺的商品模型


    public static Type getType() {
        Type type = new TypeToken<List<GoodType>>() {
        }.getType();
        return type;
    }
}
