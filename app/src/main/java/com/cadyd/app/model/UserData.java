package com.cadyd.app.model;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Administrator on 2016/5/14.
 */
public class UserData {


    public String id;//会员id
    public String createTime;//操作时间
    public String balance;//余额
    public String vipgrade;
    public String address;
    public String integration;// 积分
    public String name;
    public String current;// 花币
    public String bank;// 银行卡数量
    public String memberId;


    public static Type getType() {
        Type type = new TypeToken<List<UserData>>() {
        }.getType();
        return type;
    }

}
