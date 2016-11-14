package com.cadyd.app.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cadyd.app.R;
import com.cadyd.app.adapter.LiveFragmentAdapter;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.asynctask.retrofitmode.BaseRequestInfo;
import com.cadyd.app.asynctask.retrofitmode.HttpMethods;
import com.cadyd.app.asynctask.retrofitmode.ProgressSubscriber;
import com.cadyd.app.asynctask.retrofitmode.SubscriberOnNextListener;
import com.cadyd.app.model.LiveEndInfo;
import com.cadyd.app.model.LiveStartInfo;
import com.cadyd.app.model.LivingInfo;
import com.cadyd.app.ui.activity.LiveEndActivity;
import com.cadyd.app.ui.base.BaseFragement;
import com.cadyd.app.ui.fragment.live.LiveHotFragment;
import com.cadyd.app.ui.fragment.live.LiveNewFragment;
import com.cadyd.app.ui.push.SWCodecCameraStreamingActivity;
import com.cadyd.app.ui.push.gles.Config;
import com.cadyd.app.ui.view.AlertConfirmation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.RequestBody;

/**
 * A simple {@link Fragment} subclass.
 * 直播列表页
 */
public class LiveFragment extends BaseFragement {
    @Bind(R.id.live_tabs)
    TabLayout mLiveTabs;
    @Bind(R.id.live_viewpager)
    ViewPager mLiveViewpager;
    private List<String> mTitleList;//页卡标题集合
    private List<Fragment> mFragmentList;//页卡视图集合
    private LiveHotFragment mLiveHotFragment;
    private LiveNewFragment mLiveNewFragment;

    private LiveFragmentAdapter mLiveFragmentAdapter;

    private AlertConfirmation confirmation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_live, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initView() {
        mTitleList = new ArrayList<>();
        mTitleList.add("热门");
        mTitleList.add("最新");
        mLiveTabs.addTab(mLiveTabs.newTab().setText(mTitleList.get(0)));//添加tab选项卡
        mLiveTabs.addTab(mLiveTabs.newTab().setText(mTitleList.get(1)));

        mFragmentList = new ArrayList<>();
        mLiveHotFragment = new LiveHotFragment();
        mFragmentList.add(mLiveHotFragment);
        mLiveNewFragment = new LiveNewFragment();
        mFragmentList.add(mLiveNewFragment);

        mLiveFragmentAdapter = new LiveFragmentAdapter(getChildFragmentManager(), mFragmentList, mTitleList);
        mLiveViewpager.setAdapter(mLiveFragmentAdapter);
        mLiveTabs.setupWithViewPager(mLiveViewpager);//将TabLayout和ViewPager关联起来。
        getLiving();
    }

    /**
     * 获取直播状态
     */
    private void getLiving() {
        HttpMethods.getInstance().getLiving(new ProgressSubscriber<BaseRequestInfo<LivingInfo>>(new SubscriberOnNextListener<BaseRequestInfo<LivingInfo>>() {
            @Override
            public void onNext(final BaseRequestInfo<LivingInfo> o) {
                if (o.getData().getFlag() == 1) {
                    if (confirmation == null) {
                        confirmation = new AlertConfirmation(getActivity(), "提示", "正在直播中", "继续直播", "退出");
                    }
                    confirmation.show(new AlertConfirmation.OnClickListeners() {
                        @Override
                        public void ConfirmOnClickListener() {
                            LiveOn(o.getData());
                            confirmation.hide();
                        }

                        @Override
                        public void CancelOnClickListener() {
                            getActivity().startActivity(new Intent(getActivity(), LiveEndActivity.class));
                            confirmation.hide();
                        }
                    });
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

    public void LiveOn(LivingInfo livingInfo) {
        Map<Object, Object> map = new HashMap<>();
        map.put("conversationTitle", livingInfo.getConversationTitle());
        map.put("liveRoomCoverPath", livingInfo.getLiveRoomCoverPath());
        map.put("areaCode", livingInfo.getAreaCode());
        if (!TextUtils.isEmpty(livingInfo.getMerchantPassword())) {
            map.put("merchantPassword", livingInfo.getMerchantPassword());
        }
        RequestBody key = ApiClient.getRequestBody(map);
        HttpMethods.getInstance().doLiveOn(new ProgressSubscriber<BaseRequestInfo<LiveStartInfo>>(new SubscriberOnNextListener<BaseRequestInfo<LiveStartInfo>>() {
            @Override
            public void onNext(BaseRequestInfo<LiveStartInfo> o) {
                startSWCodecCamera(o.getData());
            }

            @Override
            public void onError(Throwable e) {
                toastError("进入直播失败");
            }

            @Override
            public void onLogicError(String msg) {

            }
        }, getActivity()), key);
    }

    /**
     * 跳转直播页面
     */
    private void startSWCodecCamera(LiveStartInfo info) {
        Intent intent = new Intent(getActivity(), SWCodecCameraStreamingActivity.class);
        String publishUrl = Config.EXTRA_PUBLISH_URL_PREFIX + info.getQiniuParameter().getPushStream();
        intent.putExtra(Config.EXTRA_KEY_PUB_URL, publishUrl);
        intent.putExtra("info", info);
        getActivity().startActivity(intent);
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
