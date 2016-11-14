package com.cadyd.app.model;

import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 购物车店铺信息
 * Created by wcy on 2016/5/22.
 */
public class CartShop implements Serializable {
    private static final long serialVersionUID = -6762830113650126488L;
    public String shopid;// //店铺的id
    public String shopname;// "好又来店铺1", //店铺名称
    public String merchantid;// 商户id
    //该商家是否有优惠券,有的话则存在其id
    public String prefermid;// "064142a499d211e59cc214dda925d83c",

    public String spId;//优惠卷
    public String freightid;//运送方式
    public List<Prefer> preferList = new ArrayList<>();//优惠券列表
    public List<Freight> freightList = new ArrayList<>();//快递列表
    public List<Suit> suitList = new ArrayList<>();//套餐列表

    /**xiongmao*/
    public BigDecimal coupon = new BigDecimal(0);//用戶選擇的優惠券
    public BigDecimal Freight = new BigDecimal(0);//用戶選擇的運費
    public BigDecimal totalManey = new BigDecimal(0);//用戶选择的商品的总价
    public boolean isChick = false;//用户选择的商家

    public static Type getType() {
        Type type = new TypeToken<List<CartShop>>() {
        }.getType();
        return type;
    }
}
