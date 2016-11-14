package com.cadyd.app.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.cadyd.app.MyApplication;
import com.cadyd.app.R;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.asynctask.retrofitmode.BaseRequestInfo;
import com.cadyd.app.asynctask.retrofitmode.HttpMethods;
import com.cadyd.app.asynctask.retrofitmode.ProgressSubscriber;
import com.cadyd.app.asynctask.retrofitmode.SubscriberOnNextListener;
import com.cadyd.app.model.RecentVisitorsModel;
import com.cadyd.app.ui.base.BaseActivity;
import com.cadyd.app.utils.CircleImageView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.wcy.android.utils.adapter.CommonAdapter;
import org.wcy.android.utils.adapter.ViewHolder;

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
 * 最近访客列表
 */
public class RecentVisitorsActivity extends BaseActivity {

    @Bind(R.id.back)
    TextView back;
    @Bind(R.id.textViewMessage)
    TextView textViewMessage;
    @Bind(R.id.pull_to_refresh_list_view)
    PullToRefreshListView pullToRefreshListView;

    private String userId;
    private int pageIndex = 1;
    private List<RecentVisitorsModel.DataBean> listModel = new ArrayList<>();
    private CommonAdapter<RecentVisitorsModel.DataBean> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_visitors);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            userId = bundle.getString("userId", "");
        }
        init();
    }

    private void init() {
        pullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        pullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                pageIndex = 1;
                getRecentVisitors();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                pageIndex++;
                getRecentVisitors();
            }
        });
        /**
         * 配置adapter
         */
        adapter = new CommonAdapter<RecentVisitorsModel.DataBean>(activity, listModel, R.layout.attention_and_fans_item) {
            @Override
            public void convert(ViewHolder helper, final RecentVisitorsModel.DataBean item) {
                if (item == null) return;
                CircleImageView image = (CircleImageView) helper.getConvertView().findViewById(R.id.image);
                ApiClient.displayImage(item.getHeadImageUrl(), image, R.mipmap.user_default);
                helper.setText(R.id.name, item.getNickName());
                TextView textview = helper.getView(R.id.check_box);
                if (item.getFocusStatus() == 1) {//一关注
                    textview.setTag(true);
                    ChangeText(textview, true);
                } else if (item.getFocusStatus() == 2) {//为关注
                    textview.setTag(false);
                    ChangeText(textview, false);
                }
                /**关注和取消关注*/
                textview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TextView text = (TextView) v;
                        if ((boolean) text.getTag()) {
                            //取消关注
                            cancelFoucs(item.getUserId(), text, position);
                        } else {
                            //进行关注
                            userAddAttention(item.getUserId(), text, position);
                        }
                    }
                });
            }
        };
        pullToRefreshListView.setAdapter(adapter);
        getRecentVisitors();
    }

    /**
     * 获得最近访客列表
     */
    private void getRecentVisitors() {
        Map<Object, Object> map = new HashMap<>();
        map.put("userId", userId);//用户Id
        map.put("pageIndex", pageIndex);//页码
        map.put("pageSize", 10);//分页大小

        RequestBody body = ApiClient.getRequestBody(map);
        HttpMethods.getInstance().getRecentVisitors(new ProgressSubscriber<BaseRequestInfo<RecentVisitorsModel>>(new SubscriberOnNextListener<BaseRequestInfo<RecentVisitorsModel>>() {
            @Override
            public void onNext(BaseRequestInfo<RecentVisitorsModel> info) {
                pullToRefreshListView.onRefreshComplete();
                if (pageIndex == 1) {
                    listModel.clear();
                }
                if (info != null && info.getData() != null && info.getData().getData() != null) {
                    if (info.getData().getData().size() <= 0) {
                        pullToRefreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                    } else {
                        listModel.addAll(info.getData().getData());
                        adapter.notifyDataSetChanged();
                    }
                }
                if (listModel.size() > 0) {
                    textViewMessage.setVisibility(View.GONE);
                } else {
                    textViewMessage.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onLogicError(String msg) {

            }
        }, RecentVisitorsActivity.this), body);

    }

    private void ChangeText(TextView textView, boolean type) {
        if (type) {
            textView.setTag(true);
            textView.setText("已关注");
            textView.setTextColor(getResources().getColor(R.color.Orange));
            textView.setBackgroundResource(R.drawable.oval_yellow_transparent);
        } else {
            textView.setTag(false);
            textView.setText(" +关注");
            textView.setTextColor(getResources().getColor(R.color.white));
            textView.setBackgroundResource(R.drawable.oval_yellow_untransparent);
        }
    }

    private SubscriberOnNextListener mOnNextListener;

    /**
     * 关注用户
     *
     * @param userId
     */
    public void userAddAttention(String userId, final TextView text, final int postion) {

        MyApplication application = (MyApplication) getApplication();
        if (userId.equals(application.model.id)) {//如果相等则为自己的个人中心
            toast("不能自己关注自己");
        }

        mOnNextListener = new SubscriberOnNextListener<BaseRequestInfo>() {
            @Override
            public void onNext(BaseRequestInfo info) {
                Log.i("wan", "关注用户成功");
                listModel.get(postion).setFocusStatus(1);
                ChangeText(text, true);
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onLogicError(String msg) {

            }
        };

        Map<Object, Object> map = new HashMap<>();
        map.put("userId", userId);
        RequestBody body = ApiClient.getRequestBody(map);
        HttpMethods.getInstance().getUserAttention(new ProgressSubscriber<BaseRequestInfo>(mOnNextListener, activity), body);
    }

    /**
     * 取消关注
     */
    private void cancelFoucs(String userId, final TextView text, final int postion) {
        Map<Object, Object> map = new HashMap<>();
        map.put("userId", userId);
        RequestBody body = ApiClient.getRequestBody(map);
        HttpMethods.getInstance().doCancelFoucs(new ProgressSubscriber<BaseRequestInfo>(new SubscriberOnNextListener<BaseRequestInfo>() {
            @Override
            public void onNext(BaseRequestInfo o) {
                Log.i("wan", "取消关注用户成功");
                listModel.get(postion).setFocusStatus(2);
                ChangeText(text, false);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onLogicError(String msg) {

            }
        }, activity), body);
    }

    @OnClick(R.id.back)
    public void onClick() {
        finishActivity();
    }
}
