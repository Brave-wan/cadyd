package com.cadyd.app.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.cadyd.app.AppManager;
import com.cadyd.app.R;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.interfaces.TowParameterInterface;
import com.cadyd.app.model.CityInfo;
import com.cadyd.app.model.LoginOutInfo;
import com.cadyd.app.ui.base.BaseFragmentActivity;
import com.cadyd.app.ui.fragment.FoundFragment;
import com.cadyd.app.ui.fragment.HomeFragment;
import com.cadyd.app.ui.fragment.LiveFragment;
import com.cadyd.app.ui.fragment.UserCententFragment;
import com.cadyd.app.ui.fragment.cart.HomeShoppingCartFragment;
import com.cadyd.app.ui.view.ProgressDialogUtil;
import com.cadyd.app.ui.view.expandablebutton.AllAngleExpandableButton;
import com.cadyd.app.ui.view.expandablebutton.ButtonData;
import com.cadyd.app.ui.view.expandablebutton.ButtonEventListener;
import com.cadyd.app.ui.window.MerchantCodePopupWindow;
import com.cadyd.app.utils.BitmapUtil;
import com.cadyd.app.utils.Util;
import com.ypy.eventbus.EventBus;

import org.wcy.android.utils.LogUtil;
import org.wcy.android.utils.PreferenceUtils;
import org.wcy.android.widget.OnEmptyFoundClick;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseFragmentActivity implements View.OnClickListener {
    private final long TWO_SECOND = 1000 * 2;
    @Bind(R.id.common_frame)
    FrameLayout commonFrame;
    @Bind(R.id.content)
    FrameLayout content;
    ImageView shoppingCart;
    ImageView userCentent;
    AllAngleExpandableButton buttonExpandable;
    @Bind(R.id.relative)
    RelativeLayout relative;
    private long mPreTime;
    ImageView home;
    ImageView found;
    ImageView live;
    @Bind(R.id.loading_bg)
    View view;
    private ImageView oldImageView;

    //个人中心
    private UserCententFragment userCententFragment;
    //购物车
    //  private ShoppingCartFragment shoppingCartFragment;
    private HomeShoppingCartFragment homeShoppingCartFragment;
    //发现
    private FoundFragment foundFragment;
    //首页
    private HomeFragment homeFragment;
    //直播
    private LiveFragment liveFragment;

    private boolean exit = false;

    //记录当前页面是否为直播
    private boolean isLiveFragment = false;

    private MerchantCodePopupWindow codePopupWindow;

    private ViewGroup.LayoutParams livelp;

    private Bitmap shotbitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.i(MainActivity.class, "onCreate");
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        AppManager.getAppManager().addActivity(this);
        EventBus.getDefault().register(this);

        foundFragment = new FoundFragment();
        homeFragment = new HomeFragment();
        liveFragment = new LiveFragment();

        userCententFragment = new UserCententFragment();
        userCententFragment.setClick(new TowParameterInterface() {
            @Override
            public void Onchange(int type, String conten) {
                ResetImageResource();
                livelp.height = Util.dip2px(MainActivity.this, 50);
                live.setLayoutParams(livelp);
                home.setImageResource(R.mipmap.main_home_sel);
                oldImageView = home;
                replaceFragment(homeFragment);
                isLiveFragment = false;
            }
        });
        homeShoppingCartFragment = HomeShoppingCartFragment.newInstance(false);
        //购物车中的去发现功能，跳转到去发现的页面
        homeShoppingCartFragment.setClick(new OnEmptyFoundClick() {
            @Override
            public void onFoundCilck() {
                ResetImageResource();
                livelp.height = Util.dip2px(MainActivity.this, 50);
                live.setLayoutParams(livelp);
                found.setImageResource(R.mipmap.main_found_sel);
                oldImageView = found;
                replaceFragment(foundFragment);
                isLiveFragment = false;
            }
        });

        home = (ImageView) findViewById(R.id.home);
        found = (ImageView) findViewById(R.id.found);
        live = (ImageView) findViewById(R.id.live);
        shoppingCart = (ImageView) findViewById(R.id.shopping_cart);
        userCentent = (ImageView) findViewById(R.id.user_centent);

        // 默认显示第一页
        replaceFragment(homeFragment);
        oldImageView = home;
        livelp = live.getLayoutParams();
        home.setImageResource(R.mipmap.main_home_sel);
        initLocation();//定位
        buttonExpandable = (AllAngleExpandableButton) findViewById(R.id.button_expandable);
        installButton();
    }

    private void installButton() {
        final List<ButtonData> buttonDatas = new ArrayList<>();
        int[] drawable = {R.mipmap.live_muen_icon, R.mipmap.live_muen_comingsoon_icon,
                R.mipmap.live_muen_per_icon, R.mipmap.live_muen_bus_icon};
        for (int i = 0; i < 4; i++) {
            ButtonData buttonData = ButtonData.buildIconButton(this, drawable[i], 0);
            buttonData.setBackgroundColorId(this, R.color.transparent);
            buttonDatas.add(buttonData);
        }
        buttonExpandable.setButtonDatas(buttonDatas);
        setListener(buttonExpandable);
    }

    private void setListener(final AllAngleExpandableButton button) {
        button.setButtonEventListener(new ButtonEventListener() {
            @Override
            public void onButtonClicked(int index) {
                switch (index) {
                    case 1:
                        toast("正在开发中...");
                        break;
                    case 2:
                        Intent intent = new Intent(MainActivity.this, LiveStartActivity.class);
                        intent.putExtra("intent", "merchantCode");
                        startActivity(intent);
                        break;
                }
            }

            @Override
            public void onExpand() {
                buttonExpandable.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCollapse() {
                buttonExpandable.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        LogUtil.i(MainActivity.class, "onRestart");
    }

    public void onEventMainThread(LoginOutInfo event) {
        Log.i("wan", " tui chu");
        if (application.isExit) {
            application.isExit = false;
        }

    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        super.onActivityResult(arg0, arg1, arg2);
        if (arg0 == 10001 && arg1 == RESULT_OK) {
            LogUtil.i(MainActivity.class, "城市选择回调成功");
            homeFragment.update();
        }
    }

    public void setHide() {
        LogUtil.i(MainActivity.class, "关闭欢迎页面");
        if (view != null && view.getVisibility() == View.VISIBLE) {
            view.setVisibility(View.GONE);
        }
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        //添加fragment的切换动画
        //        if (index > currentTab) {
        //            ft.setCustomAnimations(R.anim.slide_left_in, R.anim.slide_left_out);
        //        } else {
        //            ft.setCustomAnimations(R.anim.slide_right_in, R.anim.slide_right_out);
        //        }
        if (fragment.isAdded()) {
            ft.show(fragment);
        } else {
            ft.replace(R.id.common_frame, fragment);
        }
        ft.commitAllowingStateLoss();
    }

    private ProgressDialogUtil dialog;

    @Override
    @OnClick({R.id.home, R.id.found, R.id.live, R.id.shopping_cart, R.id.user_centent})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.found:
                ResetImageResource();
                livelp.height = Util.dip2px(MainActivity.this, 50);
                live.setLayoutParams(livelp);
                found.setImageResource(R.mipmap.main_found_sel);
                if (foundFragment == null)
                    foundFragment = new FoundFragment();
                oldImageView = found;
                replaceFragment(foundFragment);
                isLiveFragment = false;
                break;

            case R.id.shopping_cart:
                if (application.isLogin()) {
                    if (homeShoppingCartFragment == null)
                        homeShoppingCartFragment = new HomeShoppingCartFragment();
                    oldImageView = shoppingCart;
                    replaceFragment(homeShoppingCartFragment);
                } else {
                    startActivity(SignInActivity.class, false);
                    return;
                }
                ResetImageResource();
                livelp.height = Util.dip2px(MainActivity.this, 50);
                live.setLayoutParams(livelp);
                shoppingCart.setImageResource(R.mipmap.main_shoppingcar_sel);
                isLiveFragment = false;
                break;

            case R.id.user_centent:
                if (application.isLogin()) {
                    if (userCentent == null)
                        userCententFragment = new UserCententFragment();
                    oldImageView = userCentent;
                    replaceFragment(userCententFragment);
                } else {
                    startActivity(SignInActivity.class, false);
                    return;
                }
                ResetImageResource();
                livelp.height = Util.dip2px(MainActivity.this, 50);
                live.setLayoutParams(livelp);
                userCentent.setImageResource(R.mipmap.main_usercenter_sel);
                isLiveFragment = false;
                break;

            case R.id.home:
                ResetImageResource();
                livelp.height = Util.dip2px(MainActivity.this, 50);
                live.setLayoutParams(livelp);
                home.setImageResource(R.mipmap.main_home_sel);
                if (homeFragment == null)
                    homeFragment = new HomeFragment();
                oldImageView = home;
                replaceFragment(homeFragment);
                isLiveFragment = false;
                break;
            case R.id.live:
                ResetImageResource();
                livelp.height = Util.dip2px(MainActivity.this, 60);
                live.setLayoutParams(livelp);
                live.setImageResource(R.mipmap.main_live_sel);
                if (isLiveFragment) {
                    buttonExpandable.expand();
                } else {
                    isLiveFragment = true;
                    if (liveFragment == null)
                        liveFragment = new LiveFragment();
                    oldImageView = live;
                    replaceFragment(liveFragment);
                }
                break;
        }
    }

    private void ResetImageResource() {
        home.setImageResource(R.mipmap.main_home_nor);
        found.setImageResource(R.mipmap.main_found_nor);
        live.setImageResource(R.mipmap.main_live_nor);
        shoppingCart.setImageResource(R.mipmap.main_shoppingcar_nor);
        userCentent.setImageResource(R.mipmap.main_usercenter_nor);
    }

    class loading extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            ApiClient.initDatda(MainActivity.this);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            dialog.hide();
        }
    }

    private String GEOFENCE_BROADCAST_ACTION = "com.cadyd.gaode.location";
    boolean isFirstLoc = true;// 是否首次定位
    private CityInfo area;

    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明mLocationOption对象
    public AMapLocationClientOption mLocationOption = null;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (isFirstLoc) {
                if (aMapLocation != null) {
                    if (aMapLocation.getErrorCode() == 0) {
                        area = ApiClient.queryCity(aMapLocation.getCity(), aMapLocation.getDistrict());
                        Log.i("wan", "dingwei cheng gong ");
                        area.latitude = "29.580353";//纬度
                        area.longitude = "106.538112";//经度
                        area.areacode = "023";
                        /**暂时只使用默认的地区*/
                        PreferenceUtils.setObject(MainActivity.this, area);
                        isFirstLoc = false;
                    } else {
                        //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                        Log.e("AmapError", "location Error, ErrCode:"
                                + aMapLocation.getErrorCode() + ", errInfo:"
                                + aMapLocation.getErrorInfo());
                        area = ApiClient.queryCity("重庆", "");
                        area.latitude = "29.580353";//纬度
                        area.longitude = "106.538112";//经度
                        area.areacode = "023";
                        PreferenceUtils.setObject(MainActivity.this, area);
                    }


                }
            }
            if (area != null) {
                if (mLocationClient != null) {
                    mLocationClient.stopLocation();//
                    mLocationClient.onDestroy();//销毁定位客户端。
                }
            }
        }
    };

    /**
     * 高德地图定位
     */
    private void initLocation() {
        if (mLocationClient == null) {
            //初始化定位
            mLocationClient = new AMapLocationClient(activity);
            //设置定位回调监听
            mLocationClient.setLocationListener(mLocationListener);
            mLocationOption = new AMapLocationClientOption();
            //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置是否返回地址信息（默认返回地址信息）
            mLocationOption.setNeedAddress(true);
            //设置是否只定位一次,默认为false
            mLocationOption.setOnceLocation(false);
            //设置是否强制刷新WIFI，默认为强制刷新
            mLocationOption.setWifiActiveScan(true);
            //设置是否允许模拟位置,默认为false，不允许模拟位置
            mLocationOption.setMockEnable(false);
            //设置定位间隔,单位毫秒,默认为2000ms
            mLocationOption.setInterval(2000);
            //给定位客户端对象设置定位参数
            mLocationClient.setLocationOption(mLocationOption);
            //注册Receiver，设置过滤器
            IntentFilter fliter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
            fliter.addAction(GEOFENCE_BROADCAST_ACTION);
            //mGeoFenceReceiver为自定义的广播接收器
            activity.registerReceiver(mGeoFenceReceiver, fliter);
            //启动定位
            mLocationClient.startLocation();
            Log.i("wan", "zheng zai qidong dingwei ");
        }
    }

    //自定义广播接收器
    private BroadcastReceiver mGeoFenceReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // 接收广播内容，处理进出的具体操作。

        }
    };

    @Override
    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();
        if ((currentTime - mPreTime) > TWO_SECOND) {
            Toast.makeText(MainActivity.this, getString(R.string.main_press_again_exit),
                    Toast.LENGTH_SHORT).show();
            mPreTime = currentTime;
        } else {
            AppManager.getAppManager().AppExit();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activity.unregisterReceiver(mGeoFenceReceiver);
        activity.unregisterReceiver(mGeoFenceReceiver);
        mLocationClient.stopLocation();//
        mLocationClient.onDestroy();//销毁定位客户端。
    }
}