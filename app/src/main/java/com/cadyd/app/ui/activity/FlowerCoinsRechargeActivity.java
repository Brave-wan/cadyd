package com.cadyd.app.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;

import com.cadyd.app.JConstant;
import com.cadyd.app.R;
import com.cadyd.app.adapter.OneTextAdapter;
import com.cadyd.app.ui.activity.CommonActivity;
import com.cadyd.app.ui.base.BaseActivity;

import org.wcy.android.widget.NoScrollGridView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 花币充值
 * 充值金额显示界面
 **/
public class FlowerCoinsRechargeActivity extends BaseActivity {

    private OneTextAdapter adapter;
    @Bind(R.id.recharge_grid)
    NoScrollGridView gridView;
    @Bind(R.id.send)
    Button send;
    private List<String> lists = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.recharge, "充值", true);
        initView();
    }

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
        Intent intent = new Intent(activity, CommonActivity.class);
        intent.putExtra(JConstant.EXTRA_INDEX, CommonActivity.NEWFLOWERCOINSRECHARGE);
        intent.putExtra("money", adapter.getNumber());
        startActivity(intent);
    }
}
