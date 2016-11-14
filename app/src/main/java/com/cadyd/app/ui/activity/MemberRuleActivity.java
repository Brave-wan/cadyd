package com.cadyd.app.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.WebSettings;
import android.webkit.WebView;
import butterknife.Bind;
import com.cadyd.app.JConstant;
import com.cadyd.app.R;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.asynctask.DataLoader;
import com.cadyd.app.ui.base.BaseActivity;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by yaoye on 2016-08-06.
 */
public class MemberRuleActivity extends BaseActivity {

    @Bind(R.id.content)
    WebView content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.member_rule_layout, getString(R.string.member_rule_title), true);

        WebSettings setting = content.getSettings();
        setting.setJavaScriptEnabled(true);

        setting.setUseWideViewPort(true);
        setting.setLoadWithOverviewMode(true);

        loadData();
    }

    private void loadData() {
        ApiClient.send(activity, JConstant.MEMBER_RULE, null, String.class, new DataLoader<String>() {
            @Override
            public void task(String data) {

            }

            @Override
            public void succeed(String data) {
                if (!TextUtils.isEmpty(data)) {
                    try {
                        JSONObject obj = new JSONObject(data);
                        String msg = obj.optString("content");
                        System.out.println(msg);
                        content.loadDataWithBaseURL(null, msg, "text/html", "utf-8", null);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void error(String message) {

            }
        }, JConstant.MEMBER_RULE);
    }
}
