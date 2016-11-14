package com.cadyd.app.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/5/18.
 */
public class RecordModleData implements Serializable {


    private static final long serialVersionUID = 735169336672623453L;
    public String luckcode; //幸运乐购码
    public String headimg;//获奖者头像
    public String nickname; //获奖者名字
    public String title;                //标题
    public int participatetimes;    //参与人数
    public int buytims;              //参与次数
    public int price;               //价格
    public String mainimg;           //图片
    public String productid;         //商品主键
    public String announcedTime;  // 开间时间（如果没有者是没有开奖）
    public int seq;                 //正在进行的期数
    public int hasparticipatetimes; //与参与的人数
    public String tbid;             //抢购商品主键

}
