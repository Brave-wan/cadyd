package com.cadyd.app.model;

import com.cadyd.app.result.Page;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的订单分页
 * Created by wcy on 2016/6/2.
 */
public class MyOrderPage extends Page {
  public    List<MyOrder> data = new ArrayList<>();
}
