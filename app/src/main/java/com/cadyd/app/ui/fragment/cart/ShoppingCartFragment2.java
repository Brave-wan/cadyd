package com.cadyd.app.ui.fragment.cart;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import butterknife.Bind;
import butterknife.OnClick;
import com.cadyd.app.JConstant;
import com.cadyd.app.R;
import com.cadyd.app.adapter.ShoppingCartRecyclerAdapter;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.asynctask.DataLoader;
import com.cadyd.app.interfaces.CartCheckBoxChangeListener;
import com.cadyd.app.model.*;
import com.cadyd.app.ui.base.BaseFragement;
import com.cadyd.app.ui.view.AlertConfirmation;
import com.cadyd.app.ui.view.DividerGridItemDecoration;
import com.cadyd.app.ui.window.PreferPopupWindow;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import org.wcy.android.widget.EmptyLayout;
import org.wcy.common.utils.NumberUtil;
import org.wcy.common.utils.StringUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 购物车
 *
 * @author wcy
 */
public class ShoppingCartFragment2 extends BaseFragement {
    @Bind(R.id.pullListView)
    PullToRefreshScrollView pullListView;
    @Bind(R.id.my_recycler_view)
    RecyclerView list_view;
    @Bind(R.id.all_checkbox)
    CheckBox all_checkbox;
    @Bind(R.id.money_total_layout)
    LinearLayout money_total_layout;
    @Bind(R.id.purchase)
    Button purchase;
    @Bind(R.id.delete_goods)
    Button delete_goods;
    @Bind(R.id.total_money)
    TextView total_money;
    List<CartShop> listShops = new ArrayList<>();
    @Bind(R.id.bottom_layout)
    RelativeLayout bottom_layout;
    @Bind(R.id.empty_layout)
    View relativeLayout;
    ShoppingCartRecyclerAdapter adapter = null;
    AlertConfirmation alert;
    EmptyLayout mEmptyLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return setView(R.layout.fragment_shopping_cart_recycler, "购物车", true);
    }

    @Override
    protected void TitleBarEvent(int btnID) {
        if (btnID == layout.leftButtonId) {
            finishActivity();
        } else {
            if (layout.rightButton.getText().equals("编辑")) {
                layout.rightButton.setText("完成");
                money_total_layout.setVisibility(View.INVISIBLE);
                purchase.setVisibility(View.INVISIBLE);
                delete_goods.setVisibility(View.VISIBLE);
            } else {
                money_total_layout.setVisibility(View.VISIBLE);
                purchase.setVisibility(View.VISIBLE);
                delete_goods.setVisibility(View.GONE);
                layout.rightButton.setText("编辑");
            }
            if (totalNumber > 0) {
                all_checkbox.setChecked(false);
                if (adapter != null) {
                    adapter.isAllCheck(false);
                }
                updateMoney(null, null, null, false);
                updateNumber(null, 0, 0, false);
            }
        }
    }

    BigDecimal totalMoney = new BigDecimal(0);
    int totalNumber = 0;

    @Override
    protected void initView() {
        mEmptyLayout = new EmptyLayout(activity, relativeLayout);
        mEmptyLayout.setReloadOnclick(new EmptyLayout.ReloadOnClick() {
            @Override
            public void reload() {
                loading();
            }
        });
        mEmptyLayout.showLoading();
        setPullListView();
        alert = new AlertConfirmation(activity, "是否删除", "您确定要删除选中的商品吗？");
        all_checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adapter != null) {
                    adapter.isAllCheck(all_checkbox.isChecked());
                }
                updateMoney(null, null, null, all_checkbox.isChecked());
                updateNumber(null, 0, 0, all_checkbox.isChecked());
            }
        });
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(activity);
        list_view.setLayoutManager(mLayoutManager);
        list_view.addItemDecoration(new DividerGridItemDecoration(activity, LinearLayoutManager.HORIZONTAL, 20, getResources().getColor(R.color.line_bg)));
        loading();
    }

    private void updateNumber(Boolean isCheck, int buum, int number, Boolean isAllCheck) {
        if (isCheck != null) {
            if (isCheck) {
                totalNumber += number;
                if (buum > 0) {
                    totalNumber -= buum;
                }
            } else {
                totalNumber -= number;
            }
        } else {
            totalNumber = 0;
            if (isAllCheck != null && isAllCheck) {
                for (CartShop cs : listShops) {
                    for (Suit suit : cs.suitList) {
                        if (suit.suit) {
                            totalNumber = totalNumber + suit.num;
                        } else {
                            for (CartGoods cg : suit.goodsList) {
                                totalNumber = totalNumber + Integer.parseInt(cg.num);
                            }
                        }
                    }
                }
            }
        }
        purchase.setText("计算：（" + totalNumber + "）");
    }

    private void updateMoney(Boolean isCheck, BigDecimal bprice, BigDecimal money, Boolean isAllCheck) {
        if (isCheck != null) {
            if (isCheck) {
                totalMoney = totalMoney.add(money);
                if (bprice != null) {
                    totalMoney = totalMoney.subtract(bprice);
                }
            } else {
                totalMoney = totalMoney.subtract(money);
            }
        } else {
            totalMoney = new BigDecimal(0);
            if (isAllCheck != null && isAllCheck) {
                for (CartShop cs : listShops) {
                    for (Suit suit : cs.suitList) {
                        if (suit.suit) {
                            totalMoney = totalMoney.add(new BigDecimal(suit.price).multiply(new BigDecimal(suit.num)));
                        } else {
                            for (CartGoods cg : suit.goodsList) {
                                totalMoney = totalMoney.add(new BigDecimal(cg.price).multiply(new BigDecimal(cg.num)));
                            }
                        }
                    }
                }
            }
        }
        total_money.setText("合计：￥" + NumberUtil.getString(totalMoney, 2));
    }



    @OnClick({R.id.delete_goods, R.id.purchase})
    public void OnClickBtn(View view) {
        switch (view.getId()) {
            case R.id.delete_goods:
                deleteCart();
                break;
            case R.id.purchase:
                Confirm();
                break;
        }
    }

    /**
     * 结算购物车商品
     */
    private void Confirm() {
        String gids = adapter.getCheckId();
        if (StringUtil.hasText(gids)) {
            replaceFragment(R.id.common_frame, ConfirmShoppingCartFragment.newInstance(gids));
        } else {
            toastError("请选择需要结算的商品");
        }
    }

    /**
     * 删除购物车商品
     */
    private void deleteCart() {
        alert.show(new AlertConfirmation.OnClickListeners() {
            @Override
            public void ConfirmOnClickListener() {
                String gids = adapter.getCheckId();
                if (StringUtil.hasText(gids)) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("key", gids);
                    ApiClient.send(activity, JConstant.DELETE_CART_, map, true, null, new DataLoader<Object>() {

                        @Override
                        public void task(String data) {

                        }

                        @Override
                        public void succeed(Object data) {
                            alert.hide();
                            updateMoney(null, null, null, all_checkbox.isChecked());
                            updateNumber(null, 0, 0, all_checkbox.isChecked());
                            loading();
                        }

                        @Override
                        public void error(String message) {

                        }
                    }, JConstant.DELETE_CART_);
                } else {
                    toastError("请选择要删除的商品");
                }
            }

            @Override
            public void CancelOnClickListener() {
                alert.hide();
            }
        });

    }

    private void setPullListView() {
        pullListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        pullListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase refreshView) {
                Log.i("PullToRefreshBase", "onPullDownToRefresh");
                loading();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase refreshView) {
                Log.i("PullToRefreshBase", "onPullUpToRefresh");
                page++;
                loading();
            }
        });
    }

    int page = 1;
    int totalPage = 0;

    /**
     * 数据加载
     */
    public void loading() {
        Map<String, Object> map = new HashMap<>();
        map.put("pageIndex", page);
        ApiClient.send(activity, JConstant.CART_QUERY_, map, CarePage.class, new DataLoader<CarePage>() {
            @Override
            public void task(String data) {
            }

            @Override
            public void succeed(CarePage data) {
                pullListView.onRefreshComplete();
                if (page == 1) {
                    listShops.clear();
                    if (layout.rightButton.getVisibility() == View.GONE) {
                        layout.rightButton.setText("编辑");
                        layout.rightButton.setVisibility(View.VISIBLE);
                    }
                    totalPage = data.totalPage;
                }
                if (totalPage == page) {
                    pullListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                } else {
                    pullListView.setMode(PullToRefreshBase.Mode.BOTH);
                }
                if (data != null && data.data.size() > 0) {
                    mEmptyLayout.showView();
                    totalMoney = new BigDecimal(0);
                    totalNumber = 0;
                    listShops.addAll(data.data);
                    if (adapter == null) {
                        adapter = new ShoppingCartRecyclerAdapter(ShoppingCartFragment2.this, listShops);
                        adapter.setOnCheckBoxChangeListener(new CartCheckBoxChangeListener() {
                            @Override
                            public void onCheckChange(boolean isChecked) {
                                all_checkbox.setChecked(isChecked);
                            }

                            @Override
                            public void onCheckChileChange(boolean isChecked, BigDecimal bprice, BigDecimal price, String id) {
                                updateMoney(isChecked, bprice, price, null);
                            }

                            @Override
                            public void onCheckNumber(boolean isChecked, int bnum, int number) {
                                updateNumber(isChecked, bnum, number, null);
                            }

                            @Override
                            public void onCoupons(String merchantId) {
                                PreferLoading(merchantId);
                            }
                        });
                        list_view.setAdapter(adapter);
                    } else {
                        adapter.initDate(false);
                    }
                    bottom_layout.setVisibility(View.VISIBLE);
                } else {
                    pullListView.onRefreshComplete();
                    bottom_layout.setVisibility(View.GONE);
                    layout.rightButton.setVisibility(View.GONE);
                    mEmptyLayout.showEmpty("购物车空空如也\n快去挑几件好货吧！");
                }
            }

            @Override
            public void error(String message) {
                mEmptyLayout.showError(message);
                pullListView.onRefreshComplete();
            }
        }, JConstant.CART_QUERY_);
    }


    /**
     * 优惠卷选择
     *
     * @param merchantId
     */
    private void PreferLoading(String merchantId) {
        Map<String, Object> map = new HashMap<>();
        map.put("merchantId", merchantId);
        ApiClient.send(activity, JConstant.PREFER_FIND_, map, true, Prefer.getType(), new DataLoader<List<Prefer>>() {
            @Override
            public void task(String data) {

            }

            @Override
            public void succeed(List<Prefer> data) {
                if (data != null) {
                    PreferPopupWindow window = new PreferPopupWindow(activity, data);
                    window.show(activity);
                }
            }

            @Override
            public void error(String message) {

            }
        }, JConstant.PREFER_FIND_);
    }

    @Override
    public boolean onBackPressed() {
        finishActivity();
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ApiClient.cancelRequest(JConstant.DELETE_CART_);
        ApiClient.cancelRequest(JConstant.CART_QUERY_);
        ApiClient.cancelRequest(JConstant.PREFER_FIND_);
        ApiClient.cancelRequest(JConstant.PREFER_RECEIVE_);
    }
}
