package com.cadyd.app.ui.fragment.pay;


import Alipay.PayResult;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.OnClick;

import com.alipay.sdk.app.PayTask;
import com.cadyd.app.JConstant;
import com.cadyd.app.R;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.asynctask.DataLoader;
import com.cadyd.app.comm.MyChange;
import com.cadyd.app.handle.InterFace;
import com.cadyd.app.ui.base.BaseFragement;
import com.cadyd.app.utils.StringUtils;
import com.google.gson.Gson;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.wcy.common.utils.NumberUtil;
import org.wcy.common.utils.StringUtil;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * 商品统一支付中心
 *
 * @author wcy
 */
public class PayGoodsFragment extends BaseFragement {
    @Bind(R.id.tv_money)
    TextView tv_money;
    @Bind(R.id.wechat_pay_check)
    CheckBox wechat_pay_check;
    @Bind(R.id.alipay_check)
    CheckBox alipay_check;
    private String orderid;
    private BigDecimal money;
    String orderType;
    private String integral;

    public static PayGoodsFragment newInstance(String orderid, String integral, String money, String orderType) {
        PayGoodsFragment newFragment = new PayGoodsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("orderid", orderid);
        bundle.putString("money", money);
        bundle.putString("orderType", orderType);
        bundle.putString("integral", integral);//為空
        newFragment.setArguments(bundle);
        return newFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            this.orderid = args.getString("orderid");
            this.money = new BigDecimal(args.getString("money"));
            this.orderType = args.getString("orderType");
            this.integral = args.getString("integral");
        }
    }

    @Override
    protected void TitleBarEvent(int btnID) {
        activity.setResult(Activity.RESULT_CANCELED);
        finishActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return setView(R.layout.fragment_pay_goods, "第一点支付中心", true);
    }

    @Override
    protected void initView() {
        if (!"0".equals(integral) && !StringUtils.isEmpty(integral)) {
            tv_money.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.penny, 0, 0, 0);
            int start;
            int end;
            StringBuffer buffer = new StringBuffer(String.valueOf(integral));
            start = buffer.length();
            buffer.append("\t\t￥");
            end = buffer.length();
            buffer.append(money);
            tv_money.setText(MyChange.ChangeTextColor(buffer.toString(), start, end, getResources().getColor(R.color.text_primary_gray)));
        } else {
            tv_money.setText(String.format("%s元", NumberUtil.getString(money, 2)));
        }
    }

    @OnClick({R.id.wechat_pay_check, R.id.alipay_check, R.id.pay_btn})
    public void OnClickLinner(View view) {
        switch (view.getId()) {
            case R.id.wechat_pay_check:
                wechat_pay_check.setChecked(true);
                alipay_check.setChecked(false);
                break;
            case R.id.alipay_check:
                wechat_pay_check.setChecked(false);
                alipay_check.setChecked(true);
                break;
            case R.id.pay_btn:
                if (alipay_check.isChecked()) {
                    pay();
                } else if (wechat_pay_check.isChecked()) {
                    wxpay();
                } else {
                    toast("请选择支付方式");
                }
                break;
        }
    }

    private void wxpay() {
        Map<String, Object> map = new HashMap<>();
        map.put("flowno", orderid);
        map.put("channelsNo", "0101");
        map.put("orderType", orderType);
        ApiClient.send(activity, JConstant.ORDER_WX_, map, String.class, new DataLoader<String>() {

            @Override
            public void task(String data) {

            }

            @Override
            public void succeed(String data) {
                if (StringUtil.hasText(data)) {
                    StringBuffer sb = new StringBuffer("{");
                    sb.append(data);
                    sb.append("}");
                    Gson gson = new Gson();
                    WxReq wxReq = gson.fromJson(sb.toString(), WxReq.class);
                    final IWXAPI msgApi = WXAPIFactory.createWXAPI(activity, null);
                    msgApi.registerApp(wxReq.appId);
                    PayReq request = new PayReq();
                    request.appId = wxReq.appId;
                    request.nonceStr = wxReq.nonceStr;
                    request.packageValue = "Sign=WXPay";
                    request.timeStamp = wxReq.timeStamp;
                    request.sign = wxReq.paySign;
                    request.signType = wxReq.signType;
                    msgApi.sendReq(request);
                }
            }

            @Override
            public void error(String message) {

            }
        }, JConstant.ORDER_WX_);
    }

    /**
     * 商城支付
     * 积分商城支付
     * 花币充值
     */

    private void pay() {
        Map<String, Object> map = new HashMap<>();
        map.put("flowno", orderid); //如果不是充值花幣
        int url;
        if (InterFace.OrderType.VIRTUAL.ecode.equals(orderType)) {/**如果是花币支付*/
            map.remove("flowno");
            map.put("key", orderid);
            url = JConstant.ORDER_PAY_;
        } else if (InterFace.OrderType.MALL.ecode.equals(orderType)) { /**如果是商城*/
            /**因为积分也属于商城 ， 通过积分来判断是积分商城还是商城支付*/
            if (integral == null) {
                url = JConstant.ORDER_PAY_S;
            } else {
                url = JConstant.JJ;
            }
        } else {                                                    /**其他情况*/
            url = JConstant.ORDER_PAY_;
        }
        map.put("orderType", orderType);
        map.put("channelsNo", "0202");
        ApiClient.send(activity, url, map, true, String.class, new DataLoader<String>() {
            @Override
            public void task(String data) {

            }

            @Override
            public void succeed(String data) {
                final String payInfo = data;
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
            public void error(String message) {

            }
        }, url);
    }

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
                        // Toast.makeText(activity, "支付成功", Toast.LENGTH_SHORT).show();
                        replaceFragment(R.id.common_frame, PaymentResultFragment.newInstance(true, null));
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            toastError("支付结果确认中");
                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            replaceFragment(R.id.common_frame, PaymentResultFragment.newInstance(false, null));
                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };

    private class WxReq {
        public String appId;
        public String timeStamp;
        public String nonceStr;
        public String paySign;
        public String prepay_id;
        public String signType;
    }

    @Override
    public boolean onBackPressed() {
        activity.setResult(Activity.RESULT_CANCELED);
        finishActivity();
        return false;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        ApiClient.cancelRequest(JConstant.JJ);
        ApiClient.cancelRequest(JConstant.ORDER_PAY_);
        ApiClient.cancelRequest(JConstant.PAYINTEGRAL);
        ApiClient.cancelRequest(JConstant.ORDER_WX_);
    }
}
