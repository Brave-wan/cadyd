package com.cadyd.app.model;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * 顶部菜单
 * Created by wcy on 2016/5/5.
 */
public class HomeMenu {
    public String id;//b36a8a2100d84fb7980e9fe224ac9a30", //id
    public String name;// "我要开店", //名称
    public String mark;// "DISTR", //标识
    public String icon;//"http://www.cadyd.com/images/config/20160222/1456147572028.png", //导航
    public String hicon;//"http://www.cadyd.com/images/config/20160222/1456147580567.png", //热门推荐
    public int ishot;//0, //是否热门推荐：0：是，1:否，-1:全部分类(此按钮不参与功能跳转，只负责页面显示全部分类的交互)
    public int state;// 0, //状态：0：启用，1：禁用
    public String seq;// 5, //排序
    public String created;//2016-02-22 21:26:24"

    public static Type getType() {
        Type type = new TypeToken<List<HomeMenu>>() {
        }.getType();
        return type;
    }
}
