package com.cadyd.app.ui.fragment.live;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.cadyd.app.R;
import com.cadyd.app.adapter.LiveHotRecyclerAdapter;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.asynctask.retrofitmode.BaseRequestInfo;
import com.cadyd.app.asynctask.retrofitmode.HttpMethods;
import com.cadyd.app.asynctask.retrofitmode.ProgressSubscriber;
import com.cadyd.app.asynctask.retrofitmode.SubscriberOnNextListener;
import com.cadyd.app.model.Findadver;
import com.cadyd.app.model.LiveHotInfo;
import com.cadyd.app.model.ReportItemModel;
import com.cadyd.app.ui.base.BaseFragement;
import com.cadyd.app.ui.push.gles.Config;
import com.cadyd.app.ui.view.guide.ZImageCycleView;
import com.cadyd.app.utils.Util;
import com.cadyd.app.widget.PLVideoViewActivity;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;

import org.json.JSONException;
import org.json.JSONObject;
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
public class LiveHotFragment extends BaseFragement implements PullToRefreshBase.OnRefreshListener2 {

    @Bind(R.id.live_hot_imageCycleView)
    ZImageCycleView mLiveHotImageCycleView;
    @Bind(R.id.live_hot_recyclerView)
    RecyclerView mLiveHotRecyclerView;
    @Bind(R.id.pullListView)
    PullToRefreshScrollView mPullListView;
    @Bind(R.id.contact_layout)
    RelativeLayout contactLayout;

    private List<ReportItemModel> reportItemModels;
    List<Findadver> urlsImage = new ArrayList<>();
    private LiveHotRecyclerAdapter mLiveHotRecyclerAdapter;
    private List<LiveHotInfo.DataBean> liveHotInfos;

    private int pageIndex = 1;
    private int pageSize = 10;
    private boolean isRefresh = true;
    private EmptyLayout mEmptyLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_live_hot, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initView() {
        ViewGroup.LayoutParams ps = mLiveHotImageCycleView.getLayoutParams();
        ps.height = Util.getScreenWidth(getActivity()) / 3;
        mLiveHotImageCycleView.setLayoutParams(ps);

        mPullListView.setMode(PullToRefreshBase.Mode.BOTH);
        mPullListView.setOnRefreshListener(this);

        liveHotInfos = new ArrayList<>();
        mLiveHotRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        mLiveHotRecyclerAdapter = new LiveHotRecyclerAdapter(getActivity());
        mLiveHotRecyclerView.setAdapter(mLiveHotRecyclerAdapter);

        mLiveHotRecyclerAdapter.setOnItemClickListener(new LiveHotRecyclerAdapter.OnItemClickListener() {
            @Override
            public void ItemClickListener(int position) {
                if (Config.isLive) {
                    Toast.makeText(getActivity(), R.string.live_isStart, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (liveHotInfos.size() > 0) {
                    Intent intent = new Intent(getActivity(), PLVideoViewActivity.class);
                    intent.putExtra("conversationId", liveHotInfos.get(position).getConversationId());
                    intent.putExtra("type", liveHotInfos.get(position).getLiveType());
                    getActivity().startActivity(intent);
                }
            }
        });

        mLiveHotRecyclerAdapter.setOnFollowClickListener(new LiveHotRecyclerAdapter.OnFollowClickListener() {
            @Override
            public void FollowClickListener(int position, View view) {//关注
                doFoucs(position);
            }

            @Override
            public void unFollowClickListener(int position, View view) {//取消关注
                cancelFoucs(position);
            }

            @Override
            public void goLoginClickListener() {

            }
        });

        mEmptyLayout = new EmptyLayout(activity, contactLayout);
        mEmptyLayout.setReloadOnclick(new EmptyLayout.ReloadOnClick() {
            @Override
            public void reload() {
                mEmptyLayout.showLoading();
                getLiveHotList();
            }
        });
        topViewFlow();
        getLiveHotList();
    }

    private void getLiveHotList() {
        Map<Object, Object> map = new HashMap<>();
        map.put("pageIndex", pageIndex);
        map.put("pageSize", pageSize);
        RequestBody key = ApiClient.getRequestBody(map);
        HttpMethods.getInstance().getLiveHotList(new ProgressSubscriber<BaseRequestInfo<LiveHotInfo>>(new SubscriberOnNextListener<BaseRequestInfo<LiveHotInfo>>() {
            @Override
            public void onNext(BaseRequestInfo<LiveHotInfo> o) {
                if (mPullListView != null) {
                    mPullListView.onRefreshComplete();
                }

                if (o.getData().getData().size() > 0) {
                    mEmptyLayout.showView();
                    liveHotInfos.addAll(o.getData().getData());
                    mLiveHotRecyclerAdapter.setData(liveHotInfos);
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
                mPullListView.onRefreshComplete();
            }

            @Override
            public void onLogicError(String msg) {
                mEmptyLayout.showError();
            }
        }, getActivity()), key);
    }

    /**
     * 关注
     */
    private void doFoucs(final int position) {
        Map<Object, Object> map = new HashMap<>();
        map.put("userId", liveHotInfos.get(position).getUserId());
        RequestBody body = ApiClient.getRequestBody(map);
        HttpMethods.getInstance().getUserAttention(new ProgressSubscriber<BaseRequestInfo>(new SubscriberOnNextListener<BaseRequestInfo>() {
            @Override
            public void onNext(BaseRequestInfo o) {
                liveHotInfos.get(position).setFocusStatus(1);
                mLiveHotRecyclerAdapter.setData(liveHotInfos);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onLogicError(String msg) {

            }
        }, getActivity()), body);
    }

    /**
     * 取消关注
     */
    private void cancelFoucs(final int position) {
        Map<Object, Object> map = new HashMap<>();
        map.put("userId", liveHotInfos.get(position).getUserId());
        RequestBody body = ApiClient.getRequestBody(map);
        HttpMethods.getInstance().doCancelFoucs(new ProgressSubscriber<BaseRequestInfo>(new SubscriberOnNextListener<BaseRequestInfo>() {
            @Override
            public void onNext(BaseRequestInfo o) {
                liveHotInfos.get(position).setFocusStatus(2);
                mLiveHotRecyclerAdapter.setData(liveHotInfos);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onLogicError(String msg) {

            }
        }, getActivity()), body);
    }

    private void topViewFlow() {
        mLiveHotImageCycleView.pushImageCycle();
        urlsImage.clear();
        Map<Object, Object> map = new HashMap<>();
        map.put("typeId", "system_carousel_figure_id");
        RequestBody key = ApiClient.getRequestBody(map);
        HttpMethods.getInstance().getDictionaryItem(new ProgressSubscriber<BaseRequestInfo<List<ReportItemModel>>>(new SubscriberOnNextListener<BaseRequestInfo<List<ReportItemModel>>>() {
            @Override
            public void onNext(BaseRequestInfo<List<ReportItemModel>> info) {
                reportItemModels = info.getData();
                for (ReportItemModel reportItemModel : reportItemModels) {
                    try {
                        JSONObject jsonObject = new JSONObject(reportItemModel.getDicValue());
                        Findadver mFindadver = new Findadver();
                        mFindadver.imgurl = jsonObject.getString("picUrl");
                        mFindadver.url = jsonObject.getString("url");
                        urlsImage.add(mFindadver);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if (urlsImage.size() > 0) {
                    mLiveHotImageCycleView.setImageResources(urlsImage, "imgurl", R.mipmap.adv_top, new ZImageCycleView.ImageCycleViewListener() {
                        @Override
                        public void onImageClick(int position, Object object, View imageView) {
                            Toast.makeText(getActivity(), "点击了图片" + position, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void carousel() {

                        }
                    });
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onLogicError(String msg) {

            }
        }, getActivity()), key);
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
        liveHotInfos.clear();
        isRefresh = true;
        getLiveHotList();
        topViewFlow();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        pageIndex++;
        isRefresh = false;
        getLiveHotList();
        //topViewFlow();
    }
}
