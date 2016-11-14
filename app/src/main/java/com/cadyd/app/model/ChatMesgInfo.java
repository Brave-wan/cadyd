package com.cadyd.app.model;

/**
 * Created by root on 16-9-2.
 */

public class ChatMesgInfo {

    private String Content;
    private int evenType;//事件类型

    public int getEvenType() {
        return evenType;
    }

    public void setEvenType(int evenType) {
        this.evenType = evenType;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

}
