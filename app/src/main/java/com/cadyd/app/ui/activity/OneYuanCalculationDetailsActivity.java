package com.cadyd.app.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.cadyd.app.JConstant;
import com.cadyd.app.R;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.asynctask.DataLoader;
import com.cadyd.app.model.OneYuanCalcultionDetailModel;
import com.cadyd.app.ui.base.BaseActivity;

import org.wcy.android.utils.adapter.CommonAdapter;
import org.wcy.android.utils.adapter.ViewHolder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OneYuanCalculationDetailsActivity extends BaseActivity {
    private String id;
    private OneYuanCalcultionDetailModel detailModel;
    private CommonAdapter<OneYuanCalcultionDetailModel.RecordDataBean> adapter;
    @Bind(R.id.a_number)
    TextView a_number;
    @Bind(R.id.b_number)
    TextView b_number;
    @Bind(R.id.recycler_view)
    ListView recycler_view;
    @Bind(R.id.number)
    TextView number;
    @Bind(R.id.text_oo)
    TextView text_oo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_one_yuan_calculation_details, "计算详情", true);
        init();
    }

    private void init() {
        id = getIntent().getStringExtra("id");
        text_oo.setTag(false);
        getContent();
        onCheck();
    }

    private void getContent() {
        Map<String, Object> map = new HashMap<>();
        map.put("tbid", id);
        int tag = JConstant.CALCULATIONDETAI;
        ApiClient.send(activity, JConstant.CALCULATIONDETAI, map, OneYuanCalcultionDetailModel.class, new DataLoader<OneYuanCalcultionDetailModel>() {
            @Override
            public void task(String data) {

            }

            @Override
            public void succeed(OneYuanCalcultionDetailModel data) {
                if (data != null) {
                    detailModel = data;
                    setContent();
                }
            }

            @Override
            public void error(String message) {

            }
        }, tag);
    }

    private void setContent() {
        if (detailModel.getLotteryDetail() != null) {
            a_number.setText("=" + detailModel.getLotteryDetail().getTimesum());
            b_number.setText("=" + detailModel.getLotteryDetail().getLotterycode() + "(第" + detailModel.getLotteryDetail().getLotteryperiods() + "期)");
            number.setText("幸运号码:" + detailModel.getLotteryDetail().getLuckcode());
        }
        if (detailModel.getRecordData() != null) {
            List<OneYuanCalcultionDetailModel.RecordDataBean> recordDataBeans = detailModel.getRecordData();
            recordDataBeans.add(0, null);
            Log.i("xiongmao", "當前樂購碼總條數" + recordDataBeans.size());
            adapter = new CommonAdapter<OneYuanCalcultionDetailModel.RecordDataBean>(activity, recordDataBeans, R.layout.one_yuan_calculation_detail_item) {
                @Override
                public void convert(ViewHolder helper, OneYuanCalcultionDetailModel.RecordDataBean item) {
                    if (item != null) {
                        helper.setText(R.id.time, item.getCreatetime());
                        helper.setText(R.id.number, " -> " + item.getTimevalue());
                        helper.setText(R.id.phone, item.getNickname());
                    }
                }
            };
            recycler_view.setAdapter(adapter);
        }
    }

    private void onCheck() {
        text_oo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((boolean) v.getTag()) {
                    v.setTag(false);
                    text_oo.setText("展开");
                    text_oo.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.arrow_down_read, 0, 0, 0);
                    recycler_view.setVisibility(View.GONE);
                } else {
                    v.setTag(true);
                    text_oo.setText("关闭");
                    text_oo.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.arrow_up_read, 0, 0, 0);
                    recycler_view.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    /**
     * 开奖查询
     *
     * @param view
     */
    @OnClick(R.id.look)
    public void onLook(View view) {
        if (detailModel != null && detailModel.getLotteryDetail() != null && detailModel.getLotteryDetail().getLotterycode() != null) {
            Intent intent = new Intent(this, CommWebViewActivity.class);
            intent.putExtra("url", "http://caipiao.163.com/award/cqssc/" + detailModel.getLotteryDetail().getLotterycode() + ".html");
            startActivity(intent);
        }
    }

    @Override
    protected void onDestroy() {
        ApiClient.cancelRequest(JConstant.CALCULATIONDETAI);
        super.onDestroy();
    }
}
