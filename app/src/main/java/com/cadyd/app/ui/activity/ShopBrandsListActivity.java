package com.cadyd.app.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Bind;
import com.cadyd.app.JConstant;
import com.cadyd.app.R;
import com.cadyd.app.adapter.ShopBrandsListAdapter;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.asynctask.DataLoader;
import com.cadyd.app.interfaces.OnItemClickListener;
import com.cadyd.app.model.ShopBrandsEntry;
import com.cadyd.app.model.ShopEntry;
import com.cadyd.app.ui.base.BaseActivity;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by SCH-1 on 2016/8/2.
 */
public class ShopBrandsListActivity extends BaseActivity implements View.OnClickListener, OnItemClickListener<ShopBrandsEntry> {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.title)
    TextView titleView;
    @Bind(R.id.shop_brands_list)
    RecyclerView listView;
    @Bind(R.id.shop_brands_list_item_all)
    LinearLayout btnSearchAll;

    private ShopEntry shopEntry;
    private ShopBrandsListAdapter adapter;
    private List<ShopBrandsEntry> datas;
    private int pageNo = 1;
    private int pageSize = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop_brands_list_layout);
        shopEntry = (ShopEntry) getIntent().getSerializableExtra("shopEntry");
        initView();

        initData();
    }

    private void initData() {
        datas = new ArrayList<>();
        adapter = new ShopBrandsListAdapter(datas);
        adapter.setListener(this);
        listView.setAdapter(adapter);
        Map<String, Object> params = new HashMap<>();
        params.put("shopId", shopEntry.getId());
        params.put("pageIndex", pageNo);
        params.put("pageSize", pageSize);

        ApiClient.send(this, JConstant.SHOP_BRANDS_CATEGORY, params, ShopBrandsEntry.getType(), new DataLoader<List<ShopBrandsEntry>>() {

            @Override
            public void task(String data) {

            }

            @Override
            public void succeed(List<ShopBrandsEntry> data) {
                if (data != null && data.size() > 0) {
                    datas.addAll(data);
                    adapter.notifyDataSetChanged();
                    pageNo += 1;
                }
            }

            @Override
            public void error(String message) {

            }
        }, JConstant.SHOP_BRANDS_CATEGORY);
    }

    private void initView() {
        titleView.setText(getString(R.string.shop_brands));

        listView.setHasFixedSize(true);
        listView.setLayoutManager(new LinearLayoutManager(this));
        listView.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(this)
                        .colorResId(R.color.gray)
                        .size(1)
                        .margin(18)
                        .build());

        back.setOnClickListener(this);
        btnSearchAll.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finishActivity();
                break;
            case R.id.shop_brands_list_item_all:
                Intent intent = new Intent(this, ShopHomeActivity.class);
                intent.putExtra("type", 2);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishActivity();
    }

    @Override
    public void onItemClick(ShopBrandsEntry data, View view) {
        Intent intent = new Intent(this, ShopHomeActivity.class);
        intent.putExtra("id", data.getBrandId());
        intent.putExtra("type", 2);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ApiClient.cancelRequest(JConstant.SHOP_BRANDS_CATEGORY);
    }
}
