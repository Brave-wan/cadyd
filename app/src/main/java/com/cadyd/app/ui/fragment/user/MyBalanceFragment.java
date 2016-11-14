package com.cadyd.app.ui.fragment.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cadyd.app.JConstant;
import com.cadyd.app.R;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.asynctask.DataLoader;
import com.cadyd.app.model.UserData;
import com.cadyd.app.ui.activity.FlowerCoinsRechargeActivity;
import com.cadyd.app.ui.base.BaseFragement;
import com.cadyd.app.ui.fragment.user.integral.IntegralListFragment;

import org.wcy.common.utils.StringUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 我的余额
 */
public class MyBalanceFragment extends BaseFragement {

    private UserData userData;

    @Bind(R.id.balance_number)
    TextView number;

    @Bind(R.id.balance_image)
    ImageView imageView;

    @Bind(R.id.balance_relative)
    RelativeLayout relativeLayout;

    //提现
    @Bind(R.id.balance_cash)
    Button cash;

    //充值
    @Bind(R.id.balance_recharge)
    Button recharge;

    //充值2
    @Bind(R.id.balance_recharge2)
    Button recharge2;

    @Bind(R.id.balance_title)
    TextView balance_title;

    private int TYPE = 0;

    public static MyBalanceFragment newInstance(int type) {
        MyBalanceFragment newFragment = new MyBalanceFragment();
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
            this.TYPE = args.getInt("type");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        String titleStr = null;
        String rightStr = null;

        switch (TYPE) {
            case 0:
                titleStr = "我的余额";
                rightStr = "余额明细";
                break;
            case 1:
                titleStr = "我的积分";
                rightStr = "积分明细";
                break;
            case 2:
                titleStr = "我的花币";
                rightStr = "花币明细";
                break;
        }

        return setView(R.layout.fragment_my_balance, titleStr, true, rightStr);
    }

    @Override
    protected void initView() {
        cash.setBackgroundResource(R.drawable.round_gray_untransparent);
        recharge.setBackgroundResource(R.drawable.round_gray_untransparent);
        //明细
        layout.rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(R.id.common_frame, IntegralListFragment.newInstance(TYPE));
            }
        });

        switch (TYPE) {
            case 0:
                imageView.setBackgroundResource(R.mipmap.round_money);
                balance_title.setText("我的余额");
                relativeLayout.setVisibility(View.VISIBLE);
                break;
            case 1:
                imageView.setBackgroundResource(R.mipmap.too_money);
                balance_title.setText("我的积分");
                break;
            case 2:
                imageView.setBackgroundResource(R.mipmap.round_money);
                balance_title.setText("我的花币");
                recharge2.setVisibility(View.VISIBLE);
                break;
        }
        initHttp();
    }

    @OnClick(R.id.balance_cash)
    public void onChsh(View view) {//提现

    }


    //余额
    @OnClick(R.id.balance_recharge)
    public void onRecharge(View view) {
        replaceFragment(R.id.common_frame, RechargeFragment.newInstance(1));
    }

    //花币
    @OnClick(R.id.balance_recharge2)
    public void onRecharge2(View view) {
        //  replaceFragment(R.id.common_frame, RechargeFragment.newInstance(0));
        Intent intent = new Intent(activity, FlowerCoinsRechargeActivity.class);
        startActivity(intent);
    }


    private void initHttp() {
        ApiClient.send(activity, JConstant.SELECTDETAILBYMID_, null, UserData.getType(), new DataLoader<List<UserData>>() {
            @Override
            public void task(String data) {

            }

            @Override
            public void succeed(List<UserData> data) {
                if (data.size() > 0) {
                    userData = data.get(0);
                    switch (TYPE) {
                        case 0:
                            number.setText("￥" + (StringUtil.hasText(userData.balance) ? userData.balance : "0"));
                            break;
                        case 1:
                            number.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.penny, 0, 0, 0);
                            number.setText(StringUtil.hasText(userData.integration) ? userData.integration : "0");
                            break;
                        case 2:
                            number.setText(StringUtil.hasText(userData.current) ? userData.current : "0");
                            break;
                    }
                }
            }

            @Override
            public void error(String message) {

            }
        }, JConstant.SELECTDETAILBYMID_);
    }

    @Override
    public boolean onBackPressed() {
        finishFramager();
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ApiClient.cancelRequest(JConstant.SELECTDETAILBYMID_);
    }
}
