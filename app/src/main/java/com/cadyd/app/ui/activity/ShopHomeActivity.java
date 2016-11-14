package com.cadyd.app.ui.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.*;

import butterknife.Bind;
import butterknife.OnClick;

import com.cadyd.app.JConstant;
import com.cadyd.app.R;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.asynctask.DataLoader;
import com.cadyd.app.comm.KeyBoardUtils;
import com.cadyd.app.comm.Utils;
import com.cadyd.app.gaode.route.RouteActivity;
import com.cadyd.app.interfaces.TowObjectParameterInterface;
import com.cadyd.app.model.CityInfo;
import com.cadyd.app.model.ShopEntry;
import com.cadyd.app.ui.base.BaseFragmentActivity;
import com.cadyd.app.ui.fragment.shop.ShopCouponListFragment;
import com.cadyd.app.ui.fragment.shop.ShopAllGoodsFragment;
import com.cadyd.app.ui.fragment.shop.ShopNewGoodsFragment;
import com.cadyd.app.ui.fragment.shop.StoreHomeFragment;
import com.cadyd.app.ui.view.toast.ToastUtils;

import org.wcy.android.utils.LogUtil;
import org.wcy.android.utils.PreferenceUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商家店铺
 * Created by SCH-1 on 2016/8/1.
 */
public class ShopHomeActivity extends BaseFragmentActivity implements TabLayout.OnTabSelectedListener, ViewPager.OnPageChangeListener, View.OnClickListener {

    @Bind(R.id.LinearLayout)
    LinearLayout linearLayout;
    @Bind(R.id.shop_home_menu)
    TabLayout tabMenu;
    @Bind(R.id.shop_home_viewpager)
    ViewPager content;
    @Bind(R.id.title)
    TextView titleView;
    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.et_shop_home_search)//顶部搜索框
            EditText searchText;
    @Bind(R.id.btn_shop_home_search_local)
    Button searchLocal;
    @Bind(R.id.btn_shop_home_search_global)
    Button searchGlobal;
    @Bind(R.id.iv_shop_home_banner)
    ImageView banner;
    @Bind(R.id.iv_shop_home_logo)
    ImageView logo;
    @Bind(R.id.tv_shop_home_address)
    TextView address;
    @Bind(R.id.tv_shop_home_distance)
    TextView distance;
    @Bind(R.id.tv_shop_home_fans)
    TextView fansNum;
    @Bind(R.id.btn_shop_home_fav)
    ImageView fav;
    @Bind(R.id.btn_shop_home_category)
    TextView btnCategory;
    @Bind(R.id.btn_shop_home_brands)
    TextView btnBrands;
    @Bind(R.id.btn_shop_home_public)
    TextView btPublic;
    @Bind(R.id.btn_shop_home_tel)
    TextView btnTel;
    @Bind(R.id.complex)
    TextView complex; //综合
    @Bind(R.id.sales)
    TextView sales; //销量
    @Bind(R.id.New)
    TextView New; //新品
    @Bind(R.id.price)
    TextView price; //价格
    @Bind(R.id.complex_l)
    LinearLayout complex_l; //综合
    @Bind(R.id.sales_l)
    LinearLayout sales_l; //销量
    @Bind(R.id.New_l)
    LinearLayout New_l; //新品
    @Bind(R.id.price_l)
    LinearLayout price_l; //价格
    @Bind(R.id.shop_home_allgoods_toolbar)
    LinearLayout toolbar;
    @Bind(R.id.title_layout)
    RelativeLayout titleLayout;

    private final String PRICEUP = " mg.price desc";// 价格降序
    private final String PRICEDROP = "mg.price";//价格升序
    private final String CREATED = "mg.created";//商品发布时间时间升序
    private final String CREATEDDESC = "mg.created desc";//商品发布时间时间升序
    private final String NUMDESC = "Sale.num desc";//  销售降序
    private final String NUM = "Sale.num"; //  销售升序

    private String type = null;
    private int Type = 0;// 0综合 1销量 2新品 3价格
    private TextView oldText;
    private LinearLayout oldLinearLayout;

    private AlertDialog dialog;
    private List<Fragment> fragments;

    private String shopId;
    private CityInfo cityInfo;
    private String id;
    private int category;

    private ShopEntry shopEntry;

    private StoreHomeFragment fragment1;
    private ShopAllGoodsFragment fragment2;
    private ShopCouponListFragment fragment4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop_home_layout);

//        String title = getIntent().getStringExtra("shopName");
        shopId = getIntent().getStringExtra("shopId");
        cityInfo = PreferenceUtils.getObject(this, CityInfo.class);

        titleView.setTextColor(getResources().getColor(R.color.text_primary_gray));
        initView();
        initListener();
        loadDatas();
    }

    private void loadDatas() {
        Map<String, Object> params = new HashMap<>();
        params.put("shopId", shopId);
        params.put("longitude", cityInfo.longitude);
        params.put("latitude", cityInfo.latitude);
        ApiClient.send(this, JConstant.SHOP_INFO, params, ShopEntry.class, new DataLoader<ShopEntry>() {

            @Override
            public void task(String data) {

            }

            @Override
            public void succeed(ShopEntry data) {
                if (data != null) {
                    applyDataToView(data);
                    titleView.setText(data.getName());
                    fragment1.getCoupon(data.getMerchantId());
                    fragment4.setShopId(data.getMerchantId());
                }
            }

            @Override
            public void error(String message) {
                Toast.makeText(ShopHomeActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        }, JConstant.SHOP_INFO);
    }

    private void applyDataToView(ShopEntry data) {
        shopEntry = data;

//        ApiClient.displayImage(data.getThumb(), banner);
        LogUtil.i(ShopHomeActivity.class, data.getThumb());
        if (banner != null) {
            ApiClient.displayImage(data.getThumb(), banner, R.drawable.error);
            ApiClient.displayImage(data.getLogo(), logo);
        }

        address.setText(data.getAddress());
        distance.setText(data.getDistance());
        fansNum.setText(Utils.converUnit(data.getFansNum()));

        if (shopEntry.isCollect()) {
            fav.setImageResource(R.mipmap.collect_selected);
        } else {
            fav.setImageResource(R.mipmap.collect_unselected);
        }
    }

    private void initListener() {
        linearLayout.setOnClickListener(this);
        back.setOnClickListener(this);
        tabMenu.setOnTabSelectedListener(this);

        btnCategory.setOnClickListener(this);
        btnBrands.setOnClickListener(this);
        btPublic.setOnClickListener(this);
        btnTel.setOnClickListener(this);
        distance.setOnClickListener(this);

        fav.setOnClickListener(this);

        searchLocal.setOnClickListener(this);
        searchGlobal.setOnClickListener(this);
    }

    private void initTool() {
        sales.setTag(0);
        price.setTag(0);
        Type = 0;
        /**初始化背景*/
        complex_l.setBackgroundResource(R.mipmap.vertical_line_bg);
        sales_l.setBackgroundResource(R.color.white);
        New_l.setBackgroundResource(R.color.white);
        price_l.setBackgroundResource(R.color.white);
        /**初始化字体颜色*/
        complex.setTextColor(getResources().getColor(R.color.Orange));
        sales.setTextColor(getResources().getColor(R.color.text_primary_gray));
        New.setTextColor(getResources().getColor(R.color.text_primary_gray));
        price.setTextColor(getResources().getColor(R.color.text_primary_gray));
        /**初始化有排序的图片*/
        sales.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.lever, 0);
        price.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.lever, 0);

        oldText = complex;
        oldLinearLayout = complex_l;
    }

    private void initView() {

        // titleLayout.setBackgroundColor(getResources().getColor(R.color.title_bg));
        fragment1 = StoreHomeFragment.newFragment(shopId);
        fragment2 = ShopAllGoodsFragment.newFragment(shopId);
        ShopNewGoodsFragment fragment3 = ShopNewGoodsFragment.newFragment(shopId);
        fragment4 = new ShopCouponListFragment();

        fragments = new ArrayList<>();
        fragment1.setClick(new TowObjectParameterInterface() {
            @Override
            public void Onchange(int type, int postion, Object object) {
                content.setCurrentItem(1, false);
                initTool();
                switch (type) {
                    case 0://查看更多分类
                        fragment2.showMenuFirstNode((String) object, searchText.getText().toString());
                        break;
                    case 1://查看更多品牌
                        fragment2.showBrandId((String) object, searchText.getText().toString());
                        break;
                }
            }
        });
        fragments.add(fragment1);
        fragments.add(fragment2);
        fragments.add(fragment3);
        fragments.add(fragment4);
        content.setAdapter(new ShopHomePageAdapter(getSupportFragmentManager(), fragments));
        tabMenu.setupWithViewPager(content);
        content.addOnPageChangeListener(this);

        tabMenu.getTabAt(0).setIcon(R.mipmap.shop_home_menu_main_item_selected).setText("店铺首页");
        tabMenu.getTabAt(1).setIcon(R.mipmap.shop_home_menu_comm_item_unselected).setText("全部商品");
        tabMenu.getTabAt(2).setIcon(R.mipmap.shop_home_menu_new_item_unselected).setText("新品上架");
        tabMenu.getTabAt(3).setIcon(R.mipmap.shop_home_menu_sale_item_unselected).setText("店铺促销");

        tabMenu.setTabTextColors(getResources().getColor(R.color.black), getResources().getColor(R.color.Orange));
        tabMenu.setTabMode(TabLayout.MODE_FIXED);
        tabMenu.setSelectedTabIndicatorColor(getResources().getColor(R.color.transparent));
        tabMenu.setSelectedTabIndicatorHeight(0);

        oldText = complex;
        oldLinearLayout = complex_l;

        /**切换状态 0升 1降*/
        sales.setTag(0);
        price.setTag(0);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        id = intent.getStringExtra("id");
        category = intent.getIntExtra("type", 0);

        if (TextUtils.isEmpty(id) && category == 0) {
            return;
        }

        content.setCurrentItem(1, false);
        initTool();
        switch (category) {
            case 1:
                fragment2.showMenuFirstNode(id, searchText.getText().toString());
                break;
            case 2:
                fragment2.showBrandId(id, searchText.getText().toString());
                break;
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        content.setCurrentItem(tab.getPosition(), true);
        switch (tab.getPosition()) {
            case 0:
                if (tab.isSelected()) {
                    tab.setIcon(R.mipmap.shop_home_menu_main_item_selected);
                }
                break;
            case 1:
                if (tab.isSelected()) {
                    toolbar.setVisibility(View.VISIBLE);
                    tab.setIcon(R.mipmap.shop_home_menu_comm_item_selected);
                    if (fragment2 != null) {
                        if (fragment2.initialization(true, searchText.getText().toString())) {
                            initTool();
                        }
                    }
                }
                break;
            case 2:
                if (tab.isSelected()) {
                    tab.setIcon(R.mipmap.shop_home_menu_new_item_selected);
                }
                break;
            case 3:
                if (tab.isSelected()) {
                    tab.setIcon(R.mipmap.shop_home_menu_sale_item_selected);
                }
                break;
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        switch (tab.getPosition()) {
            case 0:
                if (tab.isSelected()) {
                    tab.setIcon(R.mipmap.shop_home_menu_main_item_unselected);
                }
                break;
            case 1:
                if (tab.isSelected()) {
                    toolbar.setVisibility(View.GONE);
                    tab.setIcon(R.mipmap.shop_home_menu_comm_item_unselected);
                }
                break;

            case 2:
                if (tab.isSelected()) {
                    tab.setIcon(R.mipmap.shop_home_menu_new_item_unselected);
                }
                break;
            case 3:
                if (tab.isSelected()) {
                    tab.setIcon(R.mipmap.shop_home_menu_sale_item_unselected);
                }
                break;
        }
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @OnClick({R.id.complex, R.id.sales, R.id.New, R.id.price})
    public void onMyClick(View view) {
        switch (view.getId()) {
            case R.id.complex:
                if (Type != 0) {
                    onChangeText(oldText, oldLinearLayout);
                    oldText = (TextView) view;
                    oldLinearLayout = complex_l;
                    unChangeText(oldText, oldLinearLayout);
                    Type = 0;
                    fragment2.setContent(searchText.getText().toString(), null, true);
                }
                break;
            case R.id.sales:
                if (Type != 1) {
                    onChangeText(oldText, oldLinearLayout);
                    oldText = (TextView) view;
                    oldLinearLayout = sales_l;
                    Type = 1;
                    unChangeText(oldText, oldLinearLayout);
                } else {
                    if ((int) oldText.getTag() == 0) {
                        oldText.setTag(1);
                        oldText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.drop, 0);
                        fragment2.setContent(searchText.getText().toString(), NUMDESC, true);
                    } else {
                        oldText.setTag(0);
                        oldText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.rise, 0);
                        fragment2.setContent(searchText.getText().toString(), NUM, true);
                    }
                }
                break;
            case R.id.New:
                if (Type != 2) {
                    onChangeText(oldText, oldLinearLayout);
                    oldText = (TextView) view;
                    oldLinearLayout = New_l;
                    Type = 2;
                    unChangeText(oldText, oldLinearLayout);
                    fragment2.setContent(searchText.getText().toString(), CREATEDDESC, true);
                }
                break;
            case R.id.price:
                if (Type != 3) {
                    onChangeText(oldText, oldLinearLayout);
                    oldText = (TextView) view;
                    oldLinearLayout = price_l;
                    Type = 3;
                    unChangeText(oldText, oldLinearLayout);
                } else {
                    if ((int) oldText.getTag() == 0) {
                        oldText.setTag(1);
                        oldText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.drop, 0);
                        fragment2.setContent(searchText.getText().toString(), PRICEUP, true);
                    } else {
                        oldText.setTag(0);
                        oldText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.rise, 0);
                        fragment2.setContent(searchText.getText().toString(), PRICEDROP, true);
                    }
                }
                break;
        }
    }

    /**
     * 当按钮变为未选中时
     */
    private void onChangeText(TextView textview, LinearLayout linearLayout) {
        boolean isChange = false;
        Drawable drawable[] = textview.getCompoundDrawables();
        for (Drawable d : drawable) {
            if (d != null) {
                isChange = true;
            }
        }
        if (isChange) {
            textview.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.lever, 0);
            textview.setTextColor(activity.getResources().getColor(R.color.text_primary_gray));
            linearLayout.setBackgroundResource(R.color.white);
        } else {
            textview.setTextColor(activity.getResources().getColor(R.color.text_primary_gray));
            linearLayout.setBackgroundResource(R.color.white);
        }
    }

    /**
     * 当按钮变为选中时
     */

    private void unChangeText(TextView textview, LinearLayout linearLayout) {
        boolean isChange = false;
        Drawable drawable[] = textview.getCompoundDrawables();
        for (Drawable d : drawable) {
            if (d != null) {
                isChange = true;
            }
        }
        if (isChange) {
            if ((int) textview.getTag() == 0) {
                textview.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.rise, 0);
                if (Type == 1) {
                    fragment2.setContent(searchText.getText().toString(), NUM, true);
                } else if (Type == 3) {
                    fragment2.setContent(searchText.getText().toString(), PRICEDROP, true);
                }
            } else if ((int) textview.getTag() == 1) {
                textview.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.drop, 0);
                if (Type == 1) {
                    fragment2.setContent(searchText.getText().toString(), NUMDESC, true);
                } else if (Type == 3) {
                    fragment2.setContent(searchText.getText().toString(), PRICEUP, true);
                }
            }
            textview.setTextColor(activity.getResources().getColor(R.color.Orange));
            linearLayout.setBackgroundResource(R.mipmap.vertical_line_bg);
        } else {
            textview.setTextColor(activity.getResources().getColor(R.color.Orange));
            linearLayout.setBackgroundResource(R.mipmap.vertical_line_bg);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        String searchStr = null;

        switch (v.getId()) {
            case R.id.back:
                KeyBoardUtils.closeKeybord(searchText, this);
                finishActivity();
                break;
            case R.id.LinearLayout:
                KeyBoardUtils.closeKeybord(searchText, this);
                break;
            case R.id.btn_shop_home_search_local://搜索本店
                KeyBoardUtils.closeKeybord(searchText, this);
                searchStr = searchText.getText().toString();
                content.setCurrentItem(1, true);
                fragment2.setContent(searchStr, "-1", true);
                break;
            case R.id.btn_shop_home_search_global://搜索全站
                KeyBoardUtils.closeKeybord(searchText, this);
                searchStr = searchText.getText().toString();
                if (!TextUtils.isEmpty(searchStr)) {
                    intent = new Intent(this, CommonActivity.class);
                    intent.putExtra("key", searchStr);
                    intent.putExtra(JConstant.EXTRA_INDEX, CommonActivity.SEARCH_PAGE);
                    startActivity(intent);
                }
                break;
            case R.id.btn_shop_home_fav:
                if (shopEntry != null && shopEntry.isCollect()) {
                    CancelCollect();
                } else {
                    Collect();
                }
                break;
            case R.id.btn_shop_home_brands:
                if (shopEntry != null) {
                    intent = new Intent(this, ShopBrandsListActivity.class);
                    intent.putExtra("shopEntry", shopEntry);
                    startActivity(intent);
                }
                break;
            case R.id.btn_shop_home_category:
                intent = new Intent(this, ShopCategoryListActivity.class);
                intent.putExtra("shopEntry", shopEntry);
                if (shopEntry != null) {
                    startActivity(intent);
                }
                break;
            case R.id.btn_shop_home_public://查看公众号
                if (shopEntry != null && !TextUtils.isEmpty(shopEntry.getQrcodepath())) {
                    showPublicDialog(shopEntry.getQrcodepath());
                }
                break;
            case R.id.btn_shop_home_tel:
                if (shopEntry != null && !TextUtils.isEmpty(shopEntry.getPhone())) {
                    Utils.tellPhone(this, shopEntry.getPhone(), null);
                }
                break;
            case R.id.tv_shop_home_distance:
                if (shopEntry != null) {
                    intent = new Intent(this, RouteActivity.class);
                    intent.putExtra("endLat", shopEntry.getLatitude());
                    intent.putExtra("endLong", shopEntry.getLongitude());
                    startActivity(intent);
                }
                break;
        }
    }

    /**
     * 收藏
     */
    private void Collect() {
        if (application.isLogin() && shopEntry != null) {
            Map<String, Object> map = new HashMap<>();
            map.put("shopid", shopEntry.getId());
            ApiClient.send(activity, JConstant.COLLECT_, map, true, null, new DataLoader<Object>() {
                @Override
                public void task(String data) {

                }

                @Override
                public void succeed(Object data) {
                    toastSuccess("收藏成功");
                    fav.setImageResource(R.mipmap.collect_selected);
                    shopEntry.setCollect(true);
                }

                @Override
                public void error(String message) {
                    toastError("收藏失败");
                }
            }, JConstant.COLLECT_);
        } else {
            startActivity(SignInActivity.class, false);
        }

    }

    /**
     * 取消收藏
     */
    public void CancelCollect() {
        if (application.isLogin() && shopEntry != null) {
            Map<String, Object> map = new HashMap<>();
            map.put("shopId", shopEntry.getId());
            ApiClient.send(activity, JConstant.CANCEL_COLLECT_, map, true, null, new DataLoader<Object>() {

                @Override
                public void task(String data) {

                }

                @Override
                public void succeed(Object data) {
                    toastSuccess("取消收藏成功");
                    fav.setImageResource(R.mipmap.collect_unselected);
                    shopEntry.setCollect(false);
                }

                @Override
                public void error(String message) {
                    toastSuccess("取消失败");
                }
            }, JConstant.CANCEL_COLLECT_);
        } else {
            startActivity(SignInActivity.class, false);
        }
    }

    private void showPublicDialog(String url) {
        if (dialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View dialogLayout = getLayoutInflater().inflate(R.layout.shop_home_dialog_layout, null);
            ImageView close = (ImageView) dialogLayout.findViewById(R.id.bt_shop_home_dialog_close);
            ImageView code = (ImageView) dialogLayout.findViewById(R.id.iv_shop_home_dialog_code);

            ApiClient.displayImage(url, code);

//            code.setOnLongClickListener(new View.OnLongClickListener() {
//
//                @Override
//                public boolean onLongClick(View v) {
//                    Utils.openUrlToWeb(getApplicationContext(), "http://www.baidu.com");
//                    return true;
//                }
//            });
            close.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            builder.setView(dialogLayout);
            dialog = builder.create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    private static class ShopHomePageAdapter extends FragmentPagerAdapter {

        private List<Fragment> datas;

        public ShopHomePageAdapter(FragmentManager fm, List<Fragment> datas) {
            super(fm);
            this.datas = datas;
        }

        @Override
        public Fragment getItem(int position) {
            return datas.get(position);
        }

        @Override
        public int getCount() {
            return datas.size();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishActivity();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ApiClient.cancelRequest(JConstant.SHOP_INFO);
        ApiClient.cancelRequest(JConstant.COLLECT_);
        ApiClient.cancelRequest(JConstant.CANCEL_COLLECT_);
    }
}
