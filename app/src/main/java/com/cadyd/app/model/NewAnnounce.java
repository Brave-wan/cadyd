package com.cadyd.app.model;

import com.google.gson.reflect.TypeToken;
import org.wcy.common.utils.StringUtil;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

/**
 * 最新揭晓
 * Created by wcy on 2016/5/8.
 */
public class NewAnnounce implements Serializable {
    private static final long serialVersionUID = -3345205828566485102L;

    public String productid;//	商品主键	String
    public long countdown;
    public String tbid;//	抢购商品主键	String
    public String title;//	标题	String
    public String mainimg;//	商品图片	String
    public int price;//	价格	int
    public String announcedTime;//	揭晓时间	String
    public String nickname;//	昵称	String
    public String headimg;//	头像	String
    public int buytims;//购买次数	int
    public String luckcode;//	幸运码	String
    public String seq; //商品期数

    public static Type getType() {
        Type type = new TypeToken<List<NewAnnounce>>() {
        }.getType();
        return type;
    }

    public long[] getTimes() {
//        long times[] = new long[4];
//        long hour = 0;
//        long minute = 0;
//        long second = 10;
//        times[0] = 0;
//        times[1] = hour;
//        times[2] = minute;
//        times[3] = second;
//        return times;
        return StringUtil.secToTime(countdown);
    }

}
