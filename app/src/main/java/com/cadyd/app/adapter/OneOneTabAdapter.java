package com.cadyd.app.adapter;


import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import com.cadyd.app.MyApplication;
import com.cadyd.app.R;

import java.util.List;

/**
 * 全球购list fragment
 * Author: wcy
 */
public class OneOneTabAdapter implements RadioGroup.OnCheckedChangeListener {

    private MyApplication application;
    private Intent intent;


    private List<Fragment> fragments; // 一个tab页面对应一个Fragment


    private RadioGroup rgs; // 用于切换tab
    private FragmentActivity fragmentActivity; // Fragment所属的Activity
    private int fragmentContentId; // Activity中所要被替换的区域的id

    private RadioButton rb;

    private int currentTab = 0; // 当前Tab页面索引
    private OnRgsExtraCheckedChangedListener onRgsExtraCheckedChangedListener; // 用于让调用者在切换tab时候增加新的功能

    public OneOneTabAdapter(FragmentActivity fragmentActivity, final List<Fragment> fragments, int fragmentContentId, final RadioGroup rgs) {
        this.fragments = fragments;
        this.rgs = rgs;
        this.fragmentActivity = fragmentActivity;
        this.fragmentContentId = fragmentContentId;

        // 默认显示第一页
        FragmentTransaction ft = fragmentActivity.getSupportFragmentManager().beginTransaction();
        ft.add(fragmentContentId, fragments.get(0));
        ft.commit();
        rgs.setOnCheckedChangeListener(this);
        showTab(currentTab);
        application = (MyApplication) fragmentActivity.getApplication();
        if (rb == null) {
            rb = (RadioButton) rgs.getChildAt(0);
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {

        if (checkedId == R.id.one_one_user) {
            if (onRgsExtraCheckedChangedListener != null) {
                if (!application.isLogin()) {
                    onRgsExtraCheckedChangedListener.OnRgsExtraCheckedChanged(radioGroup, checkedId);
                    return;
                }
            }
        }
        if (checkedId == R.id.one_one_car) {
            if (onRgsExtraCheckedChangedListener != null) {
                if (!application.isLogin()) {
                    onRgsExtraCheckedChangedListener.OnRgsExtraCheckedChanged(radioGroup, checkedId);
                    return;
                }
            }
        }

        if (checkedId == R.id.one_one_doing) {
            Fragment fragment = fragments.get(2);
            FragmentTransaction ft = obtainFragmentTransaction(2);
            getCurrentFragment().onPause(); // 暂停当前tab
            if (fragment.isAdded()) {
                fragment.onResume(); // 启动目标tab的onResume()
            } else {
                ft.add(fragmentContentId, fragment);
            }
            showTab(2); // 显示目标tab
            ft.commit();
            if (onRgsExtraCheckedChangedListener != null) {
                onRgsExtraCheckedChangedListener.OnRgsExtraCheckedChanged(radioGroup, checkedId);
                return;
            }
        }


        for (int i = 0; i < rgs.getChildCount(); i++) {
            if (rgs.getChildAt(i).getId() == checkedId) {
                Fragment fragment = fragments.get(i);
                FragmentTransaction ft = obtainFragmentTransaction(i);
                getCurrentFragment().onPause(); // 暂停当前tab
                if (fragment.isAdded()) {
                    fragment.onResume(); // 启动目标tab的onResume()
                } else {
                    ft.add(fragmentContentId, fragment);
                }
                showTab(i); // 显示目标tab
                ft.commit();
                // 如果设置了切换tab额外功能功能接口
                if (null != onRgsExtraCheckedChangedListener) {
                    onRgsExtraCheckedChangedListener.OnRgsExtraCheckedChanged(radioGroup, checkedId);
                }
            }
        }

    }

    /**
     * 切换tab
     *
     * @param idx
     */
    private void showTab(int idx) {
        for (int i = 0; i < fragments.size(); i++) {
            //   if (i != 1) {
            Fragment fragment = fragments.get(i);
            FragmentTransaction ft = obtainFragmentTransaction(idx);
            if (idx == i) {
                ft.show(fragment);
            } else {
                ft.hide(fragment);
            }
            ft.commit();
            //      }
        }
        currentTab = idx; // 更新目标tab为当前tab
        rb = (RadioButton) rgs.getChildAt(currentTab);
    }

    /**
     * 获取一个带动画的FragmentTransaction
     *
     * @param index
     * @return
     */
    private FragmentTransaction obtainFragmentTransaction(int index) {
        FragmentTransaction ft = fragmentActivity.getSupportFragmentManager().beginTransaction();
        // 设置切换动画
        if (index > currentTab) {
            ft.setCustomAnimations(R.anim.slide_left_in, R.anim.slide_left_out);
        } else {
            ft.setCustomAnimations(R.anim.slide_right_in, R.anim.slide_right_out);
        }
        return ft;
    }

    public int getCurrentTab() {
        return currentTab;
    }

    public Fragment getCurrentFragment() {
        return fragments.get(currentTab);
    }

    public OnRgsExtraCheckedChangedListener getOnRgsExtraCheckedChangedListener() {
        return onRgsExtraCheckedChangedListener;
    }

    public void setOnRgsExtraCheckedChangedListener(OnRgsExtraCheckedChangedListener onRgsExtraCheckedChangedListener) {
        this.onRgsExtraCheckedChangedListener = onRgsExtraCheckedChangedListener;
    }

    /**
     * 切换tab额外功能功能接口
     */
    public static class OnRgsExtraCheckedChangedListener {
        public void OnRgsExtraCheckedChanged(RadioGroup radioGroup, int checkedId) {

        }
    }

}