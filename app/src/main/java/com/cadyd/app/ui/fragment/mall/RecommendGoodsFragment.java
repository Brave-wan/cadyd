package com.cadyd.app.ui.fragment.mall;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.ListView;
import butterknife.Bind;
import com.cadyd.app.JConstant;
import com.cadyd.app.R;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.asynctask.DataLoader;
import com.cadyd.app.model.Good;
import com.cadyd.app.ui.activity.GoodsDetailActivity;
import com.cadyd.app.ui.base.BaseFragement;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import org.wcy.android.utils.adapter.CommonAdapter;
import org.wcy.android.utils.adapter.ViewHolder;
import org.wcy.android.widget.EmptyLayout;
import org.wcy.common.utils.NumberUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品推荐
 *
 * @author wcy
 */
public class RecommendGoodsFragment extends BaseFragement {
    private String gid;
    private int page;
    @Bind(R.id.user_one_record_list)
    PullToRefreshListView listView;
    @Bind(R.id.empty_layout)
    View relativeLayout;
    List<Good> mlist;
    CommonAdapter<Good> adapter;
    EmptyLayout mEmptyLayout;

    public static RecommendGoodsFragment newInstance(String gid) {
        RecommendGoodsFragment newFragment = new RecommendGoodsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("gid", gid);
        newFragment.setArguments(bundle);
        return newFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        mlist = new ArrayList<>();
        if (args != null) {
            this.gid = args.getString("gid");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return setView(R.layout.fragment_recommend_goods, "为您推荐", true);
    }

    @Override
    protected void initView() {
        mEmptyLayout = new EmptyLayout(activity, relativeLayout);
        mEmptyLayout.setReloadOnclick(new EmptyLayout.ReloadOnClick() {
            @Override
            public void reload() {
                loading();
            }
        });
        mEmptyLayout.showLoading();
        listView.setMode(PullToRefreshBase.Mode.BOTH);
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                page = 1;
                loading();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                page++;
                loading();
            }
        });
        adapter = new CommonAdapter<Good>(activity, mlist, R.layout.goodes_rank_list_iteam) {
            @Override
            public void convert(ViewHolder helper, final Good item) {
                ImageView iv = helper.getView(R.id.image_view);
                ApiClient.ImageLoadersRounde(item.thumb, iv, 5);
                helper.setText(R.id.name, item.title);
                helper.setText(R.id.price, String.format("￥%s", NumberUtil.getString(item.price, 2)));
                helper.setText(R.id.bought, String.format("已有%d人购买", item.bought));
                helper.getConvertView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(activity, GoodsDetailActivity.class);
                        intent.putExtra("gid", item.id);
                        activity.startActivity(intent);
                    }
                });
            }
        };
        listView.setAdapter(adapter);
        loading();
    }

    private void loading() {
        Map<String, Object> map = new HashMap<>();
        map.put("type", "recommend");
        map.put("pageIndex", page);
        map.put("goodsId", gid);
        ApiClient.send(activity, JConstant.GOODSRECOMMEND_, map, Good.getType(), new DataLoader<List<Good>>() {
            @Override
            public void task(String data) {

            }

            @Override
            public void succeed(List<Good> data) {
                if (page == 1) {
                    mlist.clear();
                }
                if (data != null && data.size() > 0) {
                    mlist.addAll(data);
                    adapter.notifyDataSetChanged();
                    mEmptyLayout.showView();
                } else if (mlist.size() <= 0) {
                    mEmptyLayout.showEmpty();
                }
                listView.onRefreshComplete();
            }

            @Override
            public void error(String message) {
                if (page == 1) {
                    mEmptyLayout.showError(message);
                } else {
                    page--;
                }
                listView.onRefreshComplete();
            }
        }, JConstant.GOODSRECOMMEND_);
    }

    @Override
    public boolean onBackPressed() {
        finishFramager();
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ApiClient.cancelRequest(JConstant.GOODSRECOMMEND_);
    }
}
