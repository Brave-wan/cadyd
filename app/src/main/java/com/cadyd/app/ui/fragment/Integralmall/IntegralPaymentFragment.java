package com.cadyd.app.ui.fragment.Integralmall;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import butterknife.Bind;
import butterknife.OnClick;

import com.cadyd.app.JConstant;
import com.cadyd.app.R;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.asynctask.DataLoader;
import com.cadyd.app.comm.MyChange;
import com.cadyd.app.handle.InterFace;
import com.cadyd.app.model.AddressModel;
import com.cadyd.app.model.CartGoods;
import com.cadyd.app.ui.activity.CommonActivity;
import com.cadyd.app.ui.base.BaseFragement;
import com.cadyd.app.ui.view.AlertConfirmation;

import org.wcy.android.utils.adapter.CommonAdapter;
import org.wcy.android.utils.adapter.ViewHolder;
import org.wcy.android.widget.MyListView;
import org.wcy.common.utils.StringUtil;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 积分商城的支付界面
 */
public class IntegralPaymentFragment extends BaseFragement {

    @Bind(R.id.name)
    TextView name;
    @Bind(R.id.phone)
    TextView phone;
    @Bind(R.id.address)
    TextView address;
    @Bind(R.id.list_view)
    MyListView listView;

    @Bind(R.id.total_number)
    TextView totalNumber;//商品总量
    @Bind(R.id.total_money)
    TextView totalMoney;//价格合计
    @Bind(R.id.total_integral)
    TextView totalIntegral;//积分合计
    private String aid = "";
    private Order order;

    private CommonAdapter<CartGoods> adapter;
    private List<CartGoods> cartGoods;

    private AlertConfirmation confirmation;

    private boolean type;

    public static IntegralPaymentFragment newInstens(List<CartGoods> cartGoods, boolean type) {
        IntegralPaymentFragment newFragment = new IntegralPaymentFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("GoodsList", (Serializable) cartGoods);
        bundle.putBoolean("type", type);
        newFragment.setArguments(bundle);
        return newFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        cartGoods = (List<CartGoods>) args.getSerializable("GoodsList");
        type = args.getBoolean("type");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return setView(R.layout.fragment_integral_payment, "订单详情", true);
    }

    @Override
    protected void initView() {
        confirmation = new AlertConfirmation(activity, "支付", "本订单只需积分即可完成支付");
        getUserIntegral();
        addressLoading();
        setGoods();
    }

    /**
     * 默认收货地址
     */
    private void addressLoading() {
        ApiClient.send(activity, JConstant.ADDRESS_DEFAULT_, null, AddressModel.class, new DataLoader<AddressModel>() {

            @Override
            public void task(String data) {

            }

            @Override
            public void succeed(AddressModel data) {
                aid = data.id;
                name.setText("收货人：" + data.consignee != null ? data.consignee : "");
                phone.setText(data.telphone != null ? data.telphone : "");
                address.setText("收货地址：" + data.getAddress() != null ? data.getAddress() : "");
            }

            @Override
            public void error(String message) {

            }
        }, JConstant.ADDRESS_DEFAULT_);
    }

    @OnClick(R.id.top_layout)
    public void onTop(View view) {
        Intent intent = new Intent(activity, CommonActivity.class);
        intent.putExtra(JConstant.EXTRA_INDEX, CommonActivity.ADDRESSFRAGMENT);
        startActivityForResult(intent, 1);
    }

    /**
     * 获取我的积分
     */
    private int integral;

    private void getUserIntegral() {
        ApiClient.send(activity, JConstant.FINDUSERINGRAL, null, String.class, new DataLoader<String>() {

            @Override
            public void task(String data) {

            }

            @Override
            public void succeed(String data) {
                if (data != null) {
                    integral = Integer.valueOf(data);
                }
            }

            @Override
            public void error(String message) {

            }
        }, JConstant.FINDUSERINGRAL);
    }

    /**
     * 商品设置
     **/
    private void setGoods() {
        calculation();
        adapter = new CommonAdapter<CartGoods>(activity, cartGoods, R.layout.confirm_shopping_cart_list_iteam_list) {
            @Override
            public void convert(final ViewHolder helper, CartGoods item) {
                helper.setText(R.id.good_name, item.title);
                TextView salemix = helper.getView(R.id.salemix);//设置规格
                salemix.setVisibility(View.GONE);
                TextView price = helper.getView(R.id.price);
                price.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.penny, 0, 0, 0);//在左边设置小图标
                int start;
                int end;
                StringBuffer buffer = new StringBuffer();
                buffer.append(item.integral);
                if (!StringUtil.hasText(item.price) || "0".equals(item.price)) {
                    price.setText(buffer.toString());
                } else {
                    buffer.append("\t+\t");
                    start = buffer.length();
                    buffer.append("￥");
                    end = buffer.length();
                    buffer.append(item.price);
                    price.setText(MyChange.ChangeTextColor(buffer.toString(), start, end, getResources().getColor(R.color.text_primary_gray)));
                }
                helper.setText(R.id.number, "x" + item.number);
                ImageView goods_image = helper.getView(R.id.goods_image);
                ApiClient.ImageLoadersRounde(item.thumb, goods_image);
            }
        };
        listView.setAdapter(adapter);
    }

    //计算方法
    private void calculation() {
        if (adapter != null) {
            Double aDouble = new Double(0);
            int integral = 0;
            int number = 0;
            for (int i = 0; i < cartGoods.size(); i++) {
                aDouble += Double.valueOf(String.valueOf(cartGoods.get(i).price)) * Double.valueOf(cartGoods.get(i).number);
                integral += (cartGoods.get(i).integral * Integer.valueOf(cartGoods.get(i).number));
                number += Integer.valueOf(cartGoods.get(i).number);
            }
            totalNumber.setText("共" + number + "件商品，合计：");
            totalIntegral.setText(String.valueOf(integral));
            totalMoney.setText(String.valueOf(aDouble));
            adapter.notifyDataSetChanged();
        } else {
            BigDecimal priceDecimal = new BigDecimal(0);
            int integral = 0;
            int number = 0;
            for (int i = 0; i < cartGoods.size(); i++) {
                BigDecimal bigDecimal = new BigDecimal(cartGoods.get(i).price);
                bigDecimal = bigDecimal.multiply(new BigDecimal(cartGoods.get(i).number));
                priceDecimal = priceDecimal.add(bigDecimal);
                integral += (cartGoods.get(i).integral * Integer.valueOf(cartGoods.get(i).number));
                number += Integer.valueOf(cartGoods.get(i).number);
            }
            totalNumber.setText("共" + number + "件商品，合计：");
            totalIntegral.setText(String.valueOf(integral));
            totalMoney.setText(priceDecimal.toString());
        }
    }

    /*************************************************/
    public class Order {
        private String flowno;//业务流水好
        public String price;//价格
        public String integral;//商品积分
        public String integration;//用户积分
        public String serialnum;//订单编号
    }

    private class goodsItem {
        public String integralid;
        public int integral;
        public Double price;
        public int number;
    }

    /**
     * 下单
     **/
    private void setOder() {
        if (!StringUtil.hasText(aid)) {
            toast("请选择默认地址");
            return;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("addressId", aid);
        //状态0代表直接下单，1代表从购物车下单,
        if (type) {
            map.put("type", 1);
        } else {
            map.put("type", 0);
        }
        List<goodsItem> goodsItems = new ArrayList<>();
        for (int i = 0; i < cartGoods.size(); i++) {
            goodsItem goodsitem = new goodsItem();
            goodsitem.integralid = cartGoods.get(i).goodsId;//商品id
            goodsitem.integral = cartGoods.get(i).integral;//积分单价
            goodsitem.number = Integer.parseInt(cartGoods.get(i).number);//商品数量
            goodsItems.add(goodsitem);
        }
        map.put("list", goodsItems);
        ApiClient.send(activity, JConstant.SAVEINTEGBATCHORDER, map, true, Order.class, new DataLoader<Order>() {
            @Override
            public void task(String data) {

            }

            @Override
            public void succeed(Order data) {
                order = data;
                if (Double.valueOf(order.price) <= 0) {
                    confirmation.show(new AlertConfirmation.OnClickListeners() {
                        @Override
                        public void ConfirmOnClickListener() {
                            confirmation.hide();
                            //  frozenIntegral(order.flowno);
                            toastSuccess("支付成功");
                            finishFramager();
                        }

                        @Override
                        public void CancelOnClickListener() {
                            confirmation.hide();
                        }
                    });
                } else {
                    //跳转界面到支付界面
                    Intent intent = new Intent(activity, CommonActivity.class);
                    intent.putExtra(JConstant.EXTRA_INDEX, CommonActivity.GOODS_PAY);
                    intent.putExtra("orderid", order.flowno);
                    intent.putExtra("money", order.price);
                    intent.putExtra("integral", order.integral);
                    intent.putExtra("orderType", InterFace.OrderType.MALL.ecode);
                    startActivityForResult(intent, 1);
                }
            }

            @Override
            public void error(String message) {

            }
        }, JConstant.SAVEINTEGBATCHORDER);
    }

    //结算
    @OnClick(R.id.purchase)
    public void onPurchase(View view) {
        if (Integer.valueOf(totalIntegral.getText().toString()) > integral) {
            toast("积分不足");
        } else {
            setOder();
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
                finishFramager();
            }

            @Override
            public void error(String message) {
                toastError("支付失败 ：" + message);
            }
        }, JConstant.PAYINTEGRAL);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        addressLoading();
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ApiClient.cancelRequest(JConstant.FINDUSERINGRAL);
        ApiClient.cancelRequest(JConstant.ADDRESS_DEFAULT_);
        ApiClient.cancelRequest(JConstant.SAVEINTEGBATCHORDER);
        ApiClient.cancelRequest(JConstant.PAYINTEGRAL);
    }
}
