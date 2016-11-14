package com.cadyd.app.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cadyd.app.R;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.asynctask.retrofitmode.BaseRequestInfo;
import com.cadyd.app.asynctask.retrofitmode.HttpMethods;
import com.cadyd.app.asynctask.retrofitmode.ProgressSubscriber;
import com.cadyd.app.asynctask.retrofitmode.SubscriberOnNextListener;
import com.cadyd.app.model.MessageDetailsInfo;
import com.cadyd.app.model.MessageListInfo;
import com.cadyd.app.ui.base.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import okhttp3.RequestBody;

/**
 * @Description: ${todo}(这里用一句话描述这个方法的作用)}$
 * 作者：Jerry
 * 邮箱：185214487@qq.com
 */
public class MessageDetailsActivity extends BaseActivity {
    @Bind(R.id.message_details_webview)
    WebView webView;
    @Bind(R.id.message_details_context)
    TextView messageDetailsContext;
    @Bind(R.id.message_details_time)
    TextView messageDetailsTime;
    @Bind(R.id.message_details_bt)
    LinearLayout messageDetailsBt;
    @Bind(R.id.message_details_lay)
    LinearLayout messageDetailsLay;
    private String urlPath = "";
    private String messageId = "";
    private MessageDetailsInfo messageDetailsInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activty_message_details, "消息详情", true);
        initView();
    }

    private void initView() {
        urlPath = getIntent().getStringExtra("url");
        messageId = getIntent().getStringExtra("messageId");
        if (TextUtils.isEmpty(urlPath)) {
            messageDetailsLay.setVisibility(View.VISIBLE);
            webView.setVisibility(View.GONE);
            getSignMessageDetail();
        } else {
            webView.setVisibility(View.VISIBLE);
            messageDetailsLay.setVisibility(View.GONE);
            //优先使用缓存
            webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            //支持JavaScript
            webView.getSettings().setJavaScriptEnabled(true);
            webView.loadUrl(urlPath);
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }
            });
        }
    }

    /**
     * 获取消息详情
     */
    public void getSignMessageDetail() {
        Map<Object, Object> map = new HashMap<>();
        map.put("messageId", messageId);
        RequestBody key = ApiClient.getRequestBody(map);
        HttpMethods.getInstance().getSignMessageDetail(new ProgressSubscriber<BaseRequestInfo<MessageDetailsInfo>>(new SubscriberOnNextListener<BaseRequestInfo<MessageDetailsInfo>>() {
            @Override
            public void onNext(BaseRequestInfo<MessageDetailsInfo> o) {
                messageDetailsInfo = o.getData();
                messageDetailsContext.setText(messageDetailsInfo.getContent());
                messageDetailsTime.setText("有效时间：" + messageDetailsInfo.getStartTime() + "至" + messageDetailsInfo.getEndTime());
                if (messageDetailsInfo.getHandleStatus() == 1) {
                    messageDetailsBt.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onLogicError(String msg) {

            }
        }, MessageDetailsActivity.this), key);
    }

    /**
     * 接受签约
     *
     * @param view
     */
    public void AcceptClick(View view) {
        if (messageDetailsInfo == null) {
            return;
        }
        doSignWithMerchant(1);
    }

    /**
     * 不接受签约
     *
     * @param view
     */
    public void NotAcceptClick(View view) {
        if (messageDetailsInfo == null) {
            return;
        }
        doSignWithMerchant(2);
    }

    /**
     * 签约操作
     */
    public void doSignWithMerchant(int actionType) {
        Map<Object, Object> map = new HashMap<>();
        map.put("signId", messageDetailsInfo.getSginId());
        map.put("messageId", messageId);
        map.put("actionType", actionType);
        RequestBody key = ApiClient.getRequestBody(map);
        HttpMethods.getInstance().doSignWithMerchant(new ProgressSubscriber<BaseRequestInfo>(new SubscriberOnNextListener<BaseRequestInfo>() {
            @Override
            public void onNext(BaseRequestInfo o) {
                messageDetailsBt.setVisibility(View.GONE);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onLogicError(String msg) {

            }
        }, MessageDetailsActivity.this), key);
    }

    //改写物理按键——返回的逻辑
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webView.canGoBack()) {
                webView.goBack();//返回上一页面
                return true;
            } else {
                finish();//退出程序
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
