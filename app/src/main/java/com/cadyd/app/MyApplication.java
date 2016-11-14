package com.cadyd.app;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.multidex.MultiDex;
import android.view.WindowManager;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.model.CityInfo;
import com.cadyd.app.model.LiveModel;
import com.cadyd.app.model.SignInModel;
import com.cadyd.app.model.SocketInfo;
import com.cadyd.app.ui.activity.MainActivity;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.qiniu.pili.droid.streaming.StreamingEnv;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;

import org.wcy.android.utils.PreferenceUtils;
import org.wcy.common.utils.FileUtil;
import org.wcy.common.utils.StringUtil;

import java.io.File;

import cn.jpush.android.api.JPushInterface;

public class MyApplication extends Application {

    public SignInModel model;
    public static SocketInfo info;
    public static LiveModel liveModel;
    private static MyApplication application;
    private static RequestQueue queue;
    //在自己的Application中添加如下代码
//    public RefWatcher refWatcher;

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
//        refWatcher = LeakCanary.install(this);
        application = this;

        //推流注册
        StreamingEnv.init(getApplicationContext());

        model = PreferenceUtils.getObject(this, SignInModel.class);
        initImageLoader(getApplicationContext());
        ApiClient.setDDmanager(this);
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);

        queue = Volley.newRequestQueue(this);
        checkUpdate();

    }


    public SignInModel getSignInModel() {
        return model;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    /**
     * 得到实例
     *
     * @return
     */
    public static RequestQueue getHttp() {
        return queue;
    }

    public static MyApplication getInstance() {
        return application;
    }

    /**
     * 是否登录
     *
     * @return
     */
    public static boolean isExit = false;

    public boolean isLogin() {
        if (model != null && StringUtil.hasText(model.token)) {
            System.out.println(model.token);
            return true;
        } else {
            System.out.println("meiyou token-----------------------------");
            return false;
        }
    }

    /**
     * 设置为已经加载过
     */
    public void setInitial() {
        PreferenceUtils.setValue(this, "initial", true);
    }

    /**
     * 获取是否已经加载过
     */
    public boolean getInitial() {
        return PreferenceUtils.getValue(this, "initial", false);
    }

    /**
     * 退出当前程序
     */
    public void exit() {
        AppManager.getAppManager().AppExit();
    }


    public void setGuide() {
        PreferenceUtils.setValue(this, "guide", true);
    }

    public boolean getGuide() {
        return PreferenceUtils.getValue(this, "guide", false);
    }

    /**
     * 退出清除所有信息
     */
    public void clear() {
        model.id = "";
        model.name = "";
        model.token = "";
        PreferenceUtils.clearSignModel(this, model);
        model = null;
    }


    private void initImageLoader(Context context) {

        File cacheDir = StorageUtils.getOwnCacheDirectory(
                getApplicationContext(), "Travel/"); // 设置内存卡的路径

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2)// 设置当前线程优先级
                .memoryCacheExtraOptions(480, 800) // default = device screen dimensions
                .diskCacheExtraOptions(480, 800, null)
//                .taskExecutorForCachedImages()
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .threadPoolSize(3)
                // 线程池内加载的数量
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024))
                // You can pass your own memory cache
                // implementation/你可以通过自己的内存缓存实现
                .memoryCacheSize(2 * 1024 * 1024)
                .diskCacheFileCount(100)
                // 缓存的文件数量
                .diskCacheSize(50 * 1024 * 1024)
                // 50 Mb
                .imageDownloader(new BaseImageDownloader(this, 10 * 1000, 30 * 1000)) // connectTimeout
                // readTimeout
                .discCache(new UnlimitedDiscCache(cacheDir))// 自定义缓存路径
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .build();

        ImageLoader.getInstance().init(config);

    }

    /**
     * 设置图片加载默认设置
     */
    @SuppressWarnings("deprecation")
    private void setimageloader() {
        String str = "";
        File cacheDir;
        // 初始化ImageLoader
        DisplayImageOptions options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.goods_image_default) // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.drawable.goods_image_error) // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.goods_image_error) // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true) // 设置下载的图片是否缓存在SD卡中
                .displayer(new FadeInBitmapDisplayer(0))// 是否图片加载好后渐入的动画时间
                .bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                // .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
                .build(); // 创建配置过得DisplayImageOption对象

        // 外置sd卡
        if (FileUtil.isSecondSDcardMounted()) {
            str = FileUtil.getSecondExterPath();
            cacheDir = new File(str + "/dydimageloader/Cache");
        } else if (FileUtil.isFirstSdcardMounted()) {
            str = FileUtil.getFirstExterPath();
            cacheDir = new File(str + "/dydimageloader/Cache");
        } else {
            cacheDir = StorageUtils.getOwnCacheDirectory(getApplicationContext(), "/dydimageloader/Cache");
        }

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(options)
                .memoryCacheExtraOptions(480, 800) // default = device screen dimensions
                .diskCacheExtraOptions(480, 800, null)
//                .taskExecutorForCachedImages()
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .threadPoolSize(3)
                // 线程池内加载的数量
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024))
                // You can pass your own memory cache
                // implementation/你可以通过自己的内存缓存实现
                .memoryCacheSize(2 * 1024 * 1024)
                .diskCacheFileCount(100)
                // 缓存的文件数量
                .diskCacheSize(50 * 1024 * 1024)
                // 50 Mb
                .imageDownloader(new BaseImageDownloader(this, 10 * 1000, 30 * 1000)) // connectTimeout
                // readTimeout
                .discCache(new UnlimitedDiscCache(cacheDir))// 自定义缓存路径
                .tasksProcessingOrder(QueueProcessingType.LIFO)
//                .writeDebugLogs() // Remove for release app
                .build();
        ImageLoader.getInstance().init(config);
    }


    /**
     * 创建全局变量
     * 注意在AndroidManifest.xml中的Application节点添加android:name=".MyApplication"属性
     */
    private WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams();

    public WindowManager.LayoutParams getMywmParams() {
        return wmParams;
    }


    public void checkUpdate() {
        /***** Beta高级设置 *****/
        /**
         * true表示app启动自动初始化升级模块;
         * false不会自动初始化;
         * 开发者如果担心sdk初始化影响app启动速度，可以设置为false，
         * 在后面某个时刻手动调用Beta.init(getApplicationContext(),false);
         */
        Beta.autoInit = true;

        /**
         * true表示初始化时自动检查升级;
         * false表示不会自动检查升级,需要手动调用Beta.checkUpgrade()方法;
         */
        Beta.autoCheckUpgrade = true;

        /**
         * 设置升级检查周期为60s(默认检查周期为0s)，60s内SDK不重复向后台请求策略);
         */
        Beta.upgradeCheckPeriod = 60 * 1000;

        /**
         * 设置启动延时为1s（默认延时3s），APP启动1s后初始化SDK，避免影响APP启动速度;
         */
        Beta.initDelay = 1 * 1000;

        /**
         * 设置通知栏大图标，largeIconId为项目中的图片资源;
         */
        Beta.largeIconId = R.drawable.ic_launcher;

        /**
         * 设置状态栏小图标，smallIconId为项目中的图片资源Id;
         */
        Beta.smallIconId = R.drawable.ic_launcher;

        /**
         * 设置更新弹窗默认展示的banner，defaultBannerId为项目中的图片资源Id;
         * 当后台配置的banner拉取失败时显示此banner，默认不设置则展示“loading“;
         */
        Beta.defaultBannerId = R.drawable.ic_launcher;

//        /**
//         * 设置sd卡的Download为更新资源保存目录;
//         * 后续更新资源会保存在此目录，需要在manifest中添加WRITE_EXTERNAL_STORAGE权限;
//         */
//        Beta.storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

        /**
         * 点击过确认的弹窗在APP下次启动自动检查更新时会再次显示;
         */
        Beta.showInterruptedStrategy = true;

        /**
         * 只允许在MainActivity上显示更新弹窗，其他activity上不显示弹窗;
         * 不设置会默认所有activity都可以显示弹窗;
         */
        Beta.canShowUpgradeActs.add(MainActivity.class);

        /***** 统一初始化Bugly产品，包含Beta *****/
        Bugly.init(getApplicationContext(), "900059244", false);
    }

    public static CityInfo getCityInfo() {
        CityInfo cityInfo = PreferenceUtils.getObject(getInstance(), CityInfo.class);
        return cityInfo;
    }


}
