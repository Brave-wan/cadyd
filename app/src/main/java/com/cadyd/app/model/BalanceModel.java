package com.cadyd.app.model;

/**
 * Created by xiongmao on 2016/10/15.
 */

public class BalanceModel {

    /**
     * cashAmount : 0
     */

    private float cashAmount;

    public float getCashAmount() {
        return cashAmount;
    }

    public void setCashAmount(int cashAmount) {
        this.cashAmount = cashAmount;
    }

    @Override
    public String toString() {
        return "BalanceModel{" +
                "cashAmount=" + cashAmount +
                '}';
    }
}
