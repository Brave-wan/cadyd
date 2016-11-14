package com.cadyd.app.model;

/**
 * Created by root on 16-9-2.
 */

public class LiveSendMsgInfo {


    /**
     * sayText : Test-----
     */

    private DataBean data;
    /**
     * operation : 16777218
     * security : 894e3d7f520b461dabd9cad9a651a86f
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
        private String sayText;

        public String getSayText() {
            return sayText;
        }

        public void setSayText(String sayText) {
            this.sayText = sayText;
        }
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
