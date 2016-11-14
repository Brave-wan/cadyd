package com.cadyd.app.model;

import java.util.List;

/**
 * Created by xiongmao on 2016/10/13.
 */

public class AttentionUserInfoListDataModel {

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
    private List<AttentionUserInfo> data;

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

    public List<AttentionUserInfo> getData() {
        return data;
    }

    public void setData(List<AttentionUserInfo> data) {
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
