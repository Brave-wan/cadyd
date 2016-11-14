package com.cadyd.app.ui.fragment.Integralmall;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import butterknife.Bind;
import butterknife.OnClick;

import com.cadyd.app.JConstant;
import com.cadyd.app.R;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.asynctask.DataLoader;
import com.cadyd.app.comm.MyChange;
import com.cadyd.app.interfaces.TowObjectParameterInterface;
import com.cadyd.app.model.*;
import com.cadyd.app.ui.activity.CommonActivity;
import com.cadyd.app.ui.activity.IntegralGoodsDetailActivity;
import com.cadyd.app.ui.activity.SignInActivity;
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
import org.wcy.android.widget.NoScrollGridView;
import org.wcy.common.utils.StringUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 积分商城首页
 */
public class IntegralHomeFragment extends BaseFragement {

    private TowObjectParameterInterface anInterface;

    @Bind(R.id.topedit)
    EditText topedit;

    public void setTop() {
        topedit.requestFocus();
    }

    private int alphaMax = 160;
    @Bind(R.id.title_layout)//大标题
            RelativeLayout title_bg;
    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.no_mall)
    TextView no_mall;

    @Bind(R.id.scrollview)
    PullToRefreshScrollView pullListView;
    private List<String> urlsImage = new ArrayList<>();
    @Bind(R.id.ui_tesco_head)
    ImageCycleView cycleView;

    CommonAdapter<Key> Gridviewaddapter;
    @Bind(R.id.intergral_gridview_type)
    NoScrollGridView GridViewtype;

    @Bind(R.id.goods_view)
    RecyclerView goods_view;
    List<GoodType> listGoods = new ArrayList<>();
    List<IntegralGoodsModel> integralGoodsModelList = new ArrayList<>();
    CommonRecyclerAdapter<GoodType> goodsAdapter;
    int page = 1;

    public void setInterface(TowObjectParameterInterface towObjectParameterInterface) {
        anInterface = towObjectParameterInterface;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return setView(inflater, R.layout.fragment_intergral_home);
    }

    @Override
    protected void initView() {
        pullListView.setMode(PullToRefreshBase.Mode.BOTH);
        pullListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                pullListView.setMode(PullToRefreshBase.Mode.BOTH);
                no_mall.setVisibility(View.GONE);
                page = 1;
                listGoods.clear();
                loadingGoods();
                heat();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                page++;
                loadingGoods();
            }
        });

        title_bg.getBackground().setAlpha(0);
        back.getBackground().setAlpha(alphaMax);
        title.setAlpha(1 - alphaMax / 100);
        pullListView.setOnScrollChange(new PullToRefreshScrollView.ScrollChange() {
            @Override
            public void overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY) {
                MyChange.ChangeHandAlpha(title_bg, back, title, scrollY);//头部滑动变化方法
            }
        });

        goods_view.setLayoutManager(new LinearLayoutManager(activity));
        goods_view.addItemDecoration(new DividerGridItemDecoration(activity, LinearLayoutManager.HORIZONTAL, 20, getResources().getColor(R.color.transparent)));

        getAdvert();
        heat();
        getType();
        loadingGoods();
    }

    //获得广告
    private void getAdvert() {
        CityInfo city = PreferenceUtils.getObject(getActivity(), CityInfo.class);
        Map<String, Object> map = new HashMap<>();
        map.put("position", "Y101");
        map.put("city", StringUtil.hasText(city.id) ? city.id : city.areaid);
        ApiClient.send(activity, JConstant.FINDADVER_, map, Findadver.getType(), new DataLoader<List<Findadver>>() {
            @Override
            public void task(String data) {

            }

            @Override
            public void succeed(List<Findadver> data) {
                final List<Findadver> findadvers = data;

                if (findadvers == null || findadvers.size() <= 0) {
                    cycleView.setBackgroundResource(R.mipmap.adv_top);
                } else {
                    for (int i = 0; i < findadvers.size(); i++) {
                        urlsImage.add(findadvers.get(i).imgurl);
                    }
                    //广告图片单击事件
                    cycleView.setImageResources(urlsImage, new ImageCycleView.ImageCycleViewListener() {
                        @Override
                        public void onImageClick(int position, View imageView) {
                            // TODO 单击图片处理事件
                        }

                        @Override
                        public void carousel() {
                        }
                    }, R.mipmap.adv_top);
                }
            }

            @Override
            public void error(String message) {

            }
        }, JConstant.FINDADVER_);
    }

    @Bind(R.id.image_view_left)
    ImageView imageLeft;
    @Bind(R.id.name_left)
    TextView nameLeft;
    @Bind(R.id.price_left)
    TextView priceLeft;
    @Bind(R.id.left_exchange)
    Button exchangeLeft;

    @Bind(R.id.image_view_right_top)
    ImageView imageRigetTop;
    @Bind(R.id.name_right_top)
    TextView nameRightTop;
    @Bind(R.id.price_right_top)
    TextView priceRightTop;
    @Bind(R.id.right_top_exchange)
    Button exchangeRightTop;

    @Bind(R.id.image_view_right_bottom)
    ImageView imageRigetBottom;
    @Bind(R.id.name_right_bottom)
    TextView nameRightBottom;
    @Bind(R.id.price_right_bottom)
    TextView priceRightBottom;
    @Bind(R.id.right_bottom_exchange)
    Button exchangeRightBottom;

    @Bind(R.id.left_linear)
    LinearLayout leftLinear;
    @Bind(R.id.right_relative_top)
    RelativeLayout rightRelativeTop;
    @Bind(R.id.right_relative_bottom)
    RelativeLayout rightRelativeBottom;

    private void addPrice(IntegralGoodsModel data, StringBuffer buffer, TextView textView) {
        int start;
        int end;
        if (!StringUtil.hasText(data.price) || "0".equals(data.price)) {
            textView.setText(buffer.toString());
        } else {
            buffer.append("+");
            start = buffer.length();
            buffer.append("￥");
            end = buffer.length();
            buffer.append(data.price == null ? 0 : data.price);//钱
            textView.setText(MyChange.ChangeTextColor(buffer.toString(), start, end, getResources().getColor(R.color.more_text)));
        }
    }

    //热销商品
    private void heat() {
        Map<String, Object> map = new HashMap<>();
        map.put("pageIndex", 1);
        ApiClient.send(activity, JConstant.FINDHOTGOODLIST, map, IntegralGoodsModel.getType(), new DataLoader<List<IntegralGoodsModel>>() {
            @Override
            public void task(String data) {

            }

            @Override
            public void succeed(final List<IntegralGoodsModel> data) {
                if (data == null) {
                    return;
                }

                integralGoodsModelList.addAll(data);
                StringBuffer buffer;
                for (int i = 0; i < data.size(); i++) {
                    final int finalI = i;
                    switch (i) {
                        case 0:
                            ApiClient.displayImage(data.get(i).thumb, imageLeft, R.mipmap.goods_top_defalut);
                            nameLeft.setText(data.get(i).title);
                            buffer = new StringBuffer();
                            buffer.append(data.get(i).integral);//积分
                            addPrice(data.get(i), buffer, priceLeft);
                            exchangeLeft.setVisibility(View.VISIBLE);
                            leftLinear.setOnClickListener(new View.OnClickListener() {//查看详情
                                @Override
                                public void onClick(View v) {
                                    //查看商品详情
                                    Intent intent = new Intent(activity, IntegralGoodsDetailActivity.class);
                                    intent.putExtra("gid", data.get(finalI).id);
                                    activity.startActivity(intent);
                                }
                            });
                            break;
                        case 1:
                            ApiClient.displayImage(data.get(i).thumb, imageRigetTop, R.mipmap.goods_top_defalut);
                            nameRightTop.setText(data.get(i).title);
                            buffer = new StringBuffer();
                            buffer.append(data.get(i).integral);//积分
                            addPrice(data.get(i), buffer, priceRightTop);
                            exchangeRightTop.setVisibility(View.VISIBLE);
                            rightRelativeTop.setOnClickListener(new View.OnClickListener() {//查看详情
                                @Override
                                public void onClick(View v) {
                                    //查看商品详情
                                    Intent intent = new Intent(activity, IntegralGoodsDetailActivity.class);
                                    intent.putExtra("gid", data.get(finalI).id);
                                    activity.startActivity(intent);
                                }
                            });
                            break;
                        case 2:
                            ApiClient.displayImage(data.get(i).thumb, imageRigetBottom, R.mipmap.goods_top_defalut);
                            nameRightBottom.setText(data.get(i).title);
                            buffer = new StringBuffer();
                            buffer.append(data.get(i).integral);//积分
                            addPrice(data.get(i), buffer, priceRightBottom);
                            exchangeRightBottom.setVisibility(View.VISIBLE);
                            rightRelativeBottom.setOnClickListener(new View.OnClickListener() {//查看详情
                                @Override
                                public void onClick(View v) {
                                    //查看商品详情
                                    Intent intent = new Intent(activity, IntegralGoodsDetailActivity.class);
                                    intent.putExtra("gid", data.get(finalI).id);
                                    activity.startActivity(intent);
                                }
                            });
                            break;
                    }
                }

            }

            @Override
            public void error(String message) {

            }
        }, JConstant.FINDHOTGOODLIST);
    }

    @OnClick({R.id.right_top_exchange, R.id.right_bottom_exchange, R.id.left_exchange})
    public void onRelative(View view) {
        switch (view.getId()) {
            case R.id.left_exchange://0
                sendRlative(0);
                break;
            case R.id.right_top_exchange://1
                sendRlative(1);
                break;
            case R.id.right_bottom_exchange://2
                sendRlative(2);
                break;
        }
    }

    //立即兑换
    private void sendRlative(int type) {

        if (!application.isLogin()) {
            toast("请先登录");
            Intent intent = new Intent(activity, SignInActivity.class);
            startActivity(intent);
            return;
        }

        Intent intent = new Intent(activity, CommonActivity.class);
        intent.putExtra(JConstant.EXTRA_INDEX, CommonActivity.INTEGRALDETAIL);
        intent.putExtra("type", false);
        List<CartGoods> cartGoodses = new ArrayList<>();
        CartGoods cartGoods = new CartGoods();
        cartGoods.thumb = integralGoodsModelList.get(type).thumb;
        cartGoods.title = integralGoodsModelList.get(type).title;
        cartGoods.goodsId = integralGoodsModelList.get(type).id;
        cartGoods.integral = integralGoodsModelList.get(type).integral;
        cartGoods.price = StringUtil.hasText(integralGoodsModelList.get(type).price) ? integralGoodsModelList.get(type).price : "0";
        cartGoods.number = "1";
        cartGoodses.add(cartGoods);
        intent.putExtra("goodsList", (Serializable) cartGoodses);
        startActivity(intent);
    }

    //分类
    private void getType() {
        ApiClient.send(activity, JConstant.FINDMENUINTEGRAL, null, Key.getType(), new DataLoader<List<Key>>() {
            @Override
            public void task(String data) {
            }

            @Override
            public void succeed(List<Key> data) {
                if (data != null && data.size() > 0) {
                    Gridviewaddapter = new CommonAdapter<Key>(activity, data, R.layout.gridview_home_topmenu_item) {
                        @Override
                        public void convert(ViewHolder helper, final Key item) {
                            helper.setText(R.id.name, item.name);
                            ImageView menuImage = helper.getView(R.id.image);
                            ApiClient.displayImageNoCache(item.icon, menuImage, R.mipmap.goods_type_ico);
                            helper.getConvertView().setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (anInterface != null) {
                                        anInterface.Onchange(-1, 0, item.id);
                                    }
                                }
                            });
                        }
                    };
                    GridViewtype.setAdapter(Gridviewaddapter);
                }
            }

            @Override
            public void error(String message) {

            }
        }, JConstant.FINDMENUINTEGRAL);
    }

    /**
     * 加载商品信息
     */
    private void loadingGoods() {
        Map<String, Object> map = new HashMap<>();
        map.put("pageIndex", page);
        ApiClient.send(activity, JConstant.FINDDATA_INTEGRAL, map, true, GoodType.getType(), new DataLoader<List<GoodType>>() {
            @Override
            public void task(String data) {

            }

            @Override
            public void succeed(List<GoodType> data) {
                RefreshComplete();
                if (data != null) {
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
                                    public void onClick(View v) {//更多
                                        if (anInterface != null) {
                                            anInterface.Onchange(-1, 0, item.id);
                                        }
                                    }
                                });
                                RecyclerView gv = helper.getView(R.id.grid_view);
                                if (item.chilgoods != null) {
                                    gv.setLayoutManager(new GridLayoutManager(activity, 2));
                                    gv.addItemDecoration(new DividerGridItemDecoration(activity, GridLayoutManager.VERTICAL, 20, getResources().getColor(R.color.white)));
                                    //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
                                    gv.setHasFixedSize(true);
                                    CommonRecyclerAdapter adapter = new CommonRecyclerAdapter<Good>(activity, item.chilgoods, R.layout.integral_goodes_iteam) {
                                        @Override
                                        public void convert(ViewRecyclerHolder helper, final Good item) {
                                            ImageView iv = helper.getView(R.id.image_view);
                                            ApiClient.displayImage(item.thumb, iv);
                                            TextView tv = helper.getView(R.id.price);
                                            StringBuffer sb = new StringBuffer();
                                            int start;
                                            int end;
                                            sb.append(item.integral == null ? "0" : item.integral);
                                            if (!StringUtil.hasText(item.price.toString()) || "0".equals(item.price.toString())) {
                                                tv.setText(sb.toString());
                                            } else {
                                                sb.append("+");
                                                start = sb.length();
                                                sb.append("￥");
                                                end = sb.length();
                                                sb.append(item.price.toString());//钱
                                                tv.setText(MyChange.ChangeTextColor(sb.toString(), start, end, getResources().getColor(R.color.text_primary_gray)));
                                            }
                                            helper.setText(R.id.name, item.title);
                                            helper.getConvertView().setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    //查看商品详情
                                                    Intent intent = new Intent(activity, IntegralGoodsDetailActivity.class);
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
                    } else {
                        no_mall.setVisibility(View.VISIBLE);
                        pullListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                    }
                }
            }

            @Override
            public void error(String message) {
                RefreshComplete();
            }
        }, JConstant.FINDDATA_INTEGRAL);
    }

    private void RefreshComplete() {
        pullListView.onRefreshComplete();
    }

    @OnClick(R.id.back)
    public void onBack(View view) {
        finishActivity();
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ApiClient.cancelRequest(JConstant.FINDADVER_);
        ApiClient.cancelRequest(JConstant.FINDHOTGOODLIST);
        ApiClient.cancelRequest(JConstant.FINDDATA_INTEGRAL);
        ApiClient.cancelRequest(JConstant.FINDHOTGOODLIST);
        ApiClient.cancelRequest(JConstant.FINDMENUINTEGRAL);
    }
}
