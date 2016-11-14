package com.cadyd.app.ui.fragment.shop;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cadyd.app.JConstant;
import com.cadyd.app.R;
import com.cadyd.app.adapter.confirmCart.StoreAdapter;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.asynctask.DataLoader;
import com.cadyd.app.interfaces.TowObjectParameterInterface;
import com.cadyd.app.model.BrandAndAdvertisement;
import com.cadyd.app.model.GoodType;
import com.cadyd.app.model.LeaguePreferPage;
import com.cadyd.app.ui.activity.GoodsDetailActivity;
import com.cadyd.app.ui.base.BaseFragement;
import com.cadyd.app.utils.EmptyRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

/**
 * zjh
 * 旗舰店-店铺首页
 */
public class StoreHomeFragment extends BaseFragement {

    private TowObjectParameterInterface towObjectParameterInterface;

    private int index = 1;
    private String shopId = "";
    @Bind(R.id.recyclerView)
    EmptyRecyclerView recyclerView;
    private StoreAdapter adapter;
    @Bind(R.id.listview_empty)
    View EmptyView;

    private List<GoodType> dataList = new ArrayList<>();
    private BrandAndAdvertisement brandAndAdvertisement;

    public static StoreHomeFragment newFragment(String shopId) {
        StoreHomeFragment fragment = new StoreHomeFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id", shopId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            shopId = bundle.getString("id");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return setView(inflater, R.layout.fragment_store_home);
    }

    //设置监听
    public void setClick(TowObjectParameterInterface towObjectParameterInterface) {
        this.towObjectParameterInterface = towObjectParameterInterface;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null) {
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    protected void initView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setEmptyView(EmptyView);
        getHand();
    }

    //获取顶部图片信息
    public void getHand() {
        Map<String, Object> map = new HashMap<>();
        map.put("shopId", shopId);
        ApiClient.send(baseContext, JConstant.QUERYBRABDABDADVERTISENEBT, map, BrandAndAdvertisement.class, new DataLoader<BrandAndAdvertisement>() {
            @Override
            public void task(String data) {

            }

            @Override
            public void succeed(BrandAndAdvertisement data) {
                brandAndAdvertisement = data;
                getContent();
            }

            @Override
            public void error(String message) {
                getContent();
            }
        }, JConstant.QUERYBRABDABDADVERTISENEBT);
    }


    //获取商品信息
    private class goodsType {
        List<GoodType> data;
    }

    public void getContent() {
        if (dataList.size() > 1) {
            dataList.remove(0);
            dataList.remove(dataList.size() - 1);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("pageIndex", index);
        map.put("shopId", shopId);
        ApiClient.send(baseContext, JConstant.QUERYGOODSGROUP, map, goodsType.class, new DataLoader<goodsType>() {
                    @Override
                    public void task(String data) {

                    }

                    @Override
                    public void succeed(goodsType data) {
                        if (index == 1) {
                            dataList.clear();
                        }
                        if (data == null || data.data == null || data.data.size() <= 0) {
                            if (getMall != null) {
                                getMall.setText("没有更多商品了");
                                getMall.setEnabled(false);
                            }
                        }
                        dataList.addAll(data.data);
                        setMyAdapter();

                    }

                    @Override
                    public void error(String message) {

                    }
                }

                , JConstant.QUERYGOODSGROUP);

    }

    TextView getMall;//获得更多商品按钮

    //设置界面
    public void setMyAdapter() {
        dataList.add(0, null);
        dataList.add(null);
        if (adapter == null) {
            adapter = new StoreAdapter(activity, dataList, brandAndAdvertisement, leaguePreferPage);
            adapter.setClick(new TowObjectParameterInterface() {
                @Override
                public void Onchange(int type, int postion, Object object) {
                    switch (type) {
                        case 0: //TODO 加载更多商品
                            if (getMall == null) {
                                getMall = (TextView) object;
                            }
                            index++;
                            getContent();
                            break;
                        case 1: //TODO 查看更多分类商品
                            if (towObjectParameterInterface != null) {
                                towObjectParameterInterface.Onchange(0, 0, object);
                            }
                            break;
                        case 2://TODO 查看商品的详情
                            String id = (String) object;
                            Intent intent = new Intent(activity, GoodsDetailActivity.class);
                            intent.putExtra("gid", id);
                            activity.startActivity(intent);
                            break;
                        case 3://TODO 查询品牌商品
                            if (towObjectParameterInterface != null) {
                                towObjectParameterInterface.Onchange(1, 0, object);
                            }
                            break;
                        case 4://TODO 领取优惠券
                            Map<String, Object> map = new HashMap<>();
                            map.put("preferid", (String) object);
                            map.put("merchanid", shopId);
                            ApiClient.send(activity, JConstant.PREFER_RECEIVE_, map, true, null, new DataLoader<Object>() {
                                @Override
                                public void task(String data) {

                                }

                                @Override
                                public void succeed(Object data) {
                                    toastSuccess("领取成功");
                                }

                                @Override
                                public void error(String message) {

                                }
                            }, JConstant.PREFER_RECEIVE_);
                            break;
                    }
                }
            });
            recyclerView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    private LeaguePreferPage leaguePreferPage;

    /**
     * 获取店铺的优惠券
     */
    public void getCoupon(String merchantId) {
        Map<String, Object> map = new HashMap<>();
        map.put("pageIndex", 1);
        map.put("pageSize", 2);
        map.put("merchantId", merchantId);
        ApiClient.send(baseContext, JConstant.INQUIRSHOPPREFER, map, LeaguePreferPage.class, new DataLoader<LeaguePreferPage>() {
            @Override
            public void task(String data) {

            }

            @Override
            public void succeed(LeaguePreferPage data) {
                leaguePreferPage = data;
                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void error(String message) {

            }
        }, JConstant.INQUIRSHOPPREFER);
    }


    @Override
    public boolean onBackPressed() {
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ApiClient.cancelRequest(JConstant.QUERYGOODSGROUP);
        ApiClient.cancelRequest(JConstant.QUERYBRABDABDADVERTISENEBT);
        ApiClient.cancelRequest(JConstant.PREFER_RECEIVE_);
        ApiClient.cancelRequest(JConstant.INQUIRSHOPPREFER);
    }
}
