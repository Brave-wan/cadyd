package com.cadyd.app.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.cadyd.app.MyApplication;
import com.cadyd.app.R;
import com.cadyd.app.adapter.DirectRoomViewAdapter;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.asynctask.DataLoader;
import com.cadyd.app.model.LiveModel;
import com.cadyd.app.ui.base.BaseFragmentActivity;
import com.cadyd.app.widget.PLVideoViewActivity;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 一點直播 cover
 */
public class InfoDetailsFragment extends BaseFragmentActivity implements AdapterView.OnItemClickListener {

    private PullToRefreshListView listView;
    //*直播列表
    private List<LiveModel> liveModels = new ArrayList<>();
    private List<LiveModel> list = new ArrayList<>();
    private DirectRoomViewAdapter adapter;
    private View listview_empty;
    private MyApplication application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.info_details_fragment, "一点直播", true);
        initView();
    }

    public void initView() {
        //开始链接tcp
        application = (MyApplication) getApplication();
        listView = (PullToRefreshListView) findViewById(R.id.recycler_view);
        listView.setMode(PullToRefreshBase.Mode.BOTH);
        adapter = new DirectRoomViewAdapter(InfoDetailsFragment.this, liveModels);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        listView.setEmptyView(listview_empty);

        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase refreshView) {
                Log.i("PullToRefreshBase", "onPullDownToRefresh");
                pageSize = 1;
                liveModels.clear();
                getLiveList(pageSize);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase refreshView) {
                Log.i("PullToRefreshBase", "onPullUpToRefresh");
                if (liveModels.size() > 0) {
                    pageSize++;
                }

                getLiveList(pageSize);

            }
        });

        getLiveList(pageSize);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        LiveModel liveModel = liveModels.get(position - 1);
        startPlVideo(liveModel, liveModel.flvUrl, liveModel.shopid);
    }


    private int pageSize = 1;

    private void getLiveList(int pageSize) {

        Map<String, Object> map = new HashMap<>();
        map.put("pageIndex", pageSize);
        map.put("pageSize", 10);
        ApiClient.send(getApplicationContext(), 0x1000167, map, true, LiveModel.getType(), new DataLoader<List<LiveModel>>() {
            @Override
            public void task(String data) {

            }

            @Override
            public void succeed(List<LiveModel> data) {
                listView.onRefreshComplete();
                if (data == null || data.size() <= 0) {
                } else {
                    liveModels.addAll(data);
                    adapter.setData(liveModels);
                }
            }

            @Override
            public void error(String message) {

            }
        }, 0x1000167);
    }

    /**
     * 启动视频播放
     *
     * @param info   主播个人信息
     * @param flvUrl 播放地址
     */
    public void startPlVideo(@NonNull LiveModel info, @NonNull String flvUrl, String shopId) {
        Intent intent = new Intent(InfoDetailsFragment.this, PLVideoViewActivity.class);
        intent.putExtra("videoPath", flvUrl);
        intent.putExtra("mediaCodec", 0);//軟解
        intent.putExtra("liveStreaming", 0);//點播
        intent.putExtra("info", info);
        intent.putExtra("shopId", shopId);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ApiClient.cancelRequest(0x1000167);

    }
}
