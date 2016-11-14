package com.cadyd.app.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cadyd.app.JConstant;
import com.cadyd.app.R;
import com.cadyd.app.adapter.MessageListAdapter;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.asynctask.DataLoader;
import com.cadyd.app.interfaces.OnItemClickListener;
import com.cadyd.app.model.MessageInfoEntry;
import com.cadyd.app.ui.base.BaseActivity;
import com.cadyd.app.utils.EmptyRecyclerView;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

/**
 * Created by SCH-1 on 2016/7/28.
 */
public class MessageActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, OnItemClickListener<MessageInfoEntry> {

    @Bind(R.id.message_refresh)
    SwipeRefreshLayout refreshLayout;

    @Bind(R.id.message_list)
    EmptyRecyclerView recyclerView;

    @Bind(R.id.empty_layout)
    View empty_layout;

    private List<MessageInfoEntry> datas;
    private int pageNo = 1;

    private MessageListAdapter messageListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.message_layout, getString(R.string.message_title), true);
        initView();
        loadDatas();
    }

    private void loadDatas() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("pageIndex", pageNo);
        ApiClient.send(this, JConstant.MESSAGE_LIST, params, MessageInfoEntry.getType(), new DataLoader<List<MessageInfoEntry>>() {

            @Override
            public void task(String data) {

            }

            @Override
            public void succeed(List<MessageInfoEntry> data) {


                if (data != null && data.size() > 0) {
                    datas.addAll(data);
                    messageListAdapter.notifyDataSetChanged();
                    pageNo += 1;
                }
                refreshLayout.setRefreshing(false);
            }

            @Override
            public void error(String message) {

            }
        }, JConstant.MESSAGE_LIST);
    }

    private void initView() {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setEmptyView(empty_layout);
        recyclerView.setLayoutManager(lm);
        recyclerView.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(this)
                        .colorResId(R.color.gray)
                        .size(1)
                        .margin(3)
                        .build());
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (isSlideToBottom(recyclerView)) {
                    loadDatas();
                }
            }
        });

        refreshLayout.setColorSchemeResources(R.color.Orange);
        refreshLayout.setOnRefreshListener(this);

        datas = new ArrayList<MessageInfoEntry>();
        messageListAdapter = new MessageListAdapter(datas);
        messageListAdapter.setListener(this);
        recyclerView.setAdapter(messageListAdapter);
    }

    protected boolean isSlideToBottom(RecyclerView recyclerView) {
        if (recyclerView == null) return false;
        if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset() >= recyclerView.computeVerticalScrollRange())
            return true;
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishActivity();
    }

    @Override
    public void onRefresh() {
        pageNo = 1;
        datas.clear();
        loadDatas();
    }

    @Override
    public void onItemClick(MessageInfoEntry data, View view) {
        Intent intent = new Intent(this, GoodsDetailActivity.class);
        intent.putExtra("gid", data.getGoodsid());
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ApiClient.cancelRequest(JConstant.MESSAGE_LIST);
    }
}
