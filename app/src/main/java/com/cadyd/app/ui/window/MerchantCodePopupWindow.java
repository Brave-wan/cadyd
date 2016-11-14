package com.cadyd.app.ui.window;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.cadyd.app.R;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.asynctask.retrofitmode.BaseRequestInfo;
import com.cadyd.app.asynctask.retrofitmode.HttpMethods;
import com.cadyd.app.asynctask.retrofitmode.ProgressSubscriber;
import com.cadyd.app.asynctask.retrofitmode.SubscriberOnNextListener;
import com.cadyd.app.comm.KeyBoardUtils;
import com.cadyd.app.ui.activity.LiveStartActivity;
import com.cadyd.app.ui.view.expandablebutton.Blur;
import com.cadyd.app.utils.BitmapUtil;
import com.cadyd.app.utils.Util;

import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;

/**
 * Created by rance on 2016/10/5.
 * 商家码输出PopupWindow
 */
public class MerchantCodePopupWindow extends PopupWindow implements View.OnClickListener {
    private View enSure;
    private ImageView close;
    private EditText code;
    private Context mContext;
    private LinearLayout linearLayout;
    private View popview;
    private Blur blur;
    private ImageView blurImageView;

    public MerchantCodePopupWindow(Context mContext) {
        this.mContext = mContext;
        popview = LayoutInflater.from(mContext).inflate(R.layout.pop_merchant_code, null);
        setContentView(popview);
        initView();
    }

    private void initView() {
        linearLayout = (LinearLayout) popview.findViewById(R.id.LinearLayout);
        linearLayout.setOnClickListener(this);
        enSure = popview.findViewById(R.id.dialog_merchant_ensure);
        enSure.setOnClickListener(this);
        close = (ImageView) popview.findViewById(R.id.dialog_merchant_close);
        close.setOnClickListener(this);
        code = (EditText) popview.findViewById(R.id.dialog_merchant_code);

        setBackgroundDrawable(null);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置可以获取焦点，否则弹出菜单中的EditText是无法获取输入的
        setFocusable(true);
        //防止虚拟软键盘被弹出菜单遮住
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setOutsideTouchable(false);
    }

    /**
     * 显示popwindow
     *
     * @param view
     */
    public void showPop(View view) {
        showAtLocation(view, Gravity.BOTTOM, 0, 0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.LinearLayout:
                KeyBoardUtils.closeKeybord(code, mContext);
                break;
            case R.id.dialog_merchant_ensure:
                String codeStr = code.getText().toString();
                if (TextUtils.isEmpty(codeStr)) {
                    Toast.makeText(mContext, "请输入商家码", Toast.LENGTH_SHORT).show();
                    return;
                }
                validateSignCode(codeStr);
                break;
            case R.id.dialog_merchant_close:
                this.dismiss();
                break;
        }
    }

    /**
     * 验证商家码
     */
    public void validateSignCode(final String codeStr) {
        Map<Object, Object> map = new HashMap<>();
        map.put("merchantPassword", codeStr);
        RequestBody key = ApiClient.getRequestBody(map);
        HttpMethods.getInstance().validateSignCode(new ProgressSubscriber<BaseRequestInfo>(new SubscriberOnNextListener<BaseRequestInfo>() {
            @Override
            public void onNext(BaseRequestInfo o) {
                Intent intent = new Intent(mContext, LiveStartActivity.class);
                intent.putExtra("merchantCode", codeStr);
                mContext.startActivity(intent);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onLogicError(String msg) {

            }
        }, mContext), key);
    }
}
