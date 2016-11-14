package com.cadyd.app.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/10.
 */
public class IntegralListModle {
    public List<IntegralListData> data = new ArrayList<>();
    public int pageIndex;//
    public int totalPage;
    public int totalCount;
    public int pageSize;
    public RowBounds rowBounds;
}
