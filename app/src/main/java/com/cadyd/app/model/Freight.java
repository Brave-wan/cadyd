package com.cadyd.app.model;

import java.io.Serializable;

/**
 * 运费频率
 * Created by wcy on 2016/5/12.
 */

public class Freight implements Serializable {
    private static final long serialVersionUID = 4748833630245544263L;
    public String id;
    public String name;// "平邮", //运费名称
    public String isdef;//每次能用的张数
    public String frequency;//续件
    public String extra;// 续件金额
    public String range;//首件
    public String rmoney;//首件的金额
}
