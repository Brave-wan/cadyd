package com.cadyd.app.model;

import java.util.List;

/**
 * @Description: ${todo}(这里用一句话描述这个方法的作用)}$
 * 作者：Jerry
 * 邮箱：185214487@qq.com
 */
public class ProductsFeaturedInfo {


    /**
     * data : [{"cashPrice":669,"productId":"011de37482df4eefa7cb292e72478bf6","productImageUrl":"","productName":"阿迪达斯NEO女鞋夏季板鞋新品休闲鞋AQ1514"},{"cashPrice":399,"productId":"00a9a318d0a341eeb9578c2e85711488","productImageUrl":"","productName":"Kappa男运动休闲鞋卫裤宽松款收口小脚裤"},{"cashPrice":329,"productId":"0096991d1890473490da72e11a4834d3","productImageUrl":"","productName":"Adidas2016新款男子运动基础系列套头衫"},{"cashPrice":899,"productId":"0084e83854544e57ab22cbf1f9d715aa","productImageUrl":"","productName":"adidas女鞋高帮UP三叶草内增高板鞋S74995"},{"cashPrice":399,"productId":"0043da9616474d4b87117877879d3ce6","productImageUrl":"http://schimages.img-cn-hangzhou.aliyuncs.com/images/mall_goods/2016/08/25/1472091647198.jpg@!100-100.jpg","productName":"AdidasNEO男显瘦休闲裤收口小脚长裤AJ8290 AK1100"},{"cashPrice":449,"productId":"002a3683361d4d109579854cb325be2d","productImageUrl":"http://schimages.img-cn-hangzhou.aliyuncs.com/images/mall_goods/2016/08/24/1472004921042.png@!100-100.jpg","productName":"PUMA彪马男运动卫衣2PU57032701  57032703"},{"cashPrice":299,"productId":"0004dfadda314308bb4c97b9dfd49821","productImageUrl":"","productName":"Kappa男短袖图案衫T恤衫16春夏新品K0612TD30-882"}]
     * pageIndex : 1
     * pageSize : 10
     * rowBounds : {"limit":10,"offset":0}
     * totalCount : 0
     */


    /**
     * cashPrice : 669.0
     * productId : 011de37482df4eefa7cb292e72478bf6
     * productImageUrl :
     * productName : 阿迪达斯NEO女鞋夏季板鞋新品休闲鞋AQ1514
     */

    private List<ProductsFeatBean> data;

    public List<ProductsFeatBean> getData() {
        return data;
    }

    public void setData(List<ProductsFeatBean> data) {
        this.data = data;
    }

    public static class ProductsFeatBean {
        private double cashPrice;
        private String productId;
        private String productImageUrl;
        private String productName;

        public double getCashPrice() {
            return cashPrice;
        }

        public void setCashPrice(double cashPrice) {
            this.cashPrice = cashPrice;
        }

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public String getProductImageUrl() {
            return productImageUrl;
        }

        public void setProductImageUrl(String productImageUrl) {
            this.productImageUrl = productImageUrl;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }
    }

}
