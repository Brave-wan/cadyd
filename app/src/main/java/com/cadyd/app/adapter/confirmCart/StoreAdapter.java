package com.cadyd.app.adapter.confirmCart;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cadyd.app.R;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.interfaces.TowObjectParameterInterface;
import com.cadyd.app.model.*;
import com.cadyd.app.ui.view.guide.ZImageCycleView;

import org.wcy.android.utils.adapter.CommonAdapter;
import org.wcy.android.utils.adapter.CommonRecyclerAdapter;
import org.wcy.android.utils.adapter.ViewRecyclerHolder;
import org.wcy.android.widget.NoScrollGridView;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiongmao on 2016/8/1.
 */
public class StoreAdapter extends RecyclerView.Adapter<ViewHolder> {
    private LayoutInflater inflater;
    public Activity activity;
    private List<GoodType> dataList = new ArrayList<>();
    private BrandAndAdvertisement brandAndAdvertisement;
    private LeaguePreferPage leaguePreferPage;//优惠券模型

    private TowObjectParameterInterface towObjectParameterInterface;

    private final int HEANDER = 0;
    private final int FOOT = 2;
    private final int CONTENT = 1;

    public StoreAdapter(Activity activity, List<GoodType> dataList, BrandAndAdvertisement brandAndAdvertisement, LeaguePreferPage leaguePreferPage) {
        this.activity = activity;
        this.dataList = dataList;
        this.brandAndAdvertisement = brandAndAdvertisement;
        this.leaguePreferPage = leaguePreferPage;
        inflater = LayoutInflater.from(activity);
    }

    public void setClick(TowObjectParameterInterface towObjectParameterInterface) {
        this.towObjectParameterInterface = towObjectParameterInterface;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return HEANDER;
        } else if (position == dataList.size() - 1) {
            return FOOT;
        } else {
            return CONTENT;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder;
        View view;
        if (viewType == HEANDER) {
            view = inflater.inflate(R.layout.store_home_heand, null);
        } else if (viewType == FOOT) {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            view = inflater.inflate(R.layout.store_home_bottom, null);
            view.setLayoutParams(params);
        } else {
            view = inflater.inflate(R.layout.home_goodes_iteam, null);
        }
        viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setContent(dataList, position, activity, towObjectParameterInterface, brandAndAdvertisement, leaguePreferPage);
    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }
}

/****/
class ViewHolder extends RecyclerView.ViewHolder {

    private View view;

    public ViewHolder(View itemView) {
        super(itemView);
        view = itemView;
    }

    public void setContent(List<GoodType> dataList, int position, Activity activity, final TowObjectParameterInterface towObjectParameterInterface, BrandAndAdvertisement brandAndAdvertisement, LeaguePreferPage leaguePreferPage) {
        if (position == 0) {//头部
            LinearLayout layout = (LinearLayout) view.findViewById(R.id.coupon_layout);
            if (leaguePreferPage != null && leaguePreferPage.data != null && leaguePreferPage.data.size() > 0) {
                layout.setVisibility(View.VISIBLE);
                /**首部的优惠券展示*/
                RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.coupon_relative1);
                TextView price = (TextView) view.findViewById(R.id.coupon_price);
                TextView content = (TextView) view.findViewById(R.id.coupon_content);
                TextView create = (TextView) view.findViewById(R.id.coupon_create);
                TextView receive = (TextView) view.findViewById(R.id.coupon_receive);
                RelativeLayout relativeLayout2 = (RelativeLayout) view.findViewById(R.id.coupon_relative2);
                TextView price2 = (TextView) view.findViewById(R.id.coupon_price2);
                TextView content2 = (TextView) view.findViewById(R.id.coupon_content2);
                TextView create2 = (TextView) view.findViewById(R.id.coupon_create2);
                TextView receive2 = (TextView) view.findViewById(R.id.coupon_receive2);

                relativeLayout.setVisibility(View.INVISIBLE);
                relativeLayout2.setVisibility(View.INVISIBLE);

                for (int i = 0; i < leaguePreferPage.data.size(); i++) {
                    final LeaguePrefer leaguePrefer = leaguePreferPage.data.get(i);
                    switch (i) {
                        case 0:
                            relativeLayout.setVisibility(View.VISIBLE);
                            price.setText("￥" + leaguePrefer.money);
                            content.setText(leaguePrefer.title);
                            create.setText(leaguePrefer.invalid);
                            relativeLayout.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    towObjectParameterInterface.Onchange(4, 0, leaguePrefer.id);
                                }
                            });
                            break;
                        case 1:
                            relativeLayout2.setVisibility(View.VISIBLE);
                            price2.setText("￥" + leaguePrefer.money);
                            content2.setText(leaguePrefer.title);
                            create2.setText(leaguePrefer.invalid);
                            relativeLayout2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    towObjectParameterInterface.Onchange(4, 0, leaguePrefer.id);
                                }
                            });
                            break;
                    }
                }
            } else {
                layout.setVisibility(View.GONE);
            }
            NoScrollGridView gridView = (NoScrollGridView) view.findViewById(R.id.advertising_gridView);
            ZImageCycleView imageCycleView = (ZImageCycleView) view.findViewById(R.id.ZImageCycleView);
            if (brandAndAdvertisement == null) {
                return;
            }
            /**设置广告轮播图*/
            if (brandAndAdvertisement.lShopAdvert.size() > 0) {
                imageCycleView.setVisibility(View.VISIBLE);
                if (brandAndAdvertisement.lShopAdvert != null && brandAndAdvertisement.lShopAdvert.size() > 0) {
                    imageCycleView.setImageResources(brandAndAdvertisement.lShopAdvert, "path", R.mipmap.adv_top, new ZImageCycleView.ImageCycleViewListener() {
                        @Override
                        public void onImageClick(int position, Object object, View imageView) {
                            Advertisement ment = (Advertisement) object;
                            Log.i("field", ment.path);
                            towObjectParameterInterface.Onchange(2, position, ment.goodsid);
                        }

                        @Override
                        public void carousel() {

                        }
                    });
                }
            } else {
                imageCycleView.setVisibility(View.GONE);
            }
            /**设置广告平面图*/
            CommonAdapter<Advertisement> advertisementCommonAdapter = new CommonAdapter<Advertisement>(activity, brandAndAdvertisement.gShopAdvert, R.layout.match_image_view) {
                @Override
                public void convert(org.wcy.android.utils.adapter.ViewHolder helper, final Advertisement item) {
                    ImageView imageView = helper.getView(R.id.image_view);
                    ApiClient.displayImage(item.path, imageView, R.mipmap.adv_centen);
                    helper.getConvertView().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            towObjectParameterInterface.Onchange(2, position, item.goodsid);
                        }
                    });
                }
            };
            gridView.setAdapter(advertisementCommonAdapter);
            /**热销品牌*/
            LinearLayout brands_recyclerView_title = (LinearLayout) view.findViewById(R.id.brands_recyclerView_title);
            NoScrollGridView noScrollGridView = (NoScrollGridView) view.findViewById(R.id.brands_recyclerView);
            if (brandAndAdvertisement.gShopAdvert != null) {
                brands_recyclerView_title.setVisibility(View.VISIBLE);
                CommonAdapter<Brand> adapter = new CommonAdapter<Brand>(activity, brandAndAdvertisement.brandList, R.layout.brand_item) {

                    @Override
                    public void convert(org.wcy.android.utils.adapter.ViewHolder helper, final Brand item) {
                        ImageView imageView = helper.getView(R.id.image);
                        ApiClient.displayImage(item.logo, imageView, R.mipmap.adv_top);
                        helper.getConvertView().setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // 查询品牌的商品
                                if (towObjectParameterInterface != null) {
                                    towObjectParameterInterface.Onchange(3, 0, item.brandId);
                                }
                            }
                        });
                    }
                };
                noScrollGridView.setAdapter(adapter);
            } else {
                brands_recyclerView_title.setVisibility(View.GONE);
            }
        } else if (position == dataList.size() - 1) {//尾部
            final TextView getMall = (TextView) view.findViewById(R.id.get_mall);
            getMall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 加载更多商品
                    if (towObjectParameterInterface != null) {
                        towObjectParameterInterface.Onchange(0, 0, getMall);
                    }
                }
            });

        } else {//中间内容
            final GoodType goodType = dataList.get(position);
            ImageView imageIco = (ImageView) view.findViewById(R.id.image_ico);
            TextView title = (TextView) view.findViewById(R.id.type_name);
            TextView btn_more = (TextView) view.findViewById(R.id.btn_more);
            RecyclerView grid_view = (RecyclerView) view.findViewById(R.id.grid_view);
            grid_view.setLayoutManager(new GridLayoutManager(activity, 2));

            btn_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 查看更多分类商品
                    if (towObjectParameterInterface != null) {
                        towObjectParameterInterface.Onchange(1, 0, goodType.id);
                    }
                }
            });
            if (goodType == null) {
                return;
            }
            ApiClient.displayImage(goodType.icon, imageIco, R.mipmap.defaiut_on);
            title.setText(goodType.name);
            CommonRecyclerAdapter<Good> adapter = new CommonRecyclerAdapter<Good>(activity, goodType.goodsList, R.layout.home_goodes_iteam_list) {
                @Override
                public void convert(ViewRecyclerHolder helper, final Good item) {
                    ImageView imageView = helper.getView(R.id.image_view);
                    ApiClient.displayImage(item.thumb, imageView, R.mipmap.defaiut_on);
                    helper.setText(R.id.name, item.title);//商品名字
                    helper.setText(R.id.price, "￥" + item.price);//商品价格
                    TextView textView = helper.getView(R.id.old_price);//商品的老的价格
                    textView.setText("￥" + item.original);
                    textView.setVisibility(View.GONE);
                    textView.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线
                    helper.getConvertView().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // 查看商品详情
                            if (towObjectParameterInterface != null) {
                                towObjectParameterInterface.Onchange(2, 0, item.id);
                            }
                        }
                    });
                    helper.getView(R.id.home_goods_relative).setVisibility(View.GONE);
                }
            };
            grid_view.setAdapter(adapter);

        }
    }

}
