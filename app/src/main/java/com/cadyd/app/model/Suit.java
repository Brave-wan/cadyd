package com.cadyd.app.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 套装信息
 * Created by wcy on 2016/7/2.
 */
public class Suit implements Serializable {
    private static final long serialVersionUID = -2641667641444291604L;
    public int num;
    public boolean suit;
    public float price;
    public String id;
    public String name;
    public List<CartGoods> goodsList = new ArrayList<>();//商品信息

    /**
     * 我的订单的商品模型
     */
    public List<CartGoods> detailList = new ArrayList<>();//商品信息


    /**
     * goodsPrefer : false
     * goodsPreferAmount : 0
     * luckCode : false
     * luckCodeCount : 0
     * maxCount : 0
     * reabte : false
     * rebateAmount : 0
     * rebateId :
     */

    public boolean goodsPrefer;//是否试用单品优惠卷
    public int goodsPreferAmount;//单品优惠卷金额
    public boolean luckCode;//是否使用乐购码
    public int luckCodeCount;//乐购码个数
    public int maxCount;//最多试用个数
    public boolean reabte;//是否返利
    public int rebateAmount;//返利金额
    public String rebateId;//返利id

    /**
     * xiongmao
     */
    public boolean isCheck = false;//是否被选中
}
