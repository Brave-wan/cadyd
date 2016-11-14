package com.cadyd.app.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cadyd.app.MyApplication;
import com.cadyd.app.R;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.asynctask.retrofitmode.BaseRequestInfo;
import com.cadyd.app.asynctask.retrofitmode.HttpMethods;
import com.cadyd.app.asynctask.retrofitmode.ProgressSubscriber;
import com.cadyd.app.asynctask.retrofitmode.SubscriberOnNextListener;
import com.cadyd.app.model.SignInModel;
import com.cadyd.app.model.UserInfo;
import com.cadyd.app.ui.base.BaseActivity;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import okhttp3.RequestBody;

/**
 * @Description: ${todo}(这里用一句话描述这个方法的作用)}$
 * 作者：Jerry
 * 邮箱：185214487@qq.com
 */
public class LoginBindTwoActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.loginBInding_ed_userName)
    EditText userName;
    @Bind(R.id.loginBInding_ed_pwd)
    EditText userPass;
    @Bind(R.id.loginBInding_ed_loginBtn)
    Button loginBtn;
    @Bind(R.id.loginBInding_getBackPass)
    TextView getBackPass;
    private UserInfo userInfo;
    private String usertype;//用户类型（1-微信,2-QQ，，3-新浪微博）
    private String phone;//平台账号
    private String password; //平台密码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_loginbindingtwo, "登录绑定", true);
        initView();
    }


    private void initView() {
        userInfo = (UserInfo) getIntent().getSerializableExtra("userInfo");
        if (userInfo.getPlatformName().equals(Wechat.NAME)){
            usertype = "1";
        } else if (userInfo.getPlatformName().equals(QQ.NAME)){
            usertype = "2";
        } else if (userInfo.getPlatformName().equals(SinaWeibo.NAME)){
            usertype = "3";
        }
        getBackPass.setOnClickListener(this);
        loginBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginBInding_ed_loginBtn:
                getLoginBindingData();
                break;

            case R.id.loginBInding_getBackPass:
                Intent intent = new Intent(LoginBindTwoActivity.this, RegisterActivity.class);
                intent.putExtra("title", "找回密码");
                startActivity(intent);
                break;
        }
    }

    public void getLoginBindingData() {
        if (userName.getText().toString().trim().equals("")) {
            showToas("用户名不能为空");
            return;
        } else {
            phone = userName.getText().toString().trim();
        }

        if (userPass.getText().toString().trim().equals("")) {
            showToas("密码不能为空");
            return;
        } else {
            password = userPass.getText().toString().trim();
        }

        Map<Object, Object> map = new HashMap<>();
        map.put("openid", userInfo.getToken());
        map.put("usertype", usertype);
        map.put("phone", phone);
        map.put("password", password);
        map.put("nickname",userInfo.getUserName());

        RequestBody key = ApiClient.getRequestBody(map, true);
        HttpMethods.getInstance().getLoginBindingInfo(new ProgressSubscriber<BaseRequestInfo<SignInModel>>(new SubscriberOnNextListener<BaseRequestInfo<SignInModel>>() {
            @Override
            public void onNext(BaseRequestInfo<SignInModel> info) {
                ((MyApplication) getApplication()).model = info.getData();
                finish();
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onLogicError(String msg) {

            }
        }, LoginBindTwoActivity.this), key);

    }

}
