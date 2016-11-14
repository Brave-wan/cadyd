package com.cadyd.app.model;

import java.util.List;

/**
 * Created by admin on 2016/6/2.
 */
public class MyOrder {
    /**
     * id : cc63c0da55054b8382ec520ffda846d5
     * flowno : 201512032302258518738
     * created : 2015-12-03 23:02:25
     * paydate : null
     * userid : boss
     * state : 1
     * total : 75.0
     * trade : 65.0
     * pay : 85.0
     * actual : 65.0
     * preferential : -20.0
     * virtual : 0.0
     * mailcost : 10.0
     * addressid : null
     * resource : 1
     * paytype : null
     * message : null
     * closetype : 0
     * closereason : 3
     * remark : null
     * shopid : 5
     * channelNo : null
     * updated : 2015-12-11 11:05:20
     * sprefer : null
     * sendno : 897570000
     * buyerInfo : {"id":null,"phone":"155","password":null,"name":"王五","photo":null,"sex":null,"state":null,"oftenaddress":null,"created":null}
     * serialnum : 201512032302258518738
     * statename : 交易关闭
     * reasondes : 买家已经通过网上银行直接汇款给我了
     * shopname : null
     * spId : null
     * detailList : [{"id":"0553111d62fb454b83580a38700fdf36","orderid":"cc63c0da55054b8382ec520ffda846d5","goodsid":null,"salemix":"颜色:乳白色,尺寸:M","shopid":null,"goodstitle":"好产品哦22222222","total":25,"goodsprice":25,"preferential":-20,"number":1,"pay":0,"userid":null,"resource":null,"type":null,"thumb":null,"state":1,"delay":null,"statename":null,"extra":null,"activetitle":null,"cid":null}]
     * logistics : {"id":"xxx","cid":"xxx","ordered":"xxx","created":"xxxx","company":"xxx"}
     */
    public boolean refundFlag;//是否退款
    public String shopphone;//卖家电话 "023-46788304"
    public String id;//订单好
    public String flowno;//业务流水号
    public String created;//订单创建日期
    public String paydate;// 订单付款日期
    public String userid;//用户账号id
    public Integer state;// 订单交易状态（0：交易成功(成功的订单)，1：交易关闭(关闭的订单)，2：等待买家付款，3：等待发货，4：部分发货，5：已发货，6：退款中，7：异常订单）
    public int commentstate;
    public String total;// 总金额=交易金额 - 优惠金额(负数代表优惠或者折扣)+优惠券金额
    public String trade;// 交易金额=实际付款+花币
    public String pay;// 应付金额=sum(商品价格*商品数量)+邮费
    public String actual;// 实际付款=应付金额+优惠价格-花币-优惠券金额
    public String preferential;// 优惠价格(负数代表优惠折扣)
    public String virtual;// 花币
    public String mailcost;// 邮件费用
    public String addressid;// 地址库id
    public String resource;// 交易来源(0：pc端，1：手机端)
    public String paytype;// 订单付款日期
    public String message;// 买家留言
    public String closetype;// 交易关闭类型：0：卖家取消，1：买家取消，2：超时关闭
    public String closereason;// 取消原因（外键关联system_code中的取消原因中的value）
    public String remark;// 卖家添加订单备注
    public String shopid;//店铺id
    public String channelNo;// 渠道号(非数据库字段)
    public String updated;//订单修改时间
    public String sprefer;// 商家优惠券金额
    public String sendno;//物流编号
    public BuyerInfoEntity buyerInfo;//买家信息
    public String serialnum;//业务流水号
    public String statename;//"交易关闭",//状态中文名称
    public String reasondes;//买家已经通过网上银行直接汇款给我了",//原因名称(非数据库字段)
    public String shopname;//店铺名称
    public String spId;//订单优惠券的id集合(参数字段,非数据库表字段)
    public Accept accept;
    public LogisticsEntity logistics;//物流公司信息
    public List<DetailListEntity> suitList;//订单详情列表

    /**
     * zhou
     **/
    public class Accept {
        /**
         * address : 天津天津市
         * consignee : 爸爸
         * zipcode : 333333
         * telphone : 96969696969
         */
        public String address;
        public String consignee;
        public String zipcode;
        public String telphone;
        public String accept;
    }

    public class BuyerInfoEntity {
        /**
         * id : null
         * phone : 155
         * password : null
         * name : 王五
         * photo : null
         * sex : null
         * state : null
         * oftenaddress : null
         * created : null
         */

        public String id;
        public String phone;
        public String password;
        public String name;
        public String photo;
        public String sex;// 性别:0：男，1：女
        public String state;// 状态（0 启用，1  禁用）
        public String oftenaddress;//常居地
        public String created;
    }

    public class LogisticsEntity {
        /**
         * id : xxx
         * cid : xxx
         * ordered : xxx
         * created : xxxx
         * company : xxx
         */
        public String id;
        public String cid;
        public String ordered;
        public String created;// 创建时间，即发货时间
        public String company;//公司名称，非数据库字段
    }

    /**
     * 商品详细信息
     */
    public class DetailListEntity {
        public String id;
        //zhou 加的
        public int num;
        public Double price;
        public boolean suit;
        /**
         * 未知参数
         **/
        public int count;
        public String goodsId_pkid;

        //商城的我的订单的商品模型
        // public List<NewDetailListEntity> detailList;//订单列表的key的名字
        public List<NewDetailListEntity> orderDetail;//订单详情的key的名字
    }

    public class NewDetailListEntity {
        /**
         * 商城的我的订单的数据模型
         * <p>
         * id : 0553111d62fb454b83580a38700fdf36
         * orderid : cc63c0da55054b8382ec520ffda846d5
         * goodsid : null
         * salemix : 颜色:乳白色,尺寸:M
         * shopid : null
         * goodstitle : 好产品哦22222222
         * total : 25.0
         * goodsprice : 25.0
         * preferential : -20.0
         * number : 1
         * pay : 0.0
         * userid : null
         * resource : null
         * type : null
         * thumb : null
         * state : 1
         * delay : null
         * statename : null
         * extra : null
         * activetitle : null
         * cid : null
         */
        public String orderid;//订单id
        public String goodsid;// 商品Id(mall_goods的id)或者活动id(seller_shop_active的id)
        public String salemix;//销售属性组合值
        public String shopid;//店铺id
        public String goodstitle;//商品标题或者优惠券编码
        public String total;// 总金额
        public String goodsprice;// 商品列表价格
        public String preferential;// 优惠价(负数代表优惠折扣)
        public String number;// 数量
        public String pay;// 佣金
        public String userid;// 分销人id
        public String resource;// 来源：（0：商品，1：商城优惠券，2：商家实体店活动，3积分商城 , 4商家优惠）
        public String type;// 商品类型：0是实物商品，1是虚拟商品
        public String thumb;//缩略图
        public String state;// 订单详情状态（0：未付款，1：已取消，2：退款成功，3：已确认收货，4：未确认收货）
        public String delay;// 延长收货时间
        public String statename;//状态名称
        public String extra;// 是否取消此订单详情(非数据库字段)
        public String activetitle;// 活动标题(非数据库字段)
        public String cid;//评论情况：未评论，已评论(如果id存在，则表示已评论，否则表示未评论，此评论关联comment_goods的id)，非数据库字段
    }

    public ExpMap expMap;

}
