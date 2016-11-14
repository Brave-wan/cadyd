package com.cadyd.app.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import butterknife.Bind;
import butterknife.OnClick;
import com.cadyd.app.JConstant;
import com.cadyd.app.MyApplication;
import com.cadyd.app.R;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.asynctask.DataLoader;
import com.cadyd.app.ui.base.BaseActivity;
import com.cadyd.app.ui.view.ToastView;

import java.util.HashMap;
import java.util.Map;

public class ChangePasswordActivity extends BaseActivity {

    @Bind(R.id.ui_change_password_phone)
    EditText phone;

    @Bind(R.id.ui_change_password_old_password)
    EditText OldPassword;

    @Bind(R.id.ui_change_password_new_password)
    EditText NewPassword;

    @Bind(R.id.ui_change_password_again_new_password)
    EditText AgainPassword;

    private MyApplication application;
    private ToastView toast;

    public static final int RESULT = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_change_password, "修改密码", true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        toast = new ToastView();
        application = (MyApplication) getApplication();
    }

    @OnClick(R.id.ui_change_password_send)
    public void OnSend(View view) {
        String phone = this.phone.getText().toString();
        String oldpassword = OldPassword.getText().toString();
        String newpassword = NewPassword.getText().toString();
        String againpassword = AgainPassword.getText().toString();
        if (phone == null || "".equals(phone) || !"1".equals(phone.subSequence(0, 1))) {
            toastError("请输入正确的手机号码");
        } else if (oldpassword == null || "".equals(oldpassword)) {
            toastError("请输入原始密码");
        } else if (!newpassword.equals(againpassword) || newpassword == null || "".equals(oldpassword)) {
            toastError("输入的两次密码不匹配");
        } else {
            Map<String, Object> object = new HashMap<>();
            object.put("id", application.model.id);
            object.put("phone", phone);
            object.put("password", oldpassword);
            object.put("newPwd", newpassword);
            int tag = JConstant.CHANGEPASSWORD_;
            ApiClient.send(activity, JConstant.CHANGEPASSWORD_, object, null, new DataLoader<String>() {
                @Override
                public void task(String data) {
                }

                @Override
                public void succeed(String data) {
                    toastSuccess("修改成功 , 请重新登录");
                    setResult(RESULT);
                    finishActivity();
                }

                @Override
                public void error(String message) {
                }
            }, tag);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ApiClient.cancelRequest(JConstant.CHANGEPASSWORD_);
    }
}
