package com.cadyd.app.handle;


import com.cadyd.app.R;

/**
 * 枚举类
 * Created by wcy
 */
public class InterFace {
    /**
     * 广告位置
     *
     * @author wangchaoyong
     */
    public enum FindadverType {
        A101("A101", "第一点一元购"), A102("A102", ""), A103("A103", "仅此一天，今日特价"),
        A104("A104", "优惠活动总汇"), A105("A105", "品牌特价A105"), A106("A106", "全球购", R.color.A106),
        A107("A107", "汽车"), A108("A108", "限时抢购"), A109("A109", "美食"),
        A110("A110", "休闲娱乐"), A111("A111", "生活缴费"), A112("A112", ""),
        NOT_VERIFY("", "");
        public String ecode;
        public String ename;
        public int color;

        private FindadverType(String code, String vname, int color) {
            this.ecode = code;
            this.ename = vname;
            this.color = color;
        }

        private FindadverType(String code, String vname) {
            this.ecode = code;
            this.ename = vname;
        }

        public String getEcode() {
            return ecode;
        }

        public void setEcode(String ecode) {
            this.ecode = ecode;
        }

        public String getEname() {
            return ename;
        }

        public void setEname(String ename) {
            this.ename = ename;
        }

        public static FindadverType format(String ecode) {
            for (FindadverType is : FindadverType.values()) {
                if (is.ecode.equals("ecode")) {
                    return is;
                }
            }
            return FindadverType.NOT_VERIFY;
        }
    }

    /**
     * 用户类型
     *
     * @author wangchaoyong
     */
    public enum ShareType {
        wechat(1, "微信", R.mipmap.logo_wechat), wechatmoments(2, "朋友圈", R.mipmap.logo_wechatmoments), QQ(3, "QQ",
                R.mipmap.logo_qq), qzone(4, "QQ空间", R.mipmap.logo_qzone);
        public int ecode;
        public String ename;
        public int resid;

        private ShareType(int code, String vname, int resid) {
            this.ecode = code;
            this.ename = vname;
            this.resid = resid;
        }

        public int getEcode() {
            return ecode;
        }

        public void setEcode(int ecode) {
            this.ecode = ecode;
        }

        public String getEname() {
            return ename;
        }

        public void setEname(String ename) {
            this.ename = ename;
        }

        public static ShareType format(int ecode) {
            for (ShareType is : ShareType.values()) {
                if (is.ecode == ecode) {
                    return is;
                }
            }
            return ShareType.wechat;
        }
    }

    /**
     * 订单状态
     *
     * @author wangchaoyong
     */
    public enum OrderStatus {
        /**
         * 待评价有问题应该是退款中
         */
        SUCCEED(0, "交易成功"), CLOSE(1, "交易关闭"), PAYMENT(2, "等待买家付款"), SHIPMENTS(3, "等待卖家发货"), PORTION(4, "部分发货"), REFUND(5, "待收货"), EVALUATE(6, "退款中"), EXCEPTION(7, "异常订单"), INVALID(8, "无效订单"),
        REQUESATREFUND(9, "申请退款"), DENIAL(10, "拒绝退款"), AGREE(11, "同意退款"), REFUNDSUCCESSFULLY(12, "退款成功"), UNKNOWN(-1, "未知");
        public Integer ecode;
        public String ename;

        private OrderStatus(Integer code, String vname) {
            this.ecode = code;
            this.ename = vname;
        }

        public Integer getEcode() {
            return ecode;
        }

        public void setEcode(Integer ecode) {
            this.ecode = ecode;
        }

        public String getEname() {
            return ename;
        }

        public void setEname(String ename) {
            this.ename = ename;
        }

        public static OrderStatus format(Integer ecode) {
            if (ecode == null) {
                return OrderStatus.UNKNOWN;
            }
            for (OrderStatus is : OrderStatus.values()) {
                if (is.ecode.equals(ecode)) {
                    return is;
                }
            }
            return OrderStatus.UNKNOWN;
        }
    }

    /**
     * 商品状态
     * 订单详情状态（0：未付款，1：已取消，2：退款成功，3：已确认收货，4：未确认收货）
     *
     * @author wangchaoyong
     */
    public enum GoodsStatus {
        PAYMENT(0, "未付款"), CANCEL(1, "已取消"), REFUND(2, "退款成功"), AFFIRM(3, "已确认收货"), UNCONFIRMED(4, "未确认收货"), UNKNOWN(-1, "未知");
        public Integer ecode;
        public String ename;

        private GoodsStatus(Integer code, String vname) {
            this.ecode = code;
            this.ename = vname;
        }

        public Integer getEcode() {
            return ecode;
        }

        public void setEcode(Integer ecode) {
            this.ecode = ecode;
        }

        public String getEname() {
            return ename;
        }

        public void setEname(String ename) {
            this.ename = ename;
        }

        public static OrderStatus format(Integer ecode) {
            if (ecode == null) {
                return OrderStatus.UNKNOWN;
            }
            for (OrderStatus is : OrderStatus.values()) {
                if (is.ecode.equals(ecode)) {
                    return is;
                }
            }
            return OrderStatus.UNKNOWN;
        }
    }

    /**
     * 订单状态
     * 订单详情状态（0：未付款，1：已取消，2：退款成功，3：已确认收货，4：未确认收货）
     *
     * @author wangchaoyong
     */
    public enum OrderType {
        MALL("MALL", "商城"), VIRTUAL("virtual", "花币"), BALANCE("balance", "余额");
        public String ecode;
        public String ename;

        private OrderType(String code, String vname) {
            this.ecode = code;
            this.ename = vname;
        }

        public String getEcode() {
            return ecode;
        }

        public void setEcode(String ecode) {
            this.ecode = ecode;
        }

        public String getEname() {
            return ename;
        }

        public void setEname(String ename) {
            this.ename = ename;
        }

        public static OrderType format(Integer ecode) {
            if (ecode == null) {
                return OrderType.MALL;
            }
            for (OrderType is : OrderType.values()) {
                if (is.ecode.equals(ecode)) {
                    return is;
                }
            }
            return OrderType.MALL;
        }
    }

}
