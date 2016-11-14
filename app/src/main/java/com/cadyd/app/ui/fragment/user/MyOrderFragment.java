package com.cadyd.app.ui.fragment.user;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import butterknife.Bind;
import butterknife.OnClick;

import com.cadyd.app.JConstant;
import com.cadyd.app.R;
import com.cadyd.app.adapter.MyOrderAdapter;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.asynctask.DataLoader;
import com.cadyd.app.comm.Utils;
import com.cadyd.app.handle.InterFace;
import com.cadyd.app.model.MyOrder;
import com.cadyd.app.model.MyOrderPage;
import com.cadyd.app.model.Usr;
import com.cadyd.app.ui.activity.CommonActivity;
import com.cadyd.app.ui.activity.LookEvaluateActivity;
import com.cadyd.app.ui.activity.LookLogisticsActivity;
import com.cadyd.app.ui.base.BaseFragement;
import com.cadyd.app.ui.fragment.cart.ConfirmShoppingCartFragment;
import com.cadyd.app.ui.view.AlertConfirmation;
import com.cadyd.app.ui.view.DividerGridItemDecoration;
import com.cadyd.app.ui.window.OrderCancelUsrPopupWindow;
import com.cadyd.app.utils.EmptyRecyclerView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.ta.utdid2.android.utils.PhoneInfoUtils;

import org.wcy.android.utils.LogUtil;
import org.wcy.common.utils.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商城-我的订单
 *
 * @author wcy
 */
public class MyOrderFragment extends BaseFragement {
    boolean isEvaluate = false;
    private Integer state;
    private String oid;

    public static MyOrderFragment newInstance(boolean isEvaluate) {
        MyOrderFragment newFragment = new MyOrderFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("isEvaluate", isEvaluate);
        newFragment.setArguments(bundle);
        return newFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            this.isEvaluate = args.getBoolean("isEvaluate");
        }
    }


    @Bind(R.id.detail_rg)
    RadioGroup detail_rg;
    @Bind(R.id.evaluate)
    RadioButton evaluate;
    @Bind(R.id.pullListView)
    PullToRefreshScrollView pullListView;
    @Bind(R.id.my_recycler_view)
    EmptyRecyclerView mRecyclerView;
    @Bind(R.id.bottom_layout)
    RelativeLayout bottom_layout;
    @Bind(R.id.purchase)
    Button purchase;
    @Bind(R.id.listviewEmpty)
    View listViewEmpty;
    MyOrderAdapter adapter;
    private List<MyOrder> mlist = new ArrayList<>();
    private List<Usr> usrList = new ArrayList<>();
    private int pageindext = 1;
    private int totalPage;
    OrderCancelUsrPopupWindow window;
    AlertConfirmation alert;
    AlertConfirmation toasAlert;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        alert = new AlertConfirmation(activity, "删除订单", "确认删除该订单！", "删除", "取消");
        return setView(R.layout.fragment_my_order, "我的订单", true);
    }

    /**
     *
     */
    private void loading() {
        Map<String, Object> map = new HashMap<>();
        map.put("pageIndex", pageindext);
        map.put("state", state);
        ApiClient.send(activity, JConstant.ORDER_ALL_, map, true, MyOrderPage.class, new DataLoader<MyOrderPage>() {

            @Override
            public void task(String data) {
            }

            @Override
            public void succeed(MyOrderPage data) {
                if (pageindext == 1) {
                    mlist.clear();
                }
                if (data != null && data.data.size() > 0) {
                    totalPage = data.totalPage;
                    mlist.addAll(data.data);
                }
                adapter.notifyDataSetChanged();
                if (totalPage == pageindext) {
                    pullListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                } else {
                    pullListView.setMode(PullToRefreshBase.Mode.BOTH);
                }
                RefreshComplete();
                if (pageindext == 1) {
                    mRecyclerView.smoothScrollToPosition(0);
                }
            }

            @Override
            public void error(String message) {
                RefreshComplete();
            }
        }, JConstant.ORDER_ALL_);

    }

    private void RefreshComplete() {
        pullListView.onRefreshComplete();
    }

    private void setPullListView() {
        pullListView.setMode(PullToRefreshBase.Mode.BOTH);
        pullListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase refreshView) {
                if (state != null && state.equals(InterFace.OrderStatus.PAYMENT.ecode)) {
                    bottom_layout.setVisibility(View.GONE);
                }
                pageindext = 1;
                loading();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase refreshView) {
                pageindext++;
                loading();
            }
        });
    }

    @Override
    protected void initView() {
        setPullListView();
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(activity);
        mRecyclerView.setEmptyView(listViewEmpty);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerGridItemDecoration(activity, LinearLayoutManager.HORIZONTAL, 15, getResources().getColor(R.color.light_gray)));
        //设置Item增加、移除动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //创建并设置Adapter
        adapter = new MyOrderAdapter(mlist, activity);
        adapter.setOperateClickInterface(new MyOrderAdapter.OperateClickInterface() {
            @Override
            public void delete(MyOrder order) {
                deleteOrder(order.id);
            }

            @Override
            public void cansel(MyOrder order) {
                oid = order.id;
                showUsr(0);
            }

            @Override
            public void pay(MyOrder order) {//付款
                umix(order.id);
            }

            @Override
            public void contact(MyOrder order) {
                Utils.tellPhone(activity, order.shopphone, "联系卖家");
            }

            @Override
            public void evaluate(MyOrder order) {//晒单评价
                Intent intent = new Intent(activity, CommonActivity.class);
                intent.putExtra("orderid", order.id);
                intent.putExtra(JConstant.EXTRA_INDEX, CommonActivity.GOODS_EVALUATE);
                startActivityForResult(intent, 1);
            }

            @Override
            public void isPay(boolean ispay) {//合并付款
                if (ispay) {
                    bottom_layout.setVisibility(View.VISIBLE);
                } else {
                    bottom_layout.setVisibility(View.GONE);
                }
            }

            @Override
            public void detail(MyOrder order) {//商品详情
                Intent intent = new Intent(activity, CommonActivity.class);
                intent.putExtra("orderid", order.id);
                intent.putExtra(JConstant.EXTRA_INDEX, CommonActivity.ORDER_DETAIL);
                startActivityForResult(intent, 1);
            }

            @Override
            public void receipt(MyOrder order) {//确认收货
                confirm(order.id);
            }

            @Override
            public void refound(MyOrder order) {//退款
                oid = order.id;
                showUsr(1);
            }

            @Override
            public void lookLogistics(MyOrder order) {//查看物流
                //TODO 查看物流
                Intent intent = new Intent(activity, LookLogisticsActivity.class);
                intent.putExtra("expNo", order.expMap.expNo);
                intent.putExtra("expCode", order.expMap.expCode);
                startActivity(intent);
            }

            @Override
            public void denialReason(MyOrder order) {//查看拒绝退款原因
                getDenialReason(order.id);
            }

            @Override
            public void lookEvaluate(MyOrder order) {//查看评价
                Intent intent = new Intent(activity, LookEvaluateActivity.class);
                intent.putExtra("orderid", order.id);
                startActivity(intent);
            }
        });
        mRecyclerView.setAdapter(adapter);
        if (isEvaluate) {
            evaluate.setChecked(true);
            state = InterFace.OrderStatus.EVALUATE.ecode;
        }
        detail_rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.all:
                        isPay(R.id.all);
                        state = null;
                        break;
                    case R.id.payment:
                        isPay(R.id.payment);
                        state = InterFace.OrderStatus.PAYMENT.ecode;
                        break;
                    case R.id.shipments:
                        isPay(R.id.shipments);
                        state = InterFace.OrderStatus.SHIPMENTS.ecode;
                        break;
                    case R.id.receiving:
                        isPay(R.id.receiving);
                        state = 5;
                        break;
                    case R.id.evaluate:
                        isPay(R.id.evaluate);
                        state = InterFace.OrderStatus.EVALUATE.ecode;
                        break;
                }
                update();
            }
        });
        loading();
    }

    private void update() {
        pageindext = 1;
        loading();
    }

    private void isPay(int id) {
        if (id == R.id.payment) {
            adapter.setEdit(true);
            bottom_layout.setVisibility(View.GONE);
        } else {
            adapter.setEdit(false);
            bottom_layout.setVisibility(View.GONE);
        }
    }


    /**
     * 选择取消订单原因
     * fbb1d3d3e17d45f2a537fb582cb46ca9
     */
    private void showUsr(final int i) {//i=0取消订单 i=1退款
        if (window == null) {
            Map<String, Object> map = new HashMap<>();
            map.put("type", "usr");
            ApiClient.send(activity, JConstant.ORDER_USR_, map, true, Usr.getType(), new DataLoader<List<Usr>>() {
                @Override
                public void task(String data) {

                }

                @Override
                public void succeed(List<Usr> data) {
                    switch (i) {
                        case 0:
                            if (data != null && data.size() > 0) {
                                usrList.addAll(data);
                                window = new OrderCancelUsrPopupWindow(activity, usrList);
                                window.setOnCheckChangeListener(new OrderCancelUsrPopupWindow.OnCheckChangeListener() {
                                    @Override
                                    public void onClickListener(Usr data, boolean isChecked) {
                                        cancel(oid, data.name);
                                    }
                                });
                                window.show(activity);
                            }
                            break;
                        case 1:
                            if (data != null && data.size() > 0) {
                                usrList.addAll(data);
                                window = new OrderCancelUsrPopupWindow(activity, usrList);
                                window.setOnCheckChangeListener(new OrderCancelUsrPopupWindow.OnCheckChangeListener() {
                                    @Override
                                    public void onClickListener(Usr data, boolean isChecked) {
                                        refund(oid, data.name);
                                    }
                                });
                                window.show(activity);
                            }
                            break;
                    }

                }

                @Override
                public void error(String message) {

                }
            }, JConstant.ORDER_USR_);
        } else {
            window.show(activity);
        }
    }

    /**
     * 确认收货
     */
    private void confirm(String id) {
        Map<String, Object> map = new HashMap<>();
        map.put("orderid", id);
        ApiClient.send(activity, JConstant.CONFIRMORDER, map, null, new DataLoader<String>() {
            @Override
            public void task(String data) {

            }

            @Override
            public void succeed(String data) {
                toastSuccess("确认收货");
                update();
            }

            @Override
            public void error(String message) {

            }
        }, JConstant.CONFIRMORDER);
    }

    /**
     * 取消订单
     *
     * @param id  订单id
     * @param vid 原因id
     */
    private void cancel(String id, String vid) {
        Map<String, Object> map = new HashMap<>();
        map.put("closereason", vid);
        map.put("id", id);
        ApiClient.send(activity, JConstant.ORDER_CANSEL_, map, true, null, new DataLoader<Object>() {

            @Override
            public void task(String data) {

            }

            @Override
            public void succeed(Object data) {
                toastSuccess("取消成功");
                update();
            }

            @Override
            public void error(String message) {

            }
        }, JConstant.ORDER_CANSEL_);
    }


    /**
     * 退款
     *
     * @param id    订单id
     * @param vName 原因id
     */
    private void refund(String id, String vName) {
        Map<String, Object> map = new HashMap<>();
        map.put("closereason", vName);
        map.put("id", id);
        ApiClient.send(activity, JConstant.APPLYREFUND, map, true, null, new DataLoader<Object>() {

            @Override
            public void task(String data) {

            }

            @Override
            public void succeed(Object data) {
                toastSuccess("申请成功");
                update();
            }

            @Override
            public void error(String message) {

            }
        }, JConstant.APPLYREFUND);
    }


    /**
     * 删除订单
     *
     * @param id
     */
    private void deleteOrder(final String id) {
        alert.show(new AlertConfirmation.OnClickListeners() {
            @Override
            public void ConfirmOnClickListener() {
                alert.hide();
                Map<String, Object> map = new HashMap<>();
                map.put("id", id);
                ApiClient.send(activity, JConstant.ORDER_DELETE_, map, true, null, new DataLoader<Object>() {

                    @Override
                    public void task(String data) {

                    }

                    @Override
                    public void succeed(Object data) {
                        toastSuccess("删除成功");
                        update();
                    }

                    @Override
                    public void error(String message) {

                    }
                }, JConstant.ORDER_DELETE_);
            }

            @Override
            public void CancelOnClickListener() {
                alert.hide();
            }
        });

    }

    @OnClick({R.id.purchase})
    public void OnclickLinner(View view) {
        switch (view.getId()) {
            case R.id.purchase:
                umix(adapter.getOrderId());
                break;
        }

    }

    /**
     * 合并付款
     */
    private void umix(String key) {
        if (StringUtil.hasText(key)) {
            Map<String, Object> map = new HashMap<>();
            map.put("key", key);
            ApiClient.send(activity, JConstant.ORDER_UMIX_, map, true, ConfirmShoppingCartFragment.Order.class, new DataLoader<ConfirmShoppingCartFragment.Order>() {

                @Override
                public void task(String data) {

                }

                @Override
                public void succeed(ConfirmShoppingCartFragment.Order data) {
                    Intent intent = new Intent(activity, CommonActivity.class);
                    intent.putExtra(JConstant.EXTRA_INDEX, CommonActivity.GOODS_PAY);
                    intent.putExtra("orderType", InterFace.OrderType.MALL.ecode);
                    intent.putExtra("orderid", data.flowno);
                    intent.putExtra("money", data.actual);
                    startActivityForResult(intent, 2);
                }

                @Override
                public void error(String message) {

                }
            }, JConstant.ORDER_UMIX_);
        } else {
            toast("没有选中可用订单");
        }
    }

    //获取拒绝退款信息
    private void getDenialReason(String oid) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", oid);
        ApiClient.send(activity, JConstant.DENIALREASON, map, String.class, new DataLoader<String>() {
            @Override
            public void task(String data) {

            }

            @Override
            public void succeed(String data) {
                LogUtil.i(MyOrderFragment.class, "获取拒绝退款信息-----" + data);
                if (toasAlert==null){
                    toasAlert = new AlertConfirmation(activity, "拒绝原因", data);
                }else{
                    toasAlert.setContent(data);
                }

                toasAlert.show(new AlertConfirmation.OnClickListeners() {
                    @Override
                    public void ConfirmOnClickListener() {
                        toasAlert.hide();
                    }

                    @Override
                    public void CancelOnClickListener() {
                        toasAlert.hide();
                    }
                });
            }

            @Override
            public void error(String message) {

            }
        }, JConstant.DENIALREASON);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            update();
        }
    }

    @Override
    public boolean onBackPressed() {
        finishFramager();
        return false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ApiClient.cancelRequest(JConstant.ORDER_ALL_);
        ApiClient.cancelRequest(JConstant.ORDER_USR_);
        ApiClient.cancelRequest(JConstant.CONFIRMORDER);
        ApiClient.cancelRequest(JConstant.ORDER_CANSEL_);
        ApiClient.cancelRequest(JConstant.APPLYREFUND);
        ApiClient.cancelRequest(JConstant.ORDER_DELETE_);
        ApiClient.cancelRequest(JConstant.ORDER_UMIX_);
        ApiClient.cancelRequest(JConstant.DENIALREASON);
    }
}
