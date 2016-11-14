package com.cadyd.app.model;

import java.io.Serializable;

/**
 * 用户领取的优惠卷
 * //此列表如果为空，表示该用户未领取，否则表示已经领取
 * //用户领取的优惠券（暂时保留列表的形式，现在定为每个用户每种优惠券只能领取一张）
 * Created by wcy on 2016/5/25.
 */
public class ShopPrefer implements Serializable {
    private static final long serialVersionUID = 5531518498678193052L;
    public String id;//2", //用户领取的记录id
    public String orderid;//null, //优惠券使用的订单id
    public String state;//0, //状态：0：未使用，1：已使用，2：已失效
    public String userid;//"2", //用户的id
    public String preferid;//"1", //优惠券的id
    public String created;//"2015-12-07 15:14:13"//领取时间
}
