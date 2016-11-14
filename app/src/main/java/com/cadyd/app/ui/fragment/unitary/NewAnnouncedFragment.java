package com.cadyd.app.ui.fragment.unitary;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.Bind;
import com.cadyd.app.JConstant;
import com.cadyd.app.R;
import com.cadyd.app.adapter.MyAdapter;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.asynctask.DataLoader;
import com.cadyd.app.interfaces.TowObjectParameterInterface;
import com.cadyd.app.model.NewAnnounce;
import com.cadyd.app.model.OneNewAnnounce;
import com.cadyd.app.ui.activity.CommonActivity;
import com.cadyd.app.ui.base.BaseFragement;
import com.cadyd.app.ui.view.DividerGridItemDecoration;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import org.wcy.android.widget.EmptyLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 最新揭晓
 **/
public class NewAnnouncedFragment extends BaseFragement {
    @Bind(R.id.pullListView)
    PullToRefreshScrollView pullListView;
    @Bind(R.id.my_recycler_view)
    RecyclerView mRecyclerView;
    @Bind(R.id.empty_layout)
    View relativeLayout;
    MyAdapter newAnnounceAdapter;
    private List<NewAnnounce> listNewAnnounce = new ArrayList<>();
    EmptyLayout mEmptyLayout;
    private int pageindext = 1;
    private int totalPage;

    private boolean isRefresh = false;

    public void MyRefresh() {
        if (isRefresh) {
            pageindext = 1;
            loadingNewAnnounce();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return setView(R.layout.fragment_new_announced, "最新揭晓", true);
    }

    /**
     * 最新揭晓
     */
    private void loadingNewAnnounce() {
        Map<String, Object> map = new HashMap<>();
        map.put("categroyId", 0);
        map.put("pageIndex", pageindext);
        ApiClient.send(activity, JConstant.ONEQUERYALLANNOUNCEDRECORD_, map, true, OneNewAnnounce.class, new DataLoader<OneNewAnnounce>() {

            @Override
            public void task(String data) {
            }

            @Override
            public void succeed(OneNewAnnounce data) {
                if (pageindext == 1) {
                    listNewAnnounce.clear();
                }
                if (data != null && data.data.size() > 0) {
                    totalPage = data.totalPage;
                    listNewAnnounce.addAll(data.data);
                    mEmptyLayout.showView();
                    newAnnounceAdapter.notifyDataSetChanged();
                } else {
                    mEmptyLayout.showEmpty("暂无最新揭晓");
                }
                if (totalPage == pageindext) {
                    pullListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                } else {
                    pullListView.setMode(PullToRefreshBase.Mode.BOTH);
                }
                RefreshComplete();
            }

            @Override
            public void error(String message) {
                mEmptyLayout.showError(message);
                RefreshComplete();
            }
        }, JConstant.ONEQUERYALLANNOUNCEDRECORD_);

    }

    private void RefreshComplete() {
        pullListView.onRefreshComplete();
        isRefresh = true;
    }

    private void setPullListView() {
        pullListView.setMode(PullToRefreshBase.Mode.BOTH);
        pullListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase refreshView) {
                Log.i("PullToRefreshBase", "onPullDownToRefresh");
                pageindext = 1;
                loadingNewAnnounce();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase refreshView) {
                Log.i("PullToRefreshBase", "onPullUpToRefresh");
                pageindext++;
                loadingNewAnnounce();
            }
        });
    }

    @Override
    protected void initView() {
        mEmptyLayout = new EmptyLayout(activity, relativeLayout);
        mEmptyLayout.setReloadOnclick(new EmptyLayout.ReloadOnClick() {
            @Override
            public void reload() {
                loadingNewAnnounce();
            }
        });
        mEmptyLayout.showLoading();
        layout.leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishActivity();
            }
        });
        setPullListView();
        GridLayoutManager mLayoutManager = new GridLayoutManager(activity, 2);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerGridItemDecoration(activity, -1, 1, getResources().getColor(R.color.light_gray)));
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mRecyclerView.setHasFixedSize(true);
        //设置Item增加、移除动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //创建并设置Adapter
        newAnnounceAdapter = new MyAdapter(listNewAnnounce);

        newAnnounceAdapter.setClick(new TowObjectParameterInterface() {
            @Override
            public void Onchange(int type, int postion, Object object) {
                NewAnnounce n = (NewAnnounce) object;
                //跳转到揭晓详情页面
                Intent intent = new Intent(activity, CommonActivity.class);
                intent.putExtra(JConstant.EXTRA_INDEX, CommonActivity.NEW_ANNOUNCED);
                intent.putExtra("newModel", n);
                startActivity(intent);
            }
        });

        mRecyclerView.setAdapter(newAnnounceAdapter);
        loadingNewAnnounce();
    }

    @Override
    public boolean onBackPressed() {
        finishFramager();
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ApiClient.cancelRequest(JConstant.ONEQUERYALLANNOUNCEDRECORD_);
    }
}
