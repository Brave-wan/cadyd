package com.cadyd.app.ui.view.city;

import com.cadyd.app.model.AreaInfo;
import com.cadyd.app.model.CityInfo;
import com.cadyd.app.model.ProvinceInfo;

import java.util.HashMap;
import java.util.List;


public class CityList {
    public List<ProvinceInfo> province_list;
    public HashMap<String, List<AreaInfo>> city_map;
    public HashMap<String, List<CityInfo>> couny_map;

    public List<ProvinceInfo> filtrate_province_list;
    public HashMap<String, List<AreaInfo>> filtrate_city_map;
    public HashMap<String, List<CityInfo>> filtrate_couny_map;

}

