package com.cadyd.app.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cadyd.app.JConstant;
import com.cadyd.app.MyApplication;
import com.cadyd.app.R;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.asynctask.DataLoader;
import com.cadyd.app.asynctask.retrofitmode.BaseRequestInfo;
import com.cadyd.app.asynctask.retrofitmode.HttpMethods;
import com.cadyd.app.asynctask.retrofitmode.ProgressSubscriber;
import com.cadyd.app.asynctask.retrofitmode.SubscriberOnNextListener;
import com.cadyd.app.comm.KeyBoardUtils;
import com.cadyd.app.comm.LoginApi;
import com.cadyd.app.interfaces.OnLoginListener;
import com.cadyd.app.model.SignInModel;
import com.cadyd.app.model.UserInfo;
import com.cadyd.app.ui.view.ToastView;

import org.wcy.android.utils.PreferenceUtils;
import org.wcy.common.utils.StringUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import okhttp3.RequestBody;

/**
 * Created by Administrator on 2016/5/3.
 * 登录页面
 */
public class SignInActivity extends Activity {

    @Bind(R.id.ui_sign_in_user_name)
    EditText username;

    @Bind(R.id.ui_sign_in_password)
    EditText password;

    @Bind(R.id.sign_root)
    RelativeLayout signRoot;

    @OnClick(R.id.back)
    public void onBack(View view) {
        KeyBoardUtils.closeKeybord(username, SignInActivity.this);
        KeyBoardUtils.closeKeybord(password, SignInActivity.this);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.sign_in_activity);
        ButterKnife.bind(this);

        SignInModel user = PreferenceUtils.getObject(this, SignInModel.class);
        if (user != null && StringUtil.hasText(user.phone)) {
            username.setText(user.phone);
            password.setText(user.password);
        }

//        RememberPassword.setChecked(user.isRember);
//        layout.rightButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });

//        if (JConstant.isHttp) {
//            SharedPreferences sharedata = getSharedPreferences("data", 0);
//            String data = sharedata.getString("HttpUrl", null);
//            if (StringUtil.hasText(data)) {
//                HttpUrl.setText(data);
//            }
//        } else {
//            HttpUrl.setVisibility(View.GONE);
//        }

        //监听软键盘的回车事件
        password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //当actionId == XX_SEND 或者 XX_DONE时都触发
                //或者event.getKeyCode == ENTER 且 event.getAction == ACTION_DOWN时也触发
                //注意，这是一定要判断event != null。因为在某些输入法上会返回null。
                if (actionId == EditorInfo.IME_ACTION_SEND
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction())) {
                    //处理事件
                    KeyBoardUtils.closeKeybord(password, SignInActivity.this);//关闭软键盘
                    send();
                }
                return false;
            }
        });

        signRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyBoardUtils.closeKeybord(username, SignInActivity.this);
                KeyBoardUtils.closeKeybord(password, SignInActivity.this);
//                KeyBoardUtils.closeKeybord(HttpUrl, SignInActivity.this);
            }
        });
    }

    //登录
    public void SendLoginClick(View view) {
        send();
    }

    private void send() {
        KeyBoardUtils.closeKeybord(username, SignInActivity.this);
        KeyBoardUtils.closeKeybord(password, SignInActivity.this);

        final String phone = username.getText().toString();
        final String pwd = this.password.getText().toString();

        if (phone == null || "".equals(phone) || pwd == null || "".equals(pwd)) {
            ToastView toast = new ToastView();
            toast.show(SignInActivity.this, "帐号密码不能为空");
        } else {
            String registrationId = JPushInterface.getRegistrationID(SignInActivity.this);
            Log.i("wan", "registration-----" + registrationId);
            Map<String, Object> map = new HashMap<>();
            map.put("phone", phone);
            map.put("password", pwd);
            map.put("type", "0");
            map.put("regid", registrationId);
            ApiClient.send(SignInActivity.this, JConstant.LOGIN_, map, true, SignInModel.class, new DataLoader<SignInModel>() {
                @Override
                public void task(String data) {

                }

                @Override
                public void succeed(SignInModel data) {
                    ToastView toast = new ToastView();
                    toast.show(SignInActivity.this, "登录成功");
                    //如果选择记住 帐号 密码
                    data.phone = phone;
                    data.password = pwd;
                    PreferenceUtils.setObject(SignInActivity.this, data);
                    ((MyApplication) getApplication()).model = data;
                    finish();
                }

                @Override
                public void error(String message) {

                }
            }, JConstant.LOGIN_);
        }
    }

    //注册
    public void RegisterClick(View view) {
        Intent intent = new Intent(SignInActivity.this, RegisterActivity.class);
        intent.putExtra("title", "注册");
        startActivity(intent);
    }

    //忘记密码
    public void ForgetPasswordClick(View view) {
        Intent intent = new Intent(SignInActivity.this, RegisterActivity.class);
        intent.putExtra("title", "找回密码");
        startActivity(intent);
    }

    //微信登录
    public void WeChatLoginClick(View view) {
        login(Wechat.NAME);
    }

    //qq登录
    public void QQLoginClick(View view) {
        login(QQ.NAME);
    }

    //新浪登录
    public void SinaLoginClick(View view) {
        login(SinaWeibo.NAME);
    }

    /*
     * 演示执行第三方登录/注册的方法
	 * <p>
	 * 这不是一个完整的示例代码，需要根据您项目的业务需求，改写登录/注册回调函数
	 *
	 * @param platformName 执行登录/注册的平台名称，如：SinaWeibo.NAME
	 */
    private void login(final String platformName) {
        LoginApi api = new LoginApi();
        //设置登陆的平台后执行登陆的方法
        api.setPlatform(platformName);
        api.setOnLoginListener(new OnLoginListener() {
            public void onLogin(UserInfo userInfo) {
                userInfo.setPlatformName(platformName);
                doThirdLogin(userInfo);
            }
        });
        api.login(this);
    }

    private void doThirdLogin(final UserInfo userInfo) {//（用户类型（1-微信，2-QQ，3-新浪微博））
        Map<Object, Object> map = new HashMap<>();
        map.put("openid", userInfo.getToken());
        String usertype = "";
        if (userInfo.getPlatformName().equals(Wechat.NAME)) {
            usertype = "1";
        } else if (userInfo.getPlatformName().equals(QQ.NAME)) {
            usertype = "2";
        } else {
            usertype = "3";
        }
        map.put("usertype", usertype);
        RequestBody key = ApiClient.getRequestBody(map, true);
        HttpMethods.getInstance().doThirdLogin(new ProgressSubscriber<BaseRequestInfo<SignInModel>>(new SubscriberOnNextListener<BaseRequestInfo<SignInModel>>() {
            @Override
            public void onNext(BaseRequestInfo<SignInModel> o) {
                SignInModel signInModel = o.getData();
                ToastView toast = new ToastView();
                toast.show(SignInActivity.this, "登录成功");
                PreferenceUtils.setObject(SignInActivity.this, signInModel);
                ((MyApplication) getApplication()).model = signInModel;
                finish();
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onLogicError(String msg) {
                Intent intent = new Intent(SignInActivity.this, LoginBindingActivity.class);
                intent.putExtra("userInfo", userInfo);
                startActivity(intent);
            }

        }, SignInActivity.this), key);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        ApiClient.cancelRequest(JConstant.LOGIN_);
    }
}
