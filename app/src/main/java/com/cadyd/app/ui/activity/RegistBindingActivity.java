package com.cadyd.app.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.cadyd.app.JConstant;
import com.cadyd.app.MyApplication;
import com.cadyd.app.R;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.asynctask.DataLoader;
import com.cadyd.app.asynctask.retrofitmode.BaseRequestInfo;
import com.cadyd.app.asynctask.retrofitmode.HttpMethods;
import com.cadyd.app.asynctask.retrofitmode.ProgressSubscriber;
import com.cadyd.app.asynctask.retrofitmode.SubscriberOnNextListener;
import com.cadyd.app.comm.MyChange;
import com.cadyd.app.model.ImageVerificationCodeModel;
import com.cadyd.app.model.SignInModel;
import com.cadyd.app.model.UserInfo;
import com.cadyd.app.ui.base.BaseActivity;

import org.wcy.android.utils.PreferenceUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

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
public class RegistBindingActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.phone_number)
    EditText phoneNumber;
    @Bind(R.id.verification_code)
    EditText verificationCode;
    @Bind(R.id.get_verification_code)
    Button getVerificationVode;
    @Bind(R.id.again_password)
    EditText againPassword;
    @Bind(R.id.confirm_user_agreement)
    CheckBox confirmUserAgreement;//确认用户协议
    @Bind(R.id.send)
    Button send;

    private UserInfo userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_registbinding, "注册绑定", true);
        init();
    }

    private void init() {
        Time = TIME;
        String test = "同意《“第一点”用户注册协议》";
        confirmUserAgreement.setText(MyChange.ChangeTextColor(test, 2, test.length(), getResources().getColor(R.color.fc7f45)));
        getVerificationVode.setOnClickListener(this);
        confirmUserAgreement.setOnClickListener(this);
        send.setOnClickListener(this);
        userInfo = (UserInfo) getIntent().getExtras().getSerializable("userInfo");
        queryMsgCode();//获取图片验证码
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.get_verification_code://获取验证码
                getSMSCode();
                break;
            case R.id.confirm_user_agreement://查看用户协议
                Intent intent = new Intent(activity, CommWebViewActivity.class);
                intent.putExtra("type", true);
                activity.startActivity(intent);
                break;
            case R.id.send://注册
                onSend();
                break;
        }
    }

    /**
     * 获取图片验证码
     */
    private ImageVerificationCodeModel codes;

    private void queryMsgCode() {
        HttpMethods.getInstance().getImageVerificationCode(new ProgressSubscriber<BaseRequestInfo<ImageVerificationCodeModel>>(new SubscriberOnNextListener<BaseRequestInfo<ImageVerificationCodeModel>>() {
            @Override
            public void onNext(BaseRequestInfo<ImageVerificationCodeModel> o) {
                codes = o.getData();
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onLogicError(String msg) {

            }
        }, activity));
    }

    /**
     * 获取短信验证码
     */
    private void getSMSCode() {
        String phone = phoneNumber.getText().toString();
        if (phone == null || "".equals(phone)) {
            toast("请先输入手机号");
        } else if (phone.length() < 11) {
            toast("请输入正确的手机号");
        } else {
            if (codes == null) {
                queryMsgCode();
                toast("获取失败请重试");
                return;
            }
            Map<Object, Object> map = new HashMap<>();
            map.put("tel_phone", phoneNumber.getText().toString());
            map.put("smsCode", codes.smsCode);//图片验证码
            map.put("tel_sessionId", codes.tel_sessionid);
            map.put("tel_token", codes.tel_token);
            RequestBody body = ApiClient.getRequestBody(map, true);
            HttpMethods.getInstance().getSMSCode(new ProgressSubscriber<BaseRequestInfo>(new SubscriberOnNextListener<BaseRequestInfo>() {
                @Override
                public void onNext(BaseRequestInfo o) {
                    toast("验证码已发送");
                    getVerificationVode.setEnabled(false);
                    getVerificationVode.setBackgroundResource(R.color.gray);
                    Waiting();
                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onLogicError(String msg) {

                }
            }, activity), body);
        }
    }

    /**
     * 短信验证码倒计时
     */
    private final int TIME = 30;
    private Timer timer;
    private int Time;

    private void Waiting() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Time--;
                hander.sendEmptyMessage(0);
            }
        }, 1, 1000);
    }

    Handler hander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (Time <= 0) {
                getVerificationVode.setText("获取验证码");
                getVerificationVode.setBackgroundResource(R.drawable.shape_loginbinding_btn);
                getVerificationVode.setEnabled(true);
                Time = TIME;
                timer.cancel();
            } else {
                if (getVerificationVode != null) {
                    getVerificationVode.setText(String.valueOf("重新发送:" + Time));
                }
            }

        }
    };

    /**
     * 确认注册
     */
    private void onSend() {

        if (!confirmUserAgreement.isChecked()) {
            toast("请确认平台注册协议");
            return;
        }

        Map<Object, Object> map = new HashMap<>();
        map.put("openid", userInfo.getToken());//第三方账号
        int type = 0;
        if (userInfo.getPlatformName().equals(QQ.NAME)) {
            type = 2;
        } else if (userInfo.getPlatformName().equals(Wechat.NAME)) {
            type = 1;
        } else if (userInfo.getPlatformName().equals(SinaWeibo.NAME)) {
            type = 3;
        }
        map.put("usertype", type);//用户类型（1-微信,2-QQ，，3-新浪微博）
        map.put("nickname", userInfo.getUserName());//第三方账号登录昵称
        map.put("phone", phoneNumber.getText().toString());//注册账号
        map.put("password", againPassword.getText().toString());//注册密码
        map.put("photo", userInfo.getUserIcon());//第三方头像地址，可为空
        RequestBody body = ApiClient.getRequestBody(map, true);
        HttpMethods.getInstance().tripartiteRegistration(new ProgressSubscriber<BaseRequestInfo<SignInModel>>(new SubscriberOnNextListener<BaseRequestInfo<SignInModel>>() {
            @Override
            public void onNext(BaseRequestInfo<SignInModel> o) {
                //数据处理
                PreferenceUtils.setObject(activity, o.getData());
                ((MyApplication) getApplication()).model = o.getData();
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onLogicError(String msg) {

            }
        }, activity), body);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
    }
}
