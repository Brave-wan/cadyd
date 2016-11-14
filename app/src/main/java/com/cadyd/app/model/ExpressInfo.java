package com.cadyd.app.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by SCH-1 on 2016/8/9.
 */
public class ExpressInfo implements Serializable {

    private String EBusinessID;
    private String ShipperCode;
    private boolean Success;
    private String LogisticCode;
    private int State;
    private List<ExpressPathInfo> Traces;

    public ExpressInfo() {
    }

    public String getEBusinessID() {
        return EBusinessID;
    }

    public void setEBusinessID(String EBusinessID) {
        this.EBusinessID = EBusinessID;
    }

    public String getShipperCode() {
        return ShipperCode;
    }

    public void setShipperCode(String shipperCode) {
        ShipperCode = shipperCode;
    }

    public boolean isSuccess() {
        return Success;
    }

    public void setSuccess(boolean success) {
        Success = success;
    }

    public int getState() {
        return State;
    }

    public void setState(int state) {
        State = state;
    }

    public String getLogisticCode() {
        return LogisticCode;
    }

    public void setLogisticCode(String logisticCode) {
        LogisticCode = logisticCode;
    }

    public List<ExpressPathInfo> getTraces() {
        return Traces;
    }

    public void setTraces(List<ExpressPathInfo> traces) {
        Traces = traces;
    }
}
