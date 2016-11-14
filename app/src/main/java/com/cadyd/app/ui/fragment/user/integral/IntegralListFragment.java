package com.cadyd.app.ui.fragment.user.integral;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ListView;
import butterknife.Bind;
import com.cadyd.app.JConstant;
import com.cadyd.app.R;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.asynctask.DataLoader;
import com.cadyd.app.model.*;
import com.cadyd.app.ui.base.BaseFragement;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import org.wcy.android.utils.adapter.CommonAdapter;
import org.wcy.android.utils.adapter.ViewHolder;
import org.wcy.android.widget.EmptyLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 积分明细
 */
public class IntegralListFragment extends BaseFragement {

    private int TYPE;//0.余额 1.积分 2.花币

    private int index = 1;

    @Bind(R.id.integral_list)
    PullToRefreshListView listView;
    @Bind(R.id.empty_layout)
    View relativeLayout;
    EmptyLayout mEmptyLayout;
    private CommonAdapter<IntegralListData> adapter;

    private CommonAdapter<SpendMoneylListData> Spendadapter;

    public static IntegralListFragment newInstance(int type) {
        IntegralListFragment newFragment = new IntegralListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        newFragment.setArguments(bundle);
        return newFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            this.TYPE = args.getInt("type");
        }
    }

    private String title;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        switch (TYPE) {
            case 0://余额
                title = "余额";
                break;
            case 1://积分
                title = "积分";
                break;
            case 2://花币
                title = "花币";
                break;
        }
        return setView(R.layout.fragment_integral_list, title + "明细", true);
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

                switch (TYPE) {
                    case 1://积分
                        index = 1;
                        integralListDatas.clear();
                        break;
                    case 2://花币
                        index = 1;
                        spendMoneylListDatas.clear();
                        break;
                }
                loading();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

                switch (TYPE) {
                    case 1://积分
                        index++;
                        break;
                    case 2://花币
                        index++;
                        break;
                }
                loading();
            }
        });
        loading();
    }

    private void loading() {
        switch (TYPE) {
            case 0://余额
                initEndHttp();
                break;
            case 1://积分
                initHttp();
                break;
            case 2://花币
                initSpendHttp();
                break;
        }

    }

    private List<IntegralListData> integralListDatas = new ArrayList<>();

    //积分
    private void initHttp() {
        //b81c39c2d3a611e5955b00163f000345
        Map<String, Object> map = new HashMap<>();
        map.put("pageIndex", index);
        ApiClient.send(activity, JConstant.SELECTINTEGRATIONBYMID_, map, IntegralListModle.class, new DataLoader<IntegralListModle>() {
            @Override
            public void task(String data) {
            }

            @Override
            public void succeed(IntegralListModle data) {
                listView.onRefreshComplete();
                if (data != null && data.data != null && data.data.size() > 0) {
                    integralListDatas.addAll(data.data);
                    if (adapter == null) {
                        adapter = new CommonAdapter<IntegralListData>(activity, integralListDatas, R.layout.integral_list_item) {
                            @Override
                            public void convert(ViewHolder helper, IntegralListData item) {
                                helper.setText(R.id.integral_time, item.addtime);
                                helper.setText(R.id.integral_number, item.content);
                                helper.setText(R.id.integral_content, item.detail);
                                helper.setText(R.id.integral_name, title);
                            }
                        };
                        listView.setAdapter(adapter);
                    } else {
                        adapter.notifyDataSetChanged();
                    }
                    mEmptyLayout.showView();
                } else {
                    mEmptyLayout.showEmpty("暂无积分明细");
                }
            }

            @Override
            public void error(String message) {
                mEmptyLayout.showEmpty("暂无积分明细");
                listView.onRefreshComplete();
            }
        }, JConstant.SELECTINTEGRATIONBYMID_);
    }


    private List<SpendMoneylListData> spendMoneylListDatas = new ArrayList<>();

    //花币
    private void initSpendHttp() {
        //df02f88b6feb4b71a09ab902d6ce950f
        Map<String, Object> map = new HashMap<>();
        map.put("pageIndex", index);
        ApiClient.send(activity, JConstant.SELECTCURRENTBYMID_, map, SpendMoneylListModle.class, new DataLoader<SpendMoneylListModle>() {
            @Override
            public void task(String data) {
            }

            @Override
            public void succeed(SpendMoneylListModle data) {
                listView.onRefreshComplete();
                if (data != null && data.data != null && data.data.size() > 0) {
                    spendMoneylListDatas.addAll(data.data);
                    if (Spendadapter == null) {
                        Spendadapter = new CommonAdapter<SpendMoneylListData>(activity, spendMoneylListDatas, R.layout.integral_list_item) {
                            @Override
                            public void convert(ViewHolder helper, SpendMoneylListData item) {
                                helper.setText(R.id.integral_time, item.createtime);
                                helper.setText(R.id.integral_number, item.amount);
                                helper.setText(R.id.integral_content, item.name);
                                helper.setText(R.id.integral_name, title);
                            }
                        };
                        listView.setAdapter(Spendadapter);
                    } else {
                        Spendadapter.notifyDataSetChanged();
                    }
                    mEmptyLayout.showView();
                } else {
                    mEmptyLayout.showEmpty("暂无花币明细");
                }
            }

            @Override
            public void error(String message) {
                mEmptyLayout.showEmpty("暂无花币明细");
                listView.onRefreshComplete();
            }
        }, JConstant.SELECTCURRENTBYMID_);
    }


    private List<EndMoneylListData> endMoneylListDatas;
    private CommonAdapter<EndMoneylListData> endAdapter;

    //余额
    private void initEndHttp() {
        Map<String, Object> map = new HashMap<>();
        map.put("pageIndex", index);
        //df02f88b6feb4b71a09ab902d6ce950f
        ApiClient.send(activity ,JConstant.SELECTRECHARGEBYID_, map, EndMoneylListModle.class, new DataLoader<EndMoneylListModle>() {
            @Override
            public void task(String data) {
            }

            @Override
            public void succeed(EndMoneylListModle data) {
                listView.onRefreshComplete();
                if (data != null && data.data != null && data.data.size() > 0) {
                    endMoneylListDatas = data.data;
                    if (endAdapter == null) {
                        endAdapter = new CommonAdapter<EndMoneylListData>(activity, endMoneylListDatas, R.layout.integral_list_item) {
                            @Override
                            public void convert(ViewHolder helper, EndMoneylListData item) {
                                helper.setText(R.id.integral_time, item.addtime);
                                helper.setText(R.id.integral_number, item.credit);
                                if (item.rechargetype == 0) {
                                    helper.setText(R.id.integral_content, item.content.concat("  充值成功"));
                                } else if (item.rechargetype == 0) {
                                    helper.setText(R.id.integral_content, item.content.concat("  充值失败"));
                                } else {
                                    helper.setText(R.id.integral_content, item.content.concat("  充值异常"));
                                }
                                helper.setText(R.id.integral_name, title);
                            }
                        };
                        listView.setAdapter(endAdapter);
                    } else {
                        endAdapter.notifyDataSetChanged();
                    }
                    mEmptyLayout.showView();
                } else {
                    mEmptyLayout.showEmpty("暂无余额明细");
                }
            }

            @Override
            public void error(String message) {
                mEmptyLayout.showEmpty("暂无余额明细");
                listView.onRefreshComplete();
            }
        }, JConstant.SELECTRECHARGEBYID_);
    }

    @Override
    public boolean onBackPressed() {
        finishFramager();
        return false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ApiClient.cancelRequest(JConstant.SELECTCURRENTBYMID_);
        ApiClient.cancelRequest(JConstant.SELECTINTEGRATIONBYMID_);
        ApiClient.cancelRequest(JConstant.SELECTRECHARGEBYID_);
    }
}
