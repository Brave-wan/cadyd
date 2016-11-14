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
import com.cadyd.app.comm.CommaSplice;
import com.cadyd.app.model.CartGoods;
import com.cadyd.app.ui.activity.CommonActivity;
import com.cadyd.app.ui.base.BaseFragement;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.wcy.android.widget.EmptyLayout;
import org.wcy.android.widget.OnEmptyFoundClick;
import org.wcy.common.utils.StringUtil;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 积分商城购物车
 */
public class IntegralCarFragment extends BaseFragement {

    private OnEmptyFoundClick foundClick;

    public void setClick(OnEmptyFoundClick foundClick) {
        this.foundClick = foundClick;
    }


    @Bind(R.id.bottom_layout)
    RelativeLayout bottom_layout;
    @Bind(R.id.empty_layout)
    View relativeLayout;
    EmptyLayout mEmptyLayout;
    @Bind(R.id.pulltorefreshlistview)
    PullToRefreshListView pullToRefreshListView;
    List<CartGoods> cartGoodsList = new ArrayList<>();//应用数据
    private int page = 1;
    private boolean isEdit = true;

    @Bind(R.id.total_price)
    TextView totalPrice;
    @Bind(R.id.total_integral)
    TextView totalIntegral;
    @Bind(R.id.purchase)
    Button purchase;//结算
    @Bind(R.id.delete_goods)
    Button delete_goods;//删除
    @Bind(R.id.no_freight)
    TextView noFreight;
    @Bind(R.id.all_checkbox)
    CheckBox allCheckBox;

    private LayoutInflater inflater;
    private BaseAdapter commonAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        boolean isBack = true;
        Bundle bundle = getArguments();
        if (bundle != null) {
            isBack = bundle.getBoolean("isBack");
        }
        return setView(R.layout.fragment_integral_car, "购物车", isBack, "编辑");
    }

    @Override
    protected void initView() {
        inflater = LayoutInflater.from(activity);
        layout.leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishActivity();
            }
        });
        layout.rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEdit) {
                    layout.rightButton.setText("完成");
                    isEdit = false;
                    purchase.setVisibility(View.GONE);
                    noFreight.setVisibility(View.GONE);
                    delete_goods.setVisibility(View.VISIBLE);
                } else {
                    layout.rightButton.setText("编辑");
                    isEdit = true;
                    purchase.setVisibility(View.VISIBLE);
                    noFreight.setVisibility(View.VISIBLE);
                    delete_goods.setVisibility(View.GONE);
                }
            }
        });
        if (mEmptyLayout == null) {
            mEmptyLayout = new EmptyLayout(activity, relativeLayout);
        }
        pullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        pullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                page = 1;
                getCarGoods();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                page++;
                getCarGoods();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        layout.rightButton.setText("编辑");
        isEdit = true;
        purchase.setVisibility(View.VISIBLE);
        noFreight.setVisibility(View.VISIBLE);
        delete_goods.setVisibility(View.GONE);
        getCarGoods();
    }

    private class CarGoodsList {
        public List<CartGoods> data = new ArrayList<>();
    }

    private class ViewHolder {
        private TextView unitPrice;//单价
        private TextView number;//数量
        private TextView title;//标题
        private ImageView imageView;//图片
        private CheckBox checkbox;//单选按钮
    }

    private void getCarGoods() {
        Map<String, Object> map = new HashMap<>();
        map.put("pageIndex", page);
        ApiClient.send(activity, JConstant.FINDINTEGRALGOODSCART, map, CarGoodsList.class, new DataLoader<CarGoodsList>() {
            @Override
            public void task(String data) {
            }

            @Override
            public void succeed(CarGoodsList data) {
                pullToRefreshListView.onRefreshComplete();
                if (page == 1) {
                    cartGoodsList.clear();
                    allCheckBox.setChecked(false);
                }
                cartGoodsList.addAll(data.data);
                if (cartGoodsList != null && cartGoodsList.size() > 0) {
                    mEmptyLayout.showView();
                    if (commonAdapter == null) {
                        commonAdapter = new BaseAdapter() {
                            @Override
                            public int getCount() {
                                return cartGoodsList == null ? 0 : cartGoodsList.size();
                            }

                            @Override
                            public Object getItem(int position) {
                                return null;
                            }

                            @Override
                            public long getItemId(int position) {
                                return 0;
                            }

                            @Override
                            public View getView(final int position, View view, final ViewGroup parent) {
                                IntegralCarFragment.ViewHolder holder;
                                if (view == null) {
                                    holder = new IntegralCarFragment.ViewHolder();
                                    view = inflater.inflate(R.layout.integral_car_item, null);
                                    holder.title = (TextView) view.findViewById(R.id.title);
                                    holder.imageView = (ImageView) view.findViewById(R.id.image_view);
                                    holder.unitPrice = (TextView) view.findViewById(R.id.unit_price);
                                    holder.number = (TextView) view.findViewById(R.id.number);
                                    holder.checkbox = (CheckBox) view.findViewById(R.id.checkbox);
                                    view.setTag(holder);
                                } else {
                                    holder = (IntegralCarFragment.ViewHolder) view.getTag();
                                }
                                CartGoods item = cartGoodsList.get(position);
                                /**设置数据**/
                                if (!StringUtil.hasText(item.price) || "0".equals(item.price)) {
                                    holder.unitPrice.setText(item.integral.toString());//设置价格
                                } else {
                                    holder.unitPrice.setText(item.integral + "+" + item.price);//设置价格
                                }
                                holder.number.setText("一件/一套X" + item.number);//设置数量
                                holder.title.setText(item.title);
                                ApiClient.displayImage(item.thumb, holder.imageView, R.mipmap.goods_top_defalut);
                                /**设置背景**/
                                view.setBackgroundResource(R.color.white);
                                /**单选按钮**/
                                holder.checkbox.setChecked(item.isChecked);
                                holder.checkbox.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        CheckBox checkBox1 = (CheckBox) v;
                                        cartGoodsList.get(position).isChecked = checkBox1.isChecked();
                                        CalculationPrice();
                                    }
                                });
                                return view;
                            }
                        };
                        pullToRefreshListView.setAdapter(commonAdapter);
                    } else {
                        commonAdapter.notifyDataSetChanged();
                    }

                    bottom_layout.setVisibility(View.VISIBLE);
                } else {
                    pullToRefreshListView.onRefreshComplete();
                    bottom_layout.setVisibility(View.GONE);
                    layout.rightButton.setVisibility(View.GONE);
                    mEmptyLayout.setShowEmptyClick(true, new OnEmptyFoundClick() {
                        @Override
                        public void onFoundCilck() {
                            if (foundClick != null) {
                                foundClick.onFoundCilck();
                            }

                        }
                    });
                    mEmptyLayout.showEmpty("购物车空空如也\n快去挑几件好货吧！");
                }
            }

            @Override
            public void error(String message) {
                mEmptyLayout.showError(message);
                pullToRefreshListView.onRefreshComplete();
            }
        }, JConstant.FINDINTEGRALGOODSCART);
    }

    private void CalculationPrice() {
        BigDecimal IntegralDecimal = new BigDecimal("0");
        BigDecimal PriceDecimal = new BigDecimal("0.00");
        int number = 0;
        for (int i = 0; i < cartGoodsList.size(); i++) {
            if (cartGoodsList.get(i).isChecked) {
                //积分
                BigDecimal Integral = new BigDecimal(cartGoodsList.get(i).integral);
                Integral = Integral.multiply(new BigDecimal(Integer.valueOf(cartGoodsList.get(i).number)));
                IntegralDecimal = IntegralDecimal.add(Integral);
                //钱
                BigDecimal Price = new BigDecimal(StringUtil.hasText(cartGoodsList.get(i).price) ? cartGoodsList.get(i).price : "0.00");
                Price = Price.multiply(new BigDecimal(Integer.valueOf(cartGoodsList.get(i).number)));
                PriceDecimal = PriceDecimal.add(Price);
                //数量
                number++;
            }
        }
        totalIntegral.setText(IntegralDecimal.toString());
        totalPrice.setText("￥   " + PriceDecimal.doubleValue());
        purchase.setText("结算(" + number + "）");
        if (number == cartGoodsList.size()) {
            allCheckBox.setChecked(true);
        } else {
            allCheckBox.setChecked(false);
        }
    }

    //结算
    @OnClick(R.id.purchase)
    public void onPurchase(View view) {
        List<CartGoods> purchaseList = new ArrayList<>();//传递数据
        for (int i = 0; i < cartGoodsList.size(); i++) {
            if (cartGoodsList.get(i).isChecked) {
                purchaseList.add(cartGoodsList.get(i));
            }
        }
        if (purchaseList.size() <= 0) {
            toast("请选择商品");
            return;
        }
        Intent intent = new Intent(activity, CommonActivity.class);
        intent.putExtra(JConstant.EXTRA_INDEX, CommonActivity.INTEGRALDETAIL);
        intent.putExtra("type", true);
        intent.putExtra("goodsList", (Serializable) purchaseList);
        startActivity(intent);
    }

    //全选
    @OnClick(R.id.all_checkbox)
    public void onAllCheck(View view) {
        for (int i = 0; i < cartGoodsList.size(); i++) {
            cartGoodsList.get(i).isChecked = allCheckBox.isChecked();
        }
        if (commonAdapter != null) {
            commonAdapter.notifyDataSetChanged();
            CalculationPrice();
        }
    }

    //删除
    @OnClick(R.id.delete_goods)
    public void onDelete() {
        List<String> stringList = new ArrayList<>();//传递数据
        for (int i = 0; i < cartGoodsList.size(); i++) {
            if (cartGoodsList.get(i).isChecked) {
                stringList.add(cartGoodsList.get(i).id);
            }
        }
        Map<String, Object> map = new HashMap<>();
        map.put("key", CommaSplice.Splice(stringList).toString());
        ApiClient.send(activity, JConstant.DELETE_CART_, map, true, null, new DataLoader<String>() {
            @Override
            public void task(String data) {

            }

            @Override
            public void succeed(String data) {
                toastSuccess("删除成功");
                page = 1;
                getCarGoods();
            }

            @Override
            public void error(String message) {

            }
        }, JConstant.DELETE_CART_);
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ApiClient.cancelRequest(JConstant.FINDINTEGRALGOODSCART);
        ApiClient.cancelRequest(JConstant.DELETE_CART_);
    }
}
