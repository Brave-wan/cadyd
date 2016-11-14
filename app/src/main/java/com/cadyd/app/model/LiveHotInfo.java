package com.cadyd.app.model;

import java.util.List;

/**
 * Created by rance on 2016/10/11.
 * 直播首页热门列表
 */
public class LiveHotInfo {

    public class DataBean{
        /**
         * 房间会话号
         */
        private String conversationId;

        /**
         * 房主头像
         */
        private String headImageUrl;

        /**
         * 房间名
         */
        private String conversationTitle;

        /**
         * 在线人数
         */
        private int onlineUserCount;

        /**
         * 直播地点
         */
        private String city;

        /**
         * 关注状态（1=已关注，2=未关注，3=不支持）
         */
        private int focusStatus;

        /**
         * 直播类型（1=个人，2=商家）
         */
        private int liveType;

        /**
         * 封面图片地址
         */
        private String conversationUrl;

        /**
         * 用户id
         */
        private String userId;

        public String getConversationId() {
            return conversationId;
        }

        public void setConversationId(String conversationId) {
            this.conversationId = conversationId;
        }

        public String getHeadImageUrl() {
            return headImageUrl;
        }

        public void setHeadImageUrl(String headImageUrl) {
            this.headImageUrl = headImageUrl;
        }

        public String getConversationTitle() {
            return conversationTitle;
        }

        public void setConversationTitle(String conversationTitle) {
            this.conversationTitle = conversationTitle;
        }

        public int getOnlineUserCount() {
            return onlineUserCount;
        }

        public void setOnlineUserCount(int onlineUserCount) {
            this.onlineUserCount = onlineUserCount;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public int getFocusStatus() {
            return focusStatus;
        }

        public void setFocusStatus(int focusStatus) {
            this.focusStatus = focusStatus;
        }

        public int getLiveType() {
            return liveType;
        }

        public void setLiveType(int liveType) {
            this.liveType = liveType;
        }

        public String getConversationUrl() {
            return conversationUrl;
        }

        public void setConversationUrl(String conversationUrl) {
            this.conversationUrl = conversationUrl;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        @Override
        public String toString() {
            return "LiveHotInfo{" +
                    "conversationId='" + conversationId + '\'' +
                    ", headImageUrl='" + headImageUrl + '\'' +
                    ", conversationTitle='" + conversationTitle + '\'' +
                    ", onlineUserCount=" + onlineUserCount +
                    ", city='" + city + '\'' +
                    ", focusStatus=" + focusStatus +
                    ", liveType=" + liveType +
                    ", conversationUrl='" + conversationUrl + '\'' +
                    '}';
        }

    }
    /**
     * data : []
     * pageIndex : 0
     * pageSize : 10
     * rowBounds : {"limit":10,"offset":-10}
     * totalCount : 0
     */

    private int pageIndex;
    private int pageSize;
    /**
     * limit : 10
     * offset : -10
     */

    private RowBoundsBean rowBounds;
    private int totalCount;
    private List<DataBean> data;

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public RowBoundsBean getRowBounds() {
        return rowBounds;
    }

    public void setRowBounds(RowBoundsBean rowBounds) {
        this.rowBounds = rowBounds;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class RowBoundsBean {
        private int limit;
        private int offset;

        public int getLimit() {
            return limit;
        }

        public void setLimit(int limit) {
            this.limit = limit;
        }

        public int getOffset() {
            return offset;
        }

        public void setOffset(int offset) {
            this.offset = offset;
        }
    }
}
