package com.cadyd.app.model;

/**
 * Created by root on 16-9-3.
 */

public class StoreInfo {

    /**
     * anchorid : ad522a4410404314bfbed331ffbf50e3
     */

    private StoreInfo.DataBean data;
    /**
     * operation : 16777217
     * security : 894e3d7f520b461dabd9cad9a651a86f
     */

    private StoreInfo.HeadBean head;

    public StoreInfo.DataBean getData() {
        return data;
    }

    public void setData(StoreInfo.DataBean data) {
        this.data = data;
    }

    public StoreInfo.HeadBean getHead() {
        return head;
    }

    public void setHead(StoreInfo.HeadBean head) {
        this.head = head;
    }

    public static class DataBean {

    }

    public static class HeadBean {
        private String operation;
        private String security;

        public String getOperation() {
            return operation;
        }

        public void setOperation(String operation) {
            this.operation = operation;
        }

        public String getSecurity() {
            return security;
        }

        public void setSecurity(String security) {
            this.security = security;
        }
    }
}
