package com.cadyd.app.result;

import java.io.Serializable;

public class ResultData<T> implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 2274542023260007856L;
    /**
     * 默认错误码值 0xFF
     */
    public int resultCode = -1;
    /**
     * 错误提示
     */
    public String msg;
    /***
     * string返回数据内容 <br>
     * 数据内容类型为JSON/XML从此处取
     */
    public String content;
    public T data;
}
