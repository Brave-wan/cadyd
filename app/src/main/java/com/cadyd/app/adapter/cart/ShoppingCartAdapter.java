package com.cadyd.app.adapter.cart;

import android.app.Activity;
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
import com.cadyd.app.interfaces.CartCheckBoxChangeListener;
import com.cadyd.app.interfaces.TowObjectParameterInterface;
import com.cadyd.app.model.CartShop;
import com.cadyd.app.model.Suit;
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
public class ShoppingCartAdapter extends RecyclerView.Adapter<ShoppingCartAdapter.ViewHolder> {
    private Activity mcontext;
    private List<CartShop> mlist;
    // 用来控制CheckBox的选中状况
    private ArrayList<Boolean> isSelected;
    protected Map<String, Boolean> mapid = new HashMap<>();
    private int couont = 0;
    private TowObjectParameterInterface towObjectParameterInterface;

    public void setClick(TowObjectParameterInterface towObjectParameterInterface) {
        this.towObjectParameterInterface = towObjectParameterInterface;
    }

    public ShoppingCartAdapter(Activity context, List<CartShop> list) {
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
        View view = LayoutInflater.from(mcontext).inflate(R.layout.shopping_cart_recycler_iteam, viewGroup, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        CartShop cs = mlist.get(position);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 最热推荐商品点击监听 TYPE=0
                towObjectParameterInterface.Onchange(0, position, v);
            }
        });
        viewHolder.init(cs, position);
    }


    private CartCheckBoxChangeListener onCheckBoxChangeListener;

    public void setOnCheckBoxChangeListener(CartCheckBoxChangeListener onCheckBoxChangeListener) {
        this.onCheckBoxChangeListener = onCheckBoxChangeListener;
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
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(mcontext);
            listView.setLayoutManager(mLayoutManager);
            listView.addItemDecoration(new DividerGridItemDecoration(mcontext, LinearLayoutManager.HORIZONTAL, 1, mcontext.getResources().getColor(R.color.line_bg)));
        }

        public void init(final CartShop cs, final int position) {
            shop_name.setText(cs.shopname);
            if (StringUtil.hasText(cs.prefermid)) {
                //   coupons_btn.setVisibility(View.VISIBLE);
                coupons_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //领取优惠券
                    }
                });
            } else {
                coupons_btn.setVisibility(View.GONE);
            }
            boolean bc = isSelected.size() > 0 ? isSelected.get(position) : false;
            if (bc) {
                BigDecimal bd = getSubtotalMoney(cs.suitList);
                subtotal.setText("￥" + NumberUtil.getString(bd, 2));
            } else {
                subtotal.setText("￥0.00");
            }
            final ShoppingCartGoodsAdapter adapter = new ShoppingCartGoodsAdapter(mcontext, cs.suitList, bc);
            adapter.setOnCheckBoxChangeListener(new CartCheckBoxChangeListener() {
                @Override
                public void onCheckChange(boolean isChecked) {
                    shop_checkbox.setChecked(isChecked);
                    isSelected.set(position, isChecked);
                    onCheckBoxChangeListener.onCheckChange(isChecked == true ? isAllCheck() : false);
                }

                @Override
                public void onCheckChileChange(boolean isChecked, BigDecimal bprice, BigDecimal price, String id) {
                    mapid.put(id, isChecked);
                    String money = subtotal.getText().toString();
                    BigDecimal bd = new BigDecimal(money.substring(1, money.length()));
                    if (isChecked) {
                        bd = bd.add(price);
                        if (bprice != null) {
                            bd = bd.subtract(bprice);
                        }
                        Log.i("xiongmao", "新的价格： " + bd.toString());
                    } else {
                        bd = bd.subtract(price);
                        Log.i("xiongmao", "新的价格： " + bd.toString());
                    }
                    subtotal.setText("￥" + NumberUtil.getString(bd, 2));
                    cs.totalManey = bd;
                    onCheckBoxChangeListener.onCheckChileChange(isChecked, bd, price, "");
                }

                @Override
                public void onCheckNumber(boolean isChecked, int bnum, int number) {
                    couont += number;
                    if (bnum > 0) {
                        couont -= bnum;
                    }
                    System.out.println("---------------------:" + isChecked);
                    onCheckBoxChangeListener.onCheckNumber(isChecked, bnum, number);
                }

                @Override
                public void onCoupons(String merchantId) {

                }
            });
            listView.setAdapter(adapter);
            // 根据isSelected来设置checkbox的选中状况
            shop_checkbox.setChecked(bc);
            shop_checkbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final boolean ischecked = shop_checkbox.isChecked();
                    BigDecimal sum = new BigDecimal(0);
                    int num = 0;
                    isSelected.set(position, ischecked);
                    for (Suit suit : cs.suitList) {
                        mapid.put(suit.id, ischecked);
                        sum = sum.add(new BigDecimal(String.valueOf(suit.price)).multiply(new BigDecimal(suit.num)));
                        num += suit.num;
                        if (ischecked) {
                            cs.totalManey = sum;
                        } else {
                            cs.totalManey = new BigDecimal("0");
                        }
                    }
                    if (onCheckBoxChangeListener != null) {
                        onCheckBoxChangeListener.onCheckChange(ischecked == true ? isAllCheck() : false);
                        onCheckBoxChangeListener.onCheckChileChange(ischecked, null, sum, null);
                        onCheckBoxChangeListener.onCheckNumber(ischecked, 0, cs.suitList.size());
                    }
                    if (ischecked) {
                        couont = num;
                        BigDecimal bd = getSubtotalMoney(cs.suitList);
                        subtotal.setText("￥" + NumberUtil.getString(bd, 2));
                    } else {
                        couont = 0;
                        subtotal.setText("￥0.00");
                    }
                    adapter.isAllCheck(ischecked);
                }
            });
            coupons_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onCheckBoxChangeListener.onCoupons(cs.merchantid);
                }
            });
        }

        /**
         * 总价格
         *
         * @param list
         * @return
         */
        public BigDecimal getSubtotalMoney(List<Suit> list) {
            BigDecimal price = new BigDecimal(0);
            for (Suit suit : list) {
                BigDecimal m = new BigDecimal(String.valueOf(suit.price)).multiply(new BigDecimal(suit.num));
                price = price.add(m);
            }
            return price;
        }
    }


    // 初始化isSelected的数据
    public void initDate(boolean isCheck) {
        for (int i = 0; i < mlist.size(); i++) {
            isSelected.add(i, isCheck);
        }
        isAllCheckid(isCheck);
        notifyDataSetChanged();
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
                mapid.put(suit.id, isCheck);
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
}