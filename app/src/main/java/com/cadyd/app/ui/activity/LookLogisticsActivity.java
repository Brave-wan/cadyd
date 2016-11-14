package com.cadyd.app.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.Bind;
import com.cadyd.app.JConstant;
import com.cadyd.app.R;
import com.cadyd.app.adapter.LookLogisticsListAdapter;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.asynctask.DataLoader;
import com.cadyd.app.model.ExpressInfo;
import com.cadyd.app.model.ExpressPathInfo;
import com.cadyd.app.ui.base.BaseActivity;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by SCH-1 on 2016/8/9.
 */
public class LookLogisticsActivity extends BaseActivity {

    @Bind(R.id.look_logistics_expree_name)
    TextView expreeName;

    @Bind(R.id.look_logistics_orderno)
    TextView orderNo;

    @Bind(R.id.look_logistics_list)
    ListView recyclerView;

    private ExpressInfo info;
    private List<ExpressPathInfo> paths;
    private LookLogisticsListAdapter adapter;
    private String expName;

    private String expNo = "210001633605";
    private String expCode = "ANE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.look_logistics_layout, getResources().getString(R.string.look_logistics_title), true);

        expNo = getIntent().getStringExtra("expNo");
        expCode = getIntent().getStringExtra("expCode");

        initView();
        loadData();
    }

    private void loadData() {
        Map<String, Object> param = new HashMap<>();
        param.put("expNo", expNo);
        param.put("expCode", expCode);

        ApiClient.send(this, JConstant.LOOK_LOGISTICS, param, true, String.class, new DataLoader<String>() {
            @Override
            public void task(String data) {

            }

            @Override
            public void succeed(String data) {
                if (TextUtils.isEmpty(data)) {
                    return;
                }

                try {
                    JSONObject obj = new JSONObject(data);
                    //  obj = obj.getJSONObject("data");
                    expName = obj.optString("expName");

                    String expressStr = obj.optString("express");
                    JSONObject express = new JSONObject(expressStr);
                    if (express != null) {
                        info = ApiClient.getGson().fromJson(express.toString(), ExpressInfo.class);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                refreshView();
            }

            @Override
            public void error(String message) {

            }
        }, JConstant.LOOK_LOGISTICS);
    }

    private void refreshView() {
        if (!TextUtils.isEmpty(expName)) {
            expreeName.setText(expName);
        }
        if (info != null) {
            orderNo.setText(info.getLogisticCode());

            List<ExpressPathInfo> temp = info.getTraces();
            if (temp != null && temp.size() > 0) {
                paths.addAll(temp);
                adapter.notifyDataSetChanged();
            }
        }
    }

    private void initView() {
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        paths = new ArrayList<>();
        adapter = new LookLogisticsListAdapter(paths);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ApiClient.cancelRequest(JConstant.LOOK_LOGISTICS);
    }
}
