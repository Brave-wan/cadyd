package com.cadyd.app.ui.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.cadyd.app.AppManager;
import com.cadyd.app.MyApplication;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.ui.view.BaseLayout;
import com.cadyd.app.ui.view.ToastView;
import com.cadyd.app.ui.view.toast.ToastUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.wcy.android.utils.ActivityUtil;
import org.wcy.common.utils.StringUtil;

import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;


public abstract class BaseActivity extends AppCompatActivity {
    protected Activity activity;
    public BaseLayout layout;
    protected MyApplication application;

    /**
     * 顶部button按钮单击事件抽象方法 具体需要执行的操作自己实现
     *
     * @param btnID buttonid
     */
    protected void TitleBarEvent(int btnID) {
        finishActivity();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        activity = this;
        application = (MyApplication) getApplication();
        ActivityUtil.setScreenVertical(this);
        getWindow().setBackgroundDrawable(null);
        AppManager.getAppManager().addActivity(this);
    }

    protected void onCreate(Bundle savedInstanceState, boolean falg) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        activity = this;
        application = (MyApplication) getApplication();
        if (falg) {
            ActivityUtil.setScreenVertical(this);
        }
        AppManager.getAppManager().addActivity(this);
    }


    public void setView(int layoutID, String title) {
        setView(layoutID, title, false, "");
    }

    public void setView(int layoutID, String title, boolean isBack) {
        setView(layoutID, title, isBack, "");
    }

    public void setView(int layoutID, String title, boolean isBack, String right) {
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
        setContentView(layout);
        ButterKnife.bind(this);
        layout.title_relative.getBackground().setAlpha(255);
        setLinner();
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        ButterKnife.bind(this);
    }

    private void setLinner() {
        layout.leftButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                TitleBarEvent(layout.leftButtonId);
            }
        });

        layout.rightButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                TitleBarEvent(layout.rightButtonId);
            }
        });
    }

    /**
     * 应用于Activity的获取控件实例
     *
     * @param id
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T extends View> T find(int id) {
        return (T) activity.findViewById(id);
    }

    public void startActivity(Class<?> c, boolean isclose) {
        startActivity(new Intent(activity, c));
        if (isclose) {
            finishActivity();
        }
    }


    public void finishActivity() {
        AppManager.getAppManager().finishActivity(this);
    }

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

    private void toast(Object obj, int type) {
        if (obj != null && StringUtil.hasText(obj.toString())) {
            ToastUtils.show(activity, obj.toString(), type);
        } else {
            ToastUtils.show(activity, "数据异常", ToastUtils.ERROR_TYPE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        Log.i("wan", "activity shi fang");
        AppManager.getAppManager().finishActivity(this);
        ApiClient.listener.cleanBitmap();
        ImageLoader.getInstance().clearMemoryCache();
        ImageLoader.getInstance().clearDiskCache();
        ImageLoader.getInstance().stop();
    }

    public void  showToas(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
