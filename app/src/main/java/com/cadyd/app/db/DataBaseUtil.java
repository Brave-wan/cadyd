package com.cadyd.app.db;


import com.cadyd.app.model.AreaInfo;
import com.cadyd.app.model.CityInfo;
import com.cadyd.app.model.ProvinceInfo;
import org.wcy.android.utils.CursorUtil;

public interface DataBaseUtil {
    CursorUtil cursorUtil = CursorUtil.getCursorUtil();

    /**
     * 搜索历史
     */
    public interface searchhistory {
        public final String TABLENAME = "searchhistory";
        public String code = "code";
        // 备注
        public String name = "name";
        public final String CREATESQL = new StringBuffer("CREATE TABLE IF NOT EXISTS ").append(TABLENAME).append("(").append(code).append(" INTEGER PRIMARY KEY,").append(name)
                .append(" TEXT);").toString();
        public final String DROPSQL = new StringBuffer("DROP TABLE IF EXISTS ").append(TABLENAME).toString();
    }

    public interface ProvinceTable {
        public final String TABLENAME = "ProvinceTable";
        public final String CREATESQL = cursorUtil.getSQLCreate(TABLENAME, ProvinceInfo.class);
        public final String DROPSQL = new StringBuffer("DROP TABLE IF EXISTS ").append(TABLENAME).toString();
    }

    public interface AreaTable {
        public final String TABLENAME = "AreaTable";
        public final String CREATESQL = cursorUtil.getSQLCreate(TABLENAME, AreaInfo.class);
        public final String DROPSQL = new StringBuffer("DROP TABLE IF EXISTS ").append(TABLENAME).toString();
    }

    public interface CityTable {
        public final String TABLENAME = "CityInfoTable";
        public final String CREATESQL = cursorUtil.getSQLCreate(TABLENAME, CityInfo.class);
        public final String DROPSQL = new StringBuffer("DROP TABLE IF EXISTS ").append(TABLENAME).toString();
    }


}
