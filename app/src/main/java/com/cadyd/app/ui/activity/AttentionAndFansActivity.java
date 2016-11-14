package com.cadyd.app.ui.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import com.cadyd.app.R;
import com.cadyd.app.adapter.LiveFragmentAdapter;
import com.cadyd.app.interfaces.BackHandledInterface;
import com.cadyd.app.ui.base.BaseActivity;
import com.cadyd.app.ui.base.BaseFragement;
import com.cadyd.app.ui.fragment.AttentionFragment;
import com.cadyd.app.ui.fragment.UserFansFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * xiongmao
 * 关注和粉丝的activity
 */
public class AttentionAndFansActivity extends BaseActivity implements BackHandledInterface {
    @Bind(R.id.tabs)
    TabLayout tabs;
    @Bind(R.id.viewpager)
    ViewPager viewPager;

    private String Type = "ATTENTION";
    public static final String Attention = "ATTENTION";//关注
    public static final String Fans = "FANS";//粉丝

    private List<String> titleList;
    private List<Fragment> fragmentList;
    private LiveFragmentAdapter FragmentAdapter;

    private com.cadyd.app.ui.fragment.AttentionFragment AttentionFragment;
    private UserFansFragment mUserFansFragment;

    private String userId;//用户id

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attention_and_fans);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            userId = bundle.getString("userId");
            Type = bundle.getString("Type");
        }
        initview();
    }

    private void initview() {

        titleList = new ArrayList<>();
        titleList.add("关注");
        titleList.add("粉丝");
        for (String str : titleList) {
            tabs.addTab(tabs.newTab().setText(str));
        }

        fragmentList = new ArrayList<>();
        AttentionFragment = new AttentionFragment();//实例关注的fragment
        Bundle bundle = new Bundle();
        bundle.putString("userId", userId);
        AttentionFragment.setArguments(bundle);
        mUserFansFragment = new UserFansFragment();//实例粉丝的fragment
        bundle = new Bundle();
        bundle.putString("userId", userId);
        mUserFansFragment.setArguments(bundle);

        fragmentList.add(AttentionFragment);
        fragmentList.add(mUserFansFragment);


        FragmentAdapter = new LiveFragmentAdapter(getSupportFragmentManager(), fragmentList, titleList);
        viewPager.setAdapter(FragmentAdapter);
        /**
         * 判断用户是进入的关注还是粉丝
         */
        if (Attention.equals(Type)) {
            
        } else if (Fans.equals(Type)) {
            viewPager.setCurrentItem(1);
        }
        tabs.setupWithViewPager(viewPager);//将TabLayout和ViewPager关联起来。
    }

    @OnClick(R.id.back)
    public void onBack(View view) {
        Log.i("xiongmao", "关闭当前的Activity");
        finishActivity();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public void setSelectedFragment(BaseFragement selectedFragment) {

    }
}
