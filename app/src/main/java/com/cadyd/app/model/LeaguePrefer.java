package com.cadyd.app.model;

import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

/**
 * 联盟优惠卷
 * Created by wcy on 2016/7/2.
 */
public class LeaguePrefer implements Serializable {

    private static final long serialVersionUID = -2498377227813323162L;
    /**
     * id : fgj5456dsf4f65h456df465df4s65g456df4
     * title : 4.5日狂欢
     * invalid : 2016-04-05至2016-04-06
     * name : 新世纪百货
     * money : 2
     * typeId : 1
     */

    public String id;
    public String title;
    public String invalid;
    public String name;
    public int money;
    public int typeId;

    /**
     * zjh
     */
    public String goodsid;//有值表示这条数据属于商品优惠券
    public int preCondittion;//优惠条件值

    public static Type getType() {
        Type type = new TypeToken<List<LeaguePrefer>>() {
        }.getType();
        return type;
    }
}
