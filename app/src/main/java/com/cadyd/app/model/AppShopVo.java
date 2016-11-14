package com.cadyd.app.model;
import java.util.List;
/**
 * Created by wcy on 2016/7/10.
 */
public class AppShopVo {
    public String shopid;// 店铺id
    public String spid;// 用户领取商家发布的优惠券或者平台优惠券的id,多个id以英文逗号分隔
    public String freightid;// 配送方式id
    public String message;// 买家留言
    public List<AppGoodsVo> goodsList;
}
