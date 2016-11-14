package com.cadyd.app.model;

/**
 * 店铺信息
 * Created by wcy on 2016/5/12.
 */
public class ShopInfo {
    public String id;//18268ebda31711e5b7a414dda925d83c", //店铺的id
    public String logo;// null, //店铺的Logo

    public String level;//"4",//星级评数
    /**
     * zjh
     */
    public String levelString;
    public String logisticsLevel;
    public String logisticslevelString;
    public String serviceLevel;//服务登录
    public String servicelevelString;

    public String mark;             //"mark": "qeweq",
    public String multipleLevel;    // "multipleLevel": 0,

    public String totalGoods;// 1, //所有上架的宝贝数
    public String totalInter;// 0//关注度
    public String totalNew;// 0, //上新宝贝，即7天内的宝贝数
    public String name;// "好又来店铺1", //店铺的名称
    public String address;
    public String merchantId;// "064142a499d211e59cc214dda925d83c", //店铺的商家id

}
