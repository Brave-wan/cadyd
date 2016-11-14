package com.cadyd.app.adapter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import com.cadyd.app.R;
import com.cadyd.app.interfaces.CartCheckBoxChangeListener;
import com.cadyd.app.model.CartGoods;
import com.cadyd.app.model.CartShop;
import com.cadyd.app.model.Suit;
import com.cadyd.app.ui.fragment.cart.ShoppingCartFragment2;
import com.cadyd.app.ui.view.DividerGridItemDecoration;
import org.wcy.common.utils.NumberUtil;
import org.wcy.common.utils.StringUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 我的订单
 * Created by wcy on 2016/6/1.
 */
public class ShoppingCartRecyclerAdapter extends RecyclerView.Adapter<ShoppingCartRecyclerAdapter.ViewHolder> {
    private ShoppingCartFragment2 mcontext;
    private List<CartShop> mlist;
    // 用来控制CheckBox的选中状况
    private ArrayList<Boolean> isSelected;
    protected Map<String, Boolean> mapid = new HashMap<>();

    public ShoppingCartRecyclerAdapter(ShoppingCartFragment2 context, List<CartShop> list) {
        this.mcontext = context;
        mlist = list;
        isSelected = new ArrayList<Boolean>();
        // 初始化数据
        initDate();
    }

    // 初始化isSelected的数据
    private void initDate() {
        for (int i = 0; i < mlist.size(); i++) {
            isSelected.add(i, false);
        }
    }


    //创建新View，被LayoutManager所调用
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(mcontext.getContext()).inflate(R.layout.shopping_cart_recycler_iteam, viewGroup, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        final CartShop cs = mlist.get(position);
        viewHolder.shop_name.setText(cs.shopname);
        if (StringUtil.hasText(cs.prefermid)) {
            viewHolder.coupons_btn.setVisibility(View.VISIBLE);
            viewHolder.coupons_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //领取优惠券
                }
            });
        } else {
            viewHolder.coupons_btn.setVisibility(View.GONE);
        }
        boolean bc = isSelected.size() > 0 ? isSelected.get(position) : false;
        if (bc) {
            BigDecimal bd = getSubtotalMoney(cs.suitList);
            viewHolder.subtotal.setText("￥" + NumberUtil.getString(bd, 2));
        } else {
            viewHolder.subtotal.setText("￥0.00");
        }
//        for(){
//            final ShoppingCartRecyclerGoodsAdapter adapter = new ShoppingCartRecyclerGoodsAdapter(mcontext, cs.goodsList, bc);
//            adapter.setOnCheckBoxChangeListener(new CartCheckBoxChangeListener() {
//                @Override
//                public void onCheckChange(boolean isChecked) {
//                    viewHolder.shop_checkbox.setChecked(isChecked);
//                    isSelected.set(position, isChecked);
//                    onCheckBoxChangeListener.onCheckChange(isChecked == true ? isAllCheck() : false);
//                }
//
//                @Override
//                public void onCheckChileChange(boolean isChecked, BigDecimal bprice, BigDecimal price, String id) {
//                    mapid.put(id, isChecked);
//                    String money = viewHolder.subtotal.getText().toString();
//                    BigDecimal bd = new BigDecimal(money.substring(1, money.length()));
//                    if (isChecked) {
//                        bd = bd.add(price);
//                        if (bprice != null) {
//                            bd = bd.subtract(bprice);
//                        }
//                    } else {
//                        bd = bd.subtract(price);
//                    }
//                    viewHolder.subtotal.setText("￥" + NumberUtil.getString(bd, 2));
//                    onCheckBoxChangeListener.onCheckChileChange(isChecked, bprice, price, "");
//                }
//
//                @Override
//                public void onCheckNumber(boolean isChecked, int bnum, int number) {
//                    onCheckBoxChangeListener.onCheckNumber(isChecked, bnum, number);
//                }
//
//                @Override
//                public void onCoupons(String merchantId) {
//
//                }
//            });
//            viewHolder.listView.setAdapter(adapter);
//        }

        // 根据isSelected来设置checkbox的选中状况
        viewHolder.shop_checkbox.setChecked(bc);
        viewHolder.shop_checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean ischecked = viewHolder.shop_checkbox.isChecked();
                BigDecimal sum = new BigDecimal(0);
                int num = 0;
                isSelected.set(position, ischecked);

                for (Suit suit : cs.suitList) {
                    if (suit.suit) {
                        mapid.put(suit.id, ischecked);
                        sum = sum.add(new BigDecimal(suit.price).multiply(new BigDecimal(suit.num)));
                        num += suit.num;
                    } else {
                        for (CartGoods cg : suit.goodsList) {
                            mapid.put(cg.id, ischecked);
                            sum = sum.add(new BigDecimal(cg.price).multiply(new BigDecimal(cg.num)));
                            num += Integer.parseInt(cg.num);
                        }
                    }
                }

                if (ischecked) {
                    BigDecimal bd = getSubtotalMoney(cs.suitList);
                    viewHolder.subtotal.setText("￥" + NumberUtil.getString(bd, 2));
                } else {
                    viewHolder.subtotal.setText("￥0.00");
                }
               // adapter.isAllCheck(ischecked);
                if (onCheckBoxChangeListener != null) {
                    onCheckBoxChangeListener.onCheckChange(ischecked == true ? isAllCheck() : false);
                    onCheckBoxChangeListener.onCheckChileChange(ischecked, null, sum, null);
                    onCheckBoxChangeListener.onCheckNumber(ischecked, 0, num);
                }
            }
        });
        viewHolder.coupons_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCheckBoxChangeListener.onCoupons(cs.merchantid);
            }
        });
    }

    public BigDecimal getSubtotalMoney(List<Suit> list) {
        BigDecimal price = new BigDecimal(0);
        for (Suit suit : list) {
            if (suit.suit) {
                BigDecimal m = new BigDecimal(suit.price).multiply(new BigDecimal(suit.num));
                price = price.add(m);
            } else {
                for (CartGoods cg : suit.goodsList) {
                    BigDecimal m = new BigDecimal(cg.price).multiply(new BigDecimal(cg.num));
                    price = price.add(m);
                }
            }
        }

        return price;
    }

    private CartCheckBoxChangeListener onCheckBoxChangeListener;

    public void setOnCheckBoxChangeListener(CartCheckBoxChangeListener onCheckBoxChangeListener) {
        this.onCheckBoxChangeListener = onCheckBoxChangeListener;
    }

    // 初始化isSelected的数据
    public void initDate(boolean isCheck) {
        for (int i = 0; i < mlist.size(); i++) {
            isSelected.add(i, isCheck);
        }
        isAllCheckid(isCheck);
        notifyDataSetChanged();
    }

    public void isAllCheck(boolean isCheck) {
        for (int i = 0; i < mlist.size(); i++) {
            isSelected.set(i, isCheck);
        }
        isAllCheckid(isCheck);
        notifyDataSetChanged();
    }

    private void isAllCheckid(boolean isCheck) {
        mapid.clear();
        for (CartShop cs : mlist) {
            for (Suit suit : cs.suitList) {
                if (suit.suit) {
                    mapid.put(suit.id, isCheck);
                } else {
                    for (CartGoods cg : suit.goodsList) {
                        mapid.put(cg.id, isCheck);
                    }
                }
            }
        }
    }

    public String getCheckId() {
        StringBuffer sb = new StringBuffer();
        for (String key : mapid.keySet()) {
            if (mapid.get(key)) {
                sb.append(key);
                sb.append(",");
            }
        }
        if (sb.length() > 0)
            sb.delete(sb.length() - 1, sb.length());
        return sb.toString();
    }

    /**
     * 是否全部选中
     *
     * @return
     */
    public boolean isAllCheck() {
        int i = 0;
        for (Boolean b : isSelected) {
            if (b) {
                i++;
            }
        }
        if (i == mlist.size()) {
            return true;
        } else {
            return false;
        }
    }

    //获取数据的数量
    @Override
    public int getItemCount() {
        return mlist.size();
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView subtotal;//小计
        TextView shop_name;//商户名称
        TextView coupons_btn;//领劵
        RecyclerView listView;//商品信息
        CheckBox shop_checkbox;//

        public ViewHolder(View view) {
            super(view);
            subtotal = (TextView) view.findViewById(R.id.subtotal);
            shop_name = (TextView) view.findViewById(R.id.shop_name);
            coupons_btn = (TextView) view.findViewById(R.id.coupons_btn);
            listView = (RecyclerView) view.findViewById(R.id.list_view);
            shop_checkbox = (CheckBox) view.findViewById(R.id.shop_checkbox);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(mcontext.getContext());
            listView.setLayoutManager(mLayoutManager);
            listView.addItemDecoration(new DividerGridItemDecoration(mcontext.getContext(), LinearLayoutManager.HORIZONTAL, 1, mcontext.getResources().getColor(R.color.line_bg)));
        }


    }
}