package com.cadyd.app.model;

/**
 * Created by xiongmao on 2016/10/29.
 */

public class FlowerCoinsRechargeModel {
//    orderSerialNo	String	否	订单序列号（服务端业务中其实是长整型）
//    token	String	否	报文信息

    private String orderSerialNo;
    private String token;

    public String getOrderSerialNo() {
        return orderSerialNo;
    }

    public void setOrderSerialNo(String orderSerialNo) {
        this.orderSerialNo = orderSerialNo;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "FlowerCoinsRechargeModel{" +
                "orderSerialNo='" + orderSerialNo + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
