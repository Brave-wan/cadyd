package com.cadyd.app.ui.fragment.user;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.*;

import butterknife.Bind;
import butterknife.OnClick;

import com.cadyd.app.JConstant;
import com.cadyd.app.R;
import com.cadyd.app.adapter.MyGoodsAdapter;
import com.cadyd.app.adapter.MyGoodsPackageAdapter;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.asynctask.DataLoader;
import com.cadyd.app.comm.Utils;
import com.cadyd.app.handle.InterFace;
import com.cadyd.app.model.MyOrder;
import com.cadyd.app.model.Usr;
import com.cadyd.app.ui.activity.CommonActivity;
import com.cadyd.app.ui.activity.GoodsDetailActivity;
import com.cadyd.app.ui.activity.LookLogisticsActivity;
import com.cadyd.app.ui.base.BaseFragement;
import com.cadyd.app.ui.fragment.cart.ConfirmShoppingCartFragment;
import com.cadyd.app.ui.view.AlertConfirmation;
import com.cadyd.app.ui.view.DividerGridItemDecoration;
import com.cadyd.app.ui.view.toast.ToastUtils;
import com.cadyd.app.ui.window.OrderCancelUsrPopupWindow;

import org.wcy.android.utils.LogUtil;
import org.wcy.common.utils.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 订单详情
 *
 * @author wcy
 */
public class OrderDetailFragment extends BaseFragement {
    @Bind(R.id.name)
    public TextView name;
    @Bind(R.id.phone)
    public TextView phone;
    @Bind(R.id.address)
    public TextView address;
    @Bind(R.id.shop_name)
    public TextView shop_name;
    @Bind(R.id.status_name)
    public TextView status_name;
    @Bind(R.id.goods_list_view)
    public RecyclerView goods_list_view;
    @Bind(R.id.bottom_layout)
    public RelativeLayout operate_layout;//操作
    @Bind(R.id.cancel_order)
    public TextView cancel_order;//取消订单
    @Bind(R.id.delete_order)
    public TextView delete_order;//删除订单
    @Bind(R.id.evaluate_order)
    public TextView evaluate_order;//评价晒单
    @Bind(R.id.pay_order)
    public TextView pay_order;//付款
    @Bind(R.id.mailcost)
    public TextView mailcost;//运费
    @Bind(R.id.actual)
    public TextView actual;//实际付款
    @Bind(R.id.created)
    public TextView created;
    @Bind(R.id.order_id)
    public TextView order_id;
    @Bind(R.id.look_logistics)
    TextView look_logistics;//查看物流
    @Bind(R.id.receipt_order)
    TextView receipt_order;//确认收货
    @Bind(R.id.denial_reason)
    public TextView denial_reason;//拒绝额退款原因
    @Bind(R.id.Refund)
    public TextView Refund;//申请退款

    private String oid;
    AlertConfirmation alert;
    OrderCancelUsrPopupWindow window;
    private String shopPhone = "";
    AlertConfirmation toasAlert;

    private MyOrder myOrder;

    boolean isUpdate = false;
    private List<Usr> usrList = new ArrayList<>();

    public static OrderDetailFragment newInstance(String oid) {
        OrderDetailFragment newFragment = new OrderDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString("oid", oid);
        newFragment.setArguments(bundle);
        return newFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            this.oid = args.getString("oid");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        alert = new AlertConfirmation(activity, "删除订单", "确认删除该订单！", "删除", "取消");
        return setView(R.layout.fragment_order_detail, getResources().getString(R.string.order_detail), true);
    }

    @Override
    protected void initView() {
        loading();
    }

    @Override
    protected void TitleBarEvent(int btnID) {
        finish();
    }

    private void loading() {
        Map<String, Object> map = new HashMap<>();
        map.put("oid", oid);
        ApiClient.send(activity, JConstant.ORDER_DETAIL_, map, MyOrder.class, new DataLoader<MyOrder>() {

            @Override
            public void task(String data) {

            }

            @Override
            public void succeed(final MyOrder n) {
                if (n != null) {
                    myOrder = n;
                    shopPhone = n.shopphone;
                    if (n.accept != null) {
                        name.setText("收货人：" + n.accept.consignee);
                        phone.setText(n.accept.telphone);
                        address.setText("收货地址：" + n.accept.address);
                    }
                    shop_name.setText(n.shopname);
                    actual.setText("￥" + n.actual);
                    mailcost.setText("￥" + n.mailcost);
                    created.setText(n.created);
                    order_id.setText(n.flowno);

                    status_name.setText(InterFace.OrderStatus.format(n.state).ename);
                    if (n.state.equals(InterFace.OrderStatus.SUCCEED.ecode)) {
                        operate_layout.setVisibility(View.VISIBLE);
                        delete_order.setVisibility(View.VISIBLE);
                        if (n.commentstate == 0) {
                            evaluate_order.setVisibility(View.VISIBLE);//能否晒单
                        }
                    } else if (n.state.equals(InterFace.OrderStatus.PAYMENT.ecode)) {
                        operate_layout.setVisibility(View.VISIBLE);
                        cancel_order.setVisibility(View.VISIBLE);
                        pay_order.setVisibility(View.VISIBLE);
                    } else if (n.state.equals(InterFace.OrderStatus.CLOSE.ecode)) {
                        operate_layout.setVisibility(View.VISIBLE);
                        delete_order.setVisibility(View.VISIBLE);
                    } else if (n.state.equals((InterFace.OrderStatus.DENIAL.ecode))) {//拒绝退款原因
                        operate_layout.setVisibility(View.VISIBLE);
                        denial_reason.setVisibility(View.VISIBLE);
                    } else if (n.state.equals((InterFace.OrderStatus.REFUND.ecode))) {//呆收货
                        operate_layout.setVisibility(View.VISIBLE);
                        look_logistics.setVisibility(View.VISIBLE);//查看物流
                        receipt_order.setVisibility(View.VISIBLE);//确认收货
                    }
                    /**判断这个订单是否能够退款*/
                    if (n.refundFlag) {
                        operate_layout.setVisibility(View.VISIBLE);
                        Refund.setVisibility(View.VISIBLE);
                    }
                    goods_list_view.setLayoutManager(new LinearLayoutManager(activity));
                    goods_list_view.addItemDecoration(new DividerGridItemDecoration(activity, LinearLayoutManager.HORIZONTAL, 10, getResources().getColor(R.color.white)));

                    /**
                     * 正在施工
                     * */
                    MyGoodsPackageAdapter adapter = new MyGoodsPackageAdapter(activity, n.suitList, n.state, true);
                    // MyGoodsPackageAdapter adapter = new MyGoodsPackageAdapter(activity, n.suitList, n.state);
                    adapter.setOnItemClickListener(new MyGoodsAdapter.OnItemClickListener() {
                        @Override
                        public void ItemClickListener(int index, int position) {
                            if (StringUtil.hasText(n.suitList.get(index).orderDetail.get(position).goodsid)) {
                                Intent intent = new Intent(getActivity(), GoodsDetailActivity.class);
                                intent.putExtra("gid", n.suitList.get(index).orderDetail.get(position).goodsid);
                                getActivity().startActivity(intent);
                            } else {
                                ToastUtils.show(getActivity(), "数据异常", ToastUtils.ERROR_TYPE);
                            }
                        }
                    });
                    goods_list_view.setAdapter(adapter);
                }
            }

            @Override
            public void error(String message) {

            }
        }, JConstant.ORDER_DETAIL_);
    }

    @OnClick({R.id.delete_order, R.id.cancel_order, R.id.evaluate_order, R.id.contact_shop, R.id.pay_order, R.id.call_service, R.id.denial_reason, R.id.look_logistics, R.id.receipt_order})
    public void OnClickLinner(View view) {
        switch (view.getId()) {
            case R.id.delete_order:
                deleteOrder();
                break;
            case R.id.cancel_order:
                showUsr(new OrderCancelUsrPopupWindow.OnCheckChangeListener() {
                    @Override
                    public void onClickListener(Usr data, boolean isChecked) {
                        cancel(data.name);
                    }
                });
                break;
            case R.id.evaluate_order:
                Intent intent = new Intent(activity, CommonActivity.class);
                intent.putExtra("orderid", oid);
                intent.putExtra("orderType", InterFace.OrderType.MALL.ecode);
                intent.putExtra(JConstant.EXTRA_INDEX, CommonActivity.GOODS_EVALUATE);
                startActivityForResult(intent, 1);
                break;
            case R.id.contact_shop://联系卖家
                Utils.tellPhone(activity, shopPhone, "联系卖家");
                break;
            case R.id.pay_order:
                umix();
                break;
            case R.id.call_service://联系客服
                Utils.tellPhone(activity, JConstant.customerServicePhone, "联系客服");
                break;
            case R.id.denial_reason://查看拒绝退款原因
                getDenialReason(oid);
                break;
            case R.id.Refund://申请退款
                showUsr(new OrderCancelUsrPopupWindow.OnCheckChangeListener() {
                    @Override
                    public void onClickListener(Usr data, boolean isChecked) {
                        refund(oid, data.name);
                    }
                });
                break;
            case R.id.look_logistics://查看物流
                //TODO 查看物流
                intent = new Intent(activity, LookLogisticsActivity.class);
                intent.putExtra("expNo", myOrder.expMap.expNo);
                intent.putExtra("expCode", myOrder.expMap.expCode);
                startActivity(intent);
                break;
            case R.id.receipt_order://确认收货
                confirm(oid);
                break;
        }
    }

    /**
     * 选择取消订单原因
     */
    private void showUsr(final OrderCancelUsrPopupWindow.OnCheckChangeListener onCheckChangeListener) {
        if (window == null) {
            Map<String, Object> map = new HashMap<>();
            map.put("type", "usr");
            ApiClient.send(activity, JConstant.ORDER_USR_, map, true, Usr.getType(), new DataLoader<List<Usr>>() {
                @Override
                public void task(String data) {

                }

                @Override
                public void succeed(List<Usr> data) {
                    if (data != null && data.size() > 0) {
                        usrList.addAll(data);
                        window = new OrderCancelUsrPopupWindow(activity, usrList);
                        window.setOnCheckChangeListener(onCheckChangeListener);
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
     * @param vid 原因id
     */
    private void cancel(String vid) {
        Map<String, Object> map = new HashMap<>();
        map.put("closereason", vid);
        map.put("id", oid);
        ApiClient.send(activity, JConstant.ORDER_CANSEL_, map, true, null, new DataLoader<Object>() {

            @Override
            public void task(String data) {

            }

            @Override
            public void succeed(Object data) {
                isUpdate = true;
                toastSuccess("取消成功");
                hide();
            }

            @Override
            public void error(String message) {

            }
        }, JConstant.ORDER_CANSEL_);
    }

    /**
     * 删除订单
     */
    private void deleteOrder() {
        alert.show(new AlertConfirmation.OnClickListeners() {
            @Override
            public void ConfirmOnClickListener() {
                alert.hide();
                Map<String, Object> map = new HashMap<>();
                map.put("id", oid);
                ApiClient.send(activity, JConstant.ORDER_DELETE_, map, true, null, new DataLoader<Object>() {

                    @Override
                    public void task(String data) {

                    }

                    @Override
                    public void succeed(Object data) {
                        isUpdate = true;
                        toastSuccess("删除成功");
                        finish();
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

    /**
     * 合并付款
     */
    private void umix() {
        Map<String, Object> map = new HashMap<>();
        map.put("key", oid);
        ApiClient.send(activity, JConstant.ORDER_UMIX_, map, true, ConfirmShoppingCartFragment.Order.class, new DataLoader<ConfirmShoppingCartFragment.Order>() {

            @Override
            public void task(String data) {

            }

            @Override
            public void succeed(ConfirmShoppingCartFragment.Order data) {
                Intent intent = new Intent(activity, CommonActivity.class);
                intent.putExtra(JConstant.EXTRA_INDEX, CommonActivity.GOODS_PAY);
                intent.putExtra("orderid", data.flowno);
                intent.putExtra("money", data.actual);
                startActivityForResult(intent, 2);
            }

            @Override
            public void error(String message) {

            }
        }, JConstant.ORDER_UMIX_);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            isUpdate = true;
            hide();
        }
    }

    private void hide() {
        operate_layout.setVisibility(View.GONE);
        cancel_order.setVisibility(View.GONE);//取消订单
        delete_order.setVisibility(View.GONE);//删除订单
        evaluate_order.setVisibility(View.GONE);//评价晒单
        pay_order.setVisibility(View.GONE);//付款
        denial_reason.setVisibility(View.GONE);
        Refund.setVisibility(View.GONE);
        look_logistics.setVisibility(View.GONE);
        receipt_order.setVisibility(View.GONE);
        look_logistics.setVisibility(View.GONE);
        receipt_order.setVisibility(View.GONE);
        loading();
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
                loading();
            }

            @Override
            public void error(String message) {

            }
        }, JConstant.APPLYREFUND);
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
                toasAlert = new AlertConfirmation(activity, "拒绝原因", data);
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
                loading();
            }

            @Override
            public void error(String message) {

            }
        }, JConstant.CONFIRMORDER);
    }

    private void finish() {
        if (isUpdate) {
            activity.setResult(Activity.RESULT_OK);
        }
        finishActivity();
    }

    @Override
    public boolean onBackPressed() {
        finish();
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ApiClient.cancelRequest(JConstant.ORDER_DETAIL_);
        ApiClient.cancelRequest(JConstant.ORDER_USR_);
        ApiClient.cancelRequest(JConstant.ORDER_CANSEL_);
        ApiClient.cancelRequest(JConstant.ORDER_DELETE_);
        ApiClient.cancelRequest(JConstant.ORDER_UMIX_);
        ApiClient.cancelRequest(JConstant.APPLYREFUND);
        ApiClient.cancelRequest(JConstant.DENIALREASON);
        ApiClient.cancelRequest(JConstant.CONFIRMORDER);
    }
}
