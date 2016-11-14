package com.cadyd.app.utils.http;

import java.io.Serializable;
import java.util.ArrayList;

public class BaseResponse<T> implements Serializable {

    /**
     * {"data":[{"id":"28711892ff291200899"," phone":"13888888888"," headimg":"def.jpg"," nickname":"","sex": "0"}],"state":1}
     */

    private static final long serialVersionUID = 1L;
    /**
     * 状态码(200,为成功,403为失败，404为接口没找到，500为服务器异常
     */
    protected String msg;
    /**
     * 所有返回对象存放位置
     */
    protected ArrayList<T> data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ArrayList<T> getData() {
        return data;
    }

    public void setData(ArrayList<T> data) {
        this.data = data;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

}
