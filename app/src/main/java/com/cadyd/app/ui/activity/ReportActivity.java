package com.cadyd.app.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cadyd.app.R;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.asynctask.retrofitmode.BaseRequestInfo;
import com.cadyd.app.asynctask.retrofitmode.HttpMethods;
import com.cadyd.app.asynctask.retrofitmode.ProgressSubscriber;
import com.cadyd.app.asynctask.retrofitmode.SubscriberOnNextListener;
import com.cadyd.app.comm.KeyBoardUtils;
import com.cadyd.app.model.LiveEndInfo;
import com.cadyd.app.model.ReportItemModel;
import com.cadyd.app.ui.base.BaseActivity;

import org.wcy.android.utils.adapter.CommonAdapter;
import org.wcy.android.utils.adapter.ViewHolder;
import org.wcy.android.widget.MyListView;
import org.wcy.common.utils.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.RequestBody;

/**
 * xiongmao
 * 举报
 */
public class ReportActivity extends BaseActivity {

    @Bind(R.id.activity_report)
    LinearLayout activityReport;
    @Bind(R.id.back)
    TextView back;
    @Bind(R.id.reason_list)
    MyListView reasonList;
    @Bind(R.id.report_content)
    EditText reportContent;
    @Bind(R.id.send)
    Button send;

    private TextView oldText;
    private int oldPostion = -1;
    private CommonAdapter<ReportItemModel> adapter;
    private List<ReportItemModel> itemModels = new ArrayList<>();

    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            userId = bundle.getString("userId", "");
        }
        init();
    }

    private void init() {

        activityReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyBoardUtils.closeKeybord(reportContent, activity);
            }
        });

        if (adapter == null) {
            adapter = new CommonAdapter<ReportItemModel>(this, itemModels, R.layout.report_item) {
                @Override
                public void convert(final ViewHolder helper, final ReportItemModel item) {
                    final TextView textView = helper.getView(R.id.report_text);
                    textView.setText(item.getDicValue());
                    textView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!item.isCheck) {
                                textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.yellow_gou, 0);
                                item.isCheck = true;
                                if (oldPostion != -1 && oldText != null) {
                                    itemModels.get(oldPostion).isCheck = false;
                                    oldText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                                }
                                oldPostion = helper.getPosition();
                                oldText = textView;
                            }
                        }
                    });
                }
            };
            reasonList.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
        getDictionaryItem();
    }

    /**
     * 获取举报类型
     */
    private void getDictionaryItem() {
        itemModels.clear();
        Map<Object, Object> map = new HashMap<>();
        map.put("typeId", "system_report_reason_id");
        RequestBody key = ApiClient.getRequestBody(map);
        HttpMethods.getInstance().getDictionaryItem(new ProgressSubscriber<BaseRequestInfo<List<ReportItemModel>>>(new SubscriberOnNextListener<BaseRequestInfo<List<ReportItemModel>>>() {
            @Override
            public void onNext(BaseRequestInfo<List<ReportItemModel>> info) {
                Log.i("wan", "获得举报数据");
                if (info != null) {
                    itemModels.addAll(info.getData());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onLogicError(String msg) {

            }
        }, ReportActivity.this), key);
    }

    /**
     * 发送举报信息
     */
    @OnClick(R.id.send)
    public void onSend(View view) {
        KeyBoardUtils.closeKeybord(reportContent, activity);
        if (oldText == null || StringUtil.isEmpty(reportContent.getText().toString())) {
            toast("请完成举报内容");
        } else {
            ConfirmTheReport(reportContent.getText().toString());
        }
    }

    /**
     * 执行举报
     */
    private void ConfirmTheReport(String content) {
        ReportItemModel reportItemModel = itemModels.get(oldPostion);
        Map<Object, Object> map = new HashMap<>();
        map.put("reportType", reportItemModel.getFormatType());//举报业务类型
        map.put("refId", userId);//举报的业务对象
        map.put("reportReason", reportItemModel.getDicKey());//举报的选项
        map.put("description", content);//举报内容描述
        map.put("targetUserId", userId);//被举报用户Id

        RequestBody key = ApiClient.getRequestBody(map);
        HttpMethods.getInstance().ConfirmTheReport(new ProgressSubscriber<BaseRequestInfo<String>>(new SubscriberOnNextListener<BaseRequestInfo<String>>() {
            @Override
            public void onNext(BaseRequestInfo<String> info) {
                toastSuccess("举报成功");
                finishActivity();
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onLogicError(String msg) {

            }
        }, ReportActivity.this), key);
    }

    /**
     * 退出
     *
     * @param view
     */
    @OnClick(R.id.back)
    public void onBack(View view) {
        KeyBoardUtils.closeKeybord(reportContent, activity);
        finishActivity();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private class myListMap {
        public boolean isCheck;
        public String content;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
