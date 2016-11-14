package com.cadyd.app.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/5/14.
 */
public class UserMassge implements Serializable {
    private static final long serialVersionUID = 5528459112910064215L;
    public String encode;
    public String phone;
    public String sex;
    public String state;// 状态（0 启用，1  禁用 , 注销）
    public String photo;//头像
    public String id;//会员id
    public String balance;
    public String vipgrade;
    public String address; //详细地址
    public String created;//操作时间
    public String integration;// 积分
    public String name;
    public String current;// 花币
    public String oftenaddress;//常住地
    public String bank;// 银行卡数量
    public String signature;//用户的签名

    public int integral;
}
