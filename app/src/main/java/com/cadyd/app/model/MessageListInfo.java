package com.cadyd.app.model;

import java.util.List;

/**
 * Created by Administrator on 2016/10/13.
 */
public class MessageListInfo {

    /**
     * data : [{"content":"苍老师现身重庆","handleStatus":2,"messageId":"2756d661-927f-11e6-a258-00163e000da5","messageType":101,"messageUrl":"http://www.baidu.com","operationType":2,"paramsJson":"","readStatus":1,"title":"山茶花直播"}]
     * pageIndex : 1
     * pageSize : 10
     * rowBounds : {"limit":10,"offset":0}
     * totalCount : 1
     * totalPage : 1
     */

    private int pageIndex;
    private int pageSize;
    /**
     * limit : 10
     * offset : 0
     */

    private RowBoundsBean rowBounds;
    private int totalCount;
    private int totalPage;
    /**
     * content : 苍老师现身重庆
     * handleStatus : 2
     * messageId : 2756d661-927f-11e6-a258-00163e000da5
     * messageType : 101
     * messageUrl : http://www.baidu.com
     * operationType : 2
     * paramsJson :
     * readStatus : 1
     * title : 山茶花直播
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

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
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
        private String content;
        private int handleStatus;
        private String messageId;
        private int messageType;
        private String messageUrl;
        private int operationType;
        private String paramsJson;
        private int readStatus;
        private String title;
        private String createTime;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getHandleStatus() {
            return handleStatus;
        }

        public void setHandleStatus(int handleStatus) {
            this.handleStatus = handleStatus;
        }

        public String getMessageId() {
            return messageId;
        }

        public void setMessageId(String messageId) {
            this.messageId = messageId;
        }

        public int getMessageType() {
            return messageType;
        }

        public void setMessageType(int messageType) {
            this.messageType = messageType;
        }

        public String getMessageUrl() {
            return messageUrl;
        }

        public void setMessageUrl(String messageUrl) {
            this.messageUrl = messageUrl;
        }

        public int getOperationType() {
            return operationType;
        }

        public void setOperationType(int operationType) {
            this.operationType = operationType;
        }

        public String getParamsJson() {
            return paramsJson;
        }

        public void setParamsJson(String paramsJson) {
            this.paramsJson = paramsJson;
        }

        public int getReadStatus() {
            return readStatus;
        }

        public void setReadStatus(int readStatus) {
            this.readStatus = readStatus;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }
    }
}
