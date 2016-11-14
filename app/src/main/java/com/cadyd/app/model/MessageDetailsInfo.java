package com.cadyd.app.model;

/**
 * Created by Administrator on 2016/10/13.
 */
public class MessageDetailsInfo {

    /**
     * messageId : 2de2d4ca16ce45bdae669410adb07f28
     * handleStatus : 2
     * startTime : 2016-11-09 15:11:33
     * endTime : 2016-11-09 15:11:33
     * sginId : 85032e00bb6748b7861fc5c61dd9b6a4
     * content : 商家123向你发出签约申请
     */

    private String messageId;
    private int handleStatus;
    private String startTime;
    private String endTime;
    private String sginId;
    private String content;

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public int getHandleStatus() {
        return handleStatus;
    }

    public void setHandleStatus(int handleStatus) {
        this.handleStatus = handleStatus;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getSginId() {
        return sginId;
    }

    public void setSginId(String sginId) {
        this.sginId = sginId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
