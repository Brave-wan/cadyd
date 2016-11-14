package com.cadyd.app.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.cadyd.app.JConstant;
import com.cadyd.app.MyApplication;
import com.cadyd.app.R;
import com.cadyd.app.adapter.NewAnnounceAdapter;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.asynctask.DataLoader;
import com.cadyd.app.handle.InterFace;
import com.cadyd.app.model.CityInfo;
import com.cadyd.app.model.Findadver;
import com.cadyd.app.model.FlagshipStore;
import com.cadyd.app.model.Flip;
import com.cadyd.app.model.Good;
import com.cadyd.app.model.GoodType;
import com.cadyd.app.model.HomeMenu;
import com.cadyd.app.model.HomeShopModel;
import com.cadyd.app.model.LiveModel;
import com.cadyd.app.model.NewAnnounce;
import com.cadyd.app.ui.activity.CommWebViewActivity;
import com.cadyd.app.ui.activity.CommonActivity;
import com.cadyd.app.ui.activity.GlobalManagerActivity;
import com.cadyd.app.ui.activity.GoodsDetailActivity;
import com.cadyd.app.ui.activity.IntegralMallManagerActivity;
import com.cadyd.app.ui.activity.MainActivity;
import com.cadyd.app.ui.activity.MallMainActivity;
import com.cadyd.app.ui.activity.MessageActivity;
import com.cadyd.app.ui.activity.OneOneManagerActivity;
import com.cadyd.app.ui.activity.OneYuanManagerActivity;
import com.cadyd.app.ui.activity.ShopAllClassificationActivity;
import com.cadyd.app.ui.activity.ShopHomeActivity;
import com.cadyd.app.ui.base.BaseFragement;
import com.cadyd.app.ui.push.gles.Config;
import com.cadyd.app.ui.view.DividerGridItemDecoration;
import com.cadyd.app.ui.view.ProgressDialogUtil;
import com.cadyd.app.ui.view.WrapContentHeightViewPager;
import com.cadyd.app.ui.view.adapter.MyViewPagerAdapter;
import com.cadyd.app.ui.view.control.PageControl;
import com.cadyd.app.ui.view.guide.ZImageCycleView;
import com.cadyd.app.widget.PLVideoViewActivity;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;

import org.wcy.android.utils.LogUtil;
import org.wcy.android.utils.PreferenceUtils;
import org.wcy.android.utils.adapter.CommonAdapter;
import org.wcy.android.utils.adapter.CommonRecyclerAdapter;
import org.wcy.android.utils.adapter.ViewHolder;
import org.wcy.android.utils.adapter.ViewRecyclerHolder;
import org.wcy.android.widget.MyListView;
import org.wcy.android.widget.NoScrollGridView;
import org.wcy.common.utils.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 首页
 * 但凡需要定位的数据都在定位之后调用
 *
 * @author wcy
 */
public class HomeFragment extends BaseFragement {

    @Bind(R.id.region)
    TextView region;
    @Bind(R.id.pullListView)
    PullToRefreshScrollView pullListView;
    @Bind(R.id.guid_top_view)
    ZImageCycleView guid_top_view;
    @Bind(R.id.topMenu)
    GridView topMenu;
    @Bind(R.id.flagship_store)
    GridView flagship_store;
    @Bind(R.id.viewflipper)
    ViewFlipper viewFlipper;
    @Bind(R.id.sousuo)
    RelativeLayout title_bg;
    @Bind(R.id.bargain_price)
    ImageView bargain_price;
    @Bind(R.id.favorable)
    ImageView favorable;
    @Bind(R.id.guid_brand_view)
    ZImageCycleView guid_brand_view;
    @Bind(R.id.goods_title)
    TextView goods_title;
    @Bind(R.id.goods_view)
    RecyclerView goods_view;
    @Bind(R.id.new_announce_title)
    TextView new_announce_title;
    @Bind(R.id.new_announce_viewpage)
    WrapContentHeightViewPager new_announce_viewpage;
    @Bind(R.id.new_announceviewpage_control)
    LinearLayout new_announceviewpage_control;
    @Bind(R.id.new_announce_layout)
    LinearLayout new_announce_layout;
    @Bind(R.id.new_announce_line)
    View new_announce_line;
    @Bind(R.id.flagship_store_title)
    TextView flagship_store_title;
    @Bind(R.id.address)
    TextView address;
    @Bind(R.id.message)
    ImageView message;//我的消息
    @Bind(R.id.recommended_shops_relative)
    RelativeLayout recommended_shops_relative;//为您推荐的店铺的大标题
    @Bind(R.id.recommended_shops_title)
    TextView recommendedShopsTitle;//推荐店铺标题
    @Bind(R.id.recommended_shops)
    MyListView recommendedShopsList;//推荐店铺列表

    @Bind(R.id.live_relative)
    RelativeLayout live_relative;//直播布局
    @Bind(R.id.live_title)
    TextView live_title;//直播的标题
    @Bind(R.id.live_more)
    TextView live_more;//直播的更多
    @Bind(R.id.live_grid_view)
    NoScrollGridView live_grid_view;//直播的item


    List<Findadver> urlsImage = new ArrayList<Findadver>();
    List<Findadver> brandUrlsImage = new ArrayList<Findadver>();
    List<Flip> listFlip = new ArrayList<>();
    List<GoodType> listGoods = new ArrayList<>();
    CityInfo area = new CityInfo();

    private List<FlagshipStore> listStore = new ArrayList<>();
    private List<Findadver> listCententMneu = new ArrayList<>();
    // 从下面进入，从上面退出 动画XML
    private Animation fromUpOut;
    private Animation fromDownIn;
    CommonAdapter<FlagshipStore> flagshipAdapter;
    CommonAdapter<HomeMenu> addapterGridview;
    CommonAdapter<Findadver> cententMneuAdapter;
    CommonRecyclerAdapter<GoodType> goodsAdapter;
    Thread thread = null;
    int page = 1;

    private MainActivity mactvity;
    private PageControl announceviewPageControl = null;
    private static final float APP_PAGE_SIZE = 4.0f;
    LayoutInflater inflater;
    /**
     * 是否下一张
     */
    private boolean isContinue = true;
    private AtomicInteger what = new AtomicInteger(4000);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mactvity = (MainActivity) getActivity();
        // 从下面进入，从上面退出 动画XML
        fromDownIn = AnimationUtils.loadAnimation(activity, R.anim.anim_from_down_in);
        fromUpOut = AnimationUtils.loadAnimation(activity, R.anim.anim_from_up_out);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.inflater = inflater;
        View view = setView(inflater, R.layout.fragment_home);
        return view;
    }

    @Override
    protected void initView() {
        message.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(MessageActivity.class, false);
            }
        });
        //    if (addapterGridview == null) {
        SpannableStringBuilder builder;
        title_bg.getBackground().setAlpha(0);
        builder = new SpannableStringBuilder("为您  推荐的商品");
        //ForegroundColorSpan 为文字前景色，BackgroundColorSpan为文字背景色
        ForegroundColorSpan redSpan = new ForegroundColorSpan(Color.BLACK);
        builder.setSpan(redSpan, 0, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        goods_title.setText(builder);

        builder = new SpannableStringBuilder("为您  推荐的旗舰店");
        builder.setSpan(redSpan, 0, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        flagship_store_title.setText(builder);
        goods_view.setLayoutManager(new LinearLayoutManager(activity));
        goods_view.addItemDecoration(new DividerGridItemDecoration(activity, LinearLayoutManager.HORIZONTAL, 20, getResources().getColor(R.color.transparent)));

        builder = new SpannableStringBuilder("为您  推荐的店铺");
        builder.setSpan(redSpan, 0, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        recommendedShopsTitle.setText(builder);

        builder = new SpannableStringBuilder("好商家正在直播");
        //  builder.setSpan(redSpan, 0, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        live_title.setText(builder);

        LogUtil.i(HomeFragment.class, "initView");

        //LocationHelper.getInstance(getActivity()).startOnceLocation();
        setPullListView();//刷新设置
        loadingMenu();//顶部菜单
        loadingNewAnnounce();//最新揭晓

        live_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                    Intent intent = new Intent(getActivity(), InfoDetailsFragment.class);
//                    startActivity(intent);
//                    Intent intent = new Intent(getActivity(), PersonalCenterActivity.class);
//                    startActivity(intent);
                startPlVideo(new LiveModel(), "http://www.baidu.com", "1123");
            }
        });
        getLiveList();

        topViewFlow();
        loadingFlagshipStore();
        loadingFlip();
        loadingGoods();//加载商品
        getShopList();//加载店铺
        //    }
    }

    //旗舰店更多
    @OnClick(R.id.flagship_more)
    public void onMore(View view) {
        Intent intent = new Intent(activity, CommonActivity.class);
        intent.putExtra(JConstant.EXTRA_INDEX, CommonActivity.FLAGSHIP);
        startActivity(intent);
    }

    /**
     * 设置顶部菜单
     */
    private void loadingMenu() {
        ApiClient.send(getActivity().getApplicationContext(), JConstant.QUERYHOMEMENU_, null, false, HomeMenu.getType(), new DataLoader<List<HomeMenu>>() {
            @Override
            public void task(String data) {
            }

            @Override
            public void succeed(List<HomeMenu> data) {
                if (data != null) {
                    if (data.size() > 0) {
                        addapterGridview = new CommonAdapter<HomeMenu>(activity, data, R.layout.gridview_home_topmenu_item) {
                            @Override
                            public void convert(ViewHolder helper, final HomeMenu item) {
                                if (helper != null && item != null) {
                                    helper.setText(R.id.name, item.name);
                                    ImageView menuImage = helper.getView(R.id.image);
                                    ApiClient.displayImageNoCache(item.icon, menuImage, R.mipmap.goods_type_ico);
                                }
                                helper.getConvertView().setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (item == null) {
                                            //积分商城
                                            Intent intentx = new Intent(activity, IntegralMallManagerActivity.class);
                                            startActivity(intentx);
                                        } else if (item.id.equals("3418aad9d3d74c4dbfc2aeab113167af")) {//商城
                                            //调用QQ聊天
//                                                String url="mqqwpa://im/chat?chat_type=wpa&uin=744502414";
//                                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                                            Intent intent = new Intent(getActivity(), MallMainActivity.class);
                                            activity.startActivity(intent);
                                        } else if (item.id.equals("c86d8136721b4ab598590139882ba858")) {//一元乐购
                                            Intent intent = new Intent(getActivity(), OneYuanManagerActivity.class);
                                            startActivity(intent);
                                        } else if (item.id.equals("5af79c89e1c0470d8fe019ea5d4b7603")) {//全部分类
                                            Intent intent = new Intent(getActivity(), ShopAllClassificationActivity.class);
                                            startActivity(intent);
                                        } else if (item.id.equals("3b2dd77d882b4d078f090667d37d21a8")) {//全球购
                                            Intent intent = new Intent(getActivity(), GlobalManagerActivity.class);
                                            startActivity(intent);
                                        } else if (item.id.equals("d69429bd4a3d431689257cda443cdf8d")) {//一乡一物
                                            Intent intent = new Intent(activity, OneOneManagerActivity.class);
                                            startActivity(intent);
                                        } else if (item.id.equals("2850b87515ef434fbdd5a29681e55107")) {//积分商城
                                            Intent intentx = new Intent(activity, IntegralMallManagerActivity.class);
                                            startActivity(intentx);
                                        } else {
                                        }
                                    }
                                });
                            }
                        };
                        topMenu.setAdapter(addapterGridview);
                    }
                }
                RefreshComplete();
            }

            @Override
            public void error(String message) {
                RefreshComplete();
            }
        }, JConstant.QUERYHOMEMENU_);
    }

    /**
     * 加载旗舰店
     */
    private void loadingFlagshipStore() {
        ApiClient.send(getActivity().getApplicationContext(), JConstant.RECOMMAND_, null, false, FlagshipStore.getType(), new DataLoader<List<FlagshipStore>>() {
            @Override
            public void task(String data) {
            }

            @Override
            public void succeed(List<FlagshipStore> data) {
                if (data != null) {
                    flagshipAdapter = new CommonAdapter<FlagshipStore>(activity, data, R.layout.gridview_home_flagship_store_item) {
                        @Override
                        public void convert(ViewHolder helper, final FlagshipStore item) {
                            helper.setText(R.id.name, item.shopname);
                            int leng = 0;
                            StringBuffer sb = new StringBuffer();
                            sb.append(item.num);
                            leng = sb.length();
                            sb.append("人收藏");
                            TextView tv = helper.getView(R.id.collect);
                            SpannableStringBuilder builder = new SpannableStringBuilder(sb.toString());
                            //ForegroundColorSpan 为文字前景色，BackgroundColorSpan为文字背景色
                            ForegroundColorSpan redSpan = new ForegroundColorSpan(Color.RED);
                            builder.setSpan(redSpan, 0, leng, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            tv.setText(builder);
                            ImageView menuImage = helper.getView(R.id.image);
                            ApiClient.displayImage(item.logo, menuImage);
                            helper.getConvertView().setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(getContext(), ShopHomeActivity.class);
                                    intent.putExtra("shopId", item.id);
                                    intent.putExtra("shopName", item.name);
                                    startActivity(intent);
                                }
                            });
                        }
                    };
                    flagship_store.setAdapter(flagshipAdapter);
                } else {

                }
                RefreshComplete();
            }

            @Override
            public void error(String message) {
                RefreshComplete();
            }
        }, JConstant.RECOMMAND_);

    }


    public class ViewFlip {
        public TextView typeOne;
        public TextView nameOne;
        public TextView typeTwo;
        public TextView nameTwo;
    }

    /**
     * 最新揭晓
     */
    private void loadingNewAnnounce() {
        Map<String, Object> map = new HashMap<>();
        map.put("categroyId", 0);
        map.put("pageIndex", 1);
        ApiClient.send(getActivity().getApplicationContext(), JConstant.ONEQUERYALLANNOUNCEDRECORD_, map, false, NewAnnounce.getType(), new DataLoader<List<NewAnnounce>>() {

            @Override
            public void task(String data) {
            }

            @Override
            public void succeed(List<NewAnnounce> data) {
                if (data != null && data.size() > 0) {
                    new_announce_layout.setVisibility(View.VISIBLE);
                    new_announce_title.setVisibility(View.VISIBLE);
                    int PageCount = (int) Math.ceil(data.size() / APP_PAGE_SIZE);
                    Map<Integer, GridView> map = new HashMap<Integer, GridView>();
                    for (int i = 0; i < PageCount; i++) {
                        GridView appPage = (GridView) inflater.inflate(R.layout.newannounce_gridview, null);
                        final NewAnnounceAdapter adapter = new NewAnnounceAdapter(activity, data, i);
                        appPage.setNumColumns(2);
                        appPage.setAdapter(adapter);
                        map.put(i, appPage);
                    }
                    if (PageCount > 1) {
                        announceviewPageControl = new PageControl(activity, new_announceviewpage_control, PageCount);
                        new_announceviewpage_control.setVisibility(View.VISIBLE);
                        new_announce_line.setVisibility(View.VISIBLE);
                        new_announce_layout.setPadding(0, 0, 0, 10);
                    } else {
                        new_announceviewpage_control.setVisibility(View.GONE);
                        new_announce_layout.setPadding(0, 0, 0, 0);
                        new_announce_line.setVisibility(View.GONE);
                    }
                    new_announce_viewpage.setAdapter(new MyViewPagerAdapter(activity, map));
                    new_announce_viewpage.setOnPageChangeListener(new MyListener());
                } else {
                    new_announce_layout.setVisibility(View.GONE);
                    new_announce_title.setVisibility(View.GONE);
                }
                RefreshComplete();
            }

            @Override
            public void error(String message) {
                new_announce_layout.setVisibility(View.GONE);
                new_announce_title.setVisibility(View.GONE);
                RefreshComplete();
            }
        }, JConstant.ONEQUERYALLANNOUNCEDRECORD_);

    }

    class MyListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageSelected(int arg0) {
            announceviewPageControl.selectPage(arg0);
        }

    }

    /**
     * 今日咨询
     */
    private void loadingFlip() {
        listFlip.clear();
        for (int i = 1; i < 4; i++) {
            Flip f = new Flip();
            f.typeOne = "热门";
            f.typeTwo = "最新";
            f.nameOne = "第一点，生活+平台，千万红包等你来拿";
            f.nameTwo = "第一点，生活+平台，千万红包等你来拿";
            listFlip.add(f);
        }
        ViewFlip viewHolder = new ViewFlip();
        viewFlipper.removeAllViews();
        for (int i = 0; i < listFlip.size(); i++) {
            Flip f = listFlip.get(i);
            View convertView = LayoutInflater.from(activity).inflate(
                    R.layout.flip_view, null);
            viewHolder.typeOne = (TextView) convertView.findViewById(R.id.typeOne);
            viewHolder.typeTwo = (TextView) convertView.findViewById(R.id.typeTwo);
            viewHolder.nameOne = (TextView) convertView.findViewById(R.id.nameOne);
            viewHolder.nameTwo = (TextView) convertView.findViewById(R.id.nameTwo);
            viewHolder.typeOne.setText(f.typeOne);
            viewHolder.typeTwo.setText(f.typeTwo);
            viewHolder.nameOne.setText(f.nameOne);
            viewHolder.nameTwo.setText(f.nameTwo);
            viewFlipper.addView(convertView);
        }
        startImageTimerTask();
    }

    /**
     * 开始图片滚动任务
     */
    private void startImageTimerTask() {
        isContinue = true;
        if (thread == null) {
            // 图片每5秒滚动一次
            thread = new Thread() {
                public void run() {
                    while (true) {
                        if (isContinue) {
                            try {
                                sleep(5000);
                                viewHandler.sendEmptyMessage(what.get());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            };
            thread.start();
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i("wan", "HomeFragmentSeaveInstanceStae");

    }

    private final Handler viewHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (listFlip != null && listFlip.size() > 0) {
                if (viewFlipper != null) {
                    viewFlipper.setInAnimation(fromDownIn);
                    viewFlipper.setOutAnimation(fromUpOut);
                    viewFlipper.showNext();
                }
            }

        }
    };


    @OnClick(R.id.region)
    public void onRegionClick(View view) {
        Intent intent = new Intent(getActivity(), CommonActivity.class);
        intent.putExtra(JConstant.EXTRA_INDEX, CommonActivity.INDEX_CITY);
        getActivity().startActivityForResult(intent, 10001);
    }

    @OnClick(R.id.search_btn)
    public void onSearchClick(View view) {
        Intent intent = new Intent(getActivity(), CommonActivity.class);
        intent.putExtra(JConstant.EXTRA_INDEX, CommonActivity.GOODS_SEARCH);
        startActivity(intent);
    }

    /**
     * 更新位置信息
     */
    public void update() {
        CityInfo c = MyApplication.getCityInfo();
        if (c == null || (!StringUtil.hasText(c.id) && !StringUtil.hasText(c.areaid))) {
            area = new CityInfo();
            area.id = "";
            area.areaid = "";
            region.setText("全国");
        } else if (!c.areaid.equals(area.areaid) || !c.id.equals(area.id)) {
            area = c;
            if (!StringUtil.hasText(c.id)) {
                region.setText(ApiClient.getAreaName(c.areaid));
            } else {
                region.setText(c.name);
            }
        }
    }


    /**
     * 加载商品信息
     */
    private void loadingGoods() {
        CityInfo city = PreferenceUtils.getObject(getActivity(), CityInfo.class);
        Map<String, Object> map = new HashMap<>();
        map.put("pageIndex", page);
        map.put("latitude", city.latitude);//纬度
        map.put("longitude", city.longitude);//经度
        ApiClient.send(getActivity().getApplicationContext(), JConstant.QUERYRECOMMENDGOODS_, map, false, GoodType.getType(), new DataLoader<List<GoodType>>() {
            @Override
            public void task(String data) {

            }

            @Override
            public void succeed(List<GoodType> data) {
                RefreshComplete();
                if (data != null) {
                    if (page == 1) {
                        listGoods.clear();
                    }
                    if (data.size() > 0) {
                        listGoods.addAll(data);
                        goodsAdapter = new CommonRecyclerAdapter<GoodType>(activity, listGoods, R.layout.home_goodes_iteam) {
                            @Override
                            public void convert(ViewRecyclerHolder helper, final GoodType item) {
                                ImageView timage = helper.getView(R.id.image_ico);
                                ApiClient.displayImage(item.icon, timage, R.mipmap.goods_type_ico);
                                helper.setText(R.id.type_name, item.name);
                                helper.getView(R.id.btn_more).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(activity, CommonActivity.class);
                                        intent.putExtra(JConstant.EXTRA_INDEX, CommonActivity.GOODS_TYPE);
                                        intent.putExtra("mid", item.id);
                                        activity.startActivity(intent);
                                    }
                                });
                                RecyclerView gv = helper.getView(R.id.grid_view);
                                if (item.chilgoods != null) {
                                    gv.setLayoutManager(new GridLayoutManager(activity, 2));
                                    gv.addItemDecoration(new DividerGridItemDecoration(activity, GridLayoutManager.VERTICAL, 20, getResources().getColor(R.color.white)));
                                    //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
                                    gv.setHasFixedSize(true);
                                    CommonRecyclerAdapter adapter = new CommonRecyclerAdapter<Good>(activity, item.chilgoods, R.layout.home_goodes_iteam_list) {
                                        @Override
                                        public void convert(ViewRecyclerHolder helper, final Good item) {
                                            TextView shop_name = helper.getView(R.id.shop_name);
                                            TextView shop_distance = helper.getView(R.id.shop_distance);
                                            shop_name.setVisibility(View.VISIBLE);
                                            shop_distance.setVisibility(View.VISIBLE);
                                            shop_name.setText(item.shopName);
                                            String distance;
                                            if (StringUtil.hasText(item.distance)) {
                                                distance = item.distance;
                                            } else {
                                                distance = "-/m";
                                            }
                                            shop_distance.setText(distance);
                                            ImageView iv = helper.getView(R.id.image_view);
                                            ApiClient.displayImage(item.thumb, iv);
                                            TextView tv = helper.getView(R.id.price);
                                            StringBuffer sb = new StringBuffer("￥");
                                            sb.append(item.price == null ? "0.00" : item.price);
                                            SpannableStringBuilder builder = new SpannableStringBuilder(sb.toString());
                                            //ForegroundColorSpan 为文字前景色，BackgroundColorSpan为文字背景色
                                            ForegroundColorSpan redSpan = new ForegroundColorSpan(getResources().getColor(R.color.more_text));
                                            builder.setSpan(redSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                            builder.setSpan(new AbsoluteSizeSpan(30), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                            builder.setSpan(new AbsoluteSizeSpan(40), 1, sb.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                            tv.setText(builder);
                                            helper.setText(R.id.name, item.title);
                                            helper.getConvertView().setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    Intent intent = new Intent(activity, GoodsDetailActivity.class);
                                                    intent.putExtra("gid", item.id);
                                                    activity.startActivity(intent);
                                                }
                                            });
                                        }
                                    };
                                    gv.setAdapter(adapter);
                                }
                            }
                        };
                        goods_view.setAdapter(goodsAdapter);
                    }
                    if (listGoods.size() > 0) {
                        goods_title.setVisibility(View.VISIBLE);
                    } else {
                        goods_title.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void error(String message) {
                RefreshComplete();
            }
        }, JConstant.QUERYRECOMMENDGOODS_);
    }


    private void setPullListView() {
        pullListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        pullListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase refreshView) {
                Log.i("PullToRefreshBase", "onPullDownToRefresh");
                title_bg.getBackground().setAlpha(0);
                page = 1;
                topViewFlow();
                loadingMenu();
                loadingFlagshipStore();
                loadingFlip();
                loadingNewAnnounce();
                loadingGoods();
                getShopList();
                getLiveList();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase refreshView) {
                Log.i("PullToRefreshBase", "onPullUpToRefresh");
                title_bg.getBackground().setAlpha(255);
                if (listGoods.size() > 0) {
                    page++;
                }
                loadingGoods();
            }
        });

        pullListView.setOnScrollChange(new PullToRefreshScrollView.ScrollChange() {
            @Override
            public void overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY) {
                if (scrollY >= 0 && scrollY <= 255) {
                    title_bg.getBackground().setAlpha(scrollY);
                } else if (scrollY < 0) {
                    title_bg.getBackground().setAlpha(0);
                } else if (scrollY > 255) {
                    title_bg.getBackground().setAlpha(255);
                }
            }
        });
    }

    private void RefreshComplete() {
        pullListView.onRefreshComplete();
    }

    /**
     * 顶部轮播图
     */
    private List<Findadver> findadverList = new ArrayList<>();

    private void topViewFlow() {
        guid_top_view.pushImageCycle();
        guid_brand_view.pushImageCycle();
        Map<String, Object> map = new HashMap<>();
        map.put("position", "A101,A103,A104,A105,A106,A107,A108,A109,A110,A111");
        map.put("city", StringUtil.hasText(area.id) ? area.id : area.areaid);
        ApiClient.send(getActivity().getApplicationContext(), JConstant.FINDADVER_, map, false, Findadver.getType(), new DataLoader<List<Findadver>>() {
                    @Override
                    public void task(String data) {

                    }

                    @Override
                    public void succeed(List<Findadver> data) {
                        mactvity.setHide();
                        pullListView.onRefreshComplete();
                        if (data != null && data.size() > 0) {
                            findadverList.addAll(data);
                            listCententMneu.clear();
                            urlsImage.clear();
                            brandUrlsImage.clear();
                            for (Findadver f : data) {
                                if (f.positionid.equals(InterFace.FindadverType.A101.ecode)) {
                                    urlsImage.add(f);
                                } else if (f.positionid.equals(InterFace.FindadverType.A103.ecode)) {
                                    ApiClient.displayImage(f.imgurl, bargain_price, R.mipmap.adv_top);
                                    bargain_price.setTag(f);
                                } else if (f.positionid.equals(InterFace.FindadverType.A104.ecode)) {
                                    ApiClient.displayImage(f.imgurl, favorable, R.mipmap.adv_top);
                                    favorable.setTag(f);
                                } else if (f.positionid.equals(InterFace.FindadverType.A105.ecode)) {
                                    brandUrlsImage.add(f);
                                } else if (f.positionid.equals(InterFace.FindadverType.A106.ecode) || f.positionid.equals(InterFace.FindadverType.A107.ecode) || f.positionid.equals(InterFace.FindadverType.A108.ecode)
                                        || f.positionid.equals(InterFace.FindadverType.A109.ecode) || f.positionid.equals(InterFace.FindadverType.A110.ecode) || f.positionid.equals(InterFace.FindadverType.A111.ecode)) {
                                    listCententMneu.add(f);
                                }
                            }
                            if (urlsImage.size() > 0) {
                                guid_top_view.setImageResources(urlsImage, "imgurl", R.mipmap.adv_top, new ZImageCycleView.ImageCycleViewListener() {
                                    @Override
                                    public void onImageClick(int position, Object object, View imageView) {
                                        Findadver findadver = (Findadver) object;
                                        Intent intent;
                                        Log.i("h5", findadver.isdirect + findadver.name);
                                        if (findadver.isdirect == 0) {//跳转到h5的活动页面
                                            LogUtil.i(HomeFragment.class, findadver.url);
                                            intent = new Intent(activity, CommWebViewActivity.class);
                                            intent.putExtra("url", findadver.url);
                                            activity.startActivity(intent);
                                        } else if (findadver.isdirect == 1) {
                                            intent = new Intent(getContext(), ShopHomeActivity.class);
                                            intent.putExtra("shopId", findadver.url);
                                            intent.putExtra("shopName", findadver.name);
                                            startActivity(intent);
                                        }
                                    }

                                    @Override
                                    public void carousel() {

                                    }
                                });
                            }

                            if (brandUrlsImage.size() > 0) {
                                guid_brand_view.setImageResources(brandUrlsImage, "imgurl", R.mipmap.adv_centen, new ZImageCycleView.ImageCycleViewListener() {
                                    @Override
                                    public void onImageClick(int position, Object object, View imageView) {

                                    }

                                    @Override
                                    public void carousel() {

                                    }
                                });
                            }
                            RefreshComplete();
                        }
                        mactvity.setHide();
                    }

                    @Override
                    public void error(String message) {
                        mactvity.setHide();
                        RefreshComplete();
                    }
                }

                , JConstant.FINDADVER_);
    }

    /**
     * 获得旗舰店列表
     */

    private void getShopList() {
        CityInfo city = PreferenceUtils.getObject(getActivity(), CityInfo.class);
        Map<String, Object> map = new HashMap<>();
        map.put("latitude", city.latitude);//纬度
        map.put("longitude", city.longitude);//经度
        ApiClient.send(getActivity().getApplicationContext(), JConstant.QUERYAREASELLERSHOP, map, HomeShopModel.getType(), new DataLoader<List<HomeShopModel>>() {
            @Override
            public void task(String data) {

            }

            @Override
            public void succeed(List<HomeShopModel> data) {
                pullListView.onRefreshComplete();
                if (data == null || data.size() <= 0) {
                    recommended_shops_relative.setVisibility(View.GONE);
                    recommendedShopsList.setVisibility(View.GONE);
                } else {
                    recommended_shops_relative.setVisibility(View.VISIBLE);
                    recommendedShopsList.setVisibility(View.VISIBLE);
                    CommonAdapter<HomeShopModel> commonAdapter = new CommonAdapter<HomeShopModel>(activity, data, R.layout.home_shop_item) {
                        @Override
                        public void convert(ViewHolder helper, final HomeShopModel item) {
                            ImageView imageView = helper.getView(R.id.shop_image);
                            ApiClient.displayImage(item.logo, imageView, R.mipmap.defaiut_on);
                            helper.setText(R.id.shop_title, item.name);
                            helper.setText(R.id.shop_distance, item.distance);
                            helper.setText(R.id.shop_content, item.story);
                            helper.getConvertView().setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(getContext(), ShopHomeActivity.class);
                                    intent.putExtra("shopId", item.id);
                                    intent.putExtra("shopName", item.name);
                                    startActivity(intent);
                                }
                            });
                        }
                    };
                    recommendedShopsList.setAdapter(commonAdapter);
                }
            }

            @Override
            public void error(String message) {
                pullListView.onRefreshComplete();
            }
        }, JConstant.QUERYAREASELLERSHOP);
    }

    /**
     * 获得直播列表
     */
    private List<LiveModel> liveModels;
    private ProgressDialogUtil dialog;
    private CommonAdapter<LiveModel> adapter;

    private void getLiveList() {

        Map<String, Object> map = new HashMap<>();
        map.put("pageIndex", 1);
        map.put("pageSize", 6);
        //JConstant.LIVEURL,
        ApiClient.send(getActivity().getApplicationContext(), 0x1000167, map, LiveModel.getType(), new DataLoader<List<LiveModel>>() {
            @Override
            public void task(String data) {

            }

            @Override
            public void succeed(List<LiveModel> data) {
                if (data == null || data.size() <= 0) {
                    live_relative.setVisibility(View.GONE);
                    live_grid_view.setVisibility(View.GONE);
                } else {
                    live_relative.setVisibility(View.VISIBLE);
                    live_grid_view.setVisibility(View.VISIBLE);
                    if (adapter == null) {
                        adapter = new CommonAdapter<LiveModel>(activity, data, R.layout.home_live_item) {
                            @Override
                            public void convert(ViewHolder helper, final LiveModel item) {
                                ImageView imageView = helper.getView(R.id.live_image);
                                helper.setText(R.id.live_title, item.subject);

                                ApiClient.displayImage(item.coverUrl, imageView, R.mipmap.defaiut_on);
                                helper.getConvertView().setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        flvUrl = item.flvUrl;

                                        startPlVideo(item, flvUrl, item.shopid);
                                    }
                                });
                            }
                        };
                        live_grid_view.setAdapter(adapter);
                    } else {
                        live_grid_view.setAdapter(adapter);
                    }

                }
            }

            @Override
            public void error(String message) {

            }
        }, 0x1000167);
    }

    private String flvUrl = null;
    private boolean isState = true;

    /**
     * 启动视频播放
     *
     * @param info   主播个人信息
     * @param flvUrl 播放地址
     */
    public void startPlVideo(@NonNull LiveModel info, @NonNull String flvUrl, @NonNull String shopId) {
        if (Config.isLive) {
            Toast.makeText(getActivity(), R.string.live_isStart, Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(getActivity(), PLVideoViewActivity.class);
        intent.putExtra("videoPath", flvUrl);
        intent.putExtra("mediaCodec", 0);//軟解
        intent.putExtra("liveStreaming", 0);//點播
        intent.putExtra("info", info);
        intent.putExtra("shopId", shopId);
        startActivity(intent);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ApiClient.cancelRequest(JConstant.QUERYHOMEMENU_);
        ApiClient.cancelRequest(JConstant.RECOMMAND_);
        ApiClient.cancelRequest(JConstant.ONEQUERYALLANNOUNCEDRECORD_);
        ApiClient.cancelRequest(JConstant.QUERYRECOMMENDGOODS_);
        ApiClient.cancelRequest(JConstant.FINDADVER_);
        ApiClient.cancelRequest(JConstant.QUERYAREASELLERSHOP);
        ApiClient.cancelRequest(0x1000167);
    }

    @Override
    public boolean onBackPressed() {
        finishFramager();
        return false;
    }
}
