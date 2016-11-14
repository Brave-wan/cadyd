package com.cadyd.app.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.cadyd.app.JConstant;
import com.cadyd.app.R;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.asynctask.DataLoader;
import com.cadyd.app.ui.base.BaseActivity;
import com.cadyd.app.ui.view.ProgressWebView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CommWebViewActivity extends BaseActivity {
    private ProgressWebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_comm_web_view, "", true);
        webView = find(R.id.progress_web);
        if (getIntent().getBooleanExtra("type", false)) {//当满足此条件时，则显示用户协议的html
            layout.titleText.setText("用户协议");
            getHtml();
        } else {
            webView.setOntitle(new ProgressWebView.onTitle() {
                @Override
                public void UpdateTitle(String title) {
                    layout.setTitle(title);
                }
            });
            webView.addJavascriptInterface(new myJavaScriptInterface(), "demo");
            Log.i("webView", getIntent().getStringExtra("url"));
            webView.loadUrl(getIntent().getStringExtra("url"));
        }
    }

    private void getHtml() {
        Map<String, Object> map = new HashMap<>();
        map.put("tcode", "002");//001 积分 002用户 003入驻
        int tag = JConstant.AGREEMENTHTML;
        ApiClient.send(activity, JConstant.AGREEMENTHTML, map, String.class, new DataLoader<String>() {
            @Override
            public void task(String data) {

            }

            @Override
            public void succeed(String data) {
                String html = "";
                try {
                    JSONObject object = new JSONObject(data);
                    html = object.getString("content");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                webView.setOntitle(new ProgressWebView.onTitle() {
                    @Override
                    public void UpdateTitle(String title) {
                        layout.setTitle(title);
                    }
                });
                webView.loadDataWithBaseURL(null, html, "text/html", "utf-8", null);
//                webView.loadData(html, "text/html", "UTF-8");
            }

            @Override
            public void error(String message) {

            }
        }, tag);
    }

    private class myJavaScriptInterface {
        @JavascriptInterface
        public void getUserinfo(String id) {
            Intent intent = new Intent(activity, GoodsDetailActivity.class);
            intent.putExtra("gid", id);
            activity.startActivity(intent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ApiClient.cancelRequest(JConstant.AGREEMENTHTML);
    }
}
