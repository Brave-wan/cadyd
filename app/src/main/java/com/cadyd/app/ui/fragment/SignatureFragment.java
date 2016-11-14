package com.cadyd.app.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.cadyd.app.JConstant;
import com.cadyd.app.R;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.asynctask.DataLoader;
import com.cadyd.app.asynctask.retrofitmode.BaseRequestInfo;
import com.cadyd.app.asynctask.retrofitmode.HttpMethods;
import com.cadyd.app.asynctask.retrofitmode.ProgressSubscriber;
import com.cadyd.app.asynctask.retrofitmode.SubscriberOnNextListener;
import com.cadyd.app.model.CarePage;
import com.cadyd.app.model.LiveHotInfo;
import com.cadyd.app.model.LiveNewInfo;
import com.cadyd.app.model.StringModel;
import com.cadyd.app.model.Subject;
import com.cadyd.app.ui.base.BaseFragement;
import com.cadyd.app.utils.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import okhttp3.RequestBody;
import rx.Subscriber;

/**
 * 修改签名  个人签名
 */
public class SignatureFragment extends BaseFragement {

    @Bind(R.id.edit_signature)
    EditText edit_signature;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return setView(R.layout.fragment_signature, "个人签名", true, "保存");
    }

    @Override
    protected void initView() {
        layout.rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //保存签名
                save();
            }
        });
    }

    private void save() {
        String content = "这家伙很懒什么也没有留下";
        if (!StringUtils.isEmpty(edit_signature.getText().toString())) {
            content = edit_signature.getText().toString();
        }
        Map<Object, Object> map = new HashMap<>();
        map.put("signature", content);
        ApiClient.send(activity, JConstant.UPDATASIGNATURE, map, String.class, new DataLoader<String>() {
            @Override
            public void task(String data) {

            }

            @Override
            public void succeed(String data) {
                toastSuccess("修改成功");
                Log.i("xiongmao", "----------" + data);
                finishActivity();
            }

            @Override
            public void error(String message) {

            }
        }, JConstant.UPDATASIGNATURE);

    }

    @Override
    public boolean onBackPressed() {
        return false;
    }
}
