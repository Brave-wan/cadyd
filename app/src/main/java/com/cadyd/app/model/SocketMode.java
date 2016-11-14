package com.cadyd.app.model;

/**
 * @Description: ${todo}(这里用一句话描述这个方法的作用)}$
 * 作者：Jerry
 * 邮箱：185214487@qq.com
 */
public class SocketMode {

    private String roomId;// 房间号
    private String anchorId;// 主播ID
    private Boolean isLiveUser;// 是否是主播
    private Boolean isSend;// 是否发生送方
    private String message;// 聊天内容
    private String headUrl;// 头像
    private String giftId;// 礼物ID
    private String userId;// 当前用户ID
    private String userName;//用户姓名
    private long sendDate;//发送时间
    private int evenType;//事件类型

    public int getEvenType() {
        return evenType;
    }

    public void setEvenType(int evenType) {
        this.evenType = evenType;
    }

    public long getSendDate() {
        return sendDate;
    }

    public void setSendDate(long sendDate) {
        this.sendDate = sendDate;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getAnchorId() {
        return anchorId;
    }

    public void setAnchorId(String anchorId) {
        this.anchorId = anchorId;
    }

    public Boolean getLiveUser() {
        return isLiveUser;
    }

    public void setLiveUser(Boolean liveUser) {
        isLiveUser = liveUser;
    }

    public Boolean getSend() {
        return isSend;
    }

    public void setSend(Boolean send) {
        isSend = send;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public String getGiftId() {
        return giftId;
    }

    public void setGiftId(String giftId) {
        this.giftId = giftId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "SocketMode{" +
                "roomId='" + roomId + '\'' +
                ", anchorId='" + anchorId + '\'' +
                ", isLiveUser=" + isLiveUser +
                ", isSend=" + isSend +
                ", message='" + message + '\'' +
                ", headUrl='" + headUrl + '\'' +
                ", giftId='" + giftId + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}
