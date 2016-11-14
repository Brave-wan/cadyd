package com.cadyd.app.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.widget.RadioGroup;
import com.cadyd.app.MyApplication;
import com.cadyd.app.R;

import java.util.List;

/**
 * Author: wcy
 */
public class FragmentMallTabAdapter implements RadioGroup.OnCheckedChangeListener {
    private MyApplication application;
    private List<Fragment> fragments; // 一个tab页面对应一个Fragment
    private RadioGroup rgs; // 用于切换tab
    private FragmentActivity fragmentActivity; // Fragment所属的Activity
    private int fragmentContentId; // Activity中所要被替换的区域的id

    private int currentTab = 0; // 当前Tab页面索引
    private OnRgsExtraCheckedChangedListener onRgsExtraCheckedChangedListener; // 用于让调用者在切换tab时候增加新的功能

    public FragmentMallTabAdapter(FragmentActivity fragmentActivity, List<Fragment> fragments, int fragmentContentId, RadioGroup rgs) {
        this.fragments = fragments;
        this.rgs = rgs;
        this.fragmentActivity = fragmentActivity;
        this.fragmentContentId = fragmentContentId;

        application = (MyApplication) fragmentActivity.getApplication();

        // 默认显示第一页
        FragmentTransaction ft = fragmentActivity.getSupportFragmentManager().beginTransaction();
        ft.add(fragmentContentId, fragments.get(0));
        ft.commit();
        rgs.setOnCheckedChangeListener(this);
        showTab(currentTab);
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        if (checkedId == R.id.shopping_cart || checkedId == R.id.user_centent) {
            // 如果设置了切换tab额外功能功能接口
            if (null != onRgsExtraCheckedChangedListener) {
                if (!application.isLogin()) {
                    onRgsExtraCheckedChangedListener.OnRgsExtraCheckedChanged(radioGroup, checkedId, checkedId == R.id.shopping_cart ? 2 : 3);
                    return;
                }
            }
           /* RadioButton rb = (RadioButton) radioGroup.getChildAt(currentTab);
            rb.setChecked(true);*/
        }
        //else {
        for (int i = 0; i < rgs.getChildCount(); i++) {
//                if (i != 2 && i != 3) {
            if (rgs.getChildAt(i).getId() == checkedId) {
                Fragment target = fragments.get(i);
                FragmentTransaction ft = obtainFragmentTransaction(i);

                Fragment souce = getCurrentFragment();
                if (souce != null) {
                    if (souce != target) {
                        if (target.isAdded()) {
                            ft.hide(souce).show(target);
                        } else {
                            ft.hide(souce).add(fragmentContentId, target);
                        }
                    }
                } else {
                    ft.add(fragmentContentId, target);
                }
                ft.commitAllowingStateLoss();
                showTab(i); // 显示目标tab

                // 如果设置了切换tab额外功能功能接口
                if (null != onRgsExtraCheckedChangedListener) {
                    onRgsExtraCheckedChangedListener.OnRgsExtraCheckedChanged(radioGroup, checkedId, i);
                }

            }
        }

//        }
//        }
    }

    /**
     * 切换tab
     *
     * @param idx
     */
    private void showTab(int idx) {
/*        for (int i = 0; i < fragments.size(); i++) {
            Fragment fragment = fragments.get(i);
            FragmentTransaction ft = obtainFragmentTransaction(idx);
            if (idx == i) {
                ft.show(fragment);
            } else {
                ft.hide(fragment);
            }
            ft.commitAllowingStateLoss();
        }*/
        currentTab = idx; // 更新目标tab为当前tab
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
        public void OnRgsExtraCheckedChanged(RadioGroup radioGroup, int checkedId, int index) {

        }
    }

}