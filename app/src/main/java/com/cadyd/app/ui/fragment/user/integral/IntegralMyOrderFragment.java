package com.cadyd.app.ui.fragment.user.integral;


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
import android.widget.*;

import butterknife.Bind;

import com.cadyd.app.JConstant;
import com.cadyd.app.R;
import com.cadyd.app.adapter.IntegralMyOrderAdapter;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.asynctask.DataLoader;
import com.cadyd.app.handle.InterFace;
import com.cadyd.app.model.IntegralListData;
import com.cadyd.app.model.IntegralListModle;
import com.cadyd.app.model.Usr;
import com.cadyd.app.ui.activity.CommonActivity;
import com.cadyd.app.ui.activity.LookLogisticsActivity;
import com.cadyd.app.ui.base.BaseFragement;
import com.cadyd.app.ui.view.AlertConfirmation;
import com.cadyd.app.ui.view.DividerGridItemDecoration;
import com.cadyd.app.ui.window.OrderCancelUsrPopupWindow;
import com.cadyd.app.utils.EmptyRecyclerView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 积分商城-我的订单
 *
 * @author wcy
 */
public class IntegralMyOrderFragment extends BaseFragement {
    boolean isEvaluate = false;
    private Integer state;

    public static IntegralMyOrderFragment newInstance(boolean isEvaluate) {
        IntegralMyOrderFragment newFragment = new IntegralMyOrderFragment();
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
    IntegralMyOrderAdapter adapter;
    private List<IntegralListData> mlist = new ArrayList<>();
    private List<Usr> usrList = new ArrayList<>();
    private int pageindext = 1;
    private int totalPage;
    OrderCancelUsrPopupWindow window;
    AlertConfirmation alert;

    @Bind(R.id.listviewEmpty)
    View emptlyView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        alert = new AlertConfirmation(activity, "删除订单", "确认删除该订单！", "删除", "取消");
        return setView(R.layout.fragment_my_order, "我的订单", true);
    }

    private void RefreshComplete() {
        pullListView.onRefreshComplete();
    }

    private void setPullListView() {
        mRecyclerView.setEmptyView(emptlyView);
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
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerGridItemDecoration(activity, LinearLayoutManager.HORIZONTAL, 15, getResources().getColor(R.color.light_gray)));
        //设置Item增加、移除动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //创建并设置Adapter
        adapter = new IntegralMyOrderAdapter(mlist, activity);
        adapter.setOperateClickInterface(new IntegralMyOrderAdapter.OperateClickInterface() {
            @Override
            public void delete(IntegralListData order) {//删除订单
                deleteOrder(order.id);
            }

            @Override
            public void cansel(IntegralListData order) {//取消订单
                showUsr(order.id);
            }

            @Override
            public void pay(IntegralListData order) {//支付订单
                umix(order.flowno, order.price, order.aggregate);
            }

            @Override
            public void contact(IntegralListData order) {//联系卖家
                toast("联系");
            }

            @Override
            public void evaluate(IntegralListData order) {//晒单评价
                Intent intent = new Intent(activity, CommonActivity.class);
                intent.putExtra("orderid", order.id);
                intent.putExtra("type", "integral");
                intent.putExtra(JConstant.EXTRA_INDEX, CommonActivity.GOODS_EVALUATE);
                startActivityForResult(intent, 1);
            }

            @Override
            public void isPay(boolean ispay) {
                if (ispay) {
                    bottom_layout.setVisibility(View.VISIBLE);
                } else {
                    bottom_layout.setVisibility(View.GONE);
                }
            }

            @Override
            public void detail(IntegralListData order) {//订单详情
                Intent intent = new Intent(activity, CommonActivity.class);
                intent.putExtra("orderid", order.id);
                intent.putExtra(JConstant.EXTRA_INDEX, CommonActivity.INTEGRALORDERDETAIL);
                startActivityForResult(intent, 1);
            }

            @Override
            public void lookLogistics(IntegralListData order) {
                //TODO 查看物流
                Intent intent = new Intent(activity, LookLogisticsActivity.class);
                intent.putExtra("expNo", order.expMap.expNo);
                intent.putExtra("expCode", order.expMap.expCode);
                startActivity(intent);
            }

            @Override
            public void confirmationReceipt(IntegralListData order) {
                //TODO 确认收货
                confirm(order.id);
            }
        });
        mRecyclerView.setAdapter(adapter);
        if (isEvaluate) {
            evaluate.setChecked(true);
            state = InterFace.OrderStatus.EVALUATE.ecode;
        }

        detail_rg.setWeightSum(4);
        evaluate.setVisibility(View.GONE);
        //0：交易成功(成功的订单)，1：交易关闭(关闭的订单)，2：等待买家付款，3：等待发货，4：部分发货 5：已发货
        detail_rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.all:
                        isPay(R.id.all);
                        state = null;
                        break;
                    case R.id.payment://待付款
                        isPay(R.id.payment);
                        state = InterFace.OrderStatus.PAYMENT.ecode;
                        break;
                    case R.id.shipments://待发货
                        isPay(R.id.shipments);
                        state = InterFace.OrderStatus.SHIPMENTS.ecode;
                        break;
                    case R.id.receiving://待收货（已发货）
                        isPay(R.id.receiving);
                        state = 5;
                        break;
                    case R.id.evaluate://交易成功  待评价
                        isPay(R.id.evaluate);
                        state = InterFace.OrderStatus.SUCCEED.ecode;
                        break;
                }
                update();
            }
        });
        loading();
    }

    /**
     *
     */
    private void loading() {
        Map<String, Object> map = new HashMap<>();
        map.put("pageIndex", pageindext);
        map.put("state", state);
        ApiClient.send(activity, JConstant.INTEGRALSBYUID, map, true, IntegralListModle.class, new DataLoader<IntegralListModle>() {

            @Override
            public void task(String data) {
            }

            @Override
            public void succeed(IntegralListModle data) {
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
        }, JConstant.INTEGRALSBYUID);
    }

    /**
     * 确认收货
     */
    private void confirm(String id) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("state", 0);
        ApiClient.send(activity, JConstant.UPDATEORDERINTEGRALSTATE, map, null, new DataLoader<String>() {
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
        }, JConstant.UPDATEORDERINTEGRALSTATE);
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
     *
     * @param id
     */
    private void showUsr(final String id) {
        if (window == null) {
            Map<String, Object> map = new HashMap<>();
            map.put("type", "usr");//usr用户mer店铺
            ApiClient.send(activity, JConstant.ORDER_USR_, map, true, Usr.getType(), new DataLoader<List<Usr>>() {
                @Override
                public void task(String data) {

                }

                @Override
                public void succeed(List<Usr> data) {
                    if (data != null && data.size() > 0) {
                        usrList.addAll(data);
                        window = new OrderCancelUsrPopupWindow(activity, usrList);
                        window.setOnCheckChangeListener(new OrderCancelUsrPopupWindow.OnCheckChangeListener() {
                            @Override
                            public void onClickListener(Usr data, boolean isChecked) {
                                cancel(id, data.val);
                            }
                        });
                        window.show(activity);
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
     * 取消订单
     *
     * @param id  订单id
     * @param vid 原因id
     */
    private void cancel(String id, String vid) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("state", 1);
        map.put("closeReason", vid);
        map.put("type", "usr");
        ApiClient.send(activity, JConstant.UPDATEORDERINTEGRALSTATE, map, true, null, new DataLoader<Object>() {

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
        }, JConstant.UPDATEORDERINTEGRALSTATE);
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
                ApiClient.send(activity, JConstant.DELETEINTEGRAL, map, true, null, new DataLoader<Object>() {

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
                }, JConstant.DELETEINTEGRAL);
            }

            @Override
            public void CancelOnClickListener() {
                alert.hide();
            }
        });

    }

    /**
     * 付款
     */
    private AlertConfirmation alertConfirmation;

    private void umix(final String key, Double money, int integral) {
        if (money <= 0) {
            if (alertConfirmation == null) {
                alertConfirmation = new AlertConfirmation(activity, "确定支付", "你确定100%积分支付？");
                alertConfirmation.show(new AlertConfirmation.OnClickListeners() {
                    @Override
                    public void ConfirmOnClickListener() {
                        frozenIntegral(key);
                        alertConfirmation.hide();
                    }

                    @Override
                    public void CancelOnClickListener() {
                        alertConfirmation.hide();
                    }
                });
            } else {
                alertConfirmation.show(new AlertConfirmation.OnClickListeners() {
                    @Override
                    public void ConfirmOnClickListener() {
                        frozenIntegral(key);
                        alertConfirmation.hide();
                    }

                    @Override
                    public void CancelOnClickListener() {
                        alertConfirmation.hide();
                    }
                });
            }

        } else {
            Intent intent = new Intent(activity, CommonActivity.class);
            intent.putExtra(JConstant.EXTRA_INDEX, CommonActivity.GOODS_PAY);
            intent.putExtra("orderType", InterFace.OrderType.MALL.ecode);
            intent.putExtra("orderid", key);
            intent.putExtra("money", String.valueOf(money));
            intent.putExtra("integral", String.valueOf(integral));
            startActivityForResult(intent, 2);
        }
    }

    //100%积分的商品只需要扣除积分即可
    private void frozenIntegral(String oderId) {
        Map<String, Object> map = new HashMap<>();
        map.put("flowno", oderId);
        ApiClient.send(activity, JConstant.PAYINTEGRAL, map, null, new DataLoader<String>() {
            @Override
            public void task(String data) {

            }

            @Override
            public void succeed(String data) {
                toastSuccess("支付成功");
                update();
            }

            @Override
            public void error(String message) {

            }
        }, JConstant.PAYINTEGRAL);
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
        ApiClient.cancelRequest(JConstant.INTEGRALSBYUID);
        ApiClient.cancelRequest(JConstant.UPDATEORDERINTEGRALSTATE);
        ApiClient.cancelRequest(JConstant.ORDER_USR_);
        ApiClient.cancelRequest(JConstant.DELETEINTEGRAL);
        ApiClient.cancelRequest(JConstant.PAYINTEGRAL);
    }
}
