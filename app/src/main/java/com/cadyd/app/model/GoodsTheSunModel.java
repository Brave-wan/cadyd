package com.cadyd.app.model;

import android.graphics.Bitmap;

import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by xiongmao on 2016/6/4.
 */
public class GoodsTheSunModel implements Serializable {
    private static final long serialVersionUID = -8872740313721651196L;

//        "goodsId": "c4c0a9f3a0c34ce3b954c1995aa6b960",
//            "oid": "d2b75525145a442d89a731d103f4c32d",
//            "odid": "5fcbabd655d54f0eac24b0dfc395c2e5",
//            "label": "阿达算法是(6),阿达算法(4),阿达大(4),阿达大d(4)",
//            "clabel": null,
//            "cid": null,
//            "thumb": "http://www.cadyd.com/images/goods/20160111/thumb/1452511840191.jpg",
//            "path": "http://www.cadyd
//            .com/images/goods/20160111/1452511840191.jpg",
//        "count": null,
//            "content": null,
//            "startLevel": null,
//            "picList": [
//
//        ]

    public String goodsId;
    public String oid;
    public String odid;
    public String label; //标签
    public String clabel;
    public String cid;
    public String thumb = ""; // 商品图片
    public String path;
    public String count;
    public String content;
    public String startLevel;
    public String goodsName;
    public int commentstate = -1;//是否已经评论 0未评论 1已评论

    /**
     * 修改时变化参数
     **/
    public int isprimary;
    public int number;//商品数量
    public String title;//商品名字
    public String tranflowno;
    public String trantime;

    public int service;
    public int Logistics;

    /**
     * 用来保存本地的信息
     */
    public int local_level = 0;
    public String local_content = "";

    public List<Map<String, ImageModel>> bitmapList = new ArrayList<>();

    public static Type getType() {
        Type type = new TypeToken<List<GoodsTheSunModel>>() {
        }.getType();
        return type;
    }

}
