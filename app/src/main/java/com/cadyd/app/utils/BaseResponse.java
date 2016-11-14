package com.cadyd.app.utils;

import java.io.Serializable;

public class BaseResponse<T> implements Serializable {


    private static final long serialVersionUID = 1L;

    protected int state;
    protected T data;
    protected String errorMsg;

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
