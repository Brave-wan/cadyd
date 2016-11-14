package com.cadyd.app.ui.fragment.shop;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.Bind;
import com.cadyd.app.JConstant;
import com.cadyd.app.R;
import com.cadyd.app.adapter.ShopCouponListAdapter;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.asynctask.DataLoader;
import com.cadyd.app.comm.Utils;
import com.cadyd.app.interfaces.OnItemClickListener;
import com.cadyd.app.model.CouponInfo;
import com.cadyd.app.ui.base.BaseFragement;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by SCH-1 on 2016/8/8.
 * 店铺促销
 */
public class ShopCouponListFragment extends BaseFragement implements OnItemClickListener<CouponInfo> {
    @Bind(R.id.shop_oupon_list)
    RecyclerView ouponList;

    private List<CouponInfo> datas;
    private ShopCouponListAdapter adapter;

    private int pageNo = 1;
    private int pageSize = 10;

    private String shopId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return setView(inflater, R.layout.shop_oupon_list_layout);
    }

    @Override
    public boolean onBackPressed() {
        finishActivity();
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        pageNo = 1;
        loadDatas();
    }

    @Override
    protected void initView() {
        ouponList.setHasFixedSize(true);
        ouponList.setLayoutManager(new LinearLayoutManager(activity));

        ouponList.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(activity)
                        .colorResId(R.color.gray)
                        .size(1)
                        .margin(3)
                        .build());

        ouponList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (Utils.isSlideToBottom(recyclerView)) {
                    loadDatas();
                }
            }
        });

        datas = new ArrayList<>();
        adapter = new ShopCouponListAdapter(datas);
        adapter.setListener(this);
        ouponList.setAdapter(adapter);
    }

    private void loadDatas() {
        Map<String, Object> param = new HashMap<>();
        param.put("pageIndex", pageNo);
        param.put("pageSize", pageSize);
        param.put("merchantId", shopId);
        ApiClient.send(activity, JConstant.INQUIRSHOPPREFER, param, CouponInfo.getType(), new DataLoader<List<CouponInfo>>() {
            @Override
            public void task(String data) {

            }

            @Override
            public void succeed(List<CouponInfo> data) {
                if (data != null && data.size() > 0) {
                    datas.addAll(data);
                    adapter.notifyDataSetChanged();
                    pageNo += 1;
                }
            }

            @Override
            public void error(String message) {

            }
        }, JConstant.INQUIRSHOPPREFER);
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    @Override
    public void onItemClick(CouponInfo data, View view) {
        Map<String, Object> map = new HashMap<>();
        map.put("preferid", data.getId());
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
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ApiClient.cancelRequest(JConstant.PREFER_RECEIVE_);
        ApiClient.cancelRequest(JConstant.INQUIRSHOPPREFER);
    }
}
