package com.cadyd.app.model;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Administrator on 2016/5/25.
 */
public class Key {

    public String start;
    public String end;

    /**
     * 全球购分类
     * <p>
     * "menuid": "76aa127d24ad41fa9edc089b236da2a5",
     * "name": "母婴用品",
     * "imgurl": "http://www.cadyd.com/images\n/advertisement/1453375826853.jpg"
     **/

    public String menuid;//全球购id
    public String id;//一乡一物id
    public String name;
    public String imgurl;


    /**
     * 积分商城分类
     * "icon": "http://114.55.58.18:8087/sch/images/menu/20160323/1458702741462.png",
     * "id": "1167fdd158eb41f3a5df927b5816a504",
     * "isnext": 1,
     * "name": "个人美妆",
     * "parentid": "0",
     * "seq": 1,
     * "type": "004"
     **/

    public String icon;

    public static Type getType() {
        Type type = new TypeToken<List<Key>>() {
        }.getType();
        return type;
    }
}
