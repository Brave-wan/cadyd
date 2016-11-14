package com.cadyd.app.ui.fragment.unitary;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.OnClick;

import com.cadyd.app.JConstant;
import com.cadyd.app.R;
import com.cadyd.app.adapter.PaymentAdapter;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.asynctask.DataLoader;
import com.cadyd.app.comm.MyChange;
import com.cadyd.app.interfaces.OneParameterInterface;
import com.cadyd.app.interfaces.TowObjectParameterInterface;
import com.cadyd.app.model.OneGoodsModel;
import com.cadyd.app.model.UserMassge;
import com.cadyd.app.ui.activity.CommonActivity;
import com.cadyd.app.ui.base.BaseFragement;
import com.cadyd.app.ui.fragment.pay.PaymentResultFragment;
import com.cadyd.app.ui.view.AlertConfirmation;

import org.wcy.android.widget.MyListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaymentFragment extends BaseFragement {

    @Bind(R.id.payment_send)
    Button send;

    @Bind(R.id.payment_list)
    MyListView listView;

    //商品合计
    @Bind(R.id.payment_all_goods)
    TextView allGoods;

    //花币合计
    @Bind(R.id.payment_all_monet)
    TextView allMoney;

    //剩余花币
    @Bind(R.id.payment_money)
    TextView moneyText;

    @Bind(R.id.one_pay_hua)
    CheckBox hua_box;
    @Bind(R.id.one_pay_wx)
    CheckBox wx_box;
    @Bind(R.id.one_pay_zfb)
    CheckBox zfb_box;
    private List<CheckBox> checkBoxes = new ArrayList<>();

    //接收商品模型数组(一共有好多种商品)
    public List<OneGoodsModel> model;

    private UserMassge massge;
    private int money = 0;

    public List<data> datas = new ArrayList<>();
    private Map<String, Object> object = new HashMap<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return setView(R.layout.payment, "支付", true);
    }

    @Override
    protected void initView() {

        checkBoxes.add(hua_box);
        checkBoxes.add(wx_box);
        checkBoxes.add(zfb_box);

        hua_box.setChecked(true);
        initialization();
        getMassge();
        initPay();
    }

    private void initialization() {
        H_Calculation();
        allGoods.setText("共" + model.size() + "件商品");
        allMoney.setText(MyChange.ChangeTextColor("合计：" + money + "花币", 3, 3 + String.valueOf(money).length(), Color.RED));//默认的合计
        PaymentAdapter adapter = new PaymentAdapter(activity, model);
        adapter.setOnClick(new TowObjectParameterInterface() {//adapter中的操作监听
            @Override
            public void Onchange(int type, int pos, Object conten) {
                switch (type) {
                    case 0://包尾
                        initOther(pos, Integer.valueOf((String) conten));
                        break;
                    case 1://加减
                        model.get(pos).number = Integer.valueOf((String) conten);//重新计算花币信息之前将新的数据设置到数据源中
                        H_Calculation();
                        if (allMoney != null) {
                            allMoney.setText(MyChange.ChangeTextColor("合计：" + money + "花币", 3, 3 + String.valueOf(money).length(), Color.RED));//默认的合计
                        }
                        break;
                    case 2://用户输入
                        model.get(pos).number = Integer.valueOf((String) conten);//重新计算花币信息之前将新的数据设置到数据源中
                        H_Calculation();
                        if (allMoney != null) {
                            allMoney.setText(MyChange.ChangeTextColor("合计：" + money + "花币", 3, 3 + String.valueOf(money).length(), Color.RED));//默认的合计
                        }
                        break;
                }
            }
        });
        listView.setAdapter(adapter);
    }

    //包尾请求
    private void initOther(final int pos, final int number) {
        Map<String, Object> map = new HashMap<>();
        map.put("tbid", model.get(pos).tbid);
        map.put("buytimes", (Integer.valueOf(model.get(pos).participatetimes) - Integer.valueOf(model.get(pos).hasparticipatetimes)));
        map.put("type", 1);
        ApiClient.send(activity, JConstant.ADDSHOPPINGCAR_, map, null, new DataLoader<String>() {
            @Override
            public void task(String data) {
            }

            @Override
            public void succeed(String data) {
                model.get(pos).number = number;//重新计算花币信息之前将新的数据设置到数据源中
                H_Calculation();
                if (allMoney != null) {
                    allMoney.setText(MyChange.ChangeTextColor("合计：" + money + "花币", 3, 3 + String.valueOf(money).length(), Color.RED));//默认的合计
                }
            }

            @Override
            public void error(String message) {
            }
        }, JConstant.ADDSHOPPINGCAR_);
    }

    private void initPay() {
        for (int i = 0; i < checkBoxes.size(); i++) {
            final int finalI = i;
            checkBoxes.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkBoxes.get(finalI).isChecked()) {
                        for (int i = 0; i < checkBoxes.size(); i++) {
                            checkBoxes.get(i).setChecked(false);
                        }
                        checkBoxes.get(finalI).setChecked(true);
                    }
                }
            });
        }
    }

    //花币的信息计算
    private void H_Calculation() {
        datas.clear();
        money = 0;
        for (int i = 0; i < model.size(); i++) {
            if ("10".equals(model.get(i).average)) {
                money += 10 * model.get(i).number;
            } else {
                money += 1 * model.get(i).number;
            }
            data data = new data();
            data.tbid = model.get(i).tbid;//商品id
            data.count = model.get(i).number;//商品数量
            datas.add(data);
        }
    }


    private AlertConfirmation alertConfirmation;

    //立即支付
    @OnClick(R.id.payment_send)
    public void onSend(View view) {

        /**判断是否选择支付方式*/
        int i = 0;
        for (CheckBox c : checkBoxes) {
            if (c.isChecked()) {
                break;
            }
            i++;
        }
        if (i == 3) {
            toast("请选择支付方式");
            return;
        }
        /**
         * 判斷是否獲得用戶信息，沒有則無法支付
         */
        if (massge == null) {
            return;
        }
        /**判断余额是否足够*/
        if (Integer.valueOf(massge.current) < money) {
            toast("花币不足无法支付");
            return;
        }

        alertConfirmation = new AlertConfirmation(activity, "支付确认", "是否支付此订单！");
        alertConfirmation.show(new AlertConfirmation.OnClickListeners() {
            @Override
            public void ConfirmOnClickListener() {

                for (int i = 0; i < checkBoxes.size(); i++) {
                    if (checkBoxes.get(i).isChecked()) {
                        switch (i) {
                            case 0:
                                //花币
                                sendSend();
                                break;
                            case 1:
                                //微信
                                break;
                            case 2:
                                //支付宝
                                break;
                            default:
                                toast("请选择支付方式");
                                break;
                        }
                    }
                }
                alertConfirmation.hide();
            }

            @Override
            public void CancelOnClickListener() {
                alertConfirmation.hide();
            }
        });
    }

    private void sendSend() {
        object.put("userid", application.model.id);//用户ID
        object.put("productList", datas);//商品集合
        ApiClient.send(activity, JConstant.SHOPPINGCHECKOUT_, object, null, new DataLoader<String>() {
            @Override
            public void task(String data) {
            }

            @Override
            public void succeed(String data) {
                Intent intent = new Intent(activity, CommonActivity.class);
                intent.putExtra(JConstant.EXTRA_INDEX, CommonActivity.PAY_STATUS);
                intent.putExtra("status", true);
                startActivityForResult(intent, 0);
            }

            @Override
            public void error(String message) {
                Intent intent = new Intent(activity, CommonActivity.class);
                intent.putExtra(JConstant.EXTRA_INDEX, CommonActivity.PAY_STATUS);
                intent.putExtra("status", false);
                intent.putExtra("message", message);
                startActivityForResult(intent, 0);
            }
        }, JConstant.SHOPPINGCHECKOUT_);
    }

    private void getMassge() {
        //读取用户信息
        ApiClient.send(activity, JConstant.SELECUSERBYDE_, null, UserMassge.class, new DataLoader<UserMassge>() {
            @Override
            public void task(String data) {
            }

            @Override
            public void succeed(UserMassge data) {
                massge = data;
                hander.sendEmptyMessage(0);
            }

            @Override
            public void error(String message) {
            }
        }, JConstant.SELECUSERBYDE_);
    }

    private class data {
        int count;
        String tbid;
    }

    Handler hander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            moneyText.setText(MyChange.ChangeTextColor("花币支付(花币余额：" + massge.current + "花币)", 10, (10 + massge.current.length()), Color.RED));
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            activity.setResult(Activity.RESULT_OK);
            finishFramager();
        } else {
            finishFramager();
        }
    }

    @Override
    public boolean onBackPressed() {
        finishFramager();
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ApiClient.cancelRequest(JConstant.ADDSHOPPINGCAR_);
        ApiClient.cancelRequest(JConstant.SHOPPINGCHECKOUT_);
        ApiClient.cancelRequest(JConstant.SELECUSERBYDE_);
    }
}
