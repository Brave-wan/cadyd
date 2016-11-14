package com.cadyd.app.model;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by json地区 on 2016/5/3.
 */
public class AreaCode {
    public String name;
    public String code;
    public List<AreaCode> childs;

    public static Type getType() {
        Type type = new TypeToken<List<AreaCode>>() {
        }.getType();
        return type;
    }
}
