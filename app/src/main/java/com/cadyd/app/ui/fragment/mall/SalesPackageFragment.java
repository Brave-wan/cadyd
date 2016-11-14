package com.cadyd.app.ui.fragment.mall;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.Bind;
import com.cadyd.app.JConstant;
import com.cadyd.app.R;
import com.cadyd.app.adapter.SalesPackageAdapter;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.asynctask.DataLoader;
import com.cadyd.app.interfaces.TowObjectParameterInterface;
import com.cadyd.app.model.Good;
import com.cadyd.app.model.Sales;
import com.cadyd.app.model.SalesPackage;
import com.cadyd.app.ui.activity.CommonActivity;
import com.cadyd.app.ui.activity.MainActivity;
import com.cadyd.app.ui.activity.SignInActivity;
import com.cadyd.app.ui.base.BaseFragement;
import com.cadyd.app.ui.view.DividerListItemDecoration;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class SalesPackageFragment extends BaseFragement {
    @Bind(R.id.list_view)
    RecyclerView recyclerView;
    private Sales sales;

    public static SalesPackageFragment newInstance(Sales sales) {
        SalesPackageFragment newFragment = new SalesPackageFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("sales", sales);
        newFragment.setArguments(bundle);
        return newFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            this.sales = (Sales) args.getSerializable("sales");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return setView(R.layout.fragment_sales_package, "优惠套装", true);
    }

    @Override
    protected void initView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.addItemDecoration(new DividerListItemDecoration(activity, LinearLayoutManager.HORIZONTAL, 20, getResources().getColor(R.color.background)));
        SalesPackageAdapter adapter = new SalesPackageAdapter(sales.mallpackagelist, activity);
        adapter.setClick(new TowObjectParameterInterface() {
            @Override
            public void Onchange(int type, int postion, Object object) {
                if (application.isLogin()) {
                    addCart((SalesPackage) object);
                } else {
                    startActivity(SignInActivity.class, false);
                }
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void addCart(SalesPackage data) {
        Map<String, Object> map = new HashMap<>();
        map.put("goodsidPkid", data.tid);
        ApiClient.send(activity, JConstant.SAVE_SUMMARY_CART, map, true, null, new DataLoader<Object>() {

            @Override
            public void task(String data) {

            }

            @Override
            public void succeed(Object data) {
                toastSuccess("加入购物车成功");
            }

            @Override
            public void error(String message) {

            }
        }, JConstant.SAVE_SUMMARY_CART);
    }

    @Override
    public boolean onBackPressed() {
        finishFramager();
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ApiClient.cancelRequest(JConstant.SAVE_SUMMARY_CART);
    }
}
