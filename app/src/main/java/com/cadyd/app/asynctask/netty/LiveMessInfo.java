package com.cadyd.app.asynctask.netty;

/**
 * Created by root on 16-8-30.
 */
public class LiveMessInfo {


    private DataBean data;
    /**
     * operation : 16777233
     * security : ad522a4410404314bfbed331ffbf50e3
     */

    private HeadBean head;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public HeadBean getHead() {
        return head;
    }

    public void setHead(HeadBean head) {
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
