package com.cadyd.app.ui.fragment.cart;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.cadyd.app.R;
import com.cadyd.app.interfaces.TowObjectParameterInterface;
import com.cadyd.app.model.CartShop;
import com.cadyd.app.model.Suit;
import com.cadyd.app.ui.view.DividerGridItemDecoration;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by xiongmao on 2016/10/10.
 */

public class HomeShoppingCartAdapter extends RecyclerView.Adapter<HomeShoppingCartAdapter.ViewHolder> {
    private Context context;
    private List<CartShop> list;
    private LayoutInflater inflater;

    private TowObjectParameterInterface towObjectParameterInterface;

    public HomeShoppingCartAdapter(Context context, List<CartShop> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    public void setClick(TowObjectParameterInterface towObjectParameterInterface) {
        this.towObjectParameterInterface = towObjectParameterInterface;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.shopping_cart_recycler_iteam, null);
        HomeShoppingCartAdapter.ViewHolder viewHolder = new HomeShoppingCartAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setView(position, list);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView subtotal;//小计
        TextView shop_name;//商户名称
        TextView coupons_btn;//领劵  当前已屏蔽
        RecyclerView recyclerView;//商品信息
        CheckBox shop_checkbox;//

        public ViewHolder(View itemView) {
            super(itemView);
            subtotal = (TextView) itemView.findViewById(R.id.subtotal);
            shop_name = (TextView) itemView.findViewById(R.id.shop_name);
            coupons_btn = (TextView) itemView.findViewById(R.id.coupons_btn);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.list_view);
            shop_checkbox = (CheckBox) itemView.findViewById(R.id.shop_checkbox);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.addItemDecoration(new DividerGridItemDecoration(context, LinearLayoutManager.HORIZONTAL, 1, context.getResources().getColor(R.color.line_bg)));
        }

        public void setView(final int postion, final List<CartShop> cartShopList) {
            final CartShop cartShop = cartShopList.get(postion);
            shop_name.setText(cartShop.shopname);
            /**选择商家(在初始化时或者是刷新是不会出发点击事件，再次优先计算一次价格用来显示用户未操作的情况下的显示)*/
            shop_checkbox.setChecked(cartShop.isChick);
            BigDecimal totoaalMoney = new BigDecimal("0");
            for (int i = 0; i < cartShop.suitList.size(); i++) {
                if (cartShop.suitList.get(i).isCheck) {
                    totoaalMoney = totoaalMoney.add(new BigDecimal(String.valueOf(cartShop.suitList.get(i).price))).multiply(new BigDecimal(cartShop.suitList.get(i).num));
                }
            }
            list.get(postion).totalManey = totoaalMoney;
            subtotal.setText("小计 ￥" + totoaalMoney.toString());//小计 该店铺当中以选中的商品的总价格

            final HomeShoppingCartGoodsAdapter adapter = new HomeShoppingCartGoodsAdapter(context, cartShop.suitList);
            adapter.setClick(new TowObjectParameterInterface() {
                @Override
                public void Onchange(int type, int p, Object object) {
                    if (type == HomeShoppingCartFragment.CHANGENUMBER) {//改变数量
                        list.get(postion).suitList.get(p).num = (int) object;
                        /**用户修改数量后重新计算该商铺选择的商品的总计*/
                        BigDecimal totoaalMoney = new BigDecimal("0");
                        int num = 0;
                        for (int i = 0; i < cartShop.suitList.size(); i++) {
                            if (cartShop.suitList.get(i).isCheck) {
                                num++;
                                totoaalMoney = totoaalMoney.add(new BigDecimal(String.valueOf(cartShop.suitList.get(i).price))).multiply(new BigDecimal(cartShop.suitList.get(i).num));
                            }
                        }
                        list.get(postion).totalManey = totoaalMoney;
                        subtotal.setText("小计 ￥" + totoaalMoney.toString());//小计 该店铺当中以选中的商品的总价格
                        if (num == cartShop.suitList.size()) {
                            shop_checkbox.setChecked(true);
                            list.get(p).isChick = true;
                        } else {
                            shop_checkbox.setChecked(false);
                            list.get(p).isChick = false;
                        }
                    } else if (type == HomeShoppingCartFragment.CHICKGOODS) {//选择商品
                        list.get(postion).suitList.get(p).isCheck = (boolean) object;

                        /**用户选择商品后重新计算该商铺选择的商品的总计*/
                        BigDecimal totoaalMoney = new BigDecimal("0");
                        int num = 0;
                        for (int i = 0; i < cartShop.suitList.size(); i++) {
                            if (cartShop.suitList.get(i).isCheck) {
                                num++;
                                totoaalMoney = totoaalMoney.add(new BigDecimal(String.valueOf(cartShop.suitList.get(i).price))).multiply(new BigDecimal(cartShop.suitList.get(i).num));
                            }
                        }
                        list.get(postion).totalManey = totoaalMoney;
                        subtotal.setText("小计 ￥" + totoaalMoney.toString());//小计 该店铺当中以选中的商品的总价格
                        if (num == cartShop.suitList.size()) {
                            shop_checkbox.setChecked(true);
                            list.get(p).isChick = true;
                        } else {
                            shop_checkbox.setChecked(false);
                            list.get(p).isChick = false;
                        }
                    }
                    if (towObjectParameterInterface != null) {
                        towObjectParameterInterface.Onchange(HomeShoppingCartFragment.CHICKSHOP, postion, shop_checkbox.isChecked());
                    }
                }
            });
            recyclerView.setAdapter(adapter);
            /**全选该店铺中的所有商品*/
            shop_checkbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BigDecimal totoaalMoney = new BigDecimal("0");
                    for (int i = 0; i < cartShop.suitList.size(); i++) {
                        list.get(postion).suitList.get(i).isCheck = shop_checkbox.isChecked();
                        if (shop_checkbox.isChecked()) {
                            totoaalMoney = totoaalMoney.add(new BigDecimal(String.valueOf(cartShop.suitList.get(i).price))).multiply(new BigDecimal(cartShop.suitList.get(i).num));
                        }
                    }
                    list.get(postion).totalManey = totoaalMoney;
                    subtotal.setText("小计 ￥" + totoaalMoney.toString());//小计 该店铺当中以选中的商品的总价格
                    adapter.notifyDataSetChanged();
                    if (towObjectParameterInterface != null) {
                        towObjectParameterInterface.Onchange(HomeShoppingCartFragment.CHICKSHOP, postion, shop_checkbox.isChecked());
                    }
                }
            });
        }
    }

}
