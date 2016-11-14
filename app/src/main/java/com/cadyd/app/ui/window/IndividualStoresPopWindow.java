package com.cadyd.app.ui.window;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cadyd.app.JConstant;
import com.cadyd.app.MyApplication;
import com.cadyd.app.R;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.asynctask.DataLoader;
import com.cadyd.app.model.ShopDetailsInfo;
import com.cadyd.app.model.SocketInfo;
import com.cadyd.app.ui.activity.ShopHomeActivity;
import com.cadyd.app.ui.activity.SignInActivity;
import com.cadyd.app.ui.view.CircleImageView;

import org.wcy.android.utils.adapter.CommonAdapter;
import org.wcy.android.utils.adapter.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 直播个人店铺
 */
public class IndividualStoresPopWindow extends PopupWindow implements View.OnClickListener {
    private LayoutInflater inflater;
    private Context mContext;
    private PopupWindow mPopupWindow;
    //头像
    private CircleImageView mCircleImageView;
    //姓名
    private TextView mName;
    //人气
    private TextView msentiment;
    //粉丝
    private TextView mfans;
    //关注店铺
    private CheckBox mattention;
    //进入店铺
    private TextView intoStores;
    //商品列表
    private ListView mListView;

    private RelativeLayout close;
    private ImageView cha;
    private OnIndividualStoresListener listener;
    private ShopDetailsInfo info;

    public void setOnIndividualStoresListener(OnIndividualStoresListener Onlistener) {
        this.listener = Onlistener;
    }

    public IndividualStoresPopWindow(Context context, ShopDetailsInfo info) {
        super(context);
        this.mContext = context;
        this.info = info;
        inflater = LayoutInflater.from(mContext);
        initView();
    }

    /**
     * 初始化数据
     */
    private void initDate() {
        if (info.getData() != null) {
            mName.setText(info.getData().getNickname());
            ApiClient.displayImage(info.getData().getPhoto(), mCircleImageView);
            msentiment.setText(info.getData().getOnlineCount() + "人气");
            mfans.setText(info.getData().getFollowCount() + "粉丝");
            //查看关注状态
            mattention.setChecked(info.getData().getFollowState() == 1 ? false : true);
            if (info.getData().getShopid().equals("")) {
                mattention.setFocusable(false);
            }
        }

    }

    private void initView() {
        View rootView = inflater.inflate(R.layout.window_individualstores, null);
        mPopupWindow = new PopupWindow(rootView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mName = (TextView) rootView.findViewById(R.id.individualstores_name);
        mattention = (CheckBox) rootView.findViewById(R.id.individualstores_dianpu);
        intoStores = (TextView) rootView.findViewById(R.id.individualstores_jinru);
        mfans = (TextView) rootView.findViewById(R.id.individualstores_fans);
        msentiment = (TextView) rootView.findViewById(R.id.individualstores_sentiment);
        mListView = (ListView) rootView.findViewById(R.id.individualstores_list);
        mCircleImageView = (CircleImageView) rootView.findViewById(R.id.individualstores_head);
        cha = (ImageView) rootView.findViewById(R.id.individulstore_down);
        close = (RelativeLayout) rootView.findViewById(R.id.window_close);
        //在PopupWindow里面就加上下面代码，让键盘弹出时，不会挡住pop窗口。
        mPopupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        intoStores.setOnClickListener(this);
        mattention.setOnClickListener(this);
        cha.setOnClickListener(this);
        close.setOnClickListener(this);
        setOutsideTouchable(true);
        setFocusable(true);

        setBackgroundDrawable(mContext.getResources().getDrawable(android.R.color.transparent));
        mattention.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (info.getData() != null) {
                    if (info.getData().getShopid() != null && !info.getData().getShopid().equals("")) {
                        listener.AttentionStores(isChecked, info.getData().getShopid());
                    } else {
                        Toast.makeText(mContext, "该主播不是商家用户", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(mContext, "该主播不是商家用户", Toast.LENGTH_SHORT).show();
                }

            }
        });
        initDate();
    }


    public void dismitPopWindow() {
        if (mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
    }

    public void showPopWindow(View v) {
        if (mPopupWindow != null) {
            mPopupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //进入店铺
            case R.id.individualstores_jinru:
                if (info.getData() != null) {
                    if (!info.getData().getShopid().equals("") && info.getData().getShopid() != null) {
                        Intent intent = new Intent(mContext, ShopHomeActivity.class);
                        intent.putExtra("shopId", info.getData().getShopid());
                        mContext.startActivity(intent);
                    } else {
                        Toast.makeText(mContext, "该主播不是商家用户", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(mContext, "该主播不是商家用户", Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.individulstore_down:
                dismitPopWindow();
                break;

            case R.id.window_close:
                dismitPopWindow();
                break;
        }
    }


    public interface OnIndividualStoresListener {
        void AttentionStores(boolean state, String shopId);//关注店铺

        void IntoStores();//进入店铺
    }


}
