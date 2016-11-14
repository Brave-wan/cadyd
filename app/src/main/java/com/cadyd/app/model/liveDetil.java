package com.cadyd.app.model;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by xiongmao on 2016/9/8.
 */

public class liveDetil {
    public String id;
    public String url;
    public String state;
    public String explain;
    public String detail;
    public String thumb = "";
    public String sort;//排序

    public static Type getType() {
        Type type = new TypeToken<List<liveDetil>>() {
        }.getType();
        return type;
    }
}
