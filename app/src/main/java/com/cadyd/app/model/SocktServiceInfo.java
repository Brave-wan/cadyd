package com.cadyd.app.model;


import java.util.UUID;

/**
 * @Description: ${todo}(这里用一句话描述这个方法的作用)}$
 * 作者：Jerry
 * 邮箱：185214487@qq.com
 */
public class SocktServiceInfo {


    /**
     * commandType : CONNECT
     * data : {"message":"同学***:欢迎你","totalCount":8}
     * liveUser : false
     * messageId : a6d2f39e-d986-4662-bc8e-93c6b310c89d
     * state : 100
     * userId : cd0133187bd144a0a741fa26dc1c38ea
     */

    private ResultBean result;
    /**
     * result : {"commandType":"CONNECT","data":{"message":"同学***:欢迎你","totalCount":8},"liveUser":false,"messageId":"a6d2f39e-d986-4662-bc8e-93c6b310c89d","state":100,"userId":"cd0133187bd144a0a741fa26dc1c38ea"}
     * state : 100
     */

    private int state;



    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public static class ResultBean {
        private String commandType;
        /**
         * message : 同学***:欢迎你
         * totalCount : 8
         */

        private DataBean data;
        private boolean liveUser;
        private String messageId;
        private int state;
        private String userId;
        private boolean send;

        public boolean isSend() {
            return send;
        }

        public void setSend(boolean send) {
            this.send = send;
        }

        public String getCommandType() {
            return commandType;
        }

        public void setCommandType(String commandType) {
            this.commandType = commandType;
        }

        public DataBean getData() {
            return data;
        }

        public void setData(DataBean data) {
            this.data = data;
        }

        public boolean isLiveUser() {
            return liveUser;
        }

        public void setLiveUser(boolean liveUser) {
            this.liveUser = liveUser;
        }

        public String getMessageId() {
            return messageId;
        }

        public void setMessageId(String messageId) {
            this.messageId = messageId;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public static class DataBean {

            private String headUrl;
            private String message;
            private UUID giftId;
            private int concernCount;
            private int totalCount;
            private String userId;

            public String getHeadUrl() {
                return headUrl;
            }

            public void setHeadUrl(String headUrl) {
                this.headUrl = headUrl;
            }

            public UUID getGiftId() {
                return giftId;
            }

            public void setGiftId(UUID giftId) {
                this.giftId = giftId;
            }

            public int getConcernCount() {
                return concernCount;
            }

            public void setConcernCount(int concernCount) {
                this.concernCount = concernCount;
            }

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }

            public String getMessage() {
                return message;
            }

            public void setMessage(String message) {
                this.message = message;
            }

            public int getTotalCount() {
                return totalCount;
            }

            public void setTotalCount(int totalCount) {
                this.totalCount = totalCount;
            }
        }
    }
}
