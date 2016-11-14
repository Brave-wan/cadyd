package com.cadyd.app.model;

import com.google.gson.reflect.TypeToken;
import org.wcy.common.utils.StringUtil;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by wcy on 2016/5/24.
 */
public class AddressModel implements Serializable {
    private static final long serialVersionUID = 8023931932337286577L;
    public String memberid;
    public String consignee = "";//收货人
    public String zipcode;
    public String city;
    public String telphone;
    public String id;
    public String addtime;
    public String county;
    public String countyname;
    public String country;
    public String address;//地址
    public String name;//
    public String province;///省
    public String provincename;
    public String cityname;
    public String defaultaddress;


    public static Type getType() {
        Type type = new TypeToken<List<AddressModel>>() {
        }.getType();
        return type;
    }

    public String getAddress() {
        StringBuffer sb = new StringBuffer();
        sb.append(StringUtil.hasText(province) ? province : "");
        sb.append(StringUtil.hasText(city) ? city : "");
        sb.append(StringUtil.hasText(country) ? country : "");
        sb.append(StringUtil.hasText(address) ? address : "");
        return sb.toString();
    }
}
