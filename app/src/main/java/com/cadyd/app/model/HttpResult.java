package com.cadyd.app.model;


/**
 * @Description: 所有服务器返回数据的基类
 * 作者：Jerry
 * 邮箱：185214487@qq.com
 * @time 16-9-28 下午3:44
 */

public class HttpResult<T> {

    private int state;
    //用来模仿Data
    private T data;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "HttpResult{" +
                "state=" + state +
                ", data=" + data +
                '}';
    }
}
