package com.cadyd.app.model;

import java.math.BigDecimal;

/**
 * @Description: ${todo}(这里用一句话描述这个方法的作用)}$
 * 作者：Jerry
 * 邮箱：185214487@qq.com
 */
public class BuyWatchingInfo {
    /**
     * 商品编号
     */
    private String productId;
    /**
     * 现金价格
     */
    private BigDecimal decimal;
    /**
     * 商品标题
     */
    private String productName;
    /**
     * 商品图片地址
     */
    private String productImageUrl;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public BigDecimal getDecimal() {
        return decimal;
    }

    public void setDecimal(BigDecimal decimal) {
        this.decimal = decimal;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductImageUrl() {
        return productImageUrl;
    }

    public void setProductImageUrl(String productImageUrl) {
        this.productImageUrl = productImageUrl;
    }
}
