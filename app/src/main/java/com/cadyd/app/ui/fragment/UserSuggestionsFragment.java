package com.cadyd.app.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.cadyd.app.JConstant;
import com.cadyd.app.MyApplication;
import com.cadyd.app.R;
import com.cadyd.app.adapter.SuggesTionsSpinnerAdapter;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.asynctask.DataLoader;
import com.cadyd.app.asynctask.retrofitmode.BaseRequestInfo;
import com.cadyd.app.asynctask.retrofitmode.HttpMethods;
import com.cadyd.app.asynctask.retrofitmode.ProgressSubscriber;
import com.cadyd.app.asynctask.retrofitmode.SubscriberOnNextListener;
import com.cadyd.app.comm.KeyBoardUtils;
import com.cadyd.app.ui.base.BaseFragement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.RequestBody;

/**
 * 投诉建议
 */
public class UserSuggestionsFragment extends BaseFragement implements View.OnClickListener {

    @Bind(R.id.LinearLayout)
    LinearLayout LinearLayout;
    @Bind(R.id.report_object)
    TextView reportObject;//举报对象
    @Bind(R.id.spinner)
    Spinner spinner;
    @Bind(R.id.content_editText)
    EditText contentEditText;
    @Bind(R.id.send)
    Button send;

    private int ChecksMunber;
    private List<String> list = new ArrayList<>();
    private SuggesTionsSpinnerAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = setView(R.layout.fragment_user_suggestions, "投诉建议", true);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initView() {
        layout.leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyBoardUtils.closeKeybord(contentEditText, activity);
                finishActivity();
            }
        });
        LinearLayout.setOnClickListener(this);
        send.setOnClickListener(this);
        list.add("  投诉");
        list.add("  建议");
        adapter = new SuggesTionsSpinnerAdapter(activity, list);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ChecksMunber = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Log.i("wan", "当前的安卓版本：" + "Product Model: " + android.os.Build.MODEL + "," + android.os.Build.VERSION.SDK + "," + android.os.Build.VERSION.RELEASE);
        if (Integer.valueOf(android.os.Build.VERSION.RELEASE.substring(0, 1)) >= 5) {
            int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            spinner.measure(w, h);
            spinner.setDropDownVerticalOffset(spinner.getMeasuredHeight());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send:
                suggestions();
                break;
            case R.id.LinearLayout:
                KeyBoardUtils.closeKeybord(contentEditText, activity);
                break;
        }
    }

    /**
     * 投诉建议
     */
    private void suggestions() {
//        Map<Object, Object> map = new HashMap<>();
//        map.put("content", contentEditText.getText().toString());//投诉建议内容
//        MyApplication application = (MyApplication) activity.getApplication();
//        map.put("userid", application.model.id);//用户id
//        map.put("type", 1);//类型  1平台 2商家
//        int type = 0;
//        switch (ChecksMunber) {
//            case 0:
//                type = 1;
//                break;
//            case 1:
//                type = 2;
//                break;
//        }
//        map.put("feedbackType", type);//1投诉 2建议
//        //      map.put("shopId", "");//商家id 当type为2时，此字段不能为空
//        RequestBody body = ApiClient.getRequestBody(map);
//        HttpMethods.getInstance().suggestions(new ProgressSubscriber<BaseRequestInfo>(new SubscriberOnNextListener<BaseRequestInfo>() {
//            @Override
//            public void onNext(BaseRequestInfo o) {
//                toastSuccess("提交成功");
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                toastError("提交失败");
//            }
//
//            @Override
//            public void onLogicError(String msg) {
//
//            }
//        }, activity), body);

        Map<String, Object> map = new HashMap<>();
        map.put("content", contentEditText.getText().toString());//投诉建议内容
        MyApplication application = (MyApplication) activity.getApplication();
        map.put("userid", application.model.id);//用户id
        map.put("type", 1);//类型  1平台 2商家
        int type = 0;
        switch (ChecksMunber) {
            case 0:
                type = 1;
                break;
            case 1:
                type = 2;
                break;
        }
        map.put("feedbackType", type);//1投诉 2建议
        //      map.put("shopId", "");//商家id 当type为2时，此字段不能为空
        int tag = JConstant.SUGGESTIONS;
        ApiClient.send(activity, tag, map, true, String.class, new DataLoader<String>() {
            @Override
            public void task(String data) {

            }

            @Override
            public void succeed(String data) {
                toastSuccess("提交成功");
            }

            @Override
            public void error(String message) {
                toastError("提交失败");
            }
        }, tag);

    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        ApiClient.cancelRequest(JConstant.SUGGESTIONS);
    }

}
