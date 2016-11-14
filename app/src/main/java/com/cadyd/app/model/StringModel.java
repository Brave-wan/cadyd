package com.cadyd.app.model;

/**
 * Created by xiongmao on 2016/10/8.
 */

public class StringModel {
    private boolean data;
    private int state;

    public boolean isData() {
        return data;
    }

    public void setData(boolean data) {
        this.data = data;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "StringModel{" +
                "data=" + data +
                ", state=" + state +
                '}';
    }
}
