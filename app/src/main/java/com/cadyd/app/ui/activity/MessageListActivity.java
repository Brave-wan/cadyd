package com.cadyd.app.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.cadyd.app.R;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.asynctask.retrofitmode.BaseRequestInfo;
import com.cadyd.app.asynctask.retrofitmode.HttpMethods;
import com.cadyd.app.asynctask.retrofitmode.ProgressSubscriber;
import com.cadyd.app.asynctask.retrofitmode.SubscriberOnNextListener;
import com.cadyd.app.model.MessageListInfo;
import com.cadyd.app.ui.base.BaseActivity;
import com.cadyd.app.utils.Util;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONException;
import org.json.JSONObject;
import org.wcy.android.utils.adapter.CommonAdapter;
import org.wcy.android.utils.adapter.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;

/**
 * @Description: 消息列表
 * 作者：Jerry
 * 邮箱：185214487@qq.com
 */
public class MessageListActivity extends BaseActivity implements PullToRefreshBase.OnRefreshListener2 {
    PullToRefreshListView listView;
    CommonAdapter<MessageListInfo.DataBean> adapter;
    private List<MessageListInfo.DataBean> messageInfos = new ArrayList<>();
    private int kindCode;
    private int pageIndex = 1;
    private int pageSize = 10;
    private boolean isRefresh = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activty_messagelist, "消息列表", true, "全部已读");
        initView();
    }

    private void initView() {

        kindCode = Integer.parseInt(getIntent().getStringExtra("kindCode"));

        listView = (PullToRefreshListView) findViewById(R.id.messageList_ListView);
        listView.setMode(PullToRefreshBase.Mode.BOTH);
        listView.setOnRefreshListener(this);

        adapter = new CommonAdapter<MessageListInfo.DataBean>(MessageListActivity.this, messageInfos, R.layout.item_messagelist_listview) {
            @Override
            public void convert(ViewHolder helper, final MessageListInfo.DataBean item) {
                TextView context = helper.getView(R.id.item_messageList_name);
                context.setText(item.getTitle());
                switch (item.getReadStatus()) {
                    case 1:
                        context.setTextColor(Color.BLACK);
                        break;
                    case 2:
                        context.setTextColor(Color.GRAY);
                        break;
                }
                helper.setText(R.id.item_messageList_time, item.getCreateTime().substring(0, 16));
                helper.getConvertView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setRead(item);
                    }
                });
            }
        };
        listView.setAdapter(adapter);

        layout.rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAllRead();
            }
        });

        getMessageList();
    }

    /**
     * 获取消息列表
     */
    public void getMessageList() {
        Map<Object, Object> map = new HashMap<>();
        map.put("kindCode", kindCode);
        map.put("pageIndex", pageIndex);
        map.put("pageSize", pageSize);
        RequestBody key = ApiClient.getRequestBody(map);
        HttpMethods.getInstance().getMessageList(new ProgressSubscriber<BaseRequestInfo<MessageListInfo>>(new SubscriberOnNextListener<BaseRequestInfo<MessageListInfo>>() {
            @Override
            public void onNext(BaseRequestInfo<MessageListInfo> o) {
                listView.onRefreshComplete();
                if (o.getData().getData().size() > 0) {
                    messageInfos.addAll(o.getData().getData());
                    adapter.notifyDataSetChanged();
                } else {
                    if (isRefresh) {
                        toastWarning("暂无数据");
                    } else {
                        toastWarning("没有更多数据了");
                    }
                    return;
                }
            }

            @Override
            public void onError(Throwable e) {
                listView.onRefreshComplete();
            }

            @Override
            public void onLogicError(String msg) {

            }
        }, MessageListActivity.this), key);
    }

    /**
     * 设为已读消息
     */
    public void setRead(final MessageListInfo.DataBean messageInfo) {
        Map<Object, Object> map = new HashMap<>();
        map.put("kindCode", "");
        map.put("messageId", messageInfo.getMessageId());
        RequestBody key = ApiClient.getRequestBody(map);
        HttpMethods.getInstance().setRead(new ProgressSubscriber<BaseRequestInfo>(new SubscriberOnNextListener<BaseRequestInfo>() {
            @Override
            public void onNext(BaseRequestInfo o) {
                Intent intent = new Intent(MessageListActivity.this, MessageDetailsActivity.class);
                switch (messageInfo.getOperationType()) {
                    case 1:
                        intent.putExtra("messageId", messageInfo.getMessageId());
                        break;
                    case 2:
                        intent.putExtra("url", messageInfo.getMessageUrl());
                        break;
                }
                startActivity(intent);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onLogicError(String msg) {

            }
        }, MessageListActivity.this), key);
    }

    /**
     * 设为已读消息
     */
    public void setAllRead() {
        Map<Object, Object> map = new HashMap<>();
        map.put("kindCode", kindCode);
        map.put("messageId", "");
        RequestBody key = ApiClient.getRequestBody(map);
        HttpMethods.getInstance().setRead(new ProgressSubscriber<BaseRequestInfo>(new SubscriberOnNextListener<BaseRequestInfo>() {
            @Override
            public void onNext(BaseRequestInfo o) {
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onLogicError(String msg) {

            }
        }, MessageListActivity.this), key);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        pageIndex = 1;
        messageInfos.clear();
        isRefresh = true;
        getMessageList();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        pageIndex++;
        isRefresh = false;
        getMessageList();
    }
}
