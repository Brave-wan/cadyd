package com.cadyd.app.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;

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

public class OneYuanDetialWebViewActivity extends BaseActivity {
    private ProgressWebView webView;
    private String goodsId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_comm_web_view, "", true);
        webView = find(R.id.progress_web);

//        webView.getSettings().setUseWideViewPort(true);//关键点
//        webView.getSettings().setLoadWithOverviewMode(true);
//        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        goodsId = getIntent().getStringExtra("goodsId");
        layout.setTitle("商品详情");

//        webView.setOntitle(new ProgressWebView.onTitle() {
//            @Override
//            public void UpdateTitle(String title) {
//                layout.setTitle(title);
//            }
//        });
        getHtml();
    }

    private void getHtml() {
        //http://home.cadyd.com/groupBuyProduct/queryProductDescription/779f9cf5a7354473988164c0a883e892
        Map<String, Object> map = new HashMap<>();
        map.put("productId", goodsId);
        int tag = JConstant.YIYUANMANAGER;
        ApiClient.send(activity, JConstant.YIYUANMANAGER, map, String.class, new DataLoader<String>() {
            @Override
            public void task(String data) {

            }

            @Override
            public void succeed(String data) {
                Log.i("MyHtml", data);
                String html = data;
//                try {
//                    JSONObject object = new JSONObject(data);
//                    html = object.getString("imgdes");
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                webView.setOntitle(new ProgressWebView.onTitle() {
//                    @Override
//                    public void UpdateTitle(String title) {
//                        layout.setTitle(title);
//                    }
//                });
                webView.loadDataWithBaseURL(null, html, "text/html", "utf-8", null);
            }

            @Override
            public void error(String message) {

            }
        }, tag);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ApiClient.cancelRequest(JConstant.YIYUANMANAGER);
    }
}
