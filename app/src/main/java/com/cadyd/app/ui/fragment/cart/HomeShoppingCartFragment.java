package com.cadyd.app.ui.fragment.cart;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.cadyd.app.JConstant;
import com.cadyd.app.R;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.asynctask.DataLoader;
import com.cadyd.app.interfaces.TowObjectParameterInterface;
import com.cadyd.app.model.CartShop;
import com.cadyd.app.model.Suit;
import com.cadyd.app.ui.activity.CommonActivity;
import com.cadyd.app.ui.base.BaseFragement;
import com.cadyd.app.ui.view.AlertConfirmation;
import com.cadyd.app.ui.view.DividerGridItemDecoration;
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
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

/**
 * 购物车
 *
 * @author wcy
 */
public class HomeShoppingCartFragment extends BaseFragement {

    private OnEmptyFoundClick foundClick;

    public void setClick(OnEmptyFoundClick foundClick) {
        this.foundClick = foundClick;
    }

    public static final int CHANGENUMBER = 0;//改变数量
    public static final int CHICKGOODS = 1;//选择商品
    public static final int CHICKSHOP = 2;//选择商家

    boolean isBack = false;
    @Bind(R.id.my_recycler_view)
    RecyclerView myRecyclerView;
    @Bind(R.id.pullListView)
    PullToRefreshScrollView pullListView;
    @Bind(R.id.all_checkbox)
    CheckBox allCheckbox;
    @Bind(R.id.delete_goods)
    Button deleteGoods;
    @Bind(R.id.total_money)
    TextView totalMoney;
    @Bind(R.id.money_total_layout)
    LinearLayout moneyTotalLayout;
    @Bind(R.id.purchase)
    Button purchase;
    @Bind(R.id.bottom_layout)
    RelativeLayout bottomLayout;
    @Bind(R.id.empty_layout)
    RelativeLayout emptyLayout;
    EmptyLayout mEmptyLayout;

    private AlertConfirmation alert;
    private int page = 1;
    private List<CartShop> listShops = new ArrayList<>();
    private HomeShoppingCartAdapter adapter;

    public static HomeShoppingCartFragment newInstance(boolean type) {
        HomeShoppingCartFragment newFragment = new HomeShoppingCartFragment();
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = setView(R.layout.fragment_shopping_cart_recycler, "购物车", isBack, "编辑");
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initView() {
        /**
         * 初始化
         */
        mEmptyLayout = new EmptyLayout(activity, emptyLayout);
        mEmptyLayout.setReloadOnclick(new EmptyLayout.ReloadOnClick() {
            @Override
            public void reload() {
                mEmptyLayout.showLoading();
                loading();//网络连接错误是的刷新监听
            }
        });
        alert = new AlertConfirmation(activity, "是否删除", "您确定要删除选中的商品吗？");
        /**
         * 配置recyclerView
         */
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(activity);
        myRecyclerView.setLayoutManager(mLayoutManager);
        myRecyclerView.addItemDecoration(new DividerGridItemDecoration(activity, LinearLayoutManager.HORIZONTAL, 20, getResources().getColor(R.color.line_bg)));//配置recyclerView的分割线
        /**
         * PullToRefreshScrollView的上拉加载下拉刷新
         */
        pullListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                page = 1;
                loading();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                page++;
                loading();
            }
        });

        /**
         * 编辑模式配置
         */
        layout.rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (layout.rightButton.getText().equals("编辑")) {
                    layout.rightButton.setText("完成");
                    moneyTotalLayout.setVisibility(View.INVISIBLE);
                    purchase.setVisibility(View.INVISIBLE);
                    deleteGoods.setVisibility(View.VISIBLE);
                } else {
                    moneyTotalLayout.setVisibility(View.VISIBLE);
                    purchase.setVisibility(View.VISIBLE);
                    deleteGoods.setVisibility(View.GONE);
                    layout.rightButton.setText("编辑");
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        /**
         * 获取数据
         */
        if (adapter != null) {
            myRecyclerView.setAdapter(adapter);
        }
        loading();
    }

    /**
     * 数据加载
     */
    public void loading() {
        Map<String, Object> map = new HashMap<>();
        map.put("pageIndex", page);
        int tag = JConstant.CART_QUERY_;
        ApiClient.send(baseContext, JConstant.CART_QUERY_, map, true, CartShop.getType(), new DataLoader<List<CartShop>>() {
            @Override
            public void task(String data) {
            }

            @Override
            public void succeed(List<CartShop> data) {
                pullListView.onRefreshComplete();
                /**
                 * 获取数据
                 */
                if (page == 1) {
                    listShops.clear();
                }
                if (data != null) {
                    listShops.addAll(data);
                }
                /**
                 * 当数据为空时的处理
                 */
                if (listShops.size() <= 0) {//这才是真正的数据为空好不好
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
                } else {
                    mEmptyLayout.showView();
                }
                /**
                 * 设置adapter
                 */
                if (adapter == null) {
                    adapter = new HomeShoppingCartAdapter(activity, listShops);
                    adapter.setClick(new TowObjectParameterInterface() {
                        @Override
                        public void Onchange(int type, int postion, Object object) {
                            Compute();
                        }
                    });
                    myRecyclerView.setAdapter(adapter);
                } else {
                    adapter.notifyDataSetChanged();
                }
                Compute();
            }

            @Override
            public void error(String message) {
                pullListView.onRefreshComplete();
            }
        }, tag);
    }

    /**
     * 全选按钮监听
     *
     * @param view
     */
    @OnClick(R.id.all_checkbox)
    public void CheckAll(View view) {
        if (allCheckbox.isChecked()) {
            for (CartShop carShop : listShops) {
                carShop.isChick = true;
                for (Suit suit : carShop.suitList) {
                    suit.isCheck = true;
                }
            }
        } else {
            for (CartShop carShop : listShops) {
                carShop.isChick = false;
                for (Suit suit : carShop.suitList) {
                    suit.isCheck = false;
                }
            }
        }
        if (adapter != null) {
            adapter.notifyDataSetChanged();
            Compute();
        }
    }

    /**
     * 计算选中的价格及种类
     */
    private void Compute() {
        BigDecimal bigDecimal = new BigDecimal("0");
        int shopNumber = 0;
        int number = 0;
        for (CartShop carShop : listShops) {
            BigDecimal shopMoney = new BigDecimal("0");
            for (Suit suit : carShop.suitList) {
                if (suit.isCheck) {
                    number++;
                    shopMoney = shopMoney.add(new BigDecimal(String.valueOf(suit.price)).multiply(new BigDecimal(suit.num)));
                }
            }
            if (carShop.isChick) {
                shopNumber++;
            }
            bigDecimal = bigDecimal.add(shopMoney);
        }
        if (shopNumber == listShops.size()) {
            allCheckbox.setChecked(true);
        } else {
            allCheckbox.setChecked(false);
        }
        totalMoney.setText("合计：￥" + NumberUtil.getString(bigDecimal, 2));
        purchase.setText("计算：（" + number + "）");
    }

    /**
     * 去結算
     *
     * @param view
     */
    @OnClick({R.id.delete_goods, R.id.purchase})
    public void OnClickBtn(View view) {
        switch (view.getId()) {
            case R.id.delete_goods://删除商品
                alert.show(new AlertConfirmation.OnClickListeners() {
                    @Override
                    public void ConfirmOnClickListener() {
                        alert.hide();
                        String gids = getCheckId();
                        if (StringUtil.hasText(gids)) {
                            Map<String, Object> map = new HashMap<>();
                            map.put("key", gids);
                            ApiClient.send(baseContext, JConstant.DELETE_CART_, map, true, null, new DataLoader<Object>() {

                                @Override
                                public void task(String data) {

                                }

                                @Override
                                public void succeed(Object data) {
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
                break;
            case R.id.purchase://商品结算
                if (adapter != null) {
                    String gids = getCheckId();
                    if (StringUtil.hasText(gids)) {
                        Intent intent = new Intent(activity, CommonActivity.class);
                        intent.putExtra(JConstant.EXTRA_INDEX, CommonActivity.CONFIRMSHOPPINGCARTFRAGMENT);
                        intent.putExtra("gids", gids);
                        startActivity(intent);
                    } else {
                        toastError("请选择需要结算的商品");
                    }
                }
                break;
        }
    }

    public String getCheckId() {
        StringBuffer sb = new StringBuffer();
        for (CartShop carShop : listShops) {
            for (Suit suit : carShop.suitList) {
                if (suit.isCheck) {
                    sb.append(suit.id);
                    sb.append(",");
                }
            }
        }
        if (sb.length() > 0)
            sb.delete(sb.length() - 1, sb.length());
        return sb.toString();
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        ApiClient.cancelRequest(JConstant.CART_QUERY_);
    }
}
