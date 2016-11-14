package com.cadyd.app.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/5/10.
 */
public class OneGoodsModel implements Serializable {

    private static final long serialVersionUID = -8024608683940889803L;
    public int number = 1;
    public String id;
    public String title;
    public String hastimes; //期数
    public String price;
    public String participatetimes = "0";
    public String hasparticipatetimes = "0";
    public String tbid;
    public String average;
    public String mainImg;

    public String name ;
    public String thumb="";
    public String original;
}
