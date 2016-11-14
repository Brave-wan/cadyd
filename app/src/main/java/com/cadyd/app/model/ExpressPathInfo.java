package com.cadyd.app.model;

import java.io.Serializable;

/**
 * Created by SCH-1 on 2016/8/9.
 */
public class ExpressPathInfo implements Serializable {

    private String AcceptTime;

    private String AcceptStation;

    private String Remark;

    public ExpressPathInfo() {
    }

    public String getAcceptTime() {
        return AcceptTime;
    }

    public void setAcceptTime(String acceptTime) {
        AcceptTime = acceptTime;
    }

    public String getAcceptStation() {
        return AcceptStation;
    }

    public void setAcceptStation(String acceptStation) {
        AcceptStation = acceptStation;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }
}
