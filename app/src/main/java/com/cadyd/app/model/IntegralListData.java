package com.cadyd.app.model;


import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/5/10.
 */
public class IntegralListData {
    public String content;
    public String id;
    public String operation;
    public String addtime;//时间
    public String detail;//积分明细
    public String name;
    public String number;
    public String memberId;

    /**
     * "accept": "重庆重庆市打算实施细则",
     * "addressid": "438f62a1bd2246bea01476cf1b028864",
     * "aggregate": 99,
     * "consignee": "熊猫",
     * "created": "2016-07-13 16:25:47",
     * "flowno": "221a5c8003a54870addc1e2611aa7e28",
     * "id": "60b6bdc92e7c4fe0a5906a5ed323e7b1",
     * "isdel": 0,
     * "mailcost": 0,
     * "number": 1,
     * "orderIntegralGoods": [ {}],
     * "price": 0.01,
     * "serialnum": "201607131625476958184",
     * "state": 2,
     * "statename": "等待买家付款",
     * "telphone": "18580402350",
     * "userid": "f3c5fb8bd9264f71b62f74c0921dbda0",
     * "zipcode": "400000"
     */

    public String accept;
    public String addressid;
    public int aggregate = 0;   //订单的总的积分
    public String consignee;
    public String created;
    public String flowno;
    //  public String id;  //有了
    public int isdel;
    public int mailcost;
    //   public int number;  //有了
    public List<OrderIntegralGoods> orderIntegralGoods;//积分商城的我的订单的商品模型
    public Double price;
    public String serialnum;
    public String state;
    public String statename;
    public String telphone;
    public String userid;
    public String zipcode;

    public class OrderIntegralGoods implements Serializable {
        private static final long serialVersionUID = -8872740313721651196L;
        /**
         * "goodsId": "00c7e6ad282a4e148174dedbad7b9777",
         * "goodsName": "李宁LINING 标准5号/4号足球 手缝球058(4号儿童足球)",
         * "id": "6b34617117524bdb92b5a87682fbfeda",
         * "integral": 99,
         * "number": 1,
         * "orderIntegralId": "60b6bdc92e7c4fe0a5906a5ed323e7b1",
         * "path": "http://114.55.58.18:8087/sch/images/integral/1453977268741.jpg",
         * "price": 0.01
         **/
        public String goodsId;
        public String goodsName;
        public String id;
        public int integral;
        public int number;
        public String orderIntegralId;
        public String path;
        public Double price;
        public int commentstate = -1;//是否已经评论 0未评论 1已评论
    }

    public ExpMap expMap;
}
