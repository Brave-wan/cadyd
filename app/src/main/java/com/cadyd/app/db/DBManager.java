package com.cadyd.app.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;
import com.cadyd.app.R;

import java.io.*;

/**
 * 数据管理类
 *
 * @author wangchaoyong
 *         <p>
 *         2016-4-29 下午5:33:13
 */
public class DBManager {
    private static SQLiteDatabase db = null;
    private static DBManager dbmanager = new DBManager();
    private static final String TAG = "DBManager";
    private final int BUFFER_SIZE = 400000;
    public static final String DB_NAME = "cadyd.db"; //保存的数据库文件名
    public static final String PACKAGE_NAME = "com.cadyd.app";
    public static final String DB_PATH = "/data"
            + Environment.getDataDirectory().getAbsolutePath() + "/"
            + PACKAGE_NAME;  //在手机里存放数据库的位置(/data/data/com.cadyd.app/cssystem.db)

    /**
     * 初始化
     *
     * @param context
     * @return
     */
    public DBManager getDBManager(Context context) {
        if (db == null) {
            db = openDatabase(context, DB_PATH + "/" + DB_NAME);
        }
//        if (db == null) {
//            DatabaseContext dbContext = new DatabaseContext(context);
//            db = DBHelper.getDBHelper(dbContext).getWritableDatabase();
//        }
        return dbmanager;
    }


    private SQLiteDatabase openDatabase(Context context, String dbfile) {
        try {
            if (!(new File(dbfile).exists())) {
                //判断数据库文件是否存在，若不存在则执行导入，否则直接打开数据库
                InputStream is = context.getResources().openRawResource(
                        R.raw.cadyd); //欲导入的数据库
                FileOutputStream fos = new FileOutputStream(dbfile);
                byte[] buffer = new byte[BUFFER_SIZE];
                int count = 0;
                while ((count = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                }
                fos.close();
                is.close();
            }

            SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbfile, null);
            return db;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Database", "File not found");
        }
        return null;
    }

    public void closeDatabase() {
        this.db.close();

    }

    // 如果执行过上面的初始化后，可直接调用该方法
    public static DBManager getDBManager() {
        return dbmanager;
    }

    public DBManager() {
        super();
    }

    public SQLiteDatabase getSQLiteDatabase() {
        return db;
    }

    /**
     * insert
     *
     * @param contentValues
     */
    public long insert(String tabbleName, ContentValues contentValues) {
        if (contentValues.size() <= 0)
            return 0;
        return db.insert(tabbleName, null, contentValues);
    }

    /**
     * @param tabbleName  表名
     * @param whereClause whereClause表示WHERE表达式，比如“age > ? and age < ?”等
     * @param whereArgs   whereArgs参数是占位符的实际参数值
     * @return 执行成功数据条数
     */
    public int delete(String tabbleName, String whereClause, String... whereArgs) {
        if (tabbleIsExist(tabbleName)) {
            return db.delete(tabbleName, whereClause, whereArgs);
        } else {
            return 1;
        }

    }

    /**
     * @param tabbleName 表名
     * @return 执行成功数据条数
     */
    public int delete(String tabbleName) {
        if (tabbleIsExist(tabbleName)) {
            return db.delete(tabbleName, null, null);
        } else {
            return 1;
        }

    }

    /**
     * @param tabbleName    表名
     * @param contentValues ContentValues类型的变量，是键值对组成的Map，key代表列名，value代表该列要插入的值
     * @param whereClause   whereClause表示WHERE表达式，比如“age > ? and age < ?”等
     * @param whereArgs     whereArgs参数是占位符的实际参数值
     * @return
     */
    public int update(String tabbleName, ContentValues contentValues, String whereClause, String... whereArgs) {
        return db.update(tabbleName, contentValues, whereClause, whereArgs);
    }

    /**
     * 查询表下的所有数据
     *
     * @param tableName 表明
     * @return
     */
    public Cursor queryAll(String tableName) {
        return db.query(tableName, null, null, null, null, null, null);
    }

    /**
     * 查询
     *
     * @param tableName
     * @param orderBy
     * @param columns
     * @return
     */
    public Cursor queryAllorder(String tableName, String orderBy, String columns[]) {
        return db.query(tableName, columns, null, null, null, null, orderBy);
    }

    /**
     * 查询表下的所有数据
     *
     * @return
     */
    public Cursor queryAll(String sql, String[] str) {
        return db.rawQuery(sql, str);
    }

    /**
     * 分页查询
     *
     * @param tableName 表名
     * @param pageNo    第几页
     * @param pageSize  每页显示条数
     * @return
     */
    public Cursor queryPage(String tableName, int pageNo, int pageSize) {
        StringBuffer sb = new StringBuffer(((pageNo - 1) * pageSize) == 0 ? "0" : (pageNo - 1) * pageSize + "");
        sb.append(",");
        sb.append(pageSize);
        return db.query(tableName, null, null, null, null, null, null, sb.toString());
    }

    public Cursor queryPage(String tableName, String[] columns, int pageNo, int pageSize) {
        StringBuffer sb = new StringBuffer(((pageNo - 1) * pageSize) == 0 ? "0" : (pageNo - 1) * pageSize + "");
        sb.append(",");
        sb.append(pageSize);
        return db.query(tableName, columns, null, null, null, null, null, sb.toString());
    }

    public Cursor queryPage(String tableName, String[] columns, String where, String[] wheres, int pageNo, int pageSize) {
        StringBuffer sb = new StringBuffer(((pageNo - 1) * pageSize) == 0 ? "0" : (pageNo - 1) * pageSize + "");
        sb.append(",");
        sb.append(pageSize);
        return db.query(tableName, columns, where, wheres, null, null, null, sb.toString());
    }

    /**
     * 自定义sql语句进行查询
     *
     * @param sql sql语句
     * @param obj 查询条件值
     * @return
     */
    public Cursor queryCustom(String sql, String[] obj) {
        return db.rawQuery(sql, obj);
    }

    /**
     * 总的数据条数
     *
     * @param tableName 表明
     * @return
     */
    public int count(String tableName) {
        int count = 0;
        Cursor cursor = db.rawQuery("select (1) from " + tableName, null);
        if (cursor.moveToNext()) {
            count = cursor.getInt(0);
        }
        if (cursor != null)
            cursor.close();
        return count;
    }

    /**
     * 总数据条数
     *
     * @param sql sql语句
     * @param str 条件
     * @return
     */
    public int count(String sql, String[] str) {
        int count = 0;
        Cursor cursor = db.rawQuery(sql, str);
        if (cursor.moveToNext()) {
            count = cursor.getInt(0);
        }
        if (cursor != null)
            cursor.close();
        return count;
    }

    /**
     * @param tableName 表明
     * @param selection 对查询条件
     * @param str       查询条件值
     * @return
     */
    public Cursor query(String tableName, String selection, String... str) {
        return db.query(tableName, null, selection, str, null, null, null);
    }

    /**
     * 返回制定的值 INT
     *
     * @param tableName
     * @param column
     * @param where
     * @param str
     * @return
     */
    public int getInt(String tableName, String column, String where, String... str) {
        int count = 0;
        Cursor cursor = db.query(tableName, new String[]{column}, where, str, null, null, null);
        if (cursor.moveToNext()) {
            count = cursor.getInt(0);
        }
        if (cursor != null)
            cursor.close();
        return count;
    }

    /**
     * 返回制定的值 String
     *
     * @param tableName
     * @param column
     * @param where
     * @param str
     * @return
     */
    public String getString(String tableName, String column, String where, String... str) {
        String count = "";
        Cursor cursor = db.query(tableName, new String[]{column}, where, str, null, null, null);
        if (cursor.moveToNext()) {
            count = cursor.getString(0);
        }
        if (cursor != null)
            cursor.close();
        return count;
    }

    /**
     * @param tableName 表明
     * @param columns   显示的列名
     * @param selection 对查询条件
     * @param orderBy   排序
     * @param str       查询条件值
     * @return
     */
    public Cursor query(String tableName, String[] columns, String selection, String[] str, String orderBy) {
        return db.query(tableName, columns, selection, str, null, null, orderBy);
    }

    /**
     * @param tableName 表明
     * @param columns   查询需要显示的列
     * @return
     */
    public Cursor query(String tableName, String[] columns) {
        return db.query(tableName, columns, null, null, null, null, null);
    }

    /**
     * 判断某张表是否存在
     *
     * @param tableName 表名
     * @return
     */
    public boolean tabbleIsExist(String tableName) {
        boolean result = false;
        if (tableName == null) {
            return false;
        }
        Cursor cursor = null;
        try {
            StringBuffer sql = new StringBuffer("select count(*) as c from Sqlite_master  where type ='table' and name ='").append(tableName.trim()).append("' ");
            cursor = db.rawQuery(sql.toString(), null);
            if (cursor.moveToNext()) {
                int count = cursor.getInt(0);
                if (count > 0) {
                    result = true;
                }
            }

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return result;
    }

    /**
     * close database
     */
    public void closeDB() {
        if (db != null && db.isOpen()) {
            db.close();
            db = null;
        }
    }
}
