package com.cadyd.app.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by SCH-1 on 2016/8/3.
 */
public class ShopCategoryEntry implements Parcelable, Serializable {

    private String menuFirstNode;

    private String name;

    public ShopCategoryEntry() {
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMenuFirstNode() {
        return menuFirstNode;
    }

    public void setMenuFirstNode(String menuFirstNode) {
        this.menuFirstNode = menuFirstNode;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(menuFirstNode);
        dest.writeString(name);
    }

    public static final Parcelable.Creator<ShopCategoryEntry> CREATOR = new Creator<ShopCategoryEntry>() {
        @Override
        public ShopCategoryEntry[] newArray(int size) {
            return new ShopCategoryEntry[size];
        }

        @Override
        public ShopCategoryEntry createFromParcel(Parcel in) {
            return new ShopCategoryEntry(in);
        }
    };

    public ShopCategoryEntry(Parcel in) {
        menuFirstNode = in.readString();
        name = in.readString();
    }

    public static Type getType() {
        Type type = new TypeToken<List<ShopCategoryEntry>>() {
        }.getType();
        return type;
    }
}
