package com.cadyd.app.model;

import java.util.List;

/**
 * Created by Administrator on 2016/5/10.
 */
public class QueryBaseRecord {
    public List<QueryBaseRecordData> data;
    public int pageIndex;//
    public int totalPage;
    public int totalCount;
    public int pageSize;
    public RowBounds rowBounds;
}
