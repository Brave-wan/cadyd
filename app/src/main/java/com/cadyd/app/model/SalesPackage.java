package com.cadyd.app.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 套装
 * Created by wcy on 2016/6/28.
 */
public class SalesPackage implements Serializable {
    private static final long serialVersionUID = -4814606994036013144L;
    public String tname;//套餐名称
    public String id;//套餐id
    public double price;//套餐价格
    public double lisheng;//立省金额
    public String shiti;//提示店金额
    public String pid;
    public String rid;
    public String tid;
    public double yprice;
    public String goodsid;
    public String thumb;
    public String mallname;
    public List<Good> mallgoods = new ArrayList<>();

    public List<Good> getMallgoods() {
        List<Good> list=new ArrayList<>();
        Good good = new Good();
        good.goodsid = goodsid;
        good.thumb = thumb;
        good.mallname = mallname;
        list.add(good);
        list.addAll(mallgoods);
        return list;
    }
}
