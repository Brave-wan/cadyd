package com.cadyd.app.ui.fragment.mall;

import android.content.Intent;
import android.os.Bundle;
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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.cadyd.app.AppManager;
import com.cadyd.app.JConstant;
import com.cadyd.app.R;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.asynctask.DataLoader;
import com.cadyd.app.model.CityInfo;
import com.cadyd.app.model.Findadver;
import com.cadyd.app.model.Good;
import com.cadyd.app.model.GoodType;
import com.cadyd.app.ui.activity.CommonActivity;
import com.cadyd.app.ui.activity.GoodsDetailActivity;
import com.cadyd.app.ui.activity.ShopHomeActivity;
import com.cadyd.app.ui.base.BaseFragement;
import com.cadyd.app.ui.view.DividerGridItemDecoration;
import com.cadyd.app.ui.view.guide.ImageCycleView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;

import org.wcy.android.utils.PreferenceUtils;
import org.wcy.android.utils.adapter.CommonAdapter;
import org.wcy.android.utils.adapter.CommonRecyclerAdapter;
import org.wcy.android.utils.adapter.ViewHolder;
import org.wcy.android.utils.adapter.ViewRecyclerHolder;
import org.wcy.common.utils.NumberUtil;
import org.wcy.common.utils.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;


/**
 * 商城
 *
 * @author wcy
 */
public class MallFragment extends BaseFragement {
    @Bind(R.id.pullListView)
    PullToRefreshScrollView pullListView;
    @Bind(R.id.guid_top_view)
    ImageCycleView guid_top_view;
    @Bind(R.id.bargain_price)
    ImageView bargain_price;
    @Bind(R.id.favorable)
    ImageView favorable;
    @Bind(R.id.guid_brand_view)
    ImageCycleView guid_brand_view;
    @Bind(R.id.goods_view)
    RecyclerView goods_view;
    @Bind(R.id.mall_view)
    GridView mall_view;
    CommonRecyclerAdapter<GoodType> goodsTypeAdapter;
    CommonAdapter<Good> goodsAdapter;
    List<String> urlsImage = new ArrayList<String>();
    List<String> brandUrlsImage = new ArrayList<String>();
    List<GoodType> listTypeGoods = new ArrayList<>();
    int page = 1;
    boolean isloading = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return setView(R.layout.fragment_shopping, "商城首页", true);
    }

    @Override
    public void onStart() {
        super.onStart();
        layout.title_relative.getBackground().setAlpha(255);
    }

    @Override
    protected void initView() {
        if (!isloading) {
            goods_view.setLayoutManager(new LinearLayoutManager(activity));
            goods_view.addItemDecoration(new DividerGridItemDecoration(activity, LinearLayoutManager.HORIZONTAL, 20, getResources().getColor(R.color.transparent)));
            setPullListView();
            topViewFlow();
            loadingGoods();
            loadingGoodType();
            isloading = true;
        }
    }

    @Override
    protected void TitleBarEvent(int btnID) {
        AppManager.getAppManager().finishActivity(activity);
    }

    private void setPullListView() {
        pullListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        pullListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase refreshView) {
                Log.i("PullToRefreshBase", "onPullDownToRefresh");
                page = 1;
                topViewFlow();
                loadingGoods();
                loadingGoodType();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase refreshView) {
                Log.i("PullToRefreshBase", "onPullUpToRefresh");
                if (listTypeGoods.size() > 0) {
                    page++;
                }
                loadingGoodType();
            }
        });
    }

    /**
     * 顶部轮播图
     */
    private void topViewFlow() {
        guid_top_view.pushImageCycle();
        guid_brand_view.pushImageCycle();
        CityInfo city = PreferenceUtils.getObject(getActivity(), CityInfo.class);
        Map<String, Object> map = new HashMap<>();
        map.put("position", "S101,S102,S103,S104");
        map.put("city", (StringUtil.hasText(city.id) ? city.id : city.areaid));
        ApiClient.send(activity, JConstant.FINDADVER_, map,true, Findadver.getType(), new DataLoader<List<Findadver>>() {
            @Override
            public void task(String data) {

            }

            @Override
            public void succeed(final List<Findadver> data) {
                pullListView.onRefreshComplete();
                if (data != null && data.size() > 0) {
                    brandUrlsImage.clear();
                    urlsImage.clear();
                    for (Findadver f : data) {
                        if (f.positionid.equals("S101")) {
                            urlsImage.add(f.imgurl);
                        } else if (f.positionid.equals("S102")) {
                            ApiClient.displayImage(f.imgurl, bargain_price);
                            bargain_price.setTag(f);
                        } else if (f.positionid.equals("S103")) {
                            ApiClient.displayImage(f.imgurl, favorable);
                            favorable.setTag(f);
                        } else if (f.positionid.equals("S104")) {
                            brandUrlsImage.add(f.imgurl);
                        }
                    }

                    if (urlsImage.size() > 0) {
                        guid_top_view.setImageResources(urlsImage, new ImageCycleView.ImageCycleViewListener() {
                            @Override
                            public void onImageClick(int position, View imageView) {
                                if (data != null) {
                                    Intent intent = new Intent(getContext(), ShopHomeActivity.class);
                                    intent.putExtra("shopId", data.get(position).url);
                                    intent.putExtra("shopName", data.get(position).name);
                                    startActivity(intent);
                                }
                            }

                            @Override
                            public void carousel() {
                            }
                        }, R.mipmap.adv_top);
                    }
                    if (brandUrlsImage.size() > 0) {
                        guid_brand_view.setImageResources(brandUrlsImage, new ImageCycleView.ImageCycleViewListener() {
                            @Override
                            public void onImageClick(int position, View imageView) {
                            }

                            @Override
                            public void carousel() {
                            }
                        }, R.mipmap.adv_centen);
                    }
                }
                RefreshComplete();
            }

            @Override
            public void error(String message) {
                RefreshComplete();
            }
        }, JConstant.FINDADVER_);


    }

    /**
     * 推荐商品
     */
    private void loadingGoods() {
        Map<String, Object> map = new HashMap<>();
        map.put("type", "mall");
        map.put("pageIndex", 1);
        ApiClient.send(activity, JConstant.MALL_INDEX_, map, Good.getType(), new DataLoader<List<Good>>() {
            @Override
            public void task(String data) {
            }

            @Override
            public void succeed(List<Good> data) {
                if (data != null) {
                    goodsAdapter = new CommonAdapter<Good>(activity, data, R.layout.mall_goodes_iteam_list) {
                        @Override
                        public void convert(ViewHolder helper, final Good item) {
                            ImageView iv = helper.getView(R.id.image_view);
                            ApiClient.ImageLoadersRounde(item.thumb, iv, 7);
                            TextView tv = helper.getView(R.id.price);
                            StringBuffer sb = new StringBuffer("￥");
                            sb.append(NumberUtil.getString(item.price, 2));
                            tv.setText(sb.toString());
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
                    mall_view.setAdapter(goodsAdapter);
                }
                RefreshComplete();
            }

            @Override
            public void error(String message) {
                RefreshComplete();
            }
        }, JConstant.MALL_INDEX_);

    }

    private void loadingGoodType() {
        CityInfo city = PreferenceUtils.getObject(getActivity(), CityInfo.class);
        Map<String, Object> map = new HashMap<>();
        map.put("latitude", city.latitude);//纬度
        map.put("longitude", city.longitude);//经度
        map.put("pageIndex", page);
        ApiClient.send(activity, JConstant.MALLGOODSTYPE_, map, GoodType.getType(), new DataLoader<List<GoodType>>() {
            @Override
            public void task(String data) {

            }

            @Override
            public void succeed(List<GoodType> data) {
                if (data != null) {
                    if (page == 1) {
                        listTypeGoods.clear();
                    }
                    listTypeGoods.addAll(data);
                    goodsTypeAdapter = new CommonRecyclerAdapter<GoodType>(activity, listTypeGoods, R.layout.home_goodes_iteam) {
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
                    goods_view.setAdapter(goodsTypeAdapter);
                    RefreshComplete();
                }
            }

            @Override
            public void error(String message) {
                RefreshComplete();
            }
        }, JConstant.MALLGOODSTYPE_);
    }

    private void RefreshComplete() {
        pullListView.onRefreshComplete();
    }

    @Override
    public boolean onBackPressed() {
        finishFramager();
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ApiClient.cancelRequest(JConstant.MALL_INDEX_);
        ApiClient.cancelRequest(JConstant.MALLGOODSTYPE_);
    }
}
