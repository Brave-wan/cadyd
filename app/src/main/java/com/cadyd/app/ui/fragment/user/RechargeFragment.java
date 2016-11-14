package com.cadyd.app.ui.fragment.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import butterknife.Bind;
import butterknife.OnClick;

import com.cadyd.app.JConstant;
import com.cadyd.app.R;
import com.cadyd.app.adapter.OneTextAdapter;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.asynctask.DataLoader;
import com.cadyd.app.handle.InterFace;
import com.cadyd.app.model.RechargeOrderModle;
import com.cadyd.app.ui.activity.CommonActivity;
import com.cadyd.app.ui.base.BaseFragement;
import com.cadyd.app.ui.view.ToastView;

import org.wcy.android.widget.NoScrollGridView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 充值金额显示界面
 **/
public class RechargeFragment extends BaseFragement {

    private int url;
    private String ecode;

    public static RechargeFragment newInstance(int type) {
        RechargeFragment newFragment = new RechargeFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        newFragment.setArguments(bundle);
        return newFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            int TYPE = args.getInt("type");
            switch (TYPE) {
                case 0://花币充值
                    url = JConstant.SAVEORDER_;
                    ecode = InterFace.OrderType.VIRTUAL.ecode;
                    break;
                case 1://余额充值
                    url = JConstant.SAVEBALANCEORDER_;
                    ecode = InterFace.OrderType.BALANCE.ecode;
                    break;
            }
        }
    }

    private OneTextAdapter adapter;
    private RechargeOrderModle modle;
    @Bind(R.id.recharge_grid)
    NoScrollGridView gridView;
    @Bind(R.id.send)
    Button send;

    private List<String> lists = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return setView(R.layout.recharge, "充值", true);
    }

    @Override
    protected void initView() {

        lists.add("其他金额");
        lists.add("50");
        lists.add("100");
        lists.add("200");
        lists.add("500");
        lists.add("1000");

        adapter = new OneTextAdapter(activity, lists);
        gridView.setAdapter(adapter);
    }

    @OnClick(R.id.send)
    public void onSend(View view) {

        if ("-1".equals(adapter.getNumber())) {
            ToastView.show(activity, "请选择金额");
            return;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("amount", adapter.getNumber());
        ApiClient.send(activity, url, map, true, RechargeOrderModle.class, new DataLoader<RechargeOrderModle>() {

            @Override
            public void task(String data) {

            }

            @Override
            public void succeed(RechargeOrderModle data) {
                if (data == null) {
                    toast("生成订单失败");
                } else {
                    modle = data;
                    Intent intent = new Intent(activity, CommonActivity.class);
                    intent.putExtra(JConstant.EXTRA_INDEX, CommonActivity.GOODS_PAY);
                    intent.putExtra("orderid", data.id);
                    intent.putExtra("money", data.amount);
                    intent.putExtra("orderType", ecode);
                    startActivityForResult(intent, 1);
                }
            }

            @Override
            public void error(String message) {

            }
        }, url);
    }

    @Override
    public boolean onBackPressed() {
        finishFramager();
        return false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ApiClient.cancelRequest(JConstant.SAVEORDER_);
        ApiClient.cancelRequest(JConstant.SAVEBALANCEORDER_);
    }
}
