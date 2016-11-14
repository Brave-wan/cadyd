package com.cadyd.app.ui.fragment.user;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import butterknife.Bind;
import com.cadyd.app.JConstant;
import com.cadyd.app.R;
import com.cadyd.app.adapter.TheSunAdapter;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.asynctask.DataLoader;
import com.cadyd.app.interfaces.TowObjectParameterInterface;
import com.cadyd.app.model.GoodsTheSun;
import com.cadyd.app.model.GoodsTheSunData;
import com.cadyd.app.ui.activity.SignInActivity;
import com.cadyd.app.ui.base.BaseFragement;
import com.cadyd.app.ui.fragment.unitary.TheSunAllCommentFragment;
import com.cadyd.app.ui.view.ToastView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import org.wcy.android.widget.EmptyLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TheSunFragment extends BaseFragement {
    private int url;
    public String ID;
    public String Title = "晒单";
    private int pageindext = 1;
    private List<GoodsTheSunData> goodsTheSunDatas = new ArrayList<>();

    private TheSunAdapter adapter;
    @Bind(R.id.empty_layout)
    View relativeLayout;
    @Bind(R.id.the_sun_list)
    PullToRefreshListView listView;
    EmptyLayout mEmptyLayout;

    Map<String, Object> map = new HashMap<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return setView(R.layout.the_sun_fragment, Title, true);
    }

    @Override
    protected void initView() {
        mEmptyLayout = new EmptyLayout(activity, relativeLayout);
        mEmptyLayout.setReloadOnclick(new EmptyLayout.ReloadOnClick() {
            @Override
            public void reload() {
                pageindext = 1;
                goodsTheSunDatas.clear();
                initHttp(url);
            }
        });

        mEmptyLayout.showLoading();
        if ("商品晒单".equals(Title)) {
            map.put("productId", ID);
            url = JConstant.QUERYPRODUCTSHOWDOC_;
        } else if ("所有晒单".equals(Title)) {
            url = JConstant.QURTYALLDOCUMENT_;
        }
        initHttp(url);

        listView.setMode(PullToRefreshBase.Mode.BOTH);
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                pageindext = 1;
                goodsTheSunDatas.clear();
                initHttp(url);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                pageindext++;
                initHttp(url);
            }
        });

    }

    //查询晒单
    private void initHttp(int url) {
        map.put("pageIndex", pageindext);
        ApiClient.send(activity, url, map, GoodsTheSun.class, new DataLoader<GoodsTheSun>() {
            @Override
            public void task(String data) {
            }

            @Override
            public void succeed(GoodsTheSun data) {
                listView.onRefreshComplete();
                goodsTheSunDatas.addAll(data.data);
                handler.sendEmptyMessage(0);

                if (goodsTheSunDatas != null && goodsTheSunDatas.size() > 0) {
                    mEmptyLayout.showView();
                } else {
                    mEmptyLayout.showEmpty();
                }
            }

            @Override
            public void error(String message) {
                mEmptyLayout.showError(message);
                listView.onRefreshComplete();
            }
        }, url);
    }

    //点赞
    private void initSaveLikes(final int pos) {

        if (!application.isLogin()) {
            ToastView.show(activity, "请先登录");
            Intent intent = new Intent(activity, SignInActivity.class);
            startActivity(intent);
            return;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("listid", goodsTheSunDatas.get(pos).id);
        ApiClient.send(activity, JConstant.SAVELIKES_, map, true, null, new DataLoader<String>() {

            @Override
            public void task(String data) {

            }

            @Override
            public void succeed(String data) {
                int num = Integer.valueOf(goodsTheSunDatas.get(pos).praiseCount);
                num++;
                goodsTheSunDatas.get(pos).praiseCount = String.valueOf(num);
                toastSuccess("点赞成功");
                adapter.notifyDataSetChanged();
            }

            @Override
            public void error(String message) {
            }
        }, JConstant.SAVELIKES_);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if (adapter == null) {
                        adapter = new TheSunAdapter(activity, goodsTheSunDatas);
                        listView.setAdapter(adapter);
                    } else {
                        adapter.notifyDataSetChanged();
                    }
                    adapter.setOnClick(new TowObjectParameterInterface() {
                        @Override
                        public void Onchange(int type, int pos, Object conten) {
                            switch (type) {
                                case 1:
                                    //点赞
                                    initSaveLikes(pos);
                                    break;
                                case 2:
                                    //评论
                                    replaceFragment(R.id.common_frame, TheSunAllCommentFragment.newInstance(goodsTheSunDatas.get(pos).id));
                                    break;
                                case 3:
                                    //详情
                                    replaceFragment(R.id.common_frame, TheSunDetailsFragment.newInstance(goodsTheSunDatas.get(pos), (int) conten));
                                    break;
                            }
                        }
                    });
                    break;
                case 1:
                    break;
            }
        }
    };

    @Override
    public boolean onBackPressed() {
        finishFramager();
        return false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ApiClient.cancelRequest(JConstant.QUERYPRODUCTSHOWDOC_);
        ApiClient.cancelRequest(JConstant.QURTYALLDOCUMENT_);
        ApiClient.cancelRequest(JConstant.SAVELIKES_);
    }
}
