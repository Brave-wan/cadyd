package com.cadyd.app.ui.fragment.unitary;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.OnClick;

import com.cadyd.app.JConstant;
import com.cadyd.app.R;
import com.cadyd.app.adapter.OneCarAdapter;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.asynctask.DataLoader;
import com.cadyd.app.comm.CommaSplice;
import com.cadyd.app.comm.MyChange;
import com.cadyd.app.interfaces.BooleanOneByOneInterface;
import com.cadyd.app.interfaces.TowObjectParameterInterface;
import com.cadyd.app.model.OneCarGoodsData;
import com.cadyd.app.model.OneCarGoodsModle;
import com.cadyd.app.model.OneGoodsModel;
import com.cadyd.app.ui.activity.CommonActivity;
import com.cadyd.app.ui.base.BaseFragement;
import com.cadyd.app.ui.view.AlertConfirmation;
import com.cadyd.app.ui.view.ToastView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.wcy.android.widget.EmptyLayout;
import org.wcy.android.widget.OnEmptyFoundClick;

import java.io.Serializable;
import java.util.*;

public class OneYuanShoppingCarFragment extends BaseFragement {

    private boolean setclick = false;
    private OnEmptyFoundClick foundClick;

    public void setClick(OnEmptyFoundClick foundClick) {
        setclick = true;
        this.foundClick = foundClick;
    }

    private List<OneCarGoodsData> dates = new ArrayList<>();
    private int pageindext = 1;
    private OneCarAdapter adapter;
    private AlertConfirmation aler;

    private int allMoney;

    @Bind(R.id.one_shopping_car_list)
    PullToRefreshListView listview;

    //全选
    @Bind(R.id.one_car_all_check)
    CheckBox check;

    //合计
    @Bind(R.id.one_car_money)
    TextView money;

    @Bind(R.id.empty_layout)
    View relativeLayout;
    EmptyLayout mEmptyLayout;

    private boolean isRefresh = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        boolean isBack = true;
        Bundle bundle = getArguments();
        if (bundle != null) {
            isBack = bundle.getBoolean("isBack");
        }
        return setView(R.layout.one_yuan_shopping_car, "购物车", isBack, "删除");
    }

    //刷新购物车
    public void MyRefresh() {
        if (isRefresh) {
            pageindext = 1;
            dates.clear();
            initHttp();
        }
    }

    @Override
    protected void initView() {
        mEmptyLayout = new EmptyLayout(activity, relativeLayout);
        mEmptyLayout.setReloadOnclick(new EmptyLayout.ReloadOnClick() {
            @Override
            public void reload() {
                initHttp();
            }
        });
        // mEmptyLayout.showLoading();
        layout.leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (setclick) {
                    finishActivity();
                } else {
                    finishFramager();
                }
            }
        });
        listview.setMode(PullToRefreshBase.Mode.BOTH);
        listview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                MyRefresh();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                pageindext++;
                initHttp();
            }
        });

        //删除
        layout.rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final List<String> lists = new ArrayList<>();
                for (int i = 0; i < dates.size(); i++) {
                    if (dates.get(i).checked) {
                        lists.add(dates.get(i).cartid);
                    }
                }
                if (lists.size() <= 0) {
                    ToastView.show(activity, "请先选择商品");
                    return;
                }
                aler = new AlertConfirmation(activity, "删除", "确定删除这些商品？");
                aler.show(new AlertConfirmation.OnClickListeners() {
                    @Override
                    public void ConfirmOnClickListener() {
                        deleteInitHttp(lists);
                        aler.hide();
                    }

                    @Override
                    public void CancelOnClickListener() {
                        aler.hide();
                    }
                });
            }
        });
        initHttp();
        isRefresh = true;
    }

    //删除
    public void deleteInitHttp(List<String> lists) {
        StringBuffer buffer;
        buffer = CommaSplice.Splice(lists);
        Map<String, Object> map = new HashMap<>();
        map.put("id", buffer.toString());
        ApiClient.send(activity, JConstant.DELETESCHOPPIHNGCARTLIST_, map, true, null, new DataLoader<String>() {
            @Override
            public void task(String data) {

            }

            @Override
            public void succeed(String data) {
                toastSuccess("删除成功");
                pageindext = 1;
                dates.clear();
                initHttp();
            }

            @Override
            public void error(String message) {

            }
        }, JConstant.DELETESCHOPPIHNGCARTLIST_);
    }

    //获取购物车的商品
    private void initHttp() {
        Map<String, Object> map = new HashMap<>();
        map.put("pageIndex", pageindext);
        ApiClient.send(activity, JConstant.QUERYSHOPPINGCARBYUSER_, map, true, OneCarGoodsModle.class, new DataLoader<OneCarGoodsModle>() {
            @Override
            public void task(String data) {
            }

            @Override
            public void succeed(OneCarGoodsModle data) {
                listview.onRefreshComplete();
                if (data.data != null) {
                    dates.addAll(data.data);
                    if (dates.size() <= 0) {
                        mEmptyLayout.setShowEmptyClick(true, new OnEmptyFoundClick() {
                            @Override
                            public void onFoundCilck() {
                                if (foundClick != null) {
                                    foundClick.onFoundCilck();
                                }
                            }
                        });
                        mEmptyLayout.showEmpty("购物车空空如也");
                    } else {
                        Calculation();
                        handler.sendEmptyMessage(0);
                        mEmptyLayout.showView();
                    }
                }
            }

            @Override
            public void error(String message) {
                mEmptyLayout.showError(message);
                listview.onRefreshComplete();
            }
        }, JConstant.QUERYSHOPPINGCARBYUSER_);
    }

    //重新计算
    private void Calculation() {
        allMoney = 0;
        for (int i = 0; i < dates.size(); i++) {
            if (dates.get(i).checked) {
                if ("10".equals(dates.get(i).average)) {
                    allMoney += 10 * dates.get(i).buytimes;
                } else {
                    allMoney += 1 * dates.get(i).buytimes;
                }
            }
        }
        money.setText(MyChange.ChangeTextColor("合计:" + allMoney + "花币", 3, (3 + String.valueOf(allMoney).length() + 2), Color.RED));
    }

    //全选
    @OnClick(R.id.one_car_all_check)
    public void onChecked(View view) {
        for (int i = 0; i < dates.size(); i++) {
            dates.get(i).checked = check.isChecked();
        }
        adapter.notifyDataSetChanged();
        Calculation();
    }

    //结算
    @OnClick(R.id.one_car_send)
    public void onSend(View view) {
        List<OneGoodsModel> lists = new ArrayList<>();
        for (int i = 0; i < dates.size(); i++) {
            if (dates.get(i).checked) {
                OneGoodsModel modle = new OneGoodsModel();
                modle.number = dates.get(i).buytimes;
                modle.tbid = dates.get(i).tbid;
                modle.title = dates.get(i).title;
                modle.hastimes = dates.get(i).hastimes + "";
                modle.price = "";
                modle.participatetimes = dates.get(i).participatetimes;
                modle.hasparticipatetimes = String.valueOf(Integer.valueOf(dates.get(i).participatetimes) - Integer.valueOf(dates.get(i).surplustimes));
                modle.average = dates.get(i).average;
                modle.mainImg = dates.get(i).mainImg;
                lists.add(modle);
            }
        }

        if (lists.size() <= 0) {
            ToastView.show(activity, "请先选择商品");
            return;
        }

        Intent intent = new Intent(activity, CommonActivity.class);
        intent.putExtra(JConstant.EXTRA_INDEX, CommonActivity.ONE_YUAN_PAY_MENT);
        intent.putExtra("models", (Serializable) lists);
        startActivityForResult(intent, 0);
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (adapter == null) {
                adapter = new OneCarAdapter(activity, dates);
                adapter.setOnClick(new TowObjectParameterInterface() {
                    @Override
                    public void Onchange(int type, int pos, Object conten) {
                        switch (type) {
                            case 0:
                                dates.get(pos).buytimes = Integer.valueOf((String) conten);
                                adapter.notifyDataSetChanged();
                                Calculation();
                                break;
                        }

                        listview.setFocusable(true);
                    }
                }, new BooleanOneByOneInterface() {
                    @Override
                    public void Onchange(int postion, Boolean bool) {
                        if (!bool) {
                            check.setChecked(false);
                        } else {
                            if (dates.size() > 0) {
                                int num = 1;
                                for (int i = 0; i < dates.size(); i++) {
                                    if (dates.get(i).checked) {
                                        num++;
                                    }
                                }
                                if (num == dates.size()) {
                                    check.setChecked(true);
                                }
                            }
                        }
                        dates.get(postion).checked = bool;
                        Calculation();
                    }
                });
                listview.setAdapter(adapter);
            } else {
                adapter.notifyDataSetChanged();
            }
            Calculation();
        }
    };


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            pageindext = 1;
            dates.clear();
            initHttp();
        }
    }

    @Override
    public boolean onBackPressed() {
        finishFramager();
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ApiClient.cancelRequest(JConstant.DELETESCHOPPIHNGCARTLIST_);
        ApiClient.cancelRequest(JConstant.QUERYSHOPPINGCARBYUSER_);
    }
}
