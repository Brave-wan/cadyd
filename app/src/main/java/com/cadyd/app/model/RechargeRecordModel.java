package com.cadyd.app.model;

import java.util.List;

/**
 * 花币充值记录
 * Created by xiongmao on 2016/11/2.
 */

public class RechargeRecordModel {


    /**
     * data : [{"amount":"1","count":1,"createTime":1476089426000}]
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
     * amount : 1
     * count : 1
     * createTime : 1476089426000
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
        private String amount; //总金额
        private int count; //花币数
        private long createTime;//时间

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }
    }
}
