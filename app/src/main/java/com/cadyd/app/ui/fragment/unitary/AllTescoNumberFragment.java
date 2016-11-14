package com.cadyd.app.ui.fragment.unitary;

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
import com.cadyd.app.model.AllLuckNumber;
import com.cadyd.app.model.AllLuckNumberData;
import com.cadyd.app.ui.base.BaseFragement;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import org.wcy.android.utils.adapter.CommonAdapter;
import org.wcy.android.utils.adapter.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 查询该开奖商品的所有乐购码
 */
public class AllTescoNumberFragment extends BaseFragement {
    private String id;

    public static AllTescoNumberFragment newInstance(String id) {
        AllTescoNumberFragment newFragment = new AllTescoNumberFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        newFragment.setArguments(bundle);
        return newFragment;
    }

    private int index = 1;
    private List<AllLuckNumberData> datas = new ArrayList<>();
    private CommonAdapter<AllLuckNumberData> adapter;
    @Bind(R.id.all_tesco_number_list)
    PullToRefreshListView pullToRefreshListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            id = args.getString("id");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return setView(R.layout.fragment_all_tesco_number, "我的乐购码", true);
    }

    @Override
    protected void initView() {
        pullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        pullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                index = 1;
                datas.clear();
                initHttp();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                index++;
                initHttp();
            }
        });

        initHttp();
    }

    private void initHttp() {
        Map<String, Object> map = new HashMap<>();
        map.put("tbid", id);//商品id
        map.put("pageIndex", index);//当前页
        ApiClient.send(activity, JConstant.QUERYLUCKCODEBYUSERID_, map, AllLuckNumber.class, new DataLoader<AllLuckNumber>() {
            @Override
            public void task(String data) {
            }

            @Override
            public void succeed(AllLuckNumber data) {
                pullToRefreshListView.onRefreshComplete();
                datas.addAll(data.data);
                if (adapter == null) {
                    adapter = new CommonAdapter<AllLuckNumberData>(activity, datas, R.layout.all_tesco_number_item) {
                        @Override
                        public void convert(ViewHolder helper, AllLuckNumberData item) {
                            helper.setText(R.id.number_text, item.luckCode);
                            helper.setText(R.id.time_text, item.createtime);
                        }
                    };
                    pullToRefreshListView.setAdapter(adapter);
                } else {
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void error(String message) {
                pullToRefreshListView.onRefreshComplete();
            }
        }, JConstant.QUERYLUCKCODEBYUSERID_);
    }

    @Override
    public boolean onBackPressed() {
        finishFramager();
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ApiClient.cancelRequest(JConstant.QUERYLUCKCODEBYUSERID_);
    }
}
