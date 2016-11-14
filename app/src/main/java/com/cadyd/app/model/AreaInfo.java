package com.cadyd.app.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 市
 *
 * @author wcy
 */
public class AreaInfo implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 5528459112910064215L;
    public String id = "";// ID
    public String name = "";// 名称
    public String provincenid = "";
    public String nameSort = "";
    public List<CityInfo> city = new ArrayList<>();
}
