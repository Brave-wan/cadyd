package com.cadyd.app.ui.fragment.pay;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.cadyd.app.JConstant;
import com.cadyd.app.MyApplication;
import com.cadyd.app.R;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.asynctask.DataLoader;
import com.cadyd.app.asynctask.retrofitmode.BaseRequestInfo;
import com.cadyd.app.asynctask.retrofitmode.HttpMethods;
import com.cadyd.app.asynctask.retrofitmode.ProgressSubscriber;
import com.cadyd.app.asynctask.retrofitmode.SubscriberOnNextListener;
import com.cadyd.app.model.EndMoneylListData;
import com.cadyd.app.model.EndMoneylListModle;
import com.cadyd.app.model.RechargeRecordModel;
import com.cadyd.app.model.SpendMoneyModel;
import com.cadyd.app.model.SpendMoneylListData;
import com.cadyd.app.model.SpendMoneylListModle;
import com.cadyd.app.ui.base.BaseActivity;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.wcy.android.utils.adapter.CommonAdapter;
import org.wcy.android.utils.adapter.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import okhttp3.RequestBody;

/**
 * 花币充值记录
 */

public class FlowerCurrencyRechargeRecordsActivity extends BaseActivity {

    View empty_layout;
    @Bind(R.id.pull_to_refresh_list_view)
    PullToRefreshListView pullToRefreshListView;
    @Bind(R.id.activity_flower_currency_recharge_records)
    LinearLayout activityFlowerCurrencyRechargeRecords;

    private int index = 1;
    private CommonAdapter<RechargeRecordModel.DataBean> endAdapter;

    private List<RechargeRecordModel.DataBean> beanList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_flower_currency_recharge_records, "充值记录", true);
        init();
    }

    private void init() {
        empty_layout = findViewById(R.id.empty_layout);
        pullToRefreshListView.setEmptyView(empty_layout);
        pullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        pullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                index = 1;
                initEndHttp();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                index++;
                initEndHttp();
            }
        });

        endAdapter = new CommonAdapter<RechargeRecordModel.DataBean>(activity, beanList, R.layout.flower_currency_recharge_records_item) {
            @Override
            public void convert(ViewHolder helper, RechargeRecordModel.DataBean item) {
                helper.setText(R.id.money, item.getAmount());
                helper.setText(R.id.flower, item.getCount() + "");
                helper.setText(R.id.time, item.getCreateTime() + "");
            }
        };
        pullToRefreshListView.setAdapter(endAdapter);

        initEndHttp();
    }

    //花币充值记录查询
    private void initEndHttp() {
        Map<Object, Object> map = new HashMap<>();
        map.put("pageIndex", index);
        map.put("pageSize", 10);
        MyApplication application = (MyApplication) getApplication();
        map.put("userId", application.model.id);
        RequestBody body = ApiClient.getRequestBody(map);
        HttpMethods.getInstance().getRechargeRecord(new ProgressSubscriber<BaseRequestInfo<RechargeRecordModel>>(new SubscriberOnNextListener<BaseRequestInfo<RechargeRecordModel>>() {
            @Override
            public void onNext(BaseRequestInfo<RechargeRecordModel> o) {
                pullToRefreshListView.onRefreshComplete();
                if (o.getData().getData().size() > 0) {
                    pullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
                } else {
                    pullToRefreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                }
                if (index == 1) {
                    beanList.clear();
                }
                beanList.addAll(o.getData().getData());
                endAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable e) {
                pullToRefreshListView.onRefreshComplete();
            }

            @Override
            public void onLogicError(String msg) {

            }
        }, activity), body);
    }

}
