package com.cadyd.app.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 促销
 * Created by wcy on 2016/6/28.
 */
public class Sales implements Serializable {
    private static final long serialVersionUID = -8062556293707299721L;
    public int number;
    public String money;
    public List<SalesPackage> mallpackagelist = new ArrayList<>();
}
