package com.cadyd.app.result;

import java.io.Serializable;

public class Page implements Serializable {
    private static final long serialVersionUID = -4340687742085329235L;
    public int pageIndex=1;//
    public int totalPage=1;
    public int totalCount;
    public int pageSize;
}
