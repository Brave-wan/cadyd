package com.cadyd.app.model;

/**
 * 作者：Rance on 2016/10/27 16:13
 * 邮箱：rance935@163.com
 */
public class LivingInfo {
    private int flag;
    private String conversationId;
    private String merchantPassword;
    private String conversationTitle;
    private String liveRoomCoverPath;
    private String areaCode;

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getMerchantPassword() {
        return merchantPassword;
    }

    public void setMerchantPassword(String merchantPassword) {
        this.merchantPassword = merchantPassword;
    }

    public String getConversationTitle() {
        return conversationTitle;
    }

    public void setConversationTitle(String conversationTitle) {
        this.conversationTitle = conversationTitle;
    }

    public String getLiveRoomCoverPath() {
        return liveRoomCoverPath;
    }

    public void setLiveRoomCoverPath(String liveRoomCoverPath) {
        this.liveRoomCoverPath = liveRoomCoverPath;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }
}
