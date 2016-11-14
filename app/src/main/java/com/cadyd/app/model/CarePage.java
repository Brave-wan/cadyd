package com.cadyd.app.model;

import com.cadyd.app.result.Page;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wcy on 2016/5/23.
 */
public class CarePage extends Page implements Serializable {
    private static final long serialVersionUID = 7762396357642760206L;
    public List<CartShop> data;
    public Other other;
}
