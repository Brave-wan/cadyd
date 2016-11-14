package com.cadyd.app.ui.fragment.user;

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
import com.cadyd.app.model.IntegralListData;
import com.cadyd.app.model.IntegralListModle;
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
 * 花币明细
 */
public class SpendMoneyListFragment extends BaseFragement {


    private int index = 1;
    private int totalpage;

    @Bind(R.id.spend_money_list)
    PullToRefreshListView listView;

    private CommonAdapter<IntegralListData> adapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return setView(R.layout.fragment_spend_money_list, "花币明细", true);
    }


    @Override
    protected void initView() {

        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                index = 1;
                integralListDatas.clear();
                initHttp();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (index < totalpage) {
                    index++;
                    initHttp();
                }
            }
        });
        initHttp();
    }

    private List<IntegralListData> integralListDatas = new ArrayList<>();

    private void initHttp() {
        Map<String, Object> map = new HashMap<>();
        map.put("pageIndex", index);
        ApiClient.send(activity, JConstant.SELECTCURRENTBYMID_, map, IntegralListModle.class, new DataLoader<IntegralListModle>() {
            @Override
            public void task(String data) {

            }

            @Override
            public void succeed(IntegralListModle data) {
//                listView.onRefreshComplete();
//                totalpage = data.totalPage;
//                integralListDatas.addAll(data.data);
//                if (adapter == null) {
//                    adapter = new CommonAdapter<IntegralListData>(activity, integralListDatas, R.layout.integral_list_item) {
//                        @Override
//                        public void convert(ViewHolder helper, IntegralListData item) {
//                            helper.setText(R.id.integral_time, item.addtime);
//                            helper.setText(R.id.integral_number, item.detail);
//                        }
//                    };
//                    listView.setAdapter(adapter);
//                } else {
//                    adapter.notifyDataSetChanged();
//                }
            }

            @Override
            public void error(String message) {

            }
        }, JConstant.SELECTCURRENTBYMID_);
    }

    @Override
    public boolean onBackPressed() {
        finishFramager();
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ApiClient.cancelRequest(JConstant.SELECTCURRENTBYMID_);
    }
}
