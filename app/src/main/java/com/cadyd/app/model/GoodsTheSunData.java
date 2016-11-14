package com.cadyd.app.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/5/13.
 */
public class GoodsTheSunData implements Serializable {
    private static final long serialVersionUID = -928886716263612499L;
    public String createTime;//时间
    public String des;//内容

    //所有评论
    public String name;//用户名

    public String examinetime;
    public String praiseCount = "0";//点赞次数
    public String nickname;
    public int state;
    public int commentCount;//评论次数
    public String photo;//用户头像
    public String id;
    public String title;
    public List<ImgList> imgList;//图片集合
    public String price;
    public String tbId;
    public String mainimg;
    public String announcedTime;
    public String seq;
    public String productName;

    /**
     * 积分商城的评论
     */

    public String goodsId;//商品id
    public String userid;//用户id
    //stat
    public int number;//
    public int integral;//积分
    //id
    public String content;//内容
    public int startLevel;//星级
    //imageList
    //price;
    public String created;//时间
    public String path;//图片
    public String goodsName;//商品名称


}
