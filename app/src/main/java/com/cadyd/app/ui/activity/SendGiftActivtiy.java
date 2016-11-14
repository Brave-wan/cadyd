package com.cadyd.app.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.percent.PercentRelativeLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cadyd.app.JConstant;
import com.cadyd.app.MyApplication;
import com.cadyd.app.R;
import com.cadyd.app.adapter.SendGiftViewPagerAdapter;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.asynctask.retrofitmode.BaseRequestInfo;
import com.cadyd.app.asynctask.retrofitmode.HttpMethods;
import com.cadyd.app.asynctask.retrofitmode.ProgressSubscriber;
import com.cadyd.app.asynctask.retrofitmode.SubscriberOnNextListener;
import com.cadyd.app.interfaces.TowObjectParameterInterface;
import com.cadyd.app.model.BalanceModel;
import com.cadyd.app.model.GiftModel;
import com.cadyd.app.model.LivePersonalDetailIfon;
import com.cadyd.app.model.ReportItemModel;
import com.cadyd.app.model.SendGiftInfo;
import com.cadyd.app.ui.fragment.live.SendGiftFragment;
import com.ypy.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;

/**
 * @Description: $赠送礼物
 * 作者：Jerry
 * 邮箱：185214487@qq.com
 */
public class SendGiftActivtiy extends DialogFragment implements View.OnClickListener, ViewPager.OnPageChangeListener {
    private PercentRelativeLayout rootView;
    private ViewPager mViewPager;
    private LinearLayout mPoint;
    private ImageView[] tips;
    private SendGiftViewPagerAdapter adapter;
    private FragmentPagerAdapter mAdpter;
    private TextView balance;
    private TextView sendgift_send;//送礼
    private TextView fullWorth;
    private LivePersonalDetailIfon info;
    private String conversationId;
    private ArrayList<GiftModel> sourceList = new ArrayList<>();//礼物列表

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            info = (LivePersonalDetailIfon) bundle.getSerializable("info");
            conversationId = info.getConversationId();
        } else {
            conversationId = "";
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.window_sendgift, null);
        setStyle(STYLE_NO_FRAME, R.style.TranslucentNoTitle);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Log.i("huang", "--------------------------onCreateView");
        initView(view);
        return view;
    }

    private void initView(View view) {
        fullWorth = (TextView) view.findViewById(R.id.sendgift_fullWorth);
        mViewPager = (ViewPager) view.findViewById(R.id.window_sendgift_pager);
        mPoint = (LinearLayout) view.findViewById(R.id.window_bottomPoint);
        rootView = (PercentRelativeLayout) view.findViewById(R.id.sendgift_view);
        sendgift_send = (TextView) view.findViewById(R.id.sendgift_send);
        balance = (TextView) view.findViewById(R.id.sendgift_balance);

        mViewPager.addOnPageChangeListener(this);
        rootView.setOnClickListener(this);
        sendgift_send.setOnClickListener(this);
        fullWorth.setOnClickListener(this);

        getDictionaryItem();
        getBalance();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //充值
            case R.id.sendgift_fullWorth:
                Intent intent = new Intent(getActivity(), SpendMoneyActivity.class);
                startActivity(intent);
                //    dismiss();
                break;
            case R.id.sendgift_view:
                dismiss();
                break;

            case R.id.sendgift_send://送礼
                List<String> idList = new ArrayList<>();
                for (int i = 0; i < lsList.size(); i++) {
                    for (int j = 0; j < lsList.get(i).size(); j++) {
                        if (lsList.get(i).get(j).isCheck) {
                            idList.add(lsList.get(i).get(j).getGiftId());//装载上传id
                        }
                    }
                }
                Gifts(idList);
                break;
        }
    }

    List<List<GiftModel>> lsList = new ArrayList<>();//分好的组的list
    private ImageView checkImageView;

    public void initPoint(List<GiftModel> list, LinearLayout view) {
        if (list.size() / 8 > 0) {
            int num = list.size() / 8;
            for (int i = 0; i < num; i++) {
                List<GiftModel> models = list.subList(i * 8, i * 8 + 8);
                lsList.add(models);
            }
        }

        if (list.size() % 8 > 0) {
            List<GiftModel> models = list.subList((list.size() - list.size() % 8), list.size());
            lsList.add(models);
        }

        for (int j = 0; j < lsList.size(); j++) {
            Log.i("wan", "当前条数：" + lsList.get(j).size());
        }

        tips = new ImageView[lsList.size()];
        Log.i("wan", "tips-----" + tips.length);
        for (int i = 0; i < tips.length; i++) {
            ImageView imageView = new ImageView(getActivity());
            imageView.setLayoutParams(new ViewGroup.LayoutParams(10, 10));
            tips[i] = imageView;
            if (i == 0) {
                tips[i].setBackgroundResource(R.drawable.bg_circle_sel);
            } else {
                tips[i].setBackgroundResource(R.drawable.bg_circle_white);
            }

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            layoutParams.leftMargin = 5;
            layoutParams.rightMargin = 5;
            view.addView(imageView, layoutParams);
        }

        if (mAdpter == null) {
            mAdpter = new FragmentPagerAdapter(getChildFragmentManager()) {
                @Override
                public Fragment getItem(int position) {
                    SendGiftFragment fragment = new SendGiftFragment();
                    fragment.setOnClick(new TowObjectParameterInterface() {
                        @Override
                        public void Onchange(int type, int postion, Object object) {
                            if (checkImageView != null) {
                                checkImageView.setVisibility(View.GONE);
                            }
                            checkImageView = (ImageView) object;
                            checkImageView.setVisibility(View.VISIBLE);
                        }
                    });
                    Bundle bundle = new Bundle();
                    ArrayList<GiftModel> giftModelList = new ArrayList<>();
                    giftModelList.addAll(lsList.get(position));
                    bundle.putParcelableArrayList("list", giftModelList);
                    bundle.putParcelableArrayList("sourceList", sourceList);
                    fragment.setArguments(bundle);
                    return fragment;
                }

                @Override
                public int getCount() {
                    return lsList.size();
                }
            };
            mViewPager.setAdapter(mAdpter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    @Override
    public void onPageSelected(int arg0) {
        setImageBackground(arg0);
    }

    /**
     * 设置选中的tip的背景
     *
     * @param selectItems
     */
    private void setImageBackground(int selectItems) {
        for (int i = 0; i < tips.length; i++) {
            if (i == selectItems) {
                tips[i].setBackgroundResource(R.drawable.bg_circle_sel);
            } else {
                tips[i].setBackgroundResource(R.drawable.bg_circle_white);
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        getDialog().getWindow().setLayout(dm.widthPixels, getDialog().getWindow().getAttributes().height);
    }


    /**
     * 获取礼物列表
     */
    private void getDictionaryItem() {
        Map<Object, Object> map = new HashMap<>();
        map.put("typeId", "system_livegift_type_id");
        RequestBody body = ApiClient.getRequestBody(map);
        HttpMethods.getInstance().getDictionaryItem(new ProgressSubscriber<BaseRequestInfo<List<ReportItemModel>>>(new SubscriberOnNextListener<BaseRequestInfo<List<ReportItemModel>>>() {
            @Override
            public void onNext(BaseRequestInfo<List<ReportItemModel>> o) {
                Log.i("wan", "获得礼物列表" + o.toString());
                sourceList.clear();
                if (o != null) {
                    for (ReportItemModel re : o.getData()) {
                        if (re.getStatus() == 1) { //可用
                            sourceList.add(re.getGiftModel());
                        }
                    }
                    initPoint(sourceList, mPoint);
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onLogicError(String msg) {

            }
        }, getActivity()), body);
    }

    BalanceModel mBalanceModel = null;

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
                mBalanceModel = o.getData();
                balance.setText("余额：" + o.getData().getCashAmount());

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onLogicError(String msg) {

            }
        }, getActivity()), body);
    }

    /**
     * 赠送礼物
     *
     * @param list
     */
    public void Gifts(List<String> list) {
        if (mBalanceModel.getCashAmount() <= 0) {
            Toast.makeText(getActivity(), "余额不足，请充值", Toast.LENGTH_SHORT).show();
            return;
        }
        if (list.size() <= 0) {
            Toast.makeText(getActivity(), "请选择你要赠送的礼物", Toast.LENGTH_SHORT).show();
            return;
        }
        Map<Object, Object> map = new HashMap<>();
        map.put("conversationId", conversationId);//房间会话号
        map.put("giftIds", list);//礼品列表
        RequestBody body = ApiClient.getRequestBody(map);
        HttpMethods.getInstance().Gifts(new ProgressSubscriber(new SubscriberOnNextListener() {
            @Override
            public void onNext(Object o) {
                Log.i("wan", "礼物发送成功：" + o.toString());
                EventBus.getDefault().post(new SendGiftInfo());

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onLogicError(String msg) {

            }
        }, getActivity()), body);
    }
}
