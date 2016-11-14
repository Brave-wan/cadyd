package com.cadyd.app.ui.fragment.mall;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ListView;
import android.widget.TextView;
import butterknife.Bind;
import com.cadyd.app.JConstant;
import com.cadyd.app.R;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.asynctask.DataLoader;
import com.cadyd.app.model.CityInfo;
import com.cadyd.app.model.LeaguePrefer;
import com.cadyd.app.model.LeaguePreferPage;
import com.cadyd.app.ui.base.BaseFragement;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import org.wcy.android.utils.PreferenceUtils;
import org.wcy.android.utils.adapter.CommonAdapter;
import org.wcy.android.utils.adapter.ViewHolder;
import org.wcy.android.widget.EmptyLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 联盟优惠卷
 *
 * @author wcy
 */
public class LeaguePreferFragment extends BaseFragement {
    @Bind(R.id.user_one_record_list)
    PullToRefreshListView listView;
    @Bind(R.id.empty_layout)
    View relativeLayout;
    List<LeaguePrefer> mlist;
    CommonAdapter<LeaguePrefer> adapter;
    EmptyLayout mEmptyLayout;
    private int page = 1;
    private String merchanid;

    public static LeaguePreferFragment newInstance(String merchanid) {
        LeaguePreferFragment newFragment = new LeaguePreferFragment();
        Bundle bundle = new Bundle();
        bundle.putString("merchanid", merchanid);
        newFragment.setArguments(bundle);
        return newFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            this.merchanid = args.getString("merchanid");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return setView(R.layout.fragment_league_prefer, "联盟优惠卷", true);
    }

    @Override
    protected void initView() {
        mlist = new ArrayList<>();
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
        adapter = new CommonAdapter<LeaguePrefer>(activity, mlist, R.layout.league_prefer_iteam) {
            @Override
            public void convert(ViewHolder helper, final LeaguePrefer item) {
                helper.setText(R.id.title, item.title);
                helper.setText(R.id.shop_name, item.name);
                helper.setText(R.id.valid, String.format("有效期：%s", item.invalid));
                helper.setText(R.id.price, String.format("￥%d", item.money));
                View league_bg = helper.getView(R.id.league_bg);
                TextView receive = helper.getView(R.id.receive);
                if (item.money <= 20) {
                    league_bg.setBackgroundResource(R.mipmap.league_prefer_one);
                    receive.setBackgroundResource(R.drawable.league_prefer_one_bg);
                    receive.setTextColor(getResources().getColor(R.color.league_prefer_one));
                } else if (item.money > 20 && item.money <= 50) {
                    league_bg.setBackgroundResource(R.mipmap.league_prefer_two);
                    receive.setBackgroundResource(R.drawable.league_prefer_two_bg);
                    receive.setTextColor(getResources().getColor(R.color.league_prefer_two));
                } else if (item.money > 50 && item.money <= 100) {
                    league_bg.setBackgroundResource(R.mipmap.league_prefer_three);
                    receive.setBackgroundResource(R.drawable.league_prefer_three_bg);
                    receive.setTextColor(getResources().getColor(R.color.league_prefer_three));
                } else {
                    league_bg.setBackgroundResource(R.mipmap.league_prefer_four);
                    receive.setBackgroundResource(R.drawable.league_prefer_four_bg);
                    receive.setTextColor(getResources().getColor(R.color.league_prefer_four));
                }

                receive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("preferid", item.id);
                        map.put("merchanid", merchanid);
                        ApiClient.send(activity, JConstant.PREFER_RECEIVE_, map, true, null, new DataLoader<Object>() {
                            @Override
                            public void task(String data) {

                            }

                            @Override
                            public void succeed(Object data) {
                                toastSuccess("领取成功");
                            }

                            @Override
                            public void error(String message) {

                            }
                        }, JConstant.PREFER_RECEIVE_);
                    }
                });
            }
        };
        listView.setAdapter(adapter);
        loading();
    }

    int totalPage = 1;

    //LeaguePreferPage.class
    private LeaguePrefer leaguePrefer;

    private void loading() {
        CityInfo city = PreferenceUtils.getObject(getActivity(), CityInfo.class);
        Map<String, Object> map = new HashMap<>();
        map.put("latitude", city.latitude);
        map.put("pageIndex", page);
        map.put("longitude", city.longitude);
        ApiClient.send(activity, JConstant.LEAGUEPREFER, map, LeaguePrefer.getType(), new DataLoader<List<LeaguePrefer>>() {
            @Override
            public void task(String data) {

            }

            @Override
            public void succeed(List<LeaguePrefer> data) {
                listView.onRefreshComplete();
//                if (page == 1) {
//                    totalPage = data.totalPage;
//                }
//                if (totalPage == page) {
//                    listView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
//                } else {
//                    listView.setMode(PullToRefreshBase.Mode.BOTH);
//                }
                if (data != null && data.size() > 0) {
                    if (page == 1) {
                        mlist.clear();
                    }
                    mlist.addAll(data);
                    adapter.notifyDataSetChanged();
                    mEmptyLayout.showView();
                } else {
                    if (page == 1) {
                        mEmptyLayout.showEmpty();
                    }
                }
            }

            @Override
            public void error(String message) {
                listView.onRefreshComplete();
                if (page == 1) {
                    mEmptyLayout.showError(message);
                } else {
                    page--;
                }
            }
        }, JConstant.LEAGUEPREFER);
    }

    @Override
    public boolean onBackPressed() {
        finishActivity();
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ApiClient.cancelRequest(JConstant.PREFER_RECEIVE_);
        ApiClient.cancelRequest(JConstant.LEAGUEPREFER);
    }
}
