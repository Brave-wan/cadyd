package com.cadyd.app.model;

import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.List;

/**
 * 商品信息
 * Created by wcy on 2016/5/10.
 */
public class Good implements Serializable {
    private static final long serialVersionUID = -4733485119837536887L;
    public String id;
    public String name; //商品名称
    public String menuid;// 类目id(外键关联mall_menu的id,而且该类目是最后一级)
    public String menuId;
    public String brandid; //商品名称(外键关联mall_brand的id)
    public BigDecimal original = new BigDecimal(0);  //商品原价
    public BigDecimal price = new BigDecimal(0);//商品价格
    public String created; //创建时间
    public String title = ""; //标题
    public BigDecimal pay = new BigDecimal(0);//佣金
    public int salenum;//月销售量
    public String address;//地址
    public String collid;//"-1"//-1表示未登录，’’表示未收藏，非空表示收藏记录id
    public String goodsid;
    public String mallname;
    public int bought;//已购买数量
    public String no; //货号
    public String des; //商家描述
    public String code;//商家编码
    public String purplace;//采购地
    public String place;//发货地
    public int number;//商品数量
    public String barcode;//商品条形码
    public String sellpoint;//宝贝卖点
    public Integer state;//商品状态：0上架，1下架，2即将上架
    public Integer isplaza;//是否购物中心：0：是，1：否
    public Integer isprom;//是否促销：0：是，1：否
    public String endtime;//商品结束时间：即下架时间
    public Integer type;//宝贝类型：0全新，1二手
    public Integer distribution;//是否分销：0：是，1：否，默认1
    public String shopid;//店铺id
    public Integer source;//发布来源：0：pc端，1：手机端
    public String gcode;//商品所属大分类的id(goods_category的id)
    public String freightid;//邮寄方式，商家设定的邮费
    public Integer integral = 0;//商品积分  积分商城的商品积分
    public int useintegral;
    public Integer recommend;//是否推荐橱窗：0：是，1：否
    public String releasetime;//上架时间
    public String thumb;//压缩图片地址
    public String packages;//包装售后
    public int islucky;//是否为可以一元购商品 2.可以

    public int luckycodecount;//可使用的积分

    public String distance;
    public String shopName;
    public int saleNum;
    public String minorphone;

    //    "latitude": "29.5801020",
//            "longitude": "15.5395810",
    public Double latitude;
    public Double longitude;

    public int rebatescale;//

    public static Type getType() {
        Type type = new TypeToken<List<Good>>() {
        }.getType();
        return type;
    }
}
