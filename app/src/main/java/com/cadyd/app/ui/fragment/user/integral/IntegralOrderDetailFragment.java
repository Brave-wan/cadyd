package com.cadyd.app.ui.fragment.user.integral;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.OnClick;

import com.cadyd.app.JConstant;
import com.cadyd.app.R;
import com.cadyd.app.adapter.MyIntegralGoodsAdapter;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.asynctask.DataLoader;
import com.cadyd.app.comm.Utils;
import com.cadyd.app.handle.InterFace;
import com.cadyd.app.model.IntegralListData;
import com.cadyd.app.model.Usr;
import com.cadyd.app.ui.activity.CommonActivity;
import com.cadyd.app.ui.activity.GoodsDetailActivity;
import com.cadyd.app.ui.activity.IntegralGoodsDetailActivity;
import com.cadyd.app.ui.activity.LookLogisticsActivity;
import com.cadyd.app.ui.base.BaseFragement;
import com.cadyd.app.ui.view.AlertConfirmation;
import com.cadyd.app.ui.view.DividerGridItemDecoration;
import com.cadyd.app.ui.window.OrderCancelUsrPopupWindow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 订单详情
 *
 * @author wcy
 */
public class IntegralOrderDetailFragment extends BaseFragement {
    @Bind(R.id.contact_layout)
    RelativeLayout contact_layout;
    @Bind(R.id.line_view)
    View line;
    @Bind(R.id.Shipment_layout)
    RelativeLayout Shipment_layout;
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
    @Bind(R.id.receipt_order)
    TextView receipt_order;//确认收货
    @Bind(R.id.look_logistics)
    TextView look_logistics;//查看物流
    @Bind(R.id.mailcost)
    public TextView mailcost;//运费
    @Bind(R.id.actual)
    public TextView actual;//实际付款
    @Bind(R.id.created)
    public TextView created;
    @Bind(R.id.order_id)
    public TextView order_id;
    private String oid;
    AlertConfirmation alert;
    OrderCancelUsrPopupWindow window;
    private IntegralListData integralListData;

    boolean isUpdate = false;
    private List<Usr> usrList = new ArrayList<>();

    public static IntegralOrderDetailFragment newInstance(String oid) {
        IntegralOrderDetailFragment newFragment = new IntegralOrderDetailFragment();
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
        Shipment_layout.setVisibility(View.GONE);
        contact_layout.setVisibility(View.GONE);
        line.setVisibility(View.GONE);
        loading();
    }

    @Override
    protected void TitleBarEvent(int btnID) {
        finish();
    }

    private void loading() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", oid);
        ApiClient.send(activity, JConstant.INTEGRALSBYOID, map, true, IntegralListData.class, new DataLoader<IntegralListData>() {

            @Override
            public void task(String data) {

            }

            @Override
            public void succeed(IntegralListData data) {
                if (data != null) {
                    integralListData = data;
                    name.setText("收货人：" + data.consignee);
                    phone.setText(data.telphone);
                    address.setText("收货地址：" + data.accept);
                    shop_name.setVisibility(View.GONE);//积分商城隐藏商家的名字（因为没有）
                    actual.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.penny, 0, 0, 0);
                    actual.setText(integralListData.aggregate + "\t+\t￥" + data.price);
                    mailcost.setText("￥" + "0.00");
                    created.setText(data.created);
                    order_id.setText(data.flowno);
                    status_name.setText(InterFace.OrderStatus.format(Integer.valueOf(data.state)).ename);
                    if (data.state.equals(String.valueOf(InterFace.OrderStatus.SUCCEED.ecode))) {
                        operate_layout.setVisibility(View.VISIBLE);
                        delete_order.setVisibility(View.VISIBLE);
                        evaluate_order.setVisibility(View.VISIBLE);
                    } else if (data.state.equals(String.valueOf(InterFace.OrderStatus.PAYMENT.ecode))) {
                        operate_layout.setVisibility(View.VISIBLE);
                        cancel_order.setVisibility(View.VISIBLE);
                        pay_order.setVisibility(View.VISIBLE);
                    } else if (data.state.equals(String.valueOf(InterFace.OrderStatus.CLOSE.ecode))) {
                        operate_layout.setVisibility(View.VISIBLE);
                        delete_order.setVisibility(View.VISIBLE);
                    } else if (data.state.equals(InterFace.OrderStatus.REFUND.ecode.toString())) {//等收货
                        operate_layout.setVisibility(View.VISIBLE);
                        look_logistics.setVisibility(View.VISIBLE);
                        receipt_order.setVisibility(View.VISIBLE);
                    }
                    goods_list_view.setLayoutManager(new LinearLayoutManager(activity));
                    goods_list_view.addItemDecoration(new DividerGridItemDecoration(activity, LinearLayoutManager.HORIZONTAL, 10, getResources().getColor(R.color.white)));

                    /**
                     * 正在施工
                     * */
                    MyIntegralGoodsAdapter adapter = new MyIntegralGoodsAdapter(data.orderIntegralGoods, Integer.valueOf(data.state));
                    adapter.setOnItemClickListener(new MyIntegralGoodsAdapter.OnItemClickListener() {
                        @Override
                        public void ItemClickListener(IntegralListData.OrderIntegralGoods goods) {
                            //查看商品详情
                            Intent intent = new Intent(activity, IntegralGoodsDetailActivity.class);
                            intent.putExtra("gid", goods.goodsId);
                            activity.startActivity(intent);
                        }
                    });
                    goods_list_view.setAdapter(adapter);
                }
            }

            @Override
            public void error(String message) {

            }
        }, JConstant.INTEGRALSBYOID);
    }

    @OnClick({R.id.delete_order, R.id.cancel_order, R.id.evaluate_order, R.id.contact_shop, R.id.pay_order, R.id.receipt_order, R.id.look_logistics, R.id.call_service})
    public void OnClickLinner(View view) {
        switch (view.getId()) {
            case R.id.delete_order:
                deleteOrder(integralListData.id);
                break;
            case R.id.cancel_order:
                showUsr();
                break;
            case R.id.evaluate_order:
                Intent intent = new Intent(activity, CommonActivity.class);
                intent.putExtra("orderid", oid);
                intent.putExtra("orderType", InterFace.OrderType.MALL.ecode);
                intent.putExtra(JConstant.EXTRA_INDEX, CommonActivity.GOODS_EVALUATE);
                startActivityForResult(intent, 1);
                break;
            case R.id.contact_shop://联系卖家
                Utils.tellPhone(activity, JConstant.customerServicePhone, "联系卖家");
                break;
            case R.id.pay_order:
                umix(integralListData.flowno, integralListData.price);
                break;
            case R.id.receipt_order://确认收货
                confirm(oid);
                break;
            case R.id.look_logistics://查看物流
                intent = new Intent(activity, LookLogisticsActivity.class);
                intent.putExtra("expNo", integralListData.expMap.expNo);
                intent.putExtra("expCode", integralListData.expMap.expCode);
                startActivity(intent);
                break;
            case R.id.call_service://联系客服
                Utils.tellPhone(activity, JConstant.customerServicePhone, "联系客服");
                break;
        }
    }

    /**
     * 选择取消订单原因
     */
    private void showUsr() {
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
                        window.setOnCheckChangeListener(new OrderCancelUsrPopupWindow.OnCheckChangeListener() {
                            @Override
                            public void onClickListener(Usr data, boolean isChecked) {
                                cancel(data.val);
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
     * @param vid 原因id
     */
    private void cancel(String vid) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", oid);
        map.put("state", 1);
        map.put("closeReason", vid);
        map.put("type", "usr");
        ApiClient.send(activity, JConstant.UPDATEORDERINTEGRALSTATE, map, true, null, new DataLoader<Object>() {

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
        }, JConstant.UPDATEORDERINTEGRALSTATE);
    }

    /**
     * 删除订单
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
                        isUpdate = true;
                        toastSuccess("删除成功");
                        finish();
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
     * 合并付款
     */
    private AlertConfirmation alertConfirmation;

    private void umix(final String key, Double money) {
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
            startActivityForResult(intent, 2);
        }
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
                hide();
            }

            @Override
            public void error(String message) {

            }
        }, JConstant.UPDATEORDERINTEGRALSTATE);
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
                loading();
            }

            @Override
            public void error(String message) {

            }
        }, JConstant.PAYINTEGRAL);
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
        pay_order.setVisibility(View.GONE);//
        receipt_order.setVisibility(View.GONE);//确认收货
        look_logistics.setVisibility(View.GONE);//查看物流
        loading();
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
    public void onDestroyView() {
        super.onDestroyView();
        ApiClient.cancelRequest(JConstant.INTEGRALSBYOID);
        ApiClient.cancelRequest(JConstant.ORDER_USR_);
        ApiClient.cancelRequest(JConstant.UPDATEORDERINTEGRALSTATE);
        ApiClient.cancelRequest(JConstant.DELETEINTEGRAL);
        ApiClient.cancelRequest(JConstant.PAYINTEGRAL);
    }
}
