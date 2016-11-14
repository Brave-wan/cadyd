package com.cadyd.app.utils;

import android.content.Context;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.ypy.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by liuyun on 2016/4/5 15:01.
 */
public class LocationHelper {
    private Context mContext;
    private static LocationHelper helper;
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {
                    //定位成功回调信息，设置相关消息
                    amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                    amapLocation.getLatitude();//获取纬度
                    amapLocation.getLongitude();//获取经度
                    amapLocation.getAccuracy();//获取精度信息
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = new Date(amapLocation.getTime());
                    Log.i("wan","ding wei cheng gong ");
                    df.format(date);//定位时间
                } else {
                    //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                    Log.e("wan", "location Error, ErrCode:" + amapLocation.getErrorCode() + ", errInfo:" + amapLocation.getErrorInfo());
                }
            }
        }
    };

    private LocationHelper(Context context) {
        mContext = context;
        //初始化定位
        mLocationClient = new AMapLocationClient(context);
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
    }


    public static LocationHelper getInstance(Context context) {
        if (helper == null) {
            synchronized (LocationHelper.class) {
                if (helper == null) {
                    helper = new LocationHelper(context);
                }
            }
        }
        return helper;
    }

    /**
     * @param needAddress  是否地址信息
     * @param onceLocation 是否一次定位
     * @param interval     定位间隔时间
     */
    private void startLocation(boolean needAddress, boolean onceLocation, long interval) {
        setLocationParam(needAddress, onceLocation, interval);
        mLocationClient.startLocation();
    }

    public void startOnceLocation() {
        //每6秒定位一次
        startLocation(true, false, 6000);
    }

    /**
     * 开启定位
     *
     * @param interval
     */
    public void startLocation(long interval) {
        startLocation(false, false, interval);
    }

    public void stopLocation() {
        if (mLocationClient != null && mLocationClient.isStarted())
            mLocationClient.stopLocation();//停止定位
    }

    public void destroyLocationClient() {
        mLocationClient.onDestroy();//销毁定位客户端。销毁定位客户端之后，若要重新开启定位请重新New一个AMapLocationClient对象。
    }

    /**
     * @param needAddress  是否返回错误信息
     * @param onceLocation 是否是一次定位
     * @param interval     定位间隔时间
     * @return
     * @author liuyun
     * @time 2016/4/5 16:00
     */
    public void setLocationParam(boolean needAddress, boolean onceLocation, long interval) {
        //声明mLocationOption对象
        AMapLocationClientOption mLocationOption = null;
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(needAddress);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(onceLocation);
        //设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(interval);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
//        mlocationClient.startLocation();
    }


}
