package com.cadyd.app.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by xiongmao on 2016/10/17.
 */

public class GiftModel implements Parcelable {

    /**
     * giftId : 82ce5443-f4fc-4704-9bd1-a41912f95350
     * giftName : 礼物名称1
     * sortNo : 1
     * description : 描述
     * giftPic :
     */
    public boolean isCheck = false;

    private String giftId;
    private String giftName;
    private String sortNo;
    private String description;
    private String giftPic;
    private String giftPrice;//礼物价格


    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public String getGiftId() {
        return giftId;
    }

    public void setGiftId(String giftId) {
        this.giftId = giftId;
    }

    public String getGiftName() {
        return giftName;
    }

    public void setGiftName(String giftName) {
        this.giftName = giftName;
    }

    public String getSortNo() {
        return sortNo;
    }

    public void setSortNo(String sortNo) {
        this.sortNo = sortNo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGiftPic() {
        return giftPic;
    }

    public void setGiftPic(String giftPic) {
        this.giftPic = giftPic;
    }

    public String getGiftPrice() {
        return giftPrice;
    }

    public void setGiftPrice(String giftPrice) {
        this.giftPrice = giftPrice;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.isCheck ? (byte) 1 : (byte) 0);
        dest.writeString(this.giftId);
        dest.writeString(this.giftName);
        dest.writeString(this.sortNo);
        dest.writeString(this.description);
        dest.writeString(this.giftPic);
        dest.writeString(this.giftPrice);
    }

    public GiftModel() {
    }

    protected GiftModel(Parcel in) {
        this.isCheck = in.readByte() != 0;
        this.giftId = in.readString();
        this.giftName = in.readString();
        this.sortNo = in.readString();
        this.description = in.readString();
        this.giftPic = in.readString();
        this.giftPrice = in.readString();
    }

    public static final Parcelable.Creator<GiftModel> CREATOR = new Parcelable.Creator<GiftModel>() {
        @Override
        public GiftModel createFromParcel(Parcel source) {
            return new GiftModel(source);
        }

        @Override
        public GiftModel[] newArray(int size) {
            return new GiftModel[size];
        }
    };
}
