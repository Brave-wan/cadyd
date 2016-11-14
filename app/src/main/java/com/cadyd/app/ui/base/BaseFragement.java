package com.cadyd.app.ui.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import butterknife.ButterKnife;

import cn.jpush.android.api.JPushInterface;

import com.cadyd.app.AppManager;
import com.cadyd.app.JConstant;
import com.cadyd.app.MyApplication;
import com.cadyd.app.R;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.interfaces.BackHandledInterface;
import com.cadyd.app.ui.activity.CommonActivity;
import com.cadyd.app.ui.view.BaseLayout;
import com.cadyd.app.ui.view.ToastView;
import com.cadyd.app.ui.view.toast.ToastUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.wcy.android.utils.LogUtil;
import org.wcy.common.utils.StringUtil;

public abstract class BaseFragement extends Fragment {
    protected FragmentActivity activity;
    protected Context baseContext;
    protected BaseLayout layout;
    protected MyApplication application;
    protected BackHandledInterface mBackHandledInterface;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        baseContext=getActivity();
        application = (MyApplication) activity.getApplication();
        AppManager.getAppManager().addFragment(this);
        if (!(getActivity() instanceof BackHandledInterface)) {
            throw new ClassCastException("Hosting Activity must implement BackHandledInterface");
        } else {
            this.mBackHandledInterface = (BackHandledInterface) getActivity();
        }
    }

    /**
     * 所有继承BackHandledFragment的子类都将在这个方法中实现物理Back键按下后的逻辑
     * FragmentActivity捕捉到物理返回键点击事件后会首先询问Fragment是否消费该事件
     * 如果没有Fragment消息时FragmentActivity自己才会消费该事件
     */
    public abstract boolean onBackPressed();

    public View setView(int layoutID, String title, boolean isBack) {
        return setView(layoutID, title, isBack, null);
    }

    public View setView(LayoutInflater inflat, int layoutID) {
        // 获取对象
        View contentView = inflat.inflate(layoutID, null);
        ButterKnife.bind(this, contentView);
        return contentView;
    }

    public View setView(int layoutID, String title, boolean isBack, String right) {
        layout = new BaseLayout(activity, layoutID);
        layout.setTitle(title);
        if (isBack) {
            layout.leftButton.setVisibility(View.VISIBLE);
        } else {
            layout.leftButton.setVisibility(View.GONE);
        }
        if (right != null && !right.equals("")) {
            layout.rightButton.setVisibility(View.VISIBLE);
            layout.rightButton.setText(right);
        } else {
            layout.rightButton.setVisibility(View.GONE);
        }
        ButterKnife.bind(this, layout);
        layout.title_relative.getBackground().setAlpha(255);
        setLinner();
        return layout;
    }

    private void setLinner() {
        layout.leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                TitleBarEvent(layout.leftButtonId);
            }
        });
        layout.rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                TitleBarEvent(layout.rightButtonId);
            }
        });
    }

    /**
     * 顶部button按钮单击事件抽象方法 具体需要执行的操作自己实现
     *
     * @param btnID buttonid
     */
    protected void TitleBarEvent(int btnID) {
        finishFramager();
    }

    public void finishFramager() {
        if (activity.getSupportFragmentManager().getBackStackEntryCount() == 1) {
            AppManager.getAppManager().finishActivity(activity);
        } else {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            if (fragmentManager == null) {
                LogUtil.i(BaseFragement.class, "fragmentManager为空-绝逼有问题");
                return;
            }
            AppManager.getAppManager().finishFragment(this, fragmentManager.beginTransaction());
            activity.getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        //告诉FragmentActivity，当前Fragment在栈顶
        mBackHandledInterface.setSelectedFragment(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    public void startActivity(Class<?> c, boolean isclose) {
        startActivity(new Intent(activity, c));
        if (isclose) {
            activity.finish();
        }
    }

    public void startActivity(Intent intent, boolean isclose) {
        startActivity(intent);
        if (isclose) {
            activity.finish();
        }
    }

    public void CommonActivitys(int index) {
        Intent intent = new Intent(activity, CommonActivity.class);
        intent.putExtra(JConstant.EXTRA_INDEX, index);
        activity.startActivity(intent);
    }

    protected void replaceFragment(int containerViewId, Fragment fragment) {
        FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.push_left_in, R.anim.push_left_in, R.anim.back_left_in, R.anim.back_right_out);
        ft.add(containerViewId, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.addToBackStack(null);
        ft.commitAllowingStateLoss();
    }

    protected abstract void initView();

    public void toast(Object obj) {
        if (obj != null && StringUtil.hasText(obj.toString())) {
            ToastView.show(activity, obj.toString());
        } else {
            ToastView.show(activity, "数据异常");
        }
    }

    public void toastError(Object obj) {
        toast(obj, ToastUtils.ERROR_TYPE);
    }

    public void toastWarning(Object obj) {
        toast(obj, ToastUtils.WARNING_TYPE);
    }

    public void toastSuccess(Object obj) {
        toast(obj, ToastUtils.SUCCESS_TYPE);
    }

    public void finishActivity() {
        AppManager.getAppManager().finishActivity(activity);
    }

    private void toast(Object obj, int type) {
        if (obj != null && StringUtil.hasText(obj.toString())) {
            ToastUtils.show(activity, obj.toString(), type);
        } else {
            ToastUtils.show(activity, "数据异常", ToastUtils.ERROR_TYPE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        JPushInterface.onFragmentResume(getContext(), getClass().getName());
    }

    @Override
    public void onPause() {
        super.onPause();
        JPushInterface.onFragmentPause(getContext(), getClass().getName());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(getActivity());
        LogUtil.i(BaseFragement.class, "fragment 释放");
        ApiClient.listener.cleanBitmap();
        ImageLoader.getInstance().clearMemoryCache();
        ImageLoader.getInstance().clearDiskCache();
        ImageLoader.getInstance().stop();
    }


}
