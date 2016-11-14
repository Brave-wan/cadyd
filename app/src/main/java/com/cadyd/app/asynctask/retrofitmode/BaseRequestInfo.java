package com.cadyd.app.asynctask.retrofitmode;

/**
 * @Description: ${todo}(这里用一句话描述这个方法的作用)}$
 * 作者：Jerry
 * 邮箱：185214487@qq.com
 */
public class BaseRequestInfo<T> {
    private T data;
    private int state;
    private String errorMsg;

    public T getData() {
        return data;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
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

    @Override
    public String toString() {

        return "BaseRequestInfo{" +
                "data=" + data +
                ", state=" + state +
                '}';
    }
}
