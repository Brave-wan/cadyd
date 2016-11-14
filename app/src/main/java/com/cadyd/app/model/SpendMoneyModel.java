package com.cadyd.app.model;

import java.util.List;

/**
 * Created by xiongmao on 2016/11/1.
 */

public class SpendMoneyModel {

    /**
     * cashAmount : 145
     * liveAmountMoneyInfoList : [{"amountMoney":"10","petalAmount":"10"},{"amountMoney":"100","petalAmount":"100"},{"amountMoney":"200","petalAmount":"200"},{"amountMoney":"300","petalAmount":"300"},{"amountMoney":"500","petalAmount":"500"},{"amountMoney":"1000","petalAmount":"1000"}]
     * rechargeProportion : 1
     */

    private float cashAmount;//花币余额
    private int rechargeProportion;//花币比例
    /**
     * amountMoney : 10
     * petalAmount : 10
     */

    private List<LiveAmountMoneyInfoListBean> liveAmountMoneyInfoList;

    public float getCashAmount() {
        return cashAmount;
    }

    public void setCashAmount(int cashAmount) {
        this.cashAmount = cashAmount;
    }

    public int getRechargeProportion() {
        return rechargeProportion;
    }

    public void setRechargeProportion(int rechargeProportion) {
        this.rechargeProportion = rechargeProportion;
    }

    public List<LiveAmountMoneyInfoListBean> getLiveAmountMoneyInfoList() {
        return liveAmountMoneyInfoList;
    }

    public void setLiveAmountMoneyInfoList(List<LiveAmountMoneyInfoListBean> liveAmountMoneyInfoList) {
        this.liveAmountMoneyInfoList = liveAmountMoneyInfoList;
    }

    public static class LiveAmountMoneyInfoListBean {
        private String amountMoney;
        private String petalAmount;
        private String productId;

        public String getAmountMoney() {
            return amountMoney;
        }

        public void setAmountMoney(String amountMoney) {
            this.amountMoney = amountMoney;
        }

        public String getPetalAmount() {
            return petalAmount;
        }

        public void setPetalAmount(String petalAmount) {
            this.petalAmount = petalAmount;
        }

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }
    }

    @Override
    public String toString() {
        return "SpendMoneyModel{" +
                "cashAmount=" + cashAmount +
                ", rechargeProportion=" + rechargeProportion +
                ", liveAmountMoneyInfoList=" + liveAmountMoneyInfoList +
                '}';
    }
}
