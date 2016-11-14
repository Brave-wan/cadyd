package com.cadyd.app.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品详情页
 * Created by admin on 2016/5/12.
 */
public class GoodDetail {
    public String userId = "";//分销人id
    public String rate = "";//好评百分比
    public String commentNum;//评论数
    public int depState;//是否已经申请降价通知 0没有降价通知 1已经降价通知
    public Good goods = new Good();//商品信息
    public Good mallGoods = new Good();//积分商城的商品信息
    public List<PicInfo> picList = new ArrayList<>();//图片信息
    public List<Comment> commentList = new ArrayList<>();//评论信息
    public List<SaleInfo> saleList = new ArrayList<>();//销售信息
    public List<Certification> certificationList = new ArrayList<>();//认证信息
    public ShopInfo shopInfo = new ShopInfo();//店铺信息
    public Freight freight = new Freight();//运费频率
    public List<Prefer> preferList = new ArrayList<>();//优惠卷

    public int suitNumber;
}
