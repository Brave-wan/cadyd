package com.cadyd.app.model;

import java.math.BigDecimal;

/**
 * 根据商品类型得到数量价格
 * Created by wcy on 2016/5/19.
 */
public class Saledynamic {
    public String id;//
    public BigDecimal price = new BigDecimal(0);//
    public int number;

    public String msg;//提示信息
}
