package com.cadyd.app.model;

import java.util.List;

/**
 * Created by xiongmao on 2016/10/14.
 */

public class RecentVisitorsModel {

    /**
     * data : [{"focusStatus":1,"headImageUrl":"http://schimages.img-cn-hangzhou.aliyuncs.com/images/groupbuy_img/2016/10/13/fba9db32-9085-47ae-bee0-380c5fdfd3b2.png@!100-100.jpg","nickName":"183***038","userId":"ec4254f6a2504e8caaadc88f7e80d770"}]
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
     * focusStatus : 1
     * headImageUrl : http://schimages.img-cn-hangzhou.aliyuncs.com/images/groupbuy_img/2016/10/13/fba9db32-9085-47ae-bee0-380c5fdfd3b2.png@!100-100.jpg
     * nickName : 183***038
     * userId : ec4254f6a2504e8caaadc88f7e80d770
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
        private int focusStatus;
        private String headImageUrl;
        private String nickName;
        private String userId;

        public int getFocusStatus() {
            return focusStatus;
        }

        public void setFocusStatus(int focusStatus) {
            this.focusStatus = focusStatus;
        }

        public String getHeadImageUrl() {
            return headImageUrl;
        }

        public void setHeadImageUrl(String headImageUrl) {
            this.headImageUrl = headImageUrl;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }
    }

    @Override
    public String toString() {
        return "RecentVisitorsModel{" +
                "pageIndex=" + pageIndex +
                ", pageSize=" + pageSize +
                ", rowBounds=" + rowBounds +
                ", totalCount=" + totalCount +
                ", data=" + data +
                '}';
    }
}
