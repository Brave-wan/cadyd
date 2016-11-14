package com.cadyd.app.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cadyd.app.R;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.model.UserInfo;
import com.cadyd.app.ui.base.BaseActivity;
import com.cadyd.app.ui.view.CircleImageView;

import butterknife.Bind;

/**
 * @Description: 登录绑定
 * 作者：Jerry
 * 邮箱：185214487@qq.com
 */
public class LoginBindingActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.loginBinding_associate)
    Button assiciate;

    @Bind(R.id.loginBinding_regist)
    Button regist;
    @Bind(R.id.loginBinding_head)
    CircleImageView loginBindingHead;
    @Bind(R.id.loginBinding_nickName)
    TextView loginBindingNickName;
    @Bind(R.id.loginBinding_Name)
    TextView loginBindingName;

    private UserInfo userInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_loginbinding, "登录绑定", true);
        assiciate.setOnClickListener(this);
        regist.setOnClickListener(this);
        initView();
    }

    private void initView() {
        userInfo = (UserInfo) getIntent().getSerializableExtra("userInfo");
        ApiClient.displayImage(userInfo.getUserIcon(), loginBindingHead);
        loginBindingNickName.setText("亲爱的" + userInfo.getPlatformName() + "用户：");
        loginBindingName.setText(userInfo.getUserName());
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.loginBinding_associate:
                intent = new Intent(LoginBindingActivity.this, LoginBindTwoActivity.class);
                break;

            case R.id.loginBinding_regist:
                intent = new Intent(LoginBindingActivity.this, RegistBindingActivity.class);
                break;
        }
        intent.putExtra("userInfo", userInfo);
        startActivity(intent);
    }
}
