package com.cadyd.app.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: ${todo}(这里用一句话描述这个方法的作用)}$
 * 作者：Jerry
 * 邮箱：185214487@qq.com
 */
public class BusinessLiveDetailInfo implements Serializable {


    /**
     * advertList : []
     * chatParameter : {"channelId":"c7ffc56b-079c-4691-b7bb-cb9aaabf2af2","serverId":"192.168.0.222:8099","wsServiceId":"192.168.0.222:8100"}
     * focusStatus : 2
     * foucsCount : 0
     * headImageUrl : http://schimages.img-cn-hangzhou.aliyuncs.com/images/groupbuy_img/2016/10/14/3f800c09-19ef-463f-930c-9b013aec8001.jpg@!100-100.jpg
     * merchantId : 16dd5092e1144b07b437cf69bf62aca8
     * notice : []
     * onlineCount : 0
     * onlineList : []
     * qiniuParameter : {"flyUrl":"http://pili-live-hdl.activity.cadyd.com/schdyd-live/d04fa504-926e-4062-91f2-23ae4d80f061.flv","rtmpUrl":"rtmp://pili-live-rtmp.activity.cadyd.com/schdyd-live/d04fa504-926e-4062-91f2-23ae4d80f061"}
     * roomCode : 38
     * userId : f3cc7b0ba06240bea63d6733170ee9d9
     */

    /**
     * channelId : c7ffc56b-079c-4691-b7bb-cb9aaabf2af2
     * serverId : 192.168.0.222:8099
     * wsServiceId : 192.168.0.222:8100
     */

    private ChatParameterBean chatParameter;
    private int focusStatus;
    private int foucsCount;
    private String headImageUrl;
    private String merchantId;
    private int onlineCount;
    /**
     * flyUrl : http://pili-live-hdl.activity.cadyd.com/schdyd-live/d04fa504-926e-4062-91f2-23ae4d80f061.flv
     * rtmpUrl : rtmp://pili-live-rtmp.activity.cadyd.com/schdyd-live/d04fa504-926e-4062-91f2-23ae4d80f061
     */

    private QiniuParameterBean qiniuParameter;
    private String roomCode;
    private String userId;
    private List<advertListBean> advertList;
    private ArrayList<String> notice;
    private List<onlineListBean> onlineList;
    private String conversationId;

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public static class onlineListBean implements Serializable {
        private String userId;
        private String headImageUrl;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getHeadImageUrl() {
            return headImageUrl;
        }

        public void setHeadImageUrl(String headImageUrl) {
            this.headImageUrl = headImageUrl;
        }
    }


    public static class advertListBean implements Serializable {
        /**
         * 推荐广告Id
         */
        private String recommendAdvertId;
        /**
         * 广告图片
         */
        private String advertImageUrl;
        /**
         * 系统广告 =1 商家广告 =2
         */
        private String advertType;
        /**
         * 1=网页 2=商家店铺
         */
        private String operateType;
        /**
         * 网址
         */
        private String linkUrl;

        public String getRecommendAdvertId() {
            return recommendAdvertId;
        }

        public void setRecommendAdvertId(String recommendAdvertId) {
            this.recommendAdvertId = recommendAdvertId;
        }

        public String getAdvertImageUrl() {
            return advertImageUrl;
        }

        public void setAdvertImageUrl(String advertImageUrl) {
            this.advertImageUrl = advertImageUrl;
        }

        public String getAdvertType() {
            return advertType;
        }

        public void setAdvertType(String advertType) {
            this.advertType = advertType;
        }

        public String getOperateType() {
            return operateType;
        }

        public void setOperateType(String operateType) {
            this.operateType = operateType;
        }

        public String getLinkUrl() {
            return linkUrl;
        }

        public void setLinkUrl(String linkUrl) {
            this.linkUrl = linkUrl;
        }
    }


    public ChatParameterBean getChatParameter() {
        return chatParameter;
    }

    public void setChatParameter(ChatParameterBean chatParameter) {
        this.chatParameter = chatParameter;
    }

    public int getFocusStatus() {
        return focusStatus;
    }

    public void setFocusStatus(int focusStatus) {
        this.focusStatus = focusStatus;
    }

    public int getFoucsCount() {
        return foucsCount;
    }

    public void setFoucsCount(int foucsCount) {
        this.foucsCount = foucsCount;
    }

    public String getHeadImageUrl() {
        return headImageUrl;
    }

    public void setHeadImageUrl(String headImageUrl) {
        this.headImageUrl = headImageUrl;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public int getOnlineCount() {
        return onlineCount;
    }

    public void setOnlineCount(int onlineCount) {
        this.onlineCount = onlineCount;
    }

    public QiniuParameterBean getQiniuParameter() {
        return qiniuParameter;
    }

    public void setQiniuParameter(QiniuParameterBean qiniuParameter) {
        this.qiniuParameter = qiniuParameter;
    }

    public String getRoomCode() {
        return roomCode;
    }

    public void setRoomCode(String roomCode) {
        this.roomCode = roomCode;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<advertListBean> getAdvertList() {
        return advertList;
    }

    public void setAdvertList(List<advertListBean> advertList) {
        this.advertList = advertList;
    }

    public ArrayList<String> getNotice() {
        return notice;
    }

    public void setNotice(ArrayList<String> notice) {
        this.notice = notice;
    }

    public List<onlineListBean> getOnlineList() {
        return onlineList;
    }

    public void setOnlineList(List<onlineListBean> onlineList) {
        this.onlineList = onlineList;
    }

    public static class ChatParameterBean implements Serializable {
        private String channelId;
        private String serverId;
        private String wsServiceId;

        public String getChannelId() {
            return channelId;
        }

        public void setChannelId(String channelId) {
            this.channelId = channelId;
        }

        public String getServerId() {
            return serverId;
        }

        public void setServerId(String serverId) {
            this.serverId = serverId;
        }

        public String getWsServiceId() {
            return wsServiceId;
        }

        public void setWsServiceId(String wsServiceId) {
            this.wsServiceId = wsServiceId;
        }
    }

    public static class QiniuParameterBean implements Serializable {
        private String flyUrl;
        private String rtmpUrl;

        public String getFlyUrl() {
            return flyUrl;
        }

        public void setFlyUrl(String flyUrl) {
            this.flyUrl = flyUrl;
        }

        public String getRtmpUrl() {
            return rtmpUrl;
        }

        public void setRtmpUrl(String rtmpUrl) {
            this.rtmpUrl = rtmpUrl;
        }
    }
}
