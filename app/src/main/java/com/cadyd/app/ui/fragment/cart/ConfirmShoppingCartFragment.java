package com.cadyd.app.ui.fragment.cart;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.OnClick;

import com.cadyd.app.JConstant;
import com.cadyd.app.R;
import com.cadyd.app.adapter.confirmCart.ShoppingConfirmAdapter;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.asynctask.DataLoader;
import com.cadyd.app.handle.InterFace;
import com.cadyd.app.interfaces.TowObjectParameterInterface;
import com.cadyd.app.model.*;
import com.cadyd.app.ui.activity.CommonActivity;
import com.cadyd.app.ui.base.BaseFragement;
import com.cadyd.app.ui.fragment.user.UserAddressFragment;
import com.cadyd.app.ui.view.DividerGridItemDecoration;

import org.wcy.android.utils.LogUtil;
import org.wcy.android.widget.EmptyLayout;
import org.wcy.common.utils.NumberUtil;
import org.wcy.common.utils.StringUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 购物车——确认订单
 * Created by wcy on 2016/5/25.
 */
public class ConfirmShoppingCartFragment extends BaseFragement {
    private String gids;
    @Bind(R.id.name)
    TextView name;
    @Bind(R.id.phone)
    TextView phone;
    @Bind(R.id.address)
    TextView address;
    @Bind(R.id.list_view)
    RecyclerView list_view;
    @Bind(R.id.total_money)
    TextView total_money;
    List<CartShop> mlist;
    @Bind(R.id.empty_layout)
    View relativeLayout;
    @Bind(R.id.integral)
    TextView integral;
    @Bind(R.id.integral_checkbox)
    CheckBox integral_checkbox;
    EmptyLayout mEmptyLayout;

    private String aid = "";
    ShoppingConfirmAdapter adapter;

    public static final int add = 1;
    public static final int sub = 2;
    public static final int not = 3;

    public static ConfirmShoppingCartFragment newInstance(String gids) {
        ConfirmShoppingCartFragment newFragment = new ConfirmShoppingCartFragment();
        Bundle bundle = new Bundle();
        bundle.putString("gids", gids);
        newFragment.setArguments(bundle);
        return newFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        mlist = new ArrayList<>();
        if (args != null) {
            this.gids = args.getString("gids");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return setView(R.layout.fragment_confirm_shopping_cart, "确认订单", true);
    }

    @Override
    protected void initView() {
        list_view.setVisibility(View.VISIBLE);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(activity);
        list_view.setLayoutManager(mLayoutManager);
        list_view.addItemDecoration(new DividerGridItemDecoration(activity, LinearLayoutManager.HORIZONTAL, 15, getResources().getColor(R.color.light_gray)));
        adapter = new ShoppingConfirmAdapter(activity, mlist);
        adapter.setOnCheckBoxChangeListener(new TowObjectParameterInterface() {
            @Override
            public void Onchange(int type, int postion, Object object) {
                BigDecimal b;
                if (object instanceof String) {
                    b = new BigDecimal((String) object);
                } else {
                    b = (BigDecimal) object;
                }
                if (type == ShoppingConfirmAdapter.Freight) {
                    carePage.data.get(postion).Freight = b;//為耽擱商品設置用戶選擇的運費
                } else if (type == ShoppingConfirmAdapter.Freight) {
                    carePage.data.get(postion).coupon = b;//為耽擱商品設置用戶選擇的優惠券
                }
                updateMoney();
            }
        });
        list_view.setAdapter(adapter);
        mEmptyLayout = new EmptyLayout(activity, relativeLayout);
        mEmptyLayout.setReloadOnclick(new EmptyLayout.ReloadOnClick() {
            @Override
            public void reload() {
                mEmptyLayout.showLoading();
                loading();
            }
        });
        integral_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Other other = (Other) integral_checkbox.getTag();
                if (other != null) {
                    if (isChecked) {
                        totalMoney = totalMoney.subtract(new BigDecimal(other.integralAmount));
                    } else {
                        totalMoney = totalMoney.add(new BigDecimal(other.integralAmount));
                    }
                }
                updateMoney();
            }
        });
        mEmptyLayout.showLoading();
        addressLoading();
    }

    @OnClick({R.id.top_layout, R.id.purchase})
    public void OnClicklinener(View view) {
        switch (view.getId()) {
            case R.id.top_layout:
                UserAddressFragment userAddressFragment = new UserAddressFragment();
                userAddressFragment.setOnclick(new UserAddressFragment.OnClickAddress() {
                    @Override
                    public void onChange(AddressModel model) {
                        aid = model.id;
                        name.setText("收货人：" + model.consignee);
                        phone.setText(model.telphone);
                        address.setText("收货地址：" + model.provincename + model.cityname + model.countyname + model.address);
                    }
                });
                replaceFragment(R.id.common_frame, userAddressFragment);
                break;
            case R.id.purchase:
                save();
                break;
        }
    }

    /**
     * 提交订单
     */
    private void save() {
        if (!StringUtil.hasText(aid)) {
            toast("请选择送货地址");
            return;
        }
        boolean isSave = true;
        Map<String, Object> map = new HashMap<>();
        map.put("addressid", aid);
        map.put("useIntegral", integral_checkbox.isChecked());
        if (isSave) {
            List<AppShopVo> shopVoList = new ArrayList<>();
            for (int i = 0; i < mlist.size(); i++) {
                //得到要更新的item的view
                View view = list_view.getChildAt(i);
                if (null != list_view.getChildViewHolder(view)) {
                    ShoppingConfirmAdapter.ViewHolder viewHolder = (ShoppingConfirmAdapter.ViewHolder) list_view.getChildViewHolder(view);
                    if (viewHolder.msg().freightid == null) {
                        toastError("请先选择店铺的配送方式");
                        return;
                    }
                    shopVoList.add(viewHolder.msg());
                }
            }
            map.put("shopList", shopVoList);
            ApiClient.send(activity, JConstant.ORDER_SAVE_, map, Order.class, new DataLoader<Order>() {

                @Override
                public void task(String data) {

                }

                @Override
                public void succeed(Order data) {
                    Intent intent = new Intent(activity, CommonActivity.class);
                    intent.putExtra(JConstant.EXTRA_INDEX, CommonActivity.GOODS_PAY);
                    intent.putExtra("orderid", data.flowno);//业务流水号
                    intent.putExtra("money", data.actual);//实际支付金额
                    intent.putExtra("orderType", InterFace.OrderType.MALL.ecode);
                    startActivityForResult(intent, 1);
                }

                @Override
                public void error(String message) {

                }
            }, JConstant.ORDER_SAVE_);
        }
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
                name.setText("收货人：" + data.consignee);
                phone.setText(data.telphone);
                address.setText("收货地址：" + data.getAddress());
                loading();
            }

            @Override
            public void error(String message) {

            }
        }, JConstant.ADDRESS_DEFAULT_);
    }

    int totalNum = 0;
    BigDecimal totalMoney = new BigDecimal(0);

    /**
     * 购物车确认商品信息
     */
    private CarePage carePage = new CarePage();

    private void loading() {
        Map<String, Object> map = new HashMap<>();
        map.put("key", gids);
        ApiClient.send(activity, JConstant.CONFIRM_SHOPPING_CART_, map, CartShop.getType(), new DataLoader<List<CartShop>>() {
            @Override
            public void task(String data) {

            }

            @Override
            public void succeed(List<CartShop> data) {
                if (data != null && data.size() > 0) {
                    carePage.data = data;
                    if (carePage.other != null) {
                        integral_checkbox.setTag(carePage.other);
                        integral.setText(carePage.other.integralCount);
                        if (carePage.other.integral) {
                            integral_checkbox.setClickable(true);
                            integral_checkbox.setFocusable(true);
                        } else {
                            integral_checkbox.setClickable(false);
                            integral_checkbox.setFocusable(false);
                        }
                    } else {
                        integral.setText("  ");
                    }
                    mlist.addAll(carePage.data);
                    for (CartShop cs : carePage.data) {
                        int num = 0;
                        BigDecimal money = new BigDecimal(0);
                        for (Suit cg : cs.suitList) {
                            num += cg.num;
                            money = money.add(new BigDecimal(String.valueOf(cg.price)).multiply(new BigDecimal(cg.num)));
                        }
                        totalNum += num;
                        totalMoney = totalMoney.add(money);
                        adapter.setTotalMoney(totalMoney);
                        mEmptyLayout.showView();
                    }
                    updateMoney();
                } else {
                    mEmptyLayout.showEmpty("购物车空空如也\n快去挑几件好货吧！");
                }
            }

            @Override
            public void error(String message) {
                Log.i("xiongmao" , "--------------message");
                mEmptyLayout.showError(message);
            }
        }, JConstant.CONFIRM_SHOPPING_CART_);
    }

    private void updateMoney() {

        totalMoney = new BigDecimal(0);
        totalNum = 0;

        for (CartShop cs : carePage.data) {
            int num = 0;
            BigDecimal money = new BigDecimal(0);
            for (Suit cg : cs.suitList) {
                Log.i("xiongmao", "单件商品的数量：" + cg.num);
                num += cg.num;
                //商品單價乘以商品數量減去商品的優惠加上商品的運費
                money = money.add(new BigDecimal(String.valueOf(cg.price)).multiply(new BigDecimal(cg.num)));
            }
            totalNum += num;
            totalMoney = totalMoney.add(money).subtract(cs.coupon).add(cs.Freight);
        }

        total_money.setText("共" + totalNum + "件商品，合计：￥" + NumberUtil.getString(totalMoney, 2));
    }


    public class Order {
        public String flowno;////业务流水号
        public String actual;//实际支付金额
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(R.anim.push_left_in, R.anim.push_left_in, R.anim.back_left_in, R.anim.back_right_out);
            ft.replace(R.id.common_frame, new ShoppingCartFragment2());
            ft.commit();
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
        ApiClient.cancelRequest(JConstant.ORDER_SAVE_);
        ApiClient.cancelRequest(JConstant.ADDRESS_DEFAULT_);
        ApiClient.cancelRequest(JConstant.CONFIRM_SHOPPING_CART_);
    }
}
