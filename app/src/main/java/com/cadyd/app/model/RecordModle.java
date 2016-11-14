package com.cadyd.app.model;

import java.util.List;

/**
 * Created by Administrator on 2016/5/18.
 */
public class RecordModle {
    public int pageIndex;//
    public int totalPage;
    public int totalCount;
    public int pageSize;
    public List<RecordModleData> data;
    public RowBounds rowBounds;
}
