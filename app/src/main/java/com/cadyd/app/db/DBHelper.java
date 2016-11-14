package com.cadyd.app.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 数据库帮助类
 *
 * @author wangchaoyong
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASEVERSION = 3;// 数据库版本
    private static DBHelper dbhelper = null;
    // 数据库名称常量
    private final static String DATABASENAME = "cadyd.db"; // 数据库名称

    public static DBHelper getDBHelper(Context context) {
        if (dbhelper == null) {
            dbhelper = new DBHelper(context);
        }
        return dbhelper;
    }

    public DBHelper(Context context) {
        super(context, DATABASENAME, null, DATABASEVERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DataBaseUtil.ProvinceTable.CREATESQL);
        db.execSQL(DataBaseUtil.CityTable.CREATESQL);
        db.execSQL(DataBaseUtil.searchhistory.CREATESQL);
        db.execSQL(DataBaseUtil.AreaTable.CREATESQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DataBaseUtil.searchhistory.DROPSQL);
        db.execSQL(DataBaseUtil.AreaTable.DROPSQL);
        db.execSQL(DataBaseUtil.CityTable.DROPSQL);
        db.execSQL(DataBaseUtil.ProvinceTable.DROPSQL);
        onCreate(db);
    }

}