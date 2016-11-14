package com.cadyd.app.model;

import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 优惠卷
 * Created by wcy on 2016/5/12.
 */
public class Prefer implements Serializable {

    private static final long serialVersionUID = 4508244422853718700L;
    public String id;// "1", //优惠券的id
    public String merchantid;//  "2", //商家的id
    public String codeid;//  null, //暂时不管此字段
    public String type;//  1, //暂时不管此字段
    public BigDecimal money = new BigDecimal(0);//  50, //优惠金额
    public BigDecimal condition=new BigDecimal(0);//  400, //优惠条件
    public String des;// " ", //描述
    public int state;//  0, //状态：0：未过期，1：已过期
    public String num;// 100, //优惠券数量
    public String path;// null, //暂时不管此字段
    public String valid;//  "2015-12-30", //开始时间(起)
    public String invalid;//  "2016-01-31", //有效时间(止)
    public String created;//  "2015-12-07 14:14:14", //创建时间（优惠券发布时间）
    public String codename;// null, //暂时不管此字段
    public String typename;//  null, //暂时不管此字段
    public String used;//  null, //暂时不管此字段
    public String title;
    public String spreferId;//用户领取优惠卷的id
    public String preferId;//优惠券id
    public String rid;
    public List<ShopPrefer> shopPreferList = new ArrayList<>();//

    public static Type getType() {
        Type type = new TypeToken<List<Prefer>>() {
        }.getType();
        return type;
    }
}
