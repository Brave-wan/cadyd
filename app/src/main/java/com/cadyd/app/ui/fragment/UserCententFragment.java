package com.cadyd.app.ui.fragment;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
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
import com.cadyd.app.comm.Utils;
import com.cadyd.app.interfaces.TowParameterInterface;
import com.cadyd.app.model.BalanceModel;
import com.cadyd.app.model.MessageCountInfo;
import com.cadyd.app.model.UserMassge;
import com.cadyd.app.ui.activity.CommonActivity;
import com.cadyd.app.ui.activity.MessageCenterActivity;
import com.cadyd.app.ui.activity.SignInActivity;
import com.cadyd.app.ui.activity.SpendMoneyActivity;
import com.cadyd.app.ui.base.BaseFragement;
import com.cadyd.app.ui.fragment.pay.FlowerCurrencyRechargeRecordsActivity;
import com.cadyd.app.widget.BadgeView;

import org.wcy.common.utils.StringUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.RequestBody;

/**
 * 个人中心
 *
 * @author wcy
 */
public class UserCententFragment extends BaseFragement {

    @Bind(R.id.ui_user_centent_head)
    RelativeLayout uiUserCententHead;
    @Bind(R.id.user_balance)
    TextView userBalance;
    @Bind(R.id.user_my_balance)
    RelativeLayout userMyBalance;
    @Bind(R.id.user_integral)
    TextView userIntegral;
    @Bind(R.id.user_integral_list)
    RelativeLayout userIntegralList;
    @Bind(R.id.user_huabi)
    TextView userHuabi;
    @Bind(R.id.user_huabi_list)
    RelativeLayout userHuabiList;
    @Bind(R.id.my_order)
    TextView myOrder;
    @Bind(R.id.user_shopping_collection)
    TextView userShoppingCollection;
    @Bind(R.id.order_theme)
    TextView orderTheme;
    @Bind(R.id.user_one_record)
    TextView userOneRecord;
    @Bind(R.id.user_one_prize)
    TextView userOnePrize;
    @Bind(R.id.user_one_the_sun)
    TextView userOneTheSun;
    @Bind(R.id.my_integral_order)
    TextView myIntegralOrder;
    @Bind(R.id.integral_order_evaluate)
    TextView integralOrderEvaluate;
    @Bind(R.id.user_integral_collection)
    TextView userIntegralCollection;
    @Bind(R.id.address_Management)
    RelativeLayout addressManagement;
    @Bind(R.id.setting)
    RelativeLayout setting;
    @Bind(R.id.Refresh_ScrollView)
    ScrollView RefreshScrollView;
    private TowParameterInterface towParameterInterface;

    public void setClick(TowParameterInterface towParameterInterface) {
        this.towParameterInterface = towParameterInterface;
    }

    @Bind(R.id.ui_user_center_user_image)
    ImageView UserImage;

    @Bind(R.id.user_massage)
    LinearLayout massageLinearLayout;

    @Bind(R.id.ui_user_center_name)
    TextView name;

    @Bind(R.id.user_vips)
    TextView Vip;

    @Bind(R.id.ui_user_center_address)
    TextView address;

    @Bind(R.id.balance)
    TextView Balance;

    @Bind(R.id.integral)
    TextView intergral;

    @Bind(R.id.huabi)
    TextView huabi;

    @Bind(R.id.customer_service)
    TextView customerService;
    @Bind(R.id.customer_service_r)
    RelativeLayout customer_r;
    @Bind(R.id.LinearLayout)
    LinearLayout linearLayout;
    @Bind(R.id.userCentent_msgCenter)
    TextView messageCenter;
    @Bind(R.id.userCentent_badge)
    View userCententBadge;
    @Bind(R.id.usercenter_title)
    RelativeLayout userCenterTitle;
    BadgeView badge1;
    public static final int JUMPRESULT = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = setView(inflater, R.layout.fragment_user_centent);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initView() {
        customerService.setText("客服电话：" + JConstant.customerServicePhone);
        customer_r.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.tellPhone(activity, JConstant.customerServicePhone, "联系客服");
            }
        });

        badge1 = new BadgeView(getActivity(), userCententBadge);
        badge1.setTextColor(Color.WHITE);
        badge1.setTextSize(10);
        getMessageTotalCount();
    }

    public void getMessageTotalCount() {
        HttpMethods.getInstance().getMessageTotalCount(new ProgressSubscriber<BaseRequestInfo<MessageCountInfo>>(new SubscriberOnNextListener<BaseRequestInfo<MessageCountInfo>>() {
            @Override
            public void onNext(BaseRequestInfo<MessageCountInfo> o) {
                if (Integer.parseInt(o.getData().getTotalCount()) > 0) {
                    badge1.setText(o.getData().getTotalCount());
                    badge1.show();
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onLogicError(String msg) {

            }
        }, getActivity()));
    }

    @Override
    public void onResume() {
        super.onResume();
        if (application.isLogin()) {
            getMassge();
            getBalance();
        }
    }

    @OnClick({R.id.ui_user_centent_head, R.id.user_huabi_list, R.id.user_integral_list, R.id.user_my_balance, R.id.my_order,
            R.id.user_shopping_collection, R.id.order_theme, R.id.user_one_record, R.id.user_one_prize, R.id.user_one_the_sun
            , R.id.address_Management, R.id.setting, R.id.my_integral_order, R.id.integral_order_evaluate, R.id.user_integral_collection, R.id.userCentent_msgCenter})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.userCentent_msgCenter:
                startActivity(new Intent(getActivity(), MessageCenterActivity.class));
                break;
            case R.id.ui_user_centent_head:
                if (!application.isLogin()) {
                    startActivity(new Intent(activity, SignInActivity.class));
                } else {
                    Intent intent = new Intent(activity, CommonActivity.class);
                    intent.putExtra(JConstant.EXTRA_INDEX, CommonActivity.MYHEAD);
                    startActivity(intent);
                }
                break;
            case R.id.user_huabi_list://花币明细

                Intent intent = new Intent(activity, SpendMoneyActivity.class);
                startActivity(intent);

//                Intent intent = new Intent(activity, CommonActivity.class);
//                intent.putExtra(JConstant.EXTRA_INDEX, CommonActivity.MYBALANCEFRAGMENT);
//                intent.putExtra("type", 2);
//                startActivity(intent);
                break;
            case R.id.user_integral_list://积分明细
                intent = new Intent(activity, CommonActivity.class);
                intent.putExtra(JConstant.EXTRA_INDEX, CommonActivity.MYBALANCEFRAGMENT);
                intent.putExtra("type", 1);
                startActivity(intent);
                break;
            case R.id.user_my_balance://我的余额 提现
                intent = new Intent(activity, CommonActivity.class);
                intent.putExtra(JConstant.EXTRA_INDEX, CommonActivity.MYBALANCEFRAGMENT);
                intent.putExtra("type", 0);
                startActivity(intent);
                break;
            case R.id.my_order://商城——我的订单
                intent = new Intent(activity, CommonActivity.class);
                intent.putExtra(JConstant.EXTRA_INDEX, CommonActivity.MY_ORDER);
                intent.putExtra("isEvaluate", false);
                activity.startActivity(intent);
                break;
            case R.id.user_shopping_collection://我的收藏
                intent = new Intent(activity, CommonActivity.class);
                intent.putExtra(JConstant.EXTRA_INDEX, CommonActivity.USERSHOPPINGCOLLECTIONFRAGMENT);
                activity.startActivity(intent);
                break;
            case R.id.order_theme://待评价
                //TODO 我的主题
                break;
            case R.id.user_one_record://一元购 我的记录
                intent = new Intent(activity, CommonActivity.class);
                intent.putExtra(JConstant.EXTRA_INDEX, CommonActivity.MYRECORDFRAGMENT);
                intent.putExtra("type", false);
                activity.startActivity(intent);
                break;
            case R.id.user_one_prize://一元购 获得的奖品
                intent = new Intent(activity, CommonActivity.class);
                intent.putExtra(JConstant.EXTRA_INDEX, CommonActivity.MYRECORDFRAGMENT);
                intent.putExtra("type", true);
                activity.startActivity(intent);
                break;
            case R.id.user_one_the_sun://一元购 我的晒单
                intent = new Intent(activity, CommonActivity.class);
                intent.putExtra(JConstant.EXTRA_INDEX, CommonActivity.THESUNFRAGMENT);
                intent.putExtra("title", "所有晒单");
                activity.startActivity(intent);
                break;
            case R.id.address_Management://地址管理
                intent = new Intent(activity, CommonActivity.class);
                intent.putExtra(JConstant.EXTRA_INDEX, CommonActivity.USERADDRESSFRAGMENT);
                activity.startActivity(intent);
                break;
            case R.id.setting://用户设置
                intent = new Intent(activity, CommonActivity.class);
                intent.putExtra(JConstant.EXTRA_INDEX, CommonActivity.USERSETINGFRAGMENT);
                startActivityForResult(intent, JUMPRESULT);
                break;
            case R.id.my_integral_order://积分商城-我的订单
                intent = new Intent(activity, CommonActivity.class);
                intent.putExtra(JConstant.EXTRA_INDEX, CommonActivity.INTEGRALMYORDER);
                intent.putExtra("isEvaluate", false);
                activity.startActivity(intent);
                break;
            case R.id.integral_order_evaluate://积分商城的评论列表
                intent = new Intent(activity, CommonActivity.class);
                intent.putExtra(JConstant.EXTRA_INDEX, CommonActivity.INTEGRALEVALUATELISTFRAGMENT);
                activity.startActivity(intent);
                break;
            case R.id.user_integral_collection://积分商城我的收藏
                break;
        }
    }


    private void getMassge() {
        //读取用户信息
        HttpMethods.getInstance().getUserMassge(new ProgressSubscriber<BaseRequestInfo<UserMassge>>(new SubscriberOnNextListener<BaseRequestInfo<UserMassge>>() {
            @Override
            public void onNext(BaseRequestInfo<UserMassge> o) {
                UserMassge data = o.getData();
                name.setText(data.name);
                address.setText("长居地：" + (StringUtil.hasText(data.oftenaddress) ? data.oftenaddress : ""));
                Balance.setText(data.balance);
                intergral.setText(data.integration);
                MyImage(data);//設置用戶的等級
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
     * 查询余额
     */
    public void getBalance() {
        Map<Object, Object> map = new HashMap<>();
        MyApplication myApplication = (MyApplication) getActivity().getApplication();
        map.put("userId", myApplication.model.id);
        RequestBody body = ApiClient.getRequestBody(map);
        HttpMethods.getInstance().getBalance(new ProgressSubscriber<BaseRequestInfo<BalanceModel>>(new SubscriberOnNextListener<BaseRequestInfo<BalanceModel>>() {
            @Override
            public void onNext(BaseRequestInfo<BalanceModel> o) {
                Log.i("wan", "获得用户余额" + o.toString());
                huabi.setText(o.getData().getCashAmount() + "");

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onLogicError(String msg) {

            }
        }, getActivity()), body);
    }

    private void MyImage(UserMassge data) {
        float index = Float.valueOf(data.vipgrade);
        //重新添加之前要清除之前的内容
        int num = massageLinearLayout.getChildCount();
        if (num >= 2) {
            for (int i = 0; i < num - 2; i++) {
                massageLinearLayout.removeViewAt(i + 2);
            }
        }

        int s = (int) (index * 10) % 10;
        int g = (int) (index % 10);
        switch (g) {
            case 1://花童
                Vip.setText("  花童  ");
                break;
            case 2://花学士
                Vip.setText("  花学士  ");
                break;
            case 3://花博士
                Vip.setText("  花博士  ");
                break;
            case 4://花仙
                Vip.setText("  花仙  ");
                break;
        }

        for (int j = 0; j < s; j++) {
            ImageView imageView = new ImageView(activity);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            imageView.setLayoutParams(params);
            imageView.setImageResource(R.mipmap.vip_hua);
            massageLinearLayout.addView(imageView, 2);
        }
        ApiClient.displayImage(data.photo, UserImage, R.mipmap.user_default);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //退出帐号后的处理
        if (requestCode == JUMPRESULT && resultCode == UserSetingFragment.BACKRESULT) {
            if (towParameterInterface != null) {
                towParameterInterface.Onchange(0, null);
            }
        }
    }

    @Override
    public boolean onBackPressed() {
        finishActivity();
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ApiClient.cancelRequest(JConstant.SELECUSERBYDE_);
        ApiClient.cancelRequest(JConstant.QUERYSHOPIDTOLISTGOODS);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
