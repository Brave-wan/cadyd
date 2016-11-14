package com.cadyd.app.ui.fragment.user;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import butterknife.Bind;
import butterknife.OnClick;

import com.cadyd.app.JConstant;
import com.cadyd.app.R;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.asynctask.DataLoader;
import com.cadyd.app.comm.KeyBoardUtils;
import com.cadyd.app.interfaces.TowObjectParameterInterface;
import com.cadyd.app.ui.activity.RegisterActivity;
import com.cadyd.app.ui.base.BaseFragement;

import org.wcy.android.utils.ActivityUtil;
import org.wcy.common.utils.StringUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 修改手机号码
 */
public class EditPhoneFragment extends BaseFragement {
    private TowObjectParameterInterface towObjectParameterInterface;

    public void setTowObjectParameterInterface(TowObjectParameterInterface towObjectParameterInterface) {
        this.towObjectParameterInterface = towObjectParameterInterface;
    }

    @Bind(R.id.ui_register_phone)
    EditText phone;
    @Bind(R.id.ui_register_get_code)
    Button getCode;
    @Bind(R.id.ui_register_set_code)
    EditText setCode;
    @Bind(R.id.password)
    EditText password;
    @Bind(R.id.ui_register_send)
    Button send;
    @Bind(R.id.edit_phone)
    LinearLayout edit_phone;

    private final int TIME = 30;
    private Timer timer;
    private int Time;

    private codes codes;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return setView(R.layout.fragment_edit_phone, "修改手机号码", true);
    }

    @Override
    protected void initView() {
        Time = TIME;
        queryMsgCode();
        edit_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyBoardUtils.closeKeybord(phone, activity);
                KeyBoardUtils.closeKeybord(setCode, activity);
                KeyBoardUtils.closeKeybord(password, activity);
            }
        });
    }

    private class codes {
        //  "smsCode": "smsCode",
        // "uuid": "F11F265FBF884138A24A8988937383F4",
        // "sessionid": "583C45798732F54814B58FDE231EBB52"
        public String smsCode;
        public String tel_token;
        public String tel_sessionid;
    }

    //获取图片验证码
    private void queryMsgCode() {
        ApiClient.send(activity, JConstant.QUERYMSGCODE, null, codes.class, new DataLoader<codes>() {
            @Override
            public void task(String data) {

            }

            @Override
            public void succeed(codes data) {
                codes = data;
            }

            @Override
            public void error(String message) {

            }
        }, JConstant.QUERYMSGCODE);
    }

    @OnClick(R.id.ui_register_get_code)
    public void OnGetCode(View view) {
        final String phone = this.phone.getText().toString();
        if (!StringUtil.hasText(phone)) {
            toast("请先输入手机号");
        } else if (phone.length() < 11 || !"1".equals(phone.subSequence(0, 1))) {
            toast("请输入正确的手机号");
        } else {
            if (codes == null) {
                queryMsgCode();
                toast("获取失败请重试");
                return;
            }
            /**启动验证码倒计时*/
            getCode.setEnabled(false);
            getCode.setBackgroundResource(R.color.gray);
            Waiting();

            Map<String, Object> map = new HashMap<>();
            map.put("tel_phone", this.phone.getText().toString());
            map.put("smsCode", codes.smsCode);//图片验证码
            map.put("tel_sessionId", codes.tel_sessionid);
            map.put("tel_token", codes.tel_token);
            ApiClient.send(activity, JConstant.VCODE_, map, null, new DataLoader<String>() {
                        @Override
                        public void task(String data) {

                        }

                        @Override
                        public void succeed(String data) {

                        }

                        @Override
                        public void error(String message) {
                            toast("验证码获取失败请稍后重试");
                        }
                    }
                    , JConstant.VCODE_);
        }
    }

    //修改
    @OnClick(R.id.ui_register_send)
    public void OnSend(View view) {
        if (!ActivityUtil.isEditTextNull(password)) {
            toast("请输入你的登录密码");
            return;
        }
        final String phone = this.phone.getText().toString();
        String code = setCode.getText().toString();
        if (!StringUtil.hasText(code)) {
            toast("请输入验证码");
        }
        Map<String, Object> object = new HashMap<>();
        object.put("code", code);
        object.put("id", application.model.id);
        object.put("newPhone", phone);
        object.put("phone", application.model.phone);
        object.put("password", password.getText().toString());
        ApiClient.send(activity, JConstant.UPDATEPHONE_, object, true, null, new DataLoader<String>() {
            @Override
            public void task(String data) {

            }

            @Override
            public void succeed(String data) {
                towObjectParameterInterface.Onchange(0, 0, phone);
                toastSuccess("修改成功");
                finishFramager();
            }

            @Override
            public void error(String message) {
                toast("修改失败请稍后重试");
            }
        }, JConstant.UPDATEPHONE_);
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
                getCode.setEnabled(true);
                getCode.setBackgroundResource(R.drawable.orange_button);
                Time = TIME;
                timer.cancel();
            } else {
                getCode.setText(String.valueOf("重新发送:" + Time));
            }
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ApiClient.cancelRequest(JConstant.QUERYMSGCODE);
        ApiClient.cancelRequest(JConstant.VCODE_);
        ApiClient.cancelRequest(JConstant.UPDATEPHONE_);
        if (timer != null) {
            timer.cancel();
        }
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }
}
