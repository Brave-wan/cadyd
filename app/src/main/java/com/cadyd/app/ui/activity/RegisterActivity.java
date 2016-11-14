package com.cadyd.app.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cadyd.app.JConstant;
import com.cadyd.app.R;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.asynctask.DataLoader;
import com.cadyd.app.comm.KeyBoardUtils;
import com.cadyd.app.model.ImageVerificationCodeModel;
import com.cadyd.app.model.RegisterModel;
import com.cadyd.app.ui.base.BaseActivity;
import com.cadyd.app.ui.view.ToastView;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 注册页面
 **/
public class RegisterActivity extends BaseActivity {

    @Bind(R.id.title_text)
    TextView title_text;

    @Bind(R.id.ui_register_phone)
    EditText phone;

    @Bind(R.id.ui_register_get_code)
    Button getCode;

    @Bind(R.id.ui_register_set_code)
    EditText setCode;

    @Bind(R.id.ui_register_password)
    EditText password;

    @Bind(R.id.ui_register_again_password)
    EditText againPassword;
    @Bind(R.id.regist_root)
    LinearLayout registRoot;

    @Bind(R.id.ui_register_send)
    Button send;
    private ToastView toast;
    private final int TIME = 30;
    private Timer timer;
    private int Time;


    private int MyUrl = 0;
    private DataLoader dataloader;

    public String TITLE;

    private Class myClass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().getExtras() != null) {
            TITLE = getIntent().getExtras().getString("title");
        }
        super.setView(R.layout.activity_register, TITLE, true);
        //关闭软件盘
        registRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyBoardUtils.closeKeybord(phone, RegisterActivity.this);
                KeyBoardUtils.closeKeybord(setCode, RegisterActivity.this);
                KeyBoardUtils.closeKeybord(password, RegisterActivity.this);
                KeyBoardUtils.closeKeybord(againPassword, RegisterActivity.this);
            }
        });
        layout.leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyBoardUtils.closeKeybord(phone, RegisterActivity.this);
                KeyBoardUtils.closeKeybord(setCode, RegisterActivity.this);
                KeyBoardUtils.closeKeybord(password, RegisterActivity.this);
                KeyBoardUtils.closeKeybord(againPassword, RegisterActivity.this);
                finishActivity();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        toast = new ToastView();
        if ("注册".equals(TITLE)) {
            title_text.setText("注册");
            MyUrl = JConstant.REGISTER_;
            myClass = RegisterModel.class;
            dataloader = new DataLoader<RegisterModel>() {
                @Override
                public void task(String data) {
                }

                @Override
                public void succeed(RegisterModel data) {
                    toast.show(activity, "注册成功");
                    finishActivity();
                }

                @Override
                public void error(String message) {
                }
            };
        } else if ("找回密码".equals(TITLE)) {
            send.setText("找回密码");
            title_text.setText("找回密码");
            MyUrl = JConstant.FINDPASSWORD_;
            myClass = null;
            dataloader = new DataLoader<String>() {
                @Override
                public void task(String data) {
                }

                @Override
                public void succeed(String data) {
                    toast.show(activity, "修改成功");
                    finishActivity();
                }

                @Override
                public void error(String message) {
                }
            };
        }
        Time = TIME;
        toast = new ToastView();
        queryMsgCode();
    }

    //获取图片验证码
    private void queryMsgCode() {
        ApiClient.send(activity, JConstant.QUERYMSGCODE, null, ImageVerificationCodeModel.class, new DataLoader<ImageVerificationCodeModel>() {
            @Override
            public void task(String data) {

            }

            @Override
            public void succeed(ImageVerificationCodeModel data) {
                codes = data;
            }

            @Override
            public void error(String message) {

            }
        }, JConstant.QUERYMSGCODE);
    }


    private ImageVerificationCodeModel codes;

    @OnClick(R.id.ui_register_get_code)
    public void OnGetCode(View view) {
        String phone = this.phone.getText().toString();
        if (phone == null || "".equals(phone)) {
            toast.show(activity, "请先输入手机号");
        } else if (phone.length() < 11) {
            toast.show(activity, "请输入正确的手机号");
        } else {
            if (codes == null) {
                queryMsgCode();
                toast("获取失败请重试");
                return;
            }

            Map<String, Object> map = new HashMap<>();
            map.put("tel_phone", this.phone.getText().toString());
            map.put("smsCode", codes.smsCode);//图片验证码
            map.put("tel_sessionId", codes.tel_sessionid);
            map.put("tel_token", codes.tel_token);
            ApiClient.send(activity, JConstant.VCODE_, map, true, null, new DataLoader<String>() {
                        @Override
                        public void task(String data) {

                        }

                        @Override
                        public void succeed(String data) {
                            toast("验证码已发送");
                            getCode.setEnabled(false);
                            getCode.setBackgroundResource(R.color.gray);
                            Waiting();
                        }

                        @Override
                        public void error(String message) {
                            toast("获取失败");
                        }
                    }
                    , JConstant.VCODE_);
        }
    }

    //注册
    @OnClick(R.id.ui_register_send)
    public void OnSend(View view) {

        KeyBoardUtils.closeKeybord(phone, RegisterActivity.this);
        KeyBoardUtils.closeKeybord(setCode, RegisterActivity.this);
        KeyBoardUtils.closeKeybord(password, RegisterActivity.this);
        KeyBoardUtils.closeKeybord(againPassword, RegisterActivity.this);

        String phone = this.phone.getText().toString();
        String password = this.password.getText().toString();
        String againPassword = this.againPassword.getText().toString();
        String code = setCode.getText().toString();
        if (phone == null || "".equals(phone) || password == null || "".equals(password) || "".equals(code)) {
            toast.show(activity, "请完善信息");
        } else {
            //判断两次输入的密码
            if (password.equals(againPassword)) {
                Map<String, Object> object = new HashMap<>();
                object.put("phone", phone);
                object.put("password", password);
                object.put("code", code);
                ApiClient.send(activity, MyUrl, object, true, myClass, dataloader, JConstant.REGISTER_);
            } else {
                toast.show(activity, "重复密码有误");
                return;
            }
        }
    }

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
                getCode.setText("获取验证码");
                getCode.setBackgroundResource(R.color.Orange);
                getCode.setEnabled(true);
                Time = TIME;
                timer.cancel();
            } else {
                if (getCode != null) {
                    getCode.setText(String.valueOf("重新发送:" + Time));
                }
            }

        }
    };

    /**
     * 查看协议
     */
    @OnClick(R.id.Agreement)
    public void onAgreement(View view) {
        //TODO 查看协议
        Intent intent = new Intent(activity, CommWebViewActivity.class);
        intent.putExtra("type", true);
        activity.startActivity(intent);
    }

    @OnClick(R.id.Already)
    public void onAlerady(View view) {
        KeyBoardUtils.closeKeybord(phone, RegisterActivity.this);
        KeyBoardUtils.closeKeybord(setCode, RegisterActivity.this);
        KeyBoardUtils.closeKeybord(password, RegisterActivity.this);
        KeyBoardUtils.closeKeybord(againPassword, RegisterActivity.this);
        finishActivity();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ApiClient.cancelRequest(JConstant.QUERYMSGCODE);
        ApiClient.cancelRequest(JConstant.VCODE_);
        ApiClient.cancelRequest(JConstant.REGISTER_);
        if (timer != null) {
            timer.cancel();
        }
    }

}
