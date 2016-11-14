package com.cadyd.app.utils;


import android.content.Context;
import com.cadyd.app.MyApplication;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.ui.view.city.CityList;

/**
 * 数据缓存管理类
 *
 * @author wangchaoyong
 */
public final class CacheManager {

    private static CacheManager mInstance;

    private final Context mContext;

    private CityList citylist;

    // 登录本地缓存文件名
    private static final String CACHE_SUFFIX = ".cache";

    private static final String LOGIN_RS = "ui" + CACHE_SUFFIX;

    public String mTempImageFileName;

    private CacheManager() {
        this.mContext = MyApplication.getInstance();
    }

    public synchronized static CacheManager getCacheManager() {
        return (null == mInstance) ? mInstance = new CacheManager() : mInstance;
    }


    public CityList loadCitylist() {
        citylist = (null == citylist) ? ApiClient.getCityList() : citylist;
        return citylist;
    }
}
