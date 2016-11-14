package com.cadyd.app.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.cadyd.app.JConstant;
import com.cadyd.app.R;
import com.cadyd.app.adapter.SpendMoneyAdapter;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.asynctask.retrofitmode.BaseRequestInfo;
import com.cadyd.app.asynctask.retrofitmode.HttpMethods;
import com.cadyd.app.asynctask.retrofitmode.ProgressSubscriber;
import com.cadyd.app.asynctask.retrofitmode.SubscriberOnNextListener;
import com.cadyd.app.model.FlowerCoinsRechargeModel;
import com.cadyd.app.model.SpendMoneyModel;
import com.cadyd.app.ui.base.BaseActivity;
import com.cadyd.app.ui.fragment.pay.FlowerCurrencyRechargeRecordsActivity;
import com.cadyd.app.ui.fragment.pay.PaymentResultFragment;
import com.cadyd.app.ui.view.AutoLoadRecyclerView;
import com.cadyd.app.ui.view.DividerGridItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Alipay.PayResult;
import butterknife.Bind;
import okhttp3.RequestBody;

public class SpendMoneyActivity extends BaseActivity {

    private int rechargeProportion = 1;
    private String PAYTYPE = "";//Alipay支付宝支付  WeChat微信支付

    @Bind(R.id.Currency_balance)
    TextView CurrencyBalance;//花币余额
    @Bind(R.id.RadioGroup)
    RadioGroup radioGroup;
    @Bind(R.id.the_actual_amount)
    TextView theActualAmount;//充值的实价数量显示
    @Bind(R.id.enter_editText)
    EditText enterEditText;//用户输入的金额
    @Bind(R.id.send)
    TextView send;

    @Bind(R.id.spend_money_recycler)
    AutoLoadRecyclerView mRecycler;
    private LinearLayoutManager mLayoutManager;
    private SpendMoneyAdapter mSpendMoneyAdapter;

    private List<SpendMoneyModel.LiveAmountMoneyInfoListBean> listBean = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_spend_money, "我的花币", true, "充值记录");
        layout.rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, FlowerCurrencyRechargeRecordsActivity.class);
                startActivity(intent);
            }
        });
        initView();
    }

    private void initView() {
        mLayoutManager = new LinearLayoutManager(this);
        mRecycler.setLayoutManager(mLayoutManager);
        mRecycler.addItemDecoration(new DividerGridItemDecoration(activity, LinearLayoutManager.HORIZONTAL, 1, getResources().getColor(R.color.gray)));
        mSpendMoneyAdapter = new SpendMoneyAdapter(activity, listBean);
        mSpendMoneyAdapter.setOnItemClickListener(new SpendMoneyAdapter.OnItemClickListener() {
            @Override
            public void ItemClickListener(int position) {
                pay(Integer.valueOf(listBean.get(position).getAmountMoney()), listBean.get(position).getProductId(), true);
            }
        });
        mRecycler.setAdapter(mSpendMoneyAdapter);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.Alipay://支付宝
                        PAYTYPE = "Alipay";
                        break;
                    case R.id.WeChat://微信
                        PAYTYPE = "WeChat";
                        break;
                }
            }
        });
        /**
         * 用户输入监听
         */
        enterEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!enterEditText.getText().toString().equals("")) {
                    theActualAmount.setText(String.valueOf(Integer.valueOf(enterEditText.getText().toString()) * rechargeProportion));
                } else {
                    theActualAmount.setText("0");
                }
            }
        });
        /**
         * 确认充值
         */
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ("".equals(enterEditText.getText().toString()) || "0".equals(enterEditText.getText().toString()))
                    Toast.makeText(activity, "请输入正确的充值金额", Toast.LENGTH_SHORT).show();

                if (PAYTYPE.equals("Alipay")) {
                    pay(Integer.valueOf(enterEditText.getText().toString()), "", false);
                } else if (PAYTYPE.equals("WeChat")) {
                    Toast.makeText(activity, "微信支付暂未开通", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(activity, "请选择支付方式", Toast.LENGTH_SHORT).show();
                }
            }
        });
        getRechargeInformation();
    }

    /**
     * 获得充值信息
     */
    private void getRechargeInformation() {
        HttpMethods.getInstance().getRechargeInformation(new ProgressSubscriber<BaseRequestInfo<SpendMoneyModel>>(new SubscriberOnNextListener<BaseRequestInfo<SpendMoneyModel>>() {
            @Override
            public void onNext(BaseRequestInfo<SpendMoneyModel> o) {
                rechargeProportion = o.getData().getRechargeProportion();
                CurrencyBalance.setText(o.getData().getCashAmount() + "");
                listBean.clear();
                listBean.addAll(o.getData().getLiveAmountMoneyInfoList());
                mSpendMoneyAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onLogicError(String msg) {

            }
        }, activity));
    }

    /**
     * 花币充值
     */
    private FlowerCoinsRechargeModel flowerCoinsRechargeModel;

    private void pay(int money, String productId, boolean type) {
        Map<Object, Object> map = new HashMap<>();
        map.put("orderType", 1);//int 订单类型（1=购买花币）
        map.put("money", money);//int 购买数量
        map.put("productId", productId);
        map.put("paymentType", "1");//int 支付类型（1=微信，2=支付宝 , 3 =Apple支付 ）
        map.put("fastFlag", type);//Boolean 是不是快捷支付
        RequestBody body = ApiClient.getRequestBody(map);
        HttpMethods.getInstance().FlowerCoinsRecharge(new ProgressSubscriber<BaseRequestInfo<FlowerCoinsRechargeModel>>(new SubscriberOnNextListener<BaseRequestInfo<FlowerCoinsRechargeModel>>() {
            @Override
            public void onNext(BaseRequestInfo<FlowerCoinsRechargeModel> o) {
                flowerCoinsRechargeModel = o.getData();
                final String payInfo = flowerCoinsRechargeModel.getToken();
                Runnable payRunnable = new Runnable() {

                    @Override
                    public void run() {
                        // 构造PayTask 对象
                        PayTask alipay = new PayTask(activity);
                        // 调用支付接口，获取支付结果
                        String result = alipay.pay(payInfo, true);
                        Message msg = new Message();
                        msg.what = SDK_PAY_FLAG;
                        msg.obj = result;
                        mHandler.sendMessage(msg);
                    }
                };

                // 必须异步调用
                Thread payThread = new Thread(payRunnable);
                payThread.start();
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onLogicError(String msg) {

            }
        }, activity), body);
    }

    /**
     * 支付宝回调
     */
    private static final int SDK_PAY_FLAG = 1;
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);
                    Log.e("", (String) msg.obj);
                    /**
                     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                     * docType=1) 建议商户依赖异步通知
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息

                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText(activity, "正在支付", Toast.LENGTH_SHORT).show();
                        PayTheSuccessNotification(flowerCoinsRechargeModel.getToken(), 2);
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            toastError("支付结果确认中");
                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Intent intent = new Intent(activity, CommonActivity.class);
                            intent.putExtra(JConstant.EXTRA_INDEX, CommonActivity.PAY_STATUS);
                            intent.putExtra("status", false);
                            intent.putExtra("message", "支付失败");
                            startActivityForResult(intent, 0);
                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };

    /**
     * 支付成功通知后台
     *
     * @param id
     * @param type
     */
    private void PayTheSuccessNotification(String id, int type) {
//        orderSerialNo	String	否	订单序列号
//        orderType	int	是	订单类型（1=购买花币）
//        paymentType	int	是	支付类型（1=微信，2=支付宝）
        Map<Object, Object> map = new HashMap<>();
        map.put("orderSerialNo", id);
        map.put("orderType", 1);
        map.put("paymentType", type);
        RequestBody body = ApiClient.getRequestBody(map);
        HttpMethods.getInstance().PayTheSuccessNotification(new ProgressSubscriber<BaseRequestInfo>(new SubscriberOnNextListener<BaseRequestInfo>() {
            @Override
            public void onNext(BaseRequestInfo o) {
                //支付成功
                Intent intent = new Intent(activity, CommonActivity.class);
                intent.putExtra(JConstant.EXTRA_INDEX, CommonActivity.PAY_STATUS);
                intent.putExtra("status", true);
                startActivityForResult(intent, 0);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onLogicError(String msg) {

            }
        }, activity), body);
    }

}
