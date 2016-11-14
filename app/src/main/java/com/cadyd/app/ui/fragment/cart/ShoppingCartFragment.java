package com.cadyd.app.ui.fragment.cart;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cadyd.app.JConstant;
import com.cadyd.app.R;
import com.cadyd.app.adapter.cart.ShoppingCartAdapter;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.asynctask.DataLoader;
import com.cadyd.app.interfaces.CartCheckBoxChangeListener;
import com.cadyd.app.interfaces.TowObjectParameterInterface;
import com.cadyd.app.model.CartShop;
import com.cadyd.app.model.Prefer;
import com.cadyd.app.model.Suit;
import com.cadyd.app.ui.activity.CommonActivity;
import com.cadyd.app.ui.base.BaseFragement;
import com.cadyd.app.ui.view.AlertConfirmation;
import com.cadyd.app.ui.view.DividerGridItemDecoration;
import com.cadyd.app.ui.window.PreferPopupWindow;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;

import org.wcy.android.utils.LogUtil;
import org.wcy.android.widget.EmptyLayout;
import org.wcy.android.widget.OnEmptyFoundClick;
import org.wcy.common.utils.NumberUtil;
import org.wcy.common.utils.StringUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 购物车
 *
 * @author wcy
 */
public class ShoppingCartFragment extends BaseFragement {

    private OnEmptyFoundClick foundClick;

    public void setClick(OnEmptyFoundClick foundClick) {
        this.foundClick = foundClick;
    }

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
    ShoppingCartAdapter adapter = null;
    AlertConfirmation alert;
    EmptyLayout mEmptyLayout;

    boolean isBack = false;

    public static ShoppingCartFragment newInstance(boolean type) {
        ShoppingCartFragment newFragment = new ShoppingCartFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("type", type);
        newFragment.setArguments(bundle);
        return newFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            isBack = args.getBoolean("type");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return setView(R.layout.fragment_shopping_cart_recycler, "购物车", isBack);
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
                updateMoney(false);
                updateNumber(null, 0, 0, false);
            }
        }
    }

    BigDecimal totalMoney = new BigDecimal(0);
    int totalNumber = 0;

    @Override
    protected void initView() {

    }

    @Override
    public void onResume() {
        super.onResume();
        if (mEmptyLayout == null) {
            LogUtil.i(ShoppingCartFragment.class, "mEmptyLayout为空");
        } else {
            LogUtil.i(ShoppingCartFragment.class, "mEmptyLayout不为空");
        }

        mEmptyLayout = new EmptyLayout(activity, relativeLayout);
        mEmptyLayout.setReloadOnclick(new EmptyLayout.ReloadOnClick() {
            @Override
            public void reload() {
                mEmptyLayout.showLoading();
                loading();//网络连接错误是的刷新监听
            }
        });
        //  mEmptyLayout.showLoading();
        setPullListView();
        alert = new AlertConfirmation(activity, "是否删除", "您确定要删除选中的商品吗？");
        all_checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adapter != null) {
                    adapter.isAllCheck(all_checkbox.isChecked());
                }
                updateMoney(all_checkbox.isChecked());
                updateNumber(null, 0, 0, all_checkbox.isChecked());
            }
        });
        list_view.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(activity);
        list_view.setLayoutManager(mLayoutManager);
        list_view.addItemDecoration(new DividerGridItemDecoration(activity, LinearLayoutManager.HORIZONTAL, 20, getResources().getColor(R.color.line_bg)));
        loading();
    }

    private void updateNumber(Boolean isCheck, int buum, int number, Boolean isAllCheck) {
        if (isCheck != null) {
            if (isCheck) {
                totalNumber += number;
            } else {
                totalNumber -= number;
            }
        } else {
            totalNumber = 0;
            if (isAllCheck != null && isAllCheck) {
                for (CartShop cs : listShops) {
                    totalNumber = totalNumber + cs.suitList.size();
                }
            }
        }
        purchase.setText("计算：（" + totalNumber + "）");
    }

    private void updateMoney(Boolean isAllCheck) {
        totalMoney = new BigDecimal(0);
        if (isAllCheck == null) {
            for (CartShop cs : listShops) {
                totalMoney = totalMoney.add(cs.totalManey);
            }
        } else if (isAllCheck) {
            for (CartShop cs : listShops) {
                for (Suit suit : cs.suitList) {
                    totalMoney = totalMoney.add(new BigDecimal(String.valueOf(suit.price))).multiply(new BigDecimal(suit.num));
                }
            }
        } else {
            for (CartShop cs : listShops) {
                cs.totalManey = totalMoney;
            }
        }
        total_money.setText("合计：￥" + NumberUtil.getString(totalMoney, 2));
    }

    /**
     * 去結算
     *
     * @param view
     */
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
     * 结算
     */
    private void Confirm() {
        if (adapter != null) {
            String gids = adapter.getCheckId();
            if (StringUtil.hasText(gids)) {
                Intent intent = new Intent(activity, CommonActivity.class);
                intent.putExtra(JConstant.EXTRA_INDEX, CommonActivity.CONFIRMSHOPPINGCARTFRAGMENT);
                intent.putExtra("gids", gids);
                startActivity(intent);
            } else {
                toastError("请选择需要结算的商品");
            }
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
                    ApiClient.send(baseContext, JConstant.DELETE_CART_, map, true, null, new DataLoader<Object>() {

                        @Override
                        public void task(String data) {

                        }

                        @Override
                        public void succeed(Object data) {
                            alert.hide();
                            updateMoney(all_checkbox.isChecked());
                            updateNumber(null, 0, 0, all_checkbox.isChecked());
                            page = 1;
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
    int totalPage = 1;

    /**
     * 数据加载
     */
    public void loading() {
        Map<String, Object> map = new HashMap<>();
        map.put("pageIndex", page);
//        ApiClient.send(activity, JConstant.CART_QUERY_, map, CarePage.class, new DataLoader<CarePage>() {
        ApiClient.send(baseContext, JConstant.CART_QUERY_, map, true, CartShop.getType(), new DataLoader<List<CartShop>>() {
            @Override
            public void task(String data) {
            }

            @Override
            public void succeed(List<CartShop> data) {
                pullListView.onRefreshComplete();
                if (page == 1) {
                    listShops.clear();
                    if (layout.rightButton.getVisibility() == View.GONE) {
                        layout.rightButton.setText("编辑");
                        layout.rightButton.setVisibility(View.VISIBLE);
                    }
                }
                if (data != null && data.size() > 0) {
                    listShops.addAll(data);
                }
                if (listShops.size() > 0) {
                    mEmptyLayout.showView();
                    totalMoney = new BigDecimal(0);
                    totalNumber = 0;
                    if (adapter == null) {
                        adapter = new ShoppingCartAdapter(activity, listShops);
                        adapter.setOnCheckBoxChangeListener(new CartCheckBoxChangeListener() {
                            @Override
                            public void onCheckChange(boolean isChecked) {
                                all_checkbox.setChecked(isChecked);
                            }

                            @Override
                            public void onCheckChileChange(boolean isChecked, BigDecimal bprice, BigDecimal price, String id) {
                                updateMoney(null);
                            }

                            @Override
                            public void onCheckNumber(boolean isChecked, int bnum, int number) {
                                updateNumber(isChecked, bnum, number, null);
                            }

                            @Override
                            public void onCoupons(String merchantId) {
                                PreferLoading(merchantId, "");
                            }
                        });
                        adapter.setClick(new TowObjectParameterInterface() {
                            @Override
                            public void Onchange(int type, int postion, Object object) {

                            }
                        });
                        list_view.setAdapter(adapter);
                    } else {
                        list_view.setAdapter(adapter);
                        adapter.initDate(false);
                    }
                    bottom_layout.setVisibility(View.VISIBLE);

                    updateMoney(all_checkbox.isChecked());
                    updateNumber(null, 0, 0, all_checkbox.isChecked());

                } else {
                    pullListView.onRefreshComplete();
                    bottom_layout.setVisibility(View.GONE);
                    layout.rightButton.setVisibility(View.GONE);
                    if (page == 1) {
                        mEmptyLayout.setShowEmptyClick(true, new OnEmptyFoundClick() {
                            @Override
                            public void onFoundCilck() {
                                if (foundClick != null) {
                                    foundClick.onFoundCilck();
                                }
                            }
                        });
                        LogUtil.i(ShoppingCartFragment.class, "显示空的背景");
                        mEmptyLayout.showEmpty("购物车空空如也\n快去挑几件好货吧！");
                    }
                }
            }

            @Override
            public void error(String message) {
                if (page == 1) {
                    mEmptyLayout.showError(message);
                } else {
                    page--;
                }
                pullListView.onRefreshComplete();
            }
        }, JConstant.CART_QUERY_);
    }


    /**
     * 优惠卷选择
     *
     * @param merchantId
     */
    private void PreferLoading(String merchantId, String goodsid) {
        Map<String, Object> map = new HashMap<>();
        map.put("merchantId", merchantId);//商家id
        map.put("goodsId", goodsid);
        ApiClient.send(baseContext, JConstant.PREFER_FIND_, map, true, Prefer.getType(), new DataLoader<List<Prefer>>() {
            @Override
            public void task(String data) {

            }

            @Override
            public void succeed(List<Prefer> data) {
                if (data != null && data.size() > 0) {
                    PreferPopupWindow window = new PreferPopupWindow(activity, data);
                    window.show(activity);
                } else {
                    toastWarning("暂无可领优惠卷");
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
        ApiClient.cancelRequest(JConstant.UPDATE_CART_);
        ApiClient.cancelRequest(JConstant.DELETE_CART_);
        ApiClient.cancelRequest(JConstant.CART_QUERY_);
        ApiClient.cancelRequest(JConstant.PREFER_FIND_);
        ApiClient.cancelRequest(JConstant.PREFER_RECEIVE_);
    }
}
