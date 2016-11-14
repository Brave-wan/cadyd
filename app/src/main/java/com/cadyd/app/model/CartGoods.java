package com.cadyd.app.model;


import java.io.Serializable;

/**
 * 购物车商品信息
 * Created by wcy on 2016/5/22.
 */
public class CartGoods implements Serializable{
    private static final long serialVersionUID = 1808449020366264346L;
    public String salemix;// "颜色:黑色,尺寸:M", //商品的销售属性
    public String id;//  "8607dc66e4724b6cabb2a5e7674d7283", //购物车的id
    public String num;//  5, //商品的数量
    public String title;//  "2015秋装新款男士夹克 立领修身青年时尚休闲皮外套男 B01", //商品的标题
    public String price;//  138, //商品的价格
    public String goodsid;//  "0661f23e1f6144028da0536aec6899ff", //商品的id
    public String created;//  "2015-11-17 14:42:55", //商品加入时间
    public String userid;// "ebbcc4feb3fd4258b30d40f2d02962b8", //用户的id
    public String original;//  1000, //商品市场价
    public String path;//  "images/goods/20151126/1448506181738.png", //商品的路径
    public String thumb;//  "images/goods/20151126/thumb/1448506181738.png"//商品的缩略图
    public String rid;
    public String spid;


    public int luckycodecount;//可以使用的乐购码数量

    public String goodsId;//积分商城商品id
    public Integer integral = 0;//商品积分
    public String number = "1";//  5, //商品的数量
    public boolean isChecked = false;
}
