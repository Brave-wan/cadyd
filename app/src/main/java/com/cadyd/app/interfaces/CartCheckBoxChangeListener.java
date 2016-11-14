package com.cadyd.app.interfaces;

import java.math.BigDecimal;

/**
 * 购物车接口
 * Created by wcy on 2016/5/23.
 */
public interface CartCheckBoxChangeListener {
    /**
     * 是否全选
     *
     * @param isChecked
     */
    public void onCheckChange(boolean isChecked);

    /**
     * 价格变化
     *
     * @param isChecked
     * @param bprice    编辑钱
     * @param price
     * @param id
     */
    public void onCheckChileChange(boolean isChecked, BigDecimal bprice, BigDecimal price, String id);

    /**
     * 数量变化
     *
     * @param isChecked
     * @param number    编辑后
     * @param bnum      编辑前
     */
    public void onCheckNumber(boolean isChecked, int bnum, int number);

    /**
     * 优惠卷
     *
     * @param merchantId
     */
    public void onCoupons(String merchantId);

}