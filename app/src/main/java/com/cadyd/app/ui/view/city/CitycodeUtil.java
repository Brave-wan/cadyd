package com.cadyd.app.ui.view.city;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.cadyd.app.model.AreaInfo;
import com.cadyd.app.model.CityInfo;
import com.cadyd.app.model.ProvinceInfo;
import org.wcy.common.utils.StringUtil;

/**
 * 城市代码
 *
 * @author zd
 */
public class CitycodeUtil {

    private ArrayList<String> province_list = new ArrayList<String>();
    private ArrayList<String> city_list = new ArrayList<String>();
    private ArrayList<String> couny_list = new ArrayList<String>();
    public ArrayList<String> province_list_code = new ArrayList<String>();
    public ArrayList<String> city_list_code = new ArrayList<String>();
    public ArrayList<String> couny_list_code = new ArrayList<String>();
    /**
     * 单例
     */
    public static CitycodeUtil model;

    private CitycodeUtil() {
    }

    public ArrayList<String> getProvince_list_code() {
        return province_list_code;
    }

    public ArrayList<String> getCity_list_code() {
        return city_list_code;
    }

    public void setCity_list_code(ArrayList<String> city_list_code) {
        this.city_list_code = city_list_code;
    }

    public ArrayList<String> getCouny_list_code() {
        return couny_list_code;
    }

    public void setCouny_list_code(ArrayList<String> couny_list_code) {
        this.couny_list_code = couny_list_code;
    }

    public void setProvince_list_code(ArrayList<String> province_list_code) {

        this.province_list_code = province_list_code;
    }

    /**
     * 获取单例
     *
     * @return
     */
    public static CitycodeUtil getSingleton() {
        if (null == model) {
            model = new CitycodeUtil();
        }
        return model;
    }

    public ArrayList<String> getProvince(List<ProvinceInfo> provice, String code) {
        if (province_list_code.size() > 0) {
            province_list_code.clear();
        }
        if (province_list.size() > 0) {
            province_list.clear();
        }
        for (int i = 0; i < provice.size(); i++) {
            ProvinceInfo ci = provice.get(i);
            if (StringUtil.hasText(code) && code.equals(ci.id)) {
                provinceIndex = i;
            }
            province_list.add(ci.name);
            province_list_code.add(ci.id);
        }
        if (!StringUtil.hasText(code)) {
            provinceIndex = 0;
        }
        return province_list;

    }

    public int provinceIndex = 0;
    public int cityIndex = 0;
    public int counyIndex = 0;

    public ArrayList<String> getCity(HashMap<String, List<AreaInfo>> cityHashMap, String provicecode, String code) {
        if (city_list_code.size() > 0) {
            city_list_code.clear();
        }
        if (city_list.size() > 0) {
            city_list.clear();
        }
        List<AreaInfo> city = new ArrayList<AreaInfo>();
        city = cityHashMap.get(provicecode);
        for (int i = 0; i < city.size(); i++) {
            AreaInfo co = city.get(i);
            if (StringUtil.hasText(code) && code.equals(co.id)) {
                cityIndex = i;
            }
            city_list.add(co.name);
            city_list_code.add(co.id);
        }
        if (!StringUtil.hasText(code)) {
            cityIndex = 0;
        }
        return city_list;

    }

    public ArrayList<String> getCouny(HashMap<String, List<CityInfo>> cityHashMap, String citycode, String code) {
        List<CityInfo> couny = null;
        if (couny_list_code.size() > 0) {
            couny_list_code.clear();

        }
        if (couny_list.size() > 0) {
            couny_list.clear();
        }
        if (couny == null) {
            couny = new ArrayList<CityInfo>();
        }

        couny = cityHashMap.get(citycode);
        for (int i = 0; i < couny.size(); i++) {
            CityInfo co = couny.get(i);
            if (StringUtil.hasText(code) && code.equals(co.id)) {
                counyIndex = i;
            }
            couny_list.add(co.name);
            couny_list_code.add(co.id);
        }
        if (!StringUtil.hasText(code)) {
            counyIndex = 0;
        }
        return couny_list;

    }
}
