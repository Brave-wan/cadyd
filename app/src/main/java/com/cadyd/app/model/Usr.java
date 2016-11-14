package com.cadyd.app.model;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * 取消原因
 * Created by wcy on 2016/6/6.
 */
public class Usr {
    public String val;
    public String name;
    public static Type getType() {
        Type type = new TypeToken<List<Usr>>() {
        }.getType();
        return type;
    }
}
