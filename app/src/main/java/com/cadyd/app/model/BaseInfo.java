package com.cadyd.app.model;

import java.util.List;

/**
 * Created by Administrator on 2016/5/11.
 * 商品详情
 */
public class BaseInfo {

//    baseInfo
//            productid
//    tbid
//            title
//    mainimg
//            price
//    announcedTime
//            nickname
//    headimg
//            buytims
//    participatetimes
//            hasparticipatetimes
//    buyproducttime
//            luckcode
//    hastimes
//            beforeMeber//上一云获得者
//    nickname
//            headimg
//    province //省
//            announcedTime
//    luckcode
//            buyTime //购买时间

    public String hastimes;
    public String buyproducttime;
    public String participatetimes;
    public String nickname;
    public String announcedTime; //揭晓时间
    public String headimg;
    public String userid;
    public String tbid;
    public String luckcode = "";
    public long countdown;
    public String title = "";
    public List<ImgList> imgList;
    public String price;
    public String buytims;
    public String mainimg; // 图片地址
    public String productid;
    public String listCount;
    public String hasparticipatetimes;
    public String average;
    public String loginip;


    private String beforeMeber;//上一云获得者
    private String province;//省
    private String buyTime;//购买时间
}
