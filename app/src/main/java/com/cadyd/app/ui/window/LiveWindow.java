package com.cadyd.app.ui.window;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.cadyd.app.JConstant;
import com.cadyd.app.R;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.asynctask.DataLoader;
import com.cadyd.app.model.LiveWindowInfo;
import com.cadyd.app.ui.view.ProgressWebView;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by root on 16-9-8.
 */

public class LiveWindow extends PopupWindow implements View.OnClickListener {
    private LayoutInflater inflater;
    private Activity mContext;
    private PopupWindow mPopupWindow;
    private LinearLayout live_rootview;
    //广告
    private ProgressWebView mWebView;


    public LiveWindow(Activity context, String url) {
        super(context);
        inflater = LayoutInflater.from(context);
        this.mContext = context;
        initView(url);
    }

    /**
     * 控件初始化
     */
    private void initView(@NonNull String url) {
        View rootView = inflater.inflate(R.layout.window_live, null);
        mPopupWindow = new PopupWindow(rootView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mWebView = (ProgressWebView) rootView.findViewById(R.id.wind_live_web);
        live_rootview = (LinearLayout) rootView.findViewById(R.id.live_rootview);
        //在PopupWindow里面就加上下面代码，让键盘弹出时，不会挡住pop窗口。
        mPopupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        setOutsideTouchable(true);
        setFocusable(true);
        //启用支持javascript
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        live_rootview.setOnClickListener(this);
        mWebView.setBackgroundColor(0);
        getDate(url);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.live_rootview:
                dismitPopWindow();
                break;
        }

    }

    public void dismitPopWindow() {
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
    }

    public void showPopWindow(View v) {
        if (mPopupWindow != null) {
            mPopupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
        }
    }

    public void getDate(@NonNull String id) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        ApiClient.send(mContext, JConstant.LIVEWINDOW, map, true, LiveWindowInfo.class, new DataLoader<LiveWindowInfo>() {
            @Override
            public void task(String data) {

            }

            @Override
            public void succeed(final LiveWindowInfo data) {
                Log.i("wan", "livedetils----" + data.getDetail());
                mWebView.setOntitle(new ProgressWebView.onTitle() {
                    @Override
                    public void UpdateTitle(String title) {
                    }
                });
                Log.i("wan", "liveWoid-----" + data.getDetail());
                mWebView.loadDataWithBaseURL(null, data.getDetail(), "text/html", "utf-8", null);
            }

            @Override
            public void error(String message) {

            }
        }, JConstant.LIVEWINDOW);
    }

}
