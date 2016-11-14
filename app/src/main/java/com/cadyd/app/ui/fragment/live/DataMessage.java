package com.cadyd.app.ui.fragment.live;

import java.io.Serializable;

/**
 * @author wub
 * @version 创建时间：2016年10月15日 下午6:04:53
 */
public class DataMessage implements Serializable {

    private static final long serialVersionUID = 114122932198945348L;
    private String headUrl;// 时时头像
    private Integer totalCount;// 观看人数
    private Integer concernCount;// 关注人数
    private String chatMsg;// 聊天内容
    private String giftMsg;// 礼物信息
    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getConcernCount() {
        return concernCount;
    }

    public void setConcernCount(Integer concernCount) {
        this.concernCount = concernCount;
    }

    public String getChatMsg() {
        return chatMsg;
    }

    public void setChatMsg(String chatMsg) {
        this.chatMsg = chatMsg;
    }

    public String getGiftMsg() {
        return giftMsg;
    }

    public void setGiftMsg(String giftMsg) {
        this.giftMsg = giftMsg;
    }

}
