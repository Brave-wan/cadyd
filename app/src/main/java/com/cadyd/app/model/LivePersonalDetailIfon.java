package com.cadyd.app.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: ${todo}(这里用一句话描述这个方法的作用)}$
 * 作者：Jerry
 * 邮箱：185214487@qq.com
 */
public class LivePersonalDetailIfon implements Serializable {


    /**
     * 聊天服务器参数
     */
    private ChatParameterBean chatParameter;
    /**
     * 关注状态（1=已关注，2=未关注，3=不支持）
     */
    private int focusStatus;
    /**
     * 关注人数
     */
    private int foucsCount;
    /**
     * 头像
     */
    private String headImageUrl;
    /**
     * 在线人数
     */
    private int onlineCount;

    private String merchantId;
    /**
     * 七牛云参数
     */
    private QiniuParameterBean qiniuParameter;
    /**
     * 房间号
     */
    private String roomCode;
    /**
     * 用户编号
     */
    private String userId;
    /**
     * 在线列表
     */
    private List<OnlineListBean> onlineList;

    private List<advertListBean> advertList;

    /**
     * flyUrl : http://pili-live-hdl.activity.cadyd.com/schdyd-live/d04fa504-926e-4062-91f2-23ae4d80f061.flv
     * rtmpUrl : rtmp://pili-live-rtmp.activity.cadyd.com/schdyd-live/d04fa504-926e-4062-91f2-23ae4d80f061
     */

    private ArrayList<String> notice;
    private String conversationId;


    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
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

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
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
        private int advertType;
        /**
         * 1=网页 2=商家店铺
         */
        private int operateType;
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

        public int getAdvertType() {
            return advertType;
        }

        public void setAdvertType(int advertType) {
            this.advertType = advertType;
        }

        public int getOperateType() {
            return operateType;
        }

        public void setOperateType(int operateType) {
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

    public List<OnlineListBean> getOnlineList() {
        return onlineList;
    }

    public void setOnlineList(List<OnlineListBean> onlineList) {
        this.onlineList = onlineList;
    }

    public static class ChatParameterBean implements Serializable {
        /**
         * 通道
         */
        private String channelId;
        /**
         * 安卓苹果专用服务ID
         */
        private String serverId;
        /**
         * H5用这个服务ID
         */
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

    public static class OnlineListBean implements Serializable {
        /**
         * 用户Id
         */
        private String userId;
        /**
         * 头像地址
         */
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

    public static class QiniuParameterBean implements Serializable {
        /**
         * H5用这个地址
         */
        private String flyUrl;
        /**
         * 安卓苹果用这个地址
         */
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
