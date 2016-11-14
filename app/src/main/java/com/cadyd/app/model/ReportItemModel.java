package com.cadyd.app.model;


import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by xiongmao on 2016/10/13.
 */

public class ReportItemModel implements Serializable {
    /**
     * createTime : 1476257280000
     * description : 分享个人中心
     * dicId : 67511f2b-904d-11e6-a258-00163e000da5
     * dicKey : 1001
     * dicValue : 举报内容1
     * formatType : 1
     * sortNo : 1
     * status : 1
     * typeId : system_report_reason_id
     * updateTime : 1476257280000
     */

    public boolean isCheck = false;

    private long createTime;
    private String description;
    private String dicId;
    private String dicKey;
    private String dicValue;
    private int formatType;
    private int sortNo;
    private int status;
    private String typeId;
    private long updateTime;

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDicId() {
        return dicId;
    }

    public void setDicId(String dicId) {
        this.dicId = dicId;
    }

    public String getDicKey() {
        return dicKey;
    }

    public void setDicKey(String dicKey) {
        this.dicKey = dicKey;
    }

    public String getDicValue() {
        return dicValue;
    }

    public void setDicValue(String dicValue) {
        this.dicValue = dicValue;
    }

    public int getFormatType() {
        return formatType;
    }

    public void setFormatType(int formatType) {
        this.formatType = formatType;
    }

    public int getSortNo() {
        return sortNo;
    }

    public void setSortNo(int sortNo) {
        this.sortNo = sortNo;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "ReportItemModel{" +
                "isCheck=" + isCheck +
                ", createTime=" + createTime +
                ", description='" + description + '\'' +
                ", dicId='" + dicId + '\'' +
                ", dicKey='" + dicKey + '\'' +
                ", dicValue='" + dicValue + '\'' +
                ", formatType=" + formatType +
                ", sortNo=" + sortNo +
                ", status=" + status +
                ", typeId='" + typeId + '\'' +
                ", updateTime=" + updateTime +
                '}';
    }

    public GiftModel getGiftModel() {
        Gson gson = new Gson();
        GiftModel giftModel = gson.fromJson(dicValue, GiftModel.class);
        return giftModel;
    }
}
