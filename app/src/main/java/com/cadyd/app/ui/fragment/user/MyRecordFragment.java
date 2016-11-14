package com.cadyd.app.ui.fragment.user;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import butterknife.Bind;
import com.cadyd.app.JConstant;
import com.cadyd.app.R;
import com.cadyd.app.adapter.RecordAdapter;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.asynctask.DataLoader;
import com.cadyd.app.interfaces.TowObjectParameterInterface;
import com.cadyd.app.model.RecordModle;
import com.cadyd.app.model.RecordModleData;
import com.cadyd.app.ui.base.BaseFragement;
import com.cadyd.app.ui.fragment.unitary.AddTheSunFragment;
import com.cadyd.app.ui.fragment.unitary.AllTescoNumberFragment;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import org.wcy.android.widget.EmptyLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 我的记录
 */
public class MyRecordFragment extends BaseFragement {

    private int index = 1;
    private int TYPE = 0;

    public boolean isPrize = false;

    @Bind(R.id.user_one_radio)
    RadioGroup group;
    @Bind(R.id.user_one_all)
    RadioButton allButton;

    @Bind(R.id.relative)
    RelativeLayout relativeLayout;
    @Bind(R.id.user_one_record_list)
    PullToRefreshListView listView;

    EmptyLayout emptyLayout;

    public void setIsPrize(Boolean is) {
        isPrize = is;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        String titele;
        if (isPrize) {
            titele = "获得的奖品";
        } else {
            titele = "我的记录";
        }
        return setView(R.layout.fragment_my_record, titele, true);
    }

    @Override
    protected void initView() {
        emptyLayout = new EmptyLayout(activity, relativeLayout);
        layout.leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishFramager();
            }
        });
        listView.setMode(PullToRefreshBase.Mode.BOTH);
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                recordModleDataList.clear();
                index = 1;
                initHttp();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                index++;
                initHttp();
            }
        });

        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.user_one_all://全部
                        TYPE = 0;
                        recordModleDataList.clear();
                        index = 1;
                        initHttp();
                        break;
                    case R.id.user_one_ing://进行中
                        TYPE = 1;
                        recordModleDataList.clear();
                        index = 1;
                        initHttp();
                        break;
                    case R.id.user_one_end://已经开奖
                        TYPE = 2;
                        recordModleDataList.clear();
                        index = 1;
                        initHttp();
                        break;
                }
            }
        });
        if (isPrize) {
            group.setVisibility(View.GONE);
            initHttp();
        } else {
            allButton.setChecked(true);
        }

    }

    private RecordAdapter adapter;
    private List<RecordModleData> recordModleDataList = new ArrayList<>();

    private void initHttp() {
        int url;
        Map<String, Object> map = new HashMap<>();
        if (isPrize) {//获得的奖品
            url = JConstant.QUERYMYALLPRODCT_;
            map.put("pageIndex", index);
        } else {//我的记录
            url = JConstant.QUERYMYALLRECORD_;
            map.put("pageIndex", index);
            map.put("state", TYPE);

        }

        ApiClient.send(activity, url, map, true, RecordModle.class, new DataLoader<RecordModle>() {
            @Override
            public void task(String data) {
            }

            @Override
            public void succeed(RecordModle data) {
                if (data != null) {
                    recordModleDataList.addAll(data.data);
                    if (recordModleDataList.size() <= 0) {
                        emptyLayout.showEmpty();
                    } else {
                        emptyLayout.showView();
                    }
                    if (adapter == null) {
                        adapter = new RecordAdapter(activity, recordModleDataList, isPrize);
                        adapter.setClick(new TowObjectParameterInterface() {//幸运乐购码的点击回调
                            @Override
                            public void Onchange(int type, int pos, Object obj) {
                                switch (type) {
                                    case 0://查询乐购码
                                        replaceFragment(R.id.common_frame, AllTescoNumberFragment.newInstance(recordModleDataList.get(pos).tbid));
                                        break;
                                    case 1://新增晒单
                                        replaceFragment(R.id.common_frame, AddTheSunFragment.newInstance(recordModleDataList.get(pos)));
                                        break;
                                }
                            }
                        });
                        listView.setAdapter(adapter);
                    } else {
                        adapter.notifyDataSetChanged();
                    }
                    listView.onRefreshComplete();
                }
            }

            @Override
            public void error(String message) {
                listView.onRefreshComplete();
            }
        }, url);
    }

    @Override
    public boolean onBackPressed() {
        finishFramager();
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ApiClient.cancelRequest(JConstant.QUERYMYALLPRODCT_);
        ApiClient.cancelRequest(JConstant.QUERYMYALLRECORD_);
    }
}
