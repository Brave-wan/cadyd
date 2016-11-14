package com.cadyd.app.model;

import java.io.Serializable;

/**
 * @Description:
 * 作者：Jerry
 * 邮箱：185214487@qq.com
 */
public class SendGiftInfo implements Serializable {
    private String item;

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }
}
