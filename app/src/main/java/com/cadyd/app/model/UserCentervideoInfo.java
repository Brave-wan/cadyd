package com.cadyd.app.model;

import java.util.List;

/**
 * @Description: ${todo}(这里用一句话描述这个方法的作用)}$
 * 作者：Jerry
 * 邮箱：185214487@qq.com
 */
public class UserCentervideoInfo {

    /**
     * data : [{"conversationId":"375a4dd99298419f86876668ca4b5365","coverUrl":"http://schimages.img-cn-hangzhou.aliyuncs.com/images/groupbuy_img/2016/10/12/7c9857f9-82ed-4288-934d-0735caf54a1c.jpg@!400-800.jpg","flvUrl":"http://http://pili-live-snapshot.activity.cadyd.com//schdyd-live/375a4dd99298419f86876668ca4b5365.flv","hlsUrl":"http://http://pili-live-snapshot.activity.cadyd.com//schdyd-live/375a4dd99298419f86876668ca4b5365.m3u8","liveStatus":2,"liveType":1,"rtmpUrl":"rtmp://http://pili-live-snapshot.activity.cadyd.com//schdyd-live/375a4dd99298419f86876668ca4b5365","sationUrl":"rtmp://http://pili-live-snapshot.activity.cadyd.com//schdyd-live/375a4dd99298419f86876668ca4b5365","title":"去121"}]
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
    /**
     * conversationId : 375a4dd99298419f86876668ca4b5365
     * coverUrl : http://schimages.img-cn-hangzhou.aliyuncs.com/images/groupbuy_img/2016/10/12/7c9857f9-82ed-4288-934d-0735caf54a1c.jpg@!400-800.jpg
     * flvUrl : http://http://pili-live-snapshot.activity.cadyd.com//schdyd-live/375a4dd99298419f86876668ca4b5365.flv
     * hlsUrl : http://http://pili-live-snapshot.activity.cadyd.com//schdyd-live/375a4dd99298419f86876668ca4b5365.m3u8
     * liveStatus : 2
     * liveType : 1
     * rtmpUrl : rtmp://http://pili-live-snapshot.activity.cadyd.com//schdyd-live/375a4dd99298419f86876668ca4b5365
     * sationUrl : rtmp://http://pili-live-snapshot.activity.cadyd.com//schdyd-live/375a4dd99298419f86876668ca4b5365
     * title : 去121
     */

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

    public static class DataBean {
        private String conversationId; //房间ID
        private String coverUrl;
        private String flvUrl; //网页播放地址
        private String hlsUrl; //hls播放地址
        private int liveStatus; //播放状态（1=直播中，2=回看）
        private int liveType; //直播类型（1=个人，2=商家）
        private String rtmpUrl;
        private String sationUrl;
        private String title;

        public String getConversationId() {
            return conversationId;
        }

        public void setConversationId(String conversationId) {
            this.conversationId = conversationId;
        }

        public String getCoverUrl() {
            return coverUrl;
        }

        public void setCoverUrl(String coverUrl) {
            this.coverUrl = coverUrl;
        }

        public String getFlvUrl() {
            return flvUrl;
        }

        public void setFlvUrl(String flvUrl) {
            this.flvUrl = flvUrl;
        }

        public String getHlsUrl() {
            return hlsUrl;
        }

        public void setHlsUrl(String hlsUrl) {
            this.hlsUrl = hlsUrl;
        }

        public int getLiveStatus() {
            return liveStatus;
        }

        public void setLiveStatus(int liveStatus) {
            this.liveStatus = liveStatus;
        }

        public int getLiveType() {
            return liveType;
        }

        public void setLiveType(int liveType) {
            this.liveType = liveType;
        }

        public String getRtmpUrl() {
            return rtmpUrl;
        }

        public void setRtmpUrl(String rtmpUrl) {
            this.rtmpUrl = rtmpUrl;
        }

        public String getSationUrl() {
            return sationUrl;
        }

        public void setSationUrl(String sationUrl) {
            this.sationUrl = sationUrl;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
