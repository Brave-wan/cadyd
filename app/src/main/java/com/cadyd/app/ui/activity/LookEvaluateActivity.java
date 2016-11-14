package com.cadyd.app.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.cadyd.app.JConstant;
import com.cadyd.app.R;
import com.cadyd.app.adapter.LookEvaluateRecyclerAdapter;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.asynctask.DataLoader;
import com.cadyd.app.model.LookEvaluateModel;
import com.cadyd.app.ui.base.BaseActivity;

import java.util.HashMap;
import java.util.Map;


public class LookEvaluateActivity extends BaseActivity {

    RecyclerView recyclerView;
    private String id;
    private LookEvaluateModel model;
    private LookEvaluateRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_look_evaluate, "我的晒单", true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        id = getIntent().getStringExtra("orderid");
        getGoodsList();
    }

    /**
     * 获取商品信息
     */
    private void getGoodsList() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        ApiClient.send(this, JConstant.LOOKEVALUATE, map, LookEvaluateModel.class, new DataLoader<LookEvaluateModel>() {
            @Override
            public void task(String data) {

            }

            @Override
            public void succeed(LookEvaluateModel data) {
                if (data != null) {
                    model = data;
                    setAdapter();
                }
            }

            @Override
            public void error(String message) {

            }
        }, JConstant.LOOKEVALUATE);
    }

    private void setAdapter() {
        model.itme.add(new LookEvaluateModel.ItmeBean());
        model.itme.add(0, model.itme.get(1) == null ? new LookEvaluateModel.ItmeBean() : model.itme.get(1));
        adapter = new LookEvaluateRecyclerAdapter(this, model);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
