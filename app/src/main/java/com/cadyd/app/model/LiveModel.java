package com.cadyd.app.model;

import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by xiongmao on 2016/9/1.
 */

public class LiveModel implements Serializable{
    //            "coverUrl":"http://pili-live-snapshot.activity.cadyd.com/schdyd-live/z1.schdyd-live.58bb59992a0e4babbb5887696c2fd878.jpg",
//            "flvUrl":"http://pili-live-hdl.activity.cadyd.com/schdyd-live/58bb59992a0e4babbb5887696c2fd878.flv",
//            "nickname":"15922866074",
//            "photo":"http://schimages.img-cn-hangzhou.aliyuncs.com/images/groupbuy_img/2016/08/31/60684278-8a6d-459f-8ffd-8ba3d743b1b7.jpg@!100-100.jpg",
//            "rtmpUrl":"rtmp://pili-live-rtmp.activity.cadyd.com/schdyd-live/58bb59992a0e4babbb5887696c2fd878",
//            "snapshotUrl":"http://pili-live-snapshot.activity.cadyd.com/schdyd-live/z1.schdyd-live.58bb59992a0e4babbb5887696c2fd878.jpg",
//            "subject":"好店铺正在直播",
//            "userid":"e09146da0d1248eaa365e40d08270779"
    public String coverUrl;
    public String flvUrl;//播放地址
    public String nickname;//呢称
    public String photo;//头像
    public String rtmpUrl;//播放地址
    public String snapshotUrl;
    public String subject;//标题
    public String userid;//用户ID
    public String shopid;

    public static Type getType() {
        Type type = new TypeToken<List<LiveModel>>() {
        }.getType();
        return type;
    }
}
