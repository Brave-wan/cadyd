package com.cadyd.app.model;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiongmao on 2016/9/12.
 */

public class LookEvaluateModel {


    /**
     * itme : [{"orderDetail":{"commentstate":1,"goodsid":"db9d398ea001464498b65abadcfa5d77","goodsprice":0.01,"goodstitle":"上课了飞机上考虑","id":"98ddf8b32b1344feb830baddc99ae9f9","luckcodecount":0,"number":1,"orderid":"8d19753cb5fe4670ba2f77dff96826e2","pay":0,"paystate":0,"preferamount":0,"preferential":0,"resource":0,"rid":"","salemix":" ","state":3,"thumb":"http://schimages.img-cn-hangzhou.aliyuncs.com/images/mall_goods/2016/09/05/1473063519960.jpg@!100-100.jpg","total":0.01,"type":0,"userid":"0e0ace7379a44848b98fb96bc1946b9e"},"schFileList":[{"id":"1a3c02fd36bc4d2f83456efce441f43e","refId":"78183fc829f843479c1d2ca9bddc3d73","path":"http://schimages.img-cn-hangzhou.aliyuncs.com/images/groupbuy_img/2016/09/09/80a4df39-483a-415c-b161-5f4c0ff162ac@!400-800.jpg","filename":"ZOWVXEBYAFRCLXMABRVNZUOZYZHGFETN219201617912","thumb":"http://schimages.img-cn-hangzhou.aliyuncs.com/images/groupbuy_img/2016/09/09/80a4df39-483a-415c-b161-5f4c0ff162ac@!100-100.jpg"},{"id":"78aff0775fe7460ea801cf6a28da1754","refId":"78183fc829f843479c1d2ca9bddc3d73","path":"http://schimages.img-cn-hangzhou.aliyuncs.com/images/groupbuy_img/2016/09/09/deb7c127-3f72-450f-902a-df721834a2d7@!400-800.jpg","filename":"XLBMKVUSTEAWXOGYMOQRMAZTVYAOZVWG229201617982","thumb":"http://schimages.img-cn-hangzhou.aliyuncs.com/images/groupbuy_img/2016/09/09/deb7c127-3f72-450f-902a-df721834a2d7@!100-100.jpg"},{"id":"d029bbfd61764e37a2ab233269dfd3f6","refId":"78183fc829f843479c1d2ca9bddc3d73","path":"http://schimages.img-cn-hangzhou.aliyuncs.com/images/groupbuy_img/2016/09/09/5924e07f-08c1-42dc-b6bf-a3e38583b088@!400-800.jpg","filename":"KTLEXAMUVSYNSXYSADBSZCGWINLMILIQ219201617922","thumb":"http://schimages.img-cn-hangzhou.aliyuncs.com/images/groupbuy_img/2016/09/09/5924e07f-08c1-42dc-b6bf-a3e38583b088@!100-100.jpg"}],"commentGoods":{"content":"您老","count":1,"created":"2016-09-09 17:22:20","goodsid":"db9d398ea001464498b65abadcfa5d77","id":"78183fc829f843479c1d2ca9bddc3d73","logisticsLevel":3,"odid":"98ddf8b32b1344feb830baddc99ae9f9","serviceLevel":3,"startlevel":3,"state":1,"userid":"0e0ace7379a44848b98fb96bc1946b9e"}}]
     * logisticsLevel : 3
     * serviceLevel : 3
     */

    public int logisticsLevel;
    public int serviceLevel;
    /**
     * orderDetail : {"commentstate":1,"goodsid":"db9d398ea001464498b65abadcfa5d77","goodsprice":0.01,"goodstitle":"上课了飞机上考虑","id":"98ddf8b32b1344feb830baddc99ae9f9","luckcodecount":0,"number":1,"orderid":"8d19753cb5fe4670ba2f77dff96826e2","pay":0,"paystate":0,"preferamount":0,"preferential":0,"resource":0,"rid":"","salemix":" ","state":3,"thumb":"http://schimages.img-cn-hangzhou.aliyuncs.com/images/mall_goods/2016/09/05/1473063519960.jpg@!100-100.jpg","total":0.01,"type":0,"userid":"0e0ace7379a44848b98fb96bc1946b9e"}
     * schFileList : [{"id":"1a3c02fd36bc4d2f83456efce441f43e","refId":"78183fc829f843479c1d2ca9bddc3d73","path":"http://schimages.img-cn-hangzhou.aliyuncs.com/images/groupbuy_img/2016/09/09/80a4df39-483a-415c-b161-5f4c0ff162ac@!400-800.jpg","filename":"ZOWVXEBYAFRCLXMABRVNZUOZYZHGFETN219201617912","thumb":"http://schimages.img-cn-hangzhou.aliyuncs.com/images/groupbuy_img/2016/09/09/80a4df39-483a-415c-b161-5f4c0ff162ac@!100-100.jpg"},{"id":"78aff0775fe7460ea801cf6a28da1754","refId":"78183fc829f843479c1d2ca9bddc3d73","path":"http://schimages.img-cn-hangzhou.aliyuncs.com/images/groupbuy_img/2016/09/09/deb7c127-3f72-450f-902a-df721834a2d7@!400-800.jpg","filename":"XLBMKVUSTEAWXOGYMOQRMAZTVYAOZVWG229201617982","thumb":"http://schimages.img-cn-hangzhou.aliyuncs.com/images/groupbuy_img/2016/09/09/deb7c127-3f72-450f-902a-df721834a2d7@!100-100.jpg"},{"id":"d029bbfd61764e37a2ab233269dfd3f6","refId":"78183fc829f843479c1d2ca9bddc3d73","path":"http://schimages.img-cn-hangzhou.aliyuncs.com/images/groupbuy_img/2016/09/09/5924e07f-08c1-42dc-b6bf-a3e38583b088@!400-800.jpg","filename":"KTLEXAMUVSYNSXYSADBSZCGWINLMILIQ219201617922","thumb":"http://schimages.img-cn-hangzhou.aliyuncs.com/images/groupbuy_img/2016/09/09/5924e07f-08c1-42dc-b6bf-a3e38583b088@!100-100.jpg"}]
     * commentGoods : {"content":"您老","count":1,"created":"2016-09-09 17:22:20","goodsid":"db9d398ea001464498b65abadcfa5d77","id":"78183fc829f843479c1d2ca9bddc3d73","logisticsLevel":3,"odid":"98ddf8b32b1344feb830baddc99ae9f9","serviceLevel":3,"startlevel":3,"state":1,"userid":"0e0ace7379a44848b98fb96bc1946b9e"}
     */

    public List<ItmeBean> itme = new ArrayList<>();


    public static class ItmeBean {
        /**
         * commentstate : 1
         * goodsid : db9d398ea001464498b65abadcfa5d77
         * goodsprice : 0.01
         * goodstitle : 上课了飞机上考虑
         * id : 98ddf8b32b1344feb830baddc99ae9f9
         * luckcodecount : 0
         * number : 1
         * orderid : 8d19753cb5fe4670ba2f77dff96826e2
         * pay : 0
         * paystate : 0
         * preferamount : 0
         * preferential : 0
         * resource : 0
         * rid :
         * salemix :
         * state : 3
         * thumb : http://schimages.img-cn-hangzhou.aliyuncs.com/images/mall_goods/2016/09/05/1473063519960.jpg@!100-100.jpg
         * total : 0.01
         * type : 0
         * userid : 0e0ace7379a44848b98fb96bc1946b9e
         */

        public OrderDetailBean orderDetail;
        /**
         * content : 您老
         * count : 1
         * created : 2016-09-09 17:22:20
         * goodsid : db9d398ea001464498b65abadcfa5d77
         * id : 78183fc829f843479c1d2ca9bddc3d73
         * logisticsLevel : 3
         * odid : 98ddf8b32b1344feb830baddc99ae9f9
         * serviceLevel : 3
         * startlevel : 3
         * state : 1
         * userid : 0e0ace7379a44848b98fb96bc1946b9e
         */

        public CommentGoodsBean commentGoods;
        /**
         * id : 1a3c02fd36bc4d2f83456efce441f43e
         * refId : 78183fc829f843479c1d2ca9bddc3d73
         * path : http://schimages.img-cn-hangzhou.aliyuncs.com/images/groupbuy_img/2016/09/09/80a4df39-483a-415c-b161-5f4c0ff162ac@!400-800.jpg
         * filename : ZOWVXEBYAFRCLXMABRVNZUOZYZHGFETN219201617912
         * thumb : http://schimages.img-cn-hangzhou.aliyuncs.com/images/groupbuy_img/2016/09/09/80a4df39-483a-415c-b161-5f4c0ff162ac@!100-100.jpg
         */

        public List<SchFileListBean> schFileList;


        public static class OrderDetailBean {
            public int commentstate;
            public String goodsid;
            public double goodsprice;
            public String goodstitle;
            public String id;
            public int luckcodecount;
            public int number;
            public String orderid;
            public int pay;
            public int paystate;
            public int preferamount;
            public int preferential;
            public int resource;
            public String rid;
            public String salemix;
            public int state;
            public String thumb;
            public double total;
            public int type;
            public String userid;


        }

        public static class CommentGoodsBean {
            public String content;
            public int count;
            public String created;
            public String goodsid;
            public String id;
            public int logisticsLevel;
            public String odid;
            public int serviceLevel;
            public int startlevel;
            public int state;
            public String userid;


        }

        public static class SchFileListBean {
            public String id;
            public String refId;
            public String path;
            public String filename;
            public String thumb;


        }
    }
}
