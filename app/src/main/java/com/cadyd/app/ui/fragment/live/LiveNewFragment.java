package com.cadyd.app.ui.fragment.live;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.cadyd.app.R;
import com.cadyd.app.adapter.LiveNewRecyclerAdapter;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.asynctask.retrofitmode.BaseRequestInfo;
import com.cadyd.app.asynctask.retrofitmode.HttpMethods;
import com.cadyd.app.asynctask.retrofitmode.ProgressSubscriber;
import com.cadyd.app.asynctask.retrofitmode.SubscriberOnNextListener;
import com.cadyd.app.model.LiveModel;
import com.cadyd.app.model.LiveNewInfo;
import com.cadyd.app.ui.base.BaseFragement;
import com.cadyd.app.ui.push.gles.Config;
import com.cadyd.app.widget.PLVideoViewActivity;
import com.cadyd.app.widget.SpaceItemDecoration;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;

import org.wcy.android.widget.EmptyLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.RequestBody;

/**
 * A simple {@link Fragment} subclass.
 */
public class LiveNewFragment extends BaseFragement implements PullToRefreshBase.OnRefreshListener2 {
    @Bind(R.id.live_new_recyclerView)
    RecyclerView mLiveNewRecyclerView;
    @Bind(R.id.pullListView)
    PullToRefreshScrollView mPullListView;
    @Bind(R.id.contact_layout)
    RelativeLayout contactLayout;
    private LiveNewRecyclerAdapter mLiveNewRecyclerAdapter;

    private List<LiveNewInfo.DataBean> liveNewInfos;
    private int pageIndex = 1;
    private int pageSize = 10;
    private boolean isRefresh = true;
    private EmptyLayout mEmptyLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_live_new, container, false);
        ButterKnife.bind(this, view);
        return view;
    }


    @Override
    protected void initView() {
        mPullListView.setMode(PullToRefreshBase.Mode.BOTH);
        mPullListView.setOnRefreshListener(this);
        liveNewInfos = new ArrayList<>();
        mLiveNewRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        mLiveNewRecyclerAdapter = new LiveNewRecyclerAdapter(getActivity());
        mLiveNewRecyclerView.setAdapter(mLiveNewRecyclerAdapter);
        mLiveNewRecyclerView.addItemDecoration(new SpaceItemDecoration(15, 2));

        mLiveNewRecyclerAdapter.setOnItemClickListener(new LiveNewRecyclerAdapter.OnItemClickListener() {
            @Override
            public void ItemClickListener(int position) {
                if (Config.isLive) {
                    Toast.makeText(getActivity(), R.string.live_isStart, Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(getActivity(), PLVideoViewActivity.class);
                intent.putExtra("conversationId", liveNewInfos.get(position).getConversationId());
                intent.putExtra("type", liveNewInfos.get(position).getLiveType());
                getActivity().startActivity(intent);
            }
        });

        mEmptyLayout = new EmptyLayout(activity, contactLayout);
        mEmptyLayout.setReloadOnclick(new EmptyLayout.ReloadOnClick() {
            @Override
            public void reload() {
                mEmptyLayout.showLoading();
                getLiveNewList();
            }
        });

        getLiveNewList();
    }

    private void getLiveNewList() {
        Map<Object, Object> map = new HashMap<>();
        map.put("pageIndex", pageIndex);
        map.put("pageSize", pageSize);
        RequestBody key = ApiClient.getRequestBody(map);
        HttpMethods.getInstance().getLiveNewList(new ProgressSubscriber<BaseRequestInfo<LiveNewInfo>>(new SubscriberOnNextListener<BaseRequestInfo<LiveNewInfo>>() {
            @Override
            public void onNext(BaseRequestInfo<LiveNewInfo> o) {
                if (mPullListView != null)
                    mPullListView.onRefreshComplete();
                if (o.getData().getData().size() > 0) {
                    mEmptyLayout.showView();
                    liveNewInfos.addAll(o.getData().getData());
                    mLiveNewRecyclerAdapter.setData(liveNewInfos);
                } else {
                    if (isRefresh) {
                        mEmptyLayout.showEmpty("暂无数据");
                    } else {
                        toastWarning("没有更多数据了");
                    }
                }
            }

            @Override
            public void onError(Throwable e) {
                mEmptyLayout.showError();
                if (mPullListView != null)
                    mPullListView.onRefreshComplete();
            }

            @Override
            public void onLogicError(String msg) {
                mEmptyLayout.showError();
            }
        }, getActivity()), key);
    }

    /**
     * 启动视频播放
     *
     * @param info   主播个人信息
     * @param flvUrl 播放地址
     */
    public void startPlVideo(@NonNull LiveModel info, @NonNull String flvUrl, String shopId) {
        Intent intent = new Intent(getActivity(), PLVideoViewActivity.class);
        intent.putExtra("videoPath", flvUrl);
        intent.putExtra("mediaCodec", 0);//軟解
        intent.putExtra("liveStreaming", 0);//點播
        intent.putExtra("info", info);
        intent.putExtra("shopId", shopId);
        getActivity().startActivity(intent);
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        pageIndex = 1;
        isRefresh = true;
        liveNewInfos.clear();
        getLiveNewList();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        pageIndex++;
        isRefresh = false;
        getLiveNewList();
    }
}
