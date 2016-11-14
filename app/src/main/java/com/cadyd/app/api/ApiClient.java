package com.cadyd.app.api;


import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.cadyd.app.JConstant;
import com.cadyd.app.MyApplication;
import com.cadyd.app.R;
import com.cadyd.app.asynctask.DataLoader;
import com.cadyd.app.asynctask.SimpleAsyncTask;
import com.cadyd.app.comm.ImageLodingListener;
import com.cadyd.app.db.DBManager;
import com.cadyd.app.db.DataBaseUtil;
import com.cadyd.app.model.AreaCode;
import com.cadyd.app.model.AreaInfo;
import com.cadyd.app.model.CityInfo;
import com.cadyd.app.model.ProvinceInfo;
import com.cadyd.app.model.SignInModel;
import com.cadyd.app.ui.activity.SignInActivity;
import com.cadyd.app.ui.push.gles.Config;
import com.cadyd.app.ui.view.AlertConfirmation;
import com.cadyd.app.ui.view.ToastView;
import com.cadyd.app.ui.view.city.CityList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import org.wcy.android.utils.CursorUtil;
import org.wcy.android.utils.NetworkUtil;
import org.wcy.common.utils.Pinyin;
import org.wcy.common.utils.StringUtil;
import org.wcy.common.utils.aesutils.AESUtil;

import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;

/**
 * Created by admin on 2016/4/11.
 */
public class ApiClient {
    public static ArrayList<Object> TAG = new ArrayList<>();
    public static ImageLodingListener listener = new ImageLodingListener();

    public static void CloseDBmanager() {
        db.closeDB();
    }


    public static void addRequest(Request<?> request, Object tag) {
        request.setTag(tag);
        TAG.add(tag);
        addRequest(request);
    }

    /**
     * Adds a request to the Volley request queue
     *
     * @param request is the request to add to the Volley queue
     */
    public static void addRequest(Request<?> request) {
        queue.add(request);
    }

    public static void cancelRequest(Object tag) {
        for (Object tagObj : TAG) {
            if (tagObj.equals(tag)) {
                queue.cancelAll(tagObj);
            }
        }
    }

    public static void getTcpConnect(@NonNull String serverId) {
        if (serverId != null) {
            Config.serverId = serverId;
            String[] scokt = serverId.split(":");
            Config.SOCKETPATH = scokt[0];
            Config.SOCKETPORT = Integer.valueOf(scokt[1]);
            Log.i("wan", "tcp---path----" + scokt[0] + "port---" + scokt[1]);
//            PushClient.start();//开启tcp链接
        }
    }




    /**
     * 获取地区
     *
     * @return
     */
    public static List<AreaInfo> getArea(boolean is) {
        List<AreaInfo> list = new ArrayList<AreaInfo>();
        if (!is) {
            AreaInfo area = new AreaInfo();
            area.name = "全国";
            area.nameSort = "#";
            area.id = "";
            list.add(area);

            AreaInfo area2 = new AreaInfo();
            area2.name = "热门 热门 热门 热门";
            area2.nameSort = "热门城市";
            area2.id = "";
            list.add(area2);
        }
        list.addAll(cursorUtil.converts(AreaInfo.class, db.queryAll("select * from AreaTable ORDER BY nameSort", null)));
        return list;
    }

    public static AreaInfo queryArea(String city) {
        return cursorUtil.convert(AreaInfo.class, db.queryAll("select * from AreaTable where name like '%" + city + "%'", null));
    }

    public static String getAreaName(String id) {
        return db.getString(DataBaseUtil.AreaTable.TABLENAME, "name", "id=?", id);
    }

    public static String getCityName(String id) {
        return db.getString(DataBaseUtil.CityTable.TABLENAME, "name", "id=?", id);
    }

    public static CityInfo queryCity(String area, String city) {
        CityInfo cityInfo = null;
        AreaInfo areaInfo = null;
        if (StringUtil.hasText(city)) {
            cityInfo = cursorUtil.convert(CityInfo.class, db.queryAll("select * from CityInfoTable where name like '%" + city + "%'", null));
        }
        if (cityInfo == null) {
            cityInfo = new CityInfo();
            if (!StringUtil.hasText(area)) {
                area = "重庆";
            }
            areaInfo = queryArea(area);
            cityInfo.areaid = areaInfo.id;
            cityInfo.name = areaInfo.name;
            cityInfo.areacode = areaInfo.provincenid;
        }
        return cityInfo;
    }


    /**
     * 初始化城市列表到数据库
     */
    public static void initDatda(Context content) {
        try {
            if (db.count(DataBaseUtil.AreaTable.TABLENAME) <= 0) {
                JsonReader reader = new JsonReader(new InputStreamReader(content.getAssets().open("areaCode.json"), "UTF-8"));
                List<AreaCode> areas = gson.fromJson(reader, AreaCode.getType());
                db.delete(DataBaseUtil.AreaTable.TABLENAME);
                db.delete(DataBaseUtil.CityTable.TABLENAME);
                for (AreaCode ar1 : areas) {
                    ProvinceInfo pi = new ProvinceInfo();
                    pi.id = ar1.code;
                    pi.name = ar1.name;
                    if (ar1.name.substring(0, 2).equals("重庆")) {
                        pi.nameSort = "C";
                    } else {
                        pi.nameSort = Pinyin.getPinYinHeadChar(ar1.name, 1);
                    }
                    db.insert(DataBaseUtil.ProvinceTable.TABLENAME, cursorUtil.getContentValues(pi));
                    for (AreaCode ar2 : ar1.childs) {
                        AreaInfo arinfo1 = new AreaInfo();
                        arinfo1.id = ar2.code;
                        arinfo1.name = ar2.name;
                        arinfo1.provincenid = ar1.code;
                        if (ar2.name.substring(0, 2).equals("重庆")) {
                            arinfo1.nameSort = "C";
                        } else {
                            arinfo1.nameSort = Pinyin.getPinYinHeadChar(arinfo1.name, 1);
                        }
                        db.insert(DataBaseUtil.AreaTable.TABLENAME, cursorUtil.getContentValues(arinfo1));
                        List<AreaCode> citys = ar2.childs;
                        if (null != citys && 0 < citys.size()) {
                            for (AreaCode ar3 : citys) {
                                CityInfo city = new CityInfo(ar3.code, ar3.name, ar2.code);
                                db.insert(DataBaseUtil.CityTable.TABLENAME, cursorUtil.getContentValues(city));
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除本地搜索记录
     *
     * @return
     */
    public static long deleteSearchHistory() {
        return db.delete(DataBaseUtil.searchhistory.TABLENAME);
    }

    /**
     * 添加本地搜索记录
     *
     * @param name 搜索名称
     * @return
     */
    public static long addSearchHistory(String name) {
        String str = "";
        Cursor c = db.query(DataBaseUtil.searchhistory.TABLENAME, DataBaseUtil.searchhistory.name + "=?", name);
        if (c.moveToNext()) {
            str = c.getString(c.getColumnIndex(DataBaseUtil.searchhistory.name));
        }
        if (str == null || str.equals("")) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(DataBaseUtil.searchhistory.name, name);
            db.insert(DataBaseUtil.searchhistory.TABLENAME, contentValues);
        }
        return 1;
    }

    /**
     * 查询本地搜索记录
     *
     * @param search
     * @return
     */
    public static List<String> getListSearchHistory(List<String> list, String search) {
        if (list == null) {
            list = new ArrayList<String>();
        } else {
            list.clear();
        }
        StringBuffer str = new StringBuffer("select * from ").append(DataBaseUtil.searchhistory.TABLENAME).append(" where ");
        str.append(DataBaseUtil.searchhistory.name).append(" like '%").append(search).append("%'");
        str.append(" order by ");
        str.append(DataBaseUtil.searchhistory.code);
        str.append(" DESC ");
        Cursor c = db.queryCustom(str.toString(), null);
        while (c.moveToNext()) {
            list.add(c.getString(c.getColumnIndex(DataBaseUtil.searchhistory.name)));
        }
        return list;
    }

    private static DBManager db = null;
    public static CursorUtil cursorUtil;
    private static RequestQueue queue;
    private static Gson gson;

    public static void ImageLoadersRounde(String uri, final ImageView imageView) {
        ImageLoadersRounde(uri, imageView, 20);
    }

    public static void ImageLoadersRounde(@NonNull String uri, @NonNull final ImageView imageView, @NonNull int round) {
        // 初始化ImageLoader
        DisplayImageOptions options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.goods_image_default) // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.drawable.goods_image_error) // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.goods_image_error) // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(false) // 设置下载的图片是否缓存在内存中
                .cacheOnDisk(false) // 设置下载的图片是否缓存在SD卡中
                .displayer(new FadeInBitmapDisplayer(200))// 是否图片加载好后渐入的动画时间
                .bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .displayer(new RoundedBitmapDisplayer(round)) // 设置成圆角图片
                .build(); // 创建配置过得DisplayImageOption对象

        ImageLoader.getInstance().displayImage(uri, imageView, options);
    }

    public static void displayImageNoCache(@NonNull String uri, @NonNull ImageView imageView, @NonNull int resid) {
        // 初始化ImageLoader
        DisplayImageOptions options = new DisplayImageOptions.Builder().showImageOnLoading(resid) // 设置图片下载期间显示的图片
                .showImageForEmptyUri(resid) // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(resid) // 设置图片加载或解码过程中发生错误显示的图片
                .displayer(new FadeInBitmapDisplayer(200))// 是否图片加载好后渐入的动画时间
                .bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型
                .cacheInMemory(false) // 设置下载的图片是否缓存在内存中
                .cacheOnDisk(false) // 设置下载的图片是否缓存在SD卡中
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .build(); // 创建配置过得DisplayImageOption对象
        ImageLoader.getInstance().displayImage(uri, imageView, options);
    }


    public static void displayImageNoCache(@NonNull String uri, @NonNull ImageView imageView) {
        displayImageNoCache(uri, imageView, R.mipmap.goods_type_ico);
    }

    public static void displayImage(@NonNull String uri, @NonNull ImageView imageView, @NonNull int resid) {
        // 初始化ImageLoader
        DisplayImageOptions options = new DisplayImageOptions.Builder().showImageOnLoading(resid) // 设置图片下载期间显示的图片
                .showImageForEmptyUri(resid) // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(resid) // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true) // 设置下载的图片是否缓存在SD卡中
                .displayer(new FadeInBitmapDisplayer(200))// 是否图片加载好后渐入的动画时间
                .bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT).build(); // 创建配置过得DisplayImageOption对象
        ImageLoader.getInstance().displayImage(uri, imageView, options);
    }


    public static void displayCirleImage(@NonNull String uri, @NonNull ImageView imageView, @NonNull int resid) {
        // 初始化ImageLoader
//        DisplayImageOptions options = new DisplayImageOptions.Builder().showImageOnLoading(resid) // 设置图片下载期间显示的图片
//                .showImageForEmptyUri(resid) // 设置图片Uri为空或是错误的时候显示的图片
//                .showImageOnFail(resid) // 设置图片加载或解码过程中发生错误显示的图片
//                .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
//                .cacheOnDisk(true) // 设置下载的图片是否缓存在SD卡中
//                .displayer(new RoundedBitmapDisplayer(20))//不推荐用！！！！是否设置为圆角，弧度为多少
//                .displayer(new FadeInBitmapDisplayer(200))// 是否图片加载好后渐入的动画时间
//                .bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型
//                .imageScaleType(ImageScaleType.IN_SAMPLE_INT).build(); // 创建配置过得DisplayImageOption对象


        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(resid) //设置图片在下载期间显示的图片
                .showImageForEmptyUri(resid)//设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(resid)  //设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true)//设置下载的图片是否缓存在内存中
                .cacheOnDisk(true)//设置下载的图片是否缓存在SD卡中
                .considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)//设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型
                //.decodingOptions(BitmapFactory.Options decodingOptions)//设置图片的解码配置
                .delayBeforeLoading(0)//int delayInMillis为你设置的下载前的延迟时间
                //设置图片加入缓存前，对bitmap进行设置
                //.preProcessor(BitmapProcessor preProcessor)
                .resetViewBeforeLoading(true)//设置图片在下载前是否重置，复位
                .displayer(new RoundedBitmapDisplayer(50))//不推荐用！！！！是否设置为圆角，弧度为多少
                .displayer(new FadeInBitmapDisplayer(100))//是否图片加载好后渐入的动画时间，可能会出现闪动
                .build();//构建完成
        ImageLoader.getInstance().displayImage(uri, imageView, options);
    }

    public static void displayImage(@NonNull String uri, @NonNull ImageView imageView) {
        ImageLoader.getInstance().displayImage(uri, imageView);
    }

    private static Context context;

    public static void setDDmanager(MyApplication application) {
        cursorUtil = CursorUtil.getCursorUtil();
        queue = Volley.newRequestQueue(application);
        db = new DBManager().getDBManager(application);
        gson = new GsonBuilder().disableHtmlEscaping().excludeFieldsWithModifiers().create();
        context = application.getBaseContext();
    }

    public static Gson getGson() {
        return gson;
    }

    /**
     * 根据id查找城市列表
     *
     * @return
     */
    public static List<ProvinceInfo> getProvinces() {
        Cursor cs = db.queryAll("select * from ProvinceTable", null);
        return cursorUtil.converts(ProvinceInfo.class, cs);
    }

    public static List<CityInfo> getCounys(String code) {
        return cursorUtil.converts(CityInfo.class, db.queryAll("select * from CityInfoTable where areaid=?", new String[]{code}));
    }

    /**
     * 获得省份下面的城市
     *
     * @param code 省份id
     * @return 城市列表
     */
    public static List<AreaInfo> getAreas(String code) {
        return cursorUtil.converts(AreaInfo.class, db.queryAll("select * from AreaTable where provincenid=?", new String[]{code}));
    }

    /**
     * 获得省份下面的城市
     *
     * @param pid 省份id
     * @return 城市列表
     */
    public static List<CityInfo> getCity(String pid) {
        return cursorUtil.converts(CityInfo.class, db.queryAll("select * from CityInfoTable where areaid=?", new String[]{pid}));
    }

    public static CityList getCityList() {
        CityList list = new CityList();
        list.province_list = getProvinces();
        list.filtrate_province_list = list.province_list;
        list.city_map = new HashMap<String, List<AreaInfo>>();
        list.couny_map = new HashMap<String, List<CityInfo>>();
        list.filtrate_city_map = new HashMap<String, List<AreaInfo>>();
        list.filtrate_couny_map = new HashMap<String, List<CityInfo>>();
        for (ProvinceInfo CityInfo : list.province_list) {
            List<AreaInfo> l = new ArrayList<AreaInfo>();
            list.city_map.put(CityInfo.id, getAreas(CityInfo.id));
            l.addAll(list.city_map.get(CityInfo.id));
            list.filtrate_city_map.put(CityInfo.id, l);
            for (AreaInfo c : l) {
                List<CityInfo> couny = new ArrayList<CityInfo>();
                list.couny_map.put(c.id, getCounys(c.id));
                couny.addAll(list.couny_map.get(c.id));
                list.filtrate_couny_map.put(c.id, couny);
            }
        }
        return list;
    }


    /**
     * 清除图片缓存
     */
    public static void clearImageCache() {
        ImageLoader.getInstance().clearMemoryCache();
        ImageLoader.getInstance().clearDiskCache();
        //  ImageLoader.getInstance().stop();
    }

    /**
     * 传入对象
     */
    public static <T> void send(Context context, int methodCode, Object obj, Class<?> cl,
                                DataLoader<T> dataLoader, Object tag) {
        send(null, context, methodCode, null, cl, null, dataLoader, null, true, true, true, obj, tag);
    }

    public static <T> void send(String url, Context context, int methodCode, Object obj, Class<?> cl,
                                DataLoader<T> dataLoader, Object tag) {
        send(url, context, methodCode, null, cl, null, dataLoader, null, false, true, true, obj, tag);
    }

    /*****************************/
    public static <T> void send(Context context, int methodCode, Map<String, Object> parameter, Class<?> cl,
                                DataLoader<T> dataLoader, Object tag) {
        send(null, context, methodCode, parameter, cl, null, dataLoader, null, false, true, true, null, tag);
    }

    public static <T> void send(Context context, int methodCode, Map<String, Object> parameter, boolean isDialog, Type type, DataLoader<T> dataLoader, Object tag) {
        send(null, context, methodCode, parameter, null, type, dataLoader, null, isDialog, isDialog, true, null, tag);
    }

    public static <T> void send(Context context, int methodCode, Map<String, Object> parameter, boolean isDialog, Class<?> cl,
                                DataLoader<T> dataLoader, Object tag) {
        send(null, context, methodCode, parameter, cl, null, dataLoader, null, isDialog, isDialog, true, null, tag);
    }

    public static <T> void send(Context context, int methodCode, Map<String, Object> parameter, Type type, DataLoader<T> dataLoader, Object tag) {
        send(null, context, methodCode, parameter, null, type, dataLoader, null, false, true, true, null, tag);
    }

    public static <T> void send(Context context, int methodCode, Map<String, Object> parameter, Class<?> cl,
                                DataLoader<T> dataLoader, String message, boolean isDialog, boolean isMessage, boolean cancelable, Object tag) {
        send(null, context, methodCode, parameter, cl, null, dataLoader, message, isDialog, isMessage, cancelable, null, tag);
    }

    public static <T> void send(Context context, int methodCode, Map<String, Object> parameter, Type type, DataLoader<T> dataLoader, String message, boolean isDialog,
                                boolean isMessage, boolean cancelable, Object tag) {
        send(null, context, methodCode, parameter, null, type, dataLoader, message, isDialog, isMessage,
                cancelable, null, tag);
    }

    /**
     * 访问访问请求
     *
     * @param context    上下文
     * @param methodCode 方法编号
     * @param parameter  请求内容
     * @param cl         返回类型
     * @param dataLoader 请求成功后执行的方法
     * @param message    提示内容
     * @param isDialog   是否显示提示框
     * @param isMessage  是否显示提示内容
     * @param cancelable 是否可以点击外部取消
     */
    private static <T> void send(String URl, Context context, int methodCode, Map<String, Object> parameter, Class<?> cl, Type type, DataLoader<T> dataLoader, String message,
                                 boolean isDialog, boolean isMessage, boolean cancelable, Object obj, Object tag) {
        if (NetworkUtil.isNetworkAvailable(context) && methodCode > 0) {
            SimpleAsyncTask<T> task = new SimpleAsyncTask<T>(context);
            task.isDialog = isDialog;
            task.isMessage = isMessage;
            task.cancelable = cancelable;
            task.message = message;
            task.cl = cl;
            task.type = type;
            task.setDataLoader(dataLoader);
            task.start(URl, methodCode, parameter, obj, tag);
        } else {
            if (isMessage) {
                ToastView.show(context, "无网络连接，请检测网络设置");
            }
            if (dataLoader != null) {
                dataLoader.error("无网络连接，请检测网络设置");
            }
        }
    }

    /**
     * @param map 封装的上传参数
     * @author Jerry
     * @Description: 请求参数加密
     * @time 16-10-10 上午11:50
     */
    public static RequestBody getRequestBody(@NonNull Map<Object, Object> map) {
        String body = ApiClient.getGson().toJson(map);
//        body = AESUtil.AESDoubleEncode(JConstant.key, body);
        Log.i("wan", "上传请求参数----" + body);
        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), body);
        return requestBody;
    }

    /**
     * @param map 封装的上传参数
     * @author Jerry
     * @Description: 请求参数加密
     * @time 16-10-10 上午11:50
     */
    public static RequestBody getRequestBody(@NonNull Map<Object, Object> map, boolean isEncode) {
        String body = ApiClient.getGson().toJson(map);
        Log.i("wan", "上传请求参数----" + body);
        if (isEncode) {
            body = AESUtil.AESDoubleEncode(JConstant.key, body);
        }
        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), body);
        return requestBody;
    }



    static AlertConfirmation confirmation;

    /**
     * 判断用户是否登录
     *
     * @param mContext
     */
    public static boolean isLogin(Context mContext) {
        SignInModel model = MyApplication.getInstance().getSignInModel();
        Log.i("wan", "token-----" + model.token);
        if (MyApplication.getInstance().getSignInModel().token == null) {
            confirmation = new AlertConfirmation(mContext, "掉线通知", "您已掉线是否重新登录!");
            confirmation.show(new AlertConfirmation.OnClickListeners() {
                @Override
                public void ConfirmOnClickListener() {
                    confirmation.hide();
                    Intent intent = new Intent(context, SignInActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                    context.startActivity(intent);
                }

                @Override
                public void CancelOnClickListener() {
                    confirmation.hide();
                }
            });
            return false;
        } else {
            return true;
        }

    }

}
