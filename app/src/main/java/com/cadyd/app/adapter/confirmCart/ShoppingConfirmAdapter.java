package com.cadyd.app.adapter.confirmCart;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.cadyd.app.R;
import com.cadyd.app.interfaces.TowObjectParameterInterface;
import com.cadyd.app.model.*;
import com.cadyd.app.ui.fragment.cart.ConfirmShoppingCartFragment;
import com.cadyd.app.ui.view.DividerListItemDecoration;
import com.cadyd.app.ui.view.ToastView;
import com.cadyd.app.ui.view.toast.ToastUtils;
import com.cadyd.app.ui.window.DistributionTypePopupWindow;
import com.cadyd.app.ui.window.PreferUsePopupWindow;
import com.cadyd.app.utils.StringUtils;

import org.wcy.android.utils.LogUtil;
import org.wcy.common.utils.NumberUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 确认订单
 * Created by wcy on 2016/6/1.
 */
public class ShoppingConfirmAdapter extends RecyclerView.Adapter<ShoppingConfirmAdapter.ViewHolder> {
    private Activity mcontext;
    private List<CartShop> mlist;
    private BigDecimal totalMoney;

    public static final int add = 1;
    public static final int sub = 2;
    public static final int not = 3;

    public static final int coupon = 4; //設置優惠券
    public static final int Freight = 5;//設置運費

    public ShoppingConfirmAdapter(Activity context, List<CartShop> list) {
        this.mcontext = context;
        mlist = list;
    }

    public void setTotalMoney(BigDecimal totalMoney) {
        this.totalMoney = totalMoney;
    }

    //创建新View，被LayoutManager所调用
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.confirm_shopping_cart_list_iteam, viewGroup, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        CartShop cs = mlist.get(position);
        viewHolder.init(cs, position);
    }


    private TowObjectParameterInterface towObjectParameterInterface;

    public void setOnCheckBoxChangeListener(TowObjectParameterInterface onCheckBoxChangeListener) {
        this.towObjectParameterInterface = onCheckBoxChangeListener;
    }


    //获取数据的数量
    @Override
    public int getItemCount() {
        return mlist == null ? 0 : mlist.size();
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView subtotal;//小计
        TextView shop_name;//商户名称
        TextView distribution_type;//领劵
        RecyclerView listView;//商品信息
        TextView prefer_choos;//
        EditText message;

        public ViewHolder(View view) {
            super(view);
            subtotal = (TextView) view.findViewById(R.id.subtotal);
            shop_name = (TextView) view.findViewById(R.id.shop_name);
            distribution_type = (TextView) view.findViewById(R.id.distribution_type);
            prefer_choos = (TextView) view.findViewById(R.id.prefer_choos);
            message = (EditText) view.findViewById(R.id.message);
            listView = (RecyclerView) view.findViewById(R.id.list_view);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(mcontext);
            listView.setVisibility(View.VISIBLE);
            listView.setLayoutManager(mLayoutManager);
            listView.addItemDecoration(new DividerListItemDecoration(mcontext, LinearLayoutManager.HORIZONTAL, 20, mcontext.getResources().getColor(R.color.background)));
        }


        public AppShopVo msg() {
            AppShopVo shopVo = new AppShopVo();
            shopVo.message = message.getText().toString();
            shopVo.shopid = shop_name.getTag().toString();
            Freight freight = (Freight) distribution_type.getTag();
            if (freight != null) shopVo.freightid = freight.id;
            Prefer f = (Prefer) prefer_choos.getTag();
            if (f != null) shopVo.spid = f.spreferId;
            int count = listView.getChildCount();
            if (count > 0) {
                List<AppGoodsVo> goodsList = new ArrayList<>();
                for (int i = 0; i < count; i++) {
                    //得到要更新的item的view
                    View view = listView.getChildAt(i);
                    if (null != listView.getChildViewHolder(view)) {
                        RecyclerView.ViewHolder viewHolder = listView.getChildViewHolder(view);
                        if (viewHolder instanceof ShoppingConfirmGoodsAdapter.ViewHolder) {
                            AppGoodsVo appGoodsVo = ((ShoppingConfirmGoodsAdapter.ViewHolder) viewHolder).getGoods();
                            goodsList.add(appGoodsVo);
                        } else if (viewHolder instanceof ShoppingConfirmGoodsAdapter.ViewHolder2) {
                            AppGoodsVo appGoodsVo = ((ShoppingConfirmGoodsAdapter.ViewHolder2) viewHolder).getGoods();
                            goodsList.add(appGoodsVo);
                        }
                    }
                }
                shopVo.goodsList = goodsList;
            }
            return shopVo;
        }

        private int num = 0;

        public void init(final CartShop cs, final int position) {
            shop_name.setText(cs.shopname);
            shop_name.setTag(cs.shopid);
            BigDecimal money = new BigDecimal(0);
            for (Suit cg : cs.suitList) {
                num += cg.num;
                money = money.add(new BigDecimal(String.valueOf(cg.price)).multiply(new BigDecimal(cg.num)));
            }
            subtotal.setText("共" + num + "件商品，合计：￥" + NumberUtil.getString(money, 2));
            subtotal.setTag(money);
            ShoppingConfirmGoodsAdapter adapter = new ShoppingConfirmGoodsAdapter(mcontext, cs.suitList);
            adapter.setOnCheckBoxChangeListener(new TowObjectParameterInterface() {
                @Override
                public void Onchange(int type, int postion, Object object) {
                    if (towObjectParameterInterface != null) {
                        BigDecimal b = (BigDecimal) object;
                        if (type == not) {
                            totalMoney = b;
                        } else if (type == add) {
                            totalMoney = totalMoney.add(b);
                        } else if (type == sub) {
                            totalMoney = totalMoney.subtract(b);
                        }
                        subtotal.setText("共" + num + "件商品，合计：￥" + NumberUtil.getString(totalMoney, 2));
                        towObjectParameterInterface.Onchange(type, 0, object);
                    }
                }
            });
            listView.setAdapter(adapter);
            final int finalNum = num;
            if (cs.freightList != null && cs.freightList.size() > 0) {
                distribution_type.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Freight f = (Freight) distribution_type.getTag();
                        DistributionTypePopupWindow window = new DistributionTypePopupWindow(mcontext, cs.freightList, f == null ? "" : f.id);
                        window.setOnCheckChangeListener(new DistributionTypePopupWindow.OnCheckChangeListener() {
                            @Override
                            public void onClickListener(Freight data) {
                                Freight f2 = (Freight) distribution_type.getTag();
                                BigDecimal extra = f2 != null && f2.extra != null ? NumberUtil.getBigDecimal(f2.extra) : new BigDecimal(0);
                                BigDecimal finalMoney = (BigDecimal) subtotal.getTag();
                                distribution_type.setText(data.name + " " + NumberUtil.getString(data.extra, 2) + "元");
                                totalMoney = totalMoney.subtract(extra);
                                BigDecimal am = finalMoney.subtract(extra);
                                am = am.add(NumberUtil.getBigDecimal(data.extra));
                                totalMoney = totalMoney.add(NumberUtil.getBigDecimal(data.extra));
                                subtotal.setText(String.format("共%d件商品，合计：￥%s", finalNum, NumberUtil.getString(am, 2)));
                                distribution_type.setTag(data);
                                subtotal.setTag(am);
                                if (towObjectParameterInterface != null) {
                                    towObjectParameterInterface.Onchange(ShoppingConfirmAdapter.Freight, position, NumberUtil.getString(data.extra, 2));
                                }
                            }
                        });
                        window.show(mcontext);
                    }
                });
            }
            final BigDecimal finalMoney = money;
            prefer_choos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (cs.preferList == null || cs.preferList.size() <= 0) {
                        ToastUtils.show(mcontext, "暂时没有可用优惠券", ToastUtils.WARNING_TYPE);
                        return;
                    }
                    final Prefer f = (Prefer) prefer_choos.getTag();
                    PreferUsePopupWindow window = new PreferUsePopupWindow(mcontext, cs.preferList, f == null ? "" : f.id, finalMoney);
                    window.setOnCheckChangeListener(new PreferUsePopupWindow.OnCheckChangeListener() {
                        @Override
                        public void onClickListener(Prefer data, boolean isChecked) {
                            BigDecimal money = f != null && f.money != null ? f.money : new BigDecimal(0);
                            BigDecimal finalMoney = (BigDecimal) subtotal.getTag();
                            BigDecimal am = finalMoney.add(money);
                            totalMoney = totalMoney.add(money);
                            if (data != null && isChecked) {
                                am = am.subtract(data.money);
                                totalMoney = totalMoney.subtract(data.money);
                                prefer_choos.setText(data.des);
                                prefer_choos.setTag(data);
                            } else {
                                mlist.get(position).spId = "";
                                prefer_choos.setText("");
                                prefer_choos.setTag(null);
                            }
                            subtotal.setText(String.format("共%d件商品，合计：￥%s", finalNum, NumberUtil.getString(am, 2)));
                            subtotal.setTag(am);
                            if (towObjectParameterInterface != null) {
                                towObjectParameterInterface.Onchange(ShoppingConfirmAdapter.coupon, position, NumberUtil.getString(am, 2));
                            }
                        }
                    });
                    window.show(mcontext);
                }
            });
        }
    }

}