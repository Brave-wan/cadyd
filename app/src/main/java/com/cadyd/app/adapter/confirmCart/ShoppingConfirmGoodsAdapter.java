package com.cadyd.app.adapter.confirmCart;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.cadyd.app.JConstant;
import com.cadyd.app.R;
import com.cadyd.app.adapter.cart.ShoppingCartSuitGoodsAdapter;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.asynctask.DataLoader;
import com.cadyd.app.interfaces.CartCheckBoxChangeListener;
import com.cadyd.app.interfaces.TowObjectParameterInterface;
import com.cadyd.app.model.AppGoodsVo;
import com.cadyd.app.model.CartGoods;
import com.cadyd.app.model.Suit;
import com.cadyd.app.ui.fragment.cart.ConfirmShoppingCartFragment;
import com.cadyd.app.ui.view.AddAndSubView;
import com.cadyd.app.ui.view.DividerGridItemDecoration;
import com.cadyd.app.ui.view.toast.ToastUtils;
import org.wcy.common.utils.NumberUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 确认订单商户下面的商品信息
 * Created by wcy on 2016/5/8.
 */
public class ShoppingConfirmGoodsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<Suit> mlist;

    //建立枚举 2个item 类型
    public enum ITEM_TYPE {
        ITEM1,
        ITEM2
    }

    public ShoppingConfirmGoodsAdapter(Context context, List<Suit> list) {
        this.context = context;
        mlist = list;
    }

    //创建新View，被LayoutManager所调用
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == ITEM_TYPE.ITEM1.ordinal()) {
            View view = LayoutInflater.from(context).inflate(R.layout.shopping_confirm_list_iteam_list_two, viewGroup, false);
            return new ShoppingConfirmGoodsAdapter.ViewHolder2(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.confirm_shopping_cart_list_iteam_list, viewGroup, false);
            return new ShoppingConfirmGoodsAdapter.ViewHolder(view);
        }
    }

    //设置ITEM类型，可以自由发挥，这里设置item position单数显示item1 偶数显示item2
    @Override
    public int getItemViewType(int position) {
        Suit cg = mlist.get(position);
        //Enum类提供了一个ordinal()方法，返回枚举类型的序数，这里ITEM_TYPE.ITEM1.ordinal()代表0， ITEM_TYPE.ITEM2.ordinal()代表1
        return cg.suit ? ITEM_TYPE.ITEM1.ordinal() : ITEM_TYPE.ITEM2.ordinal();
    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        final Suit cg = mlist.get(position);
        if (viewHolder instanceof ViewHolder) {
            ((ViewHolder) viewHolder).init(cg, position);
        } else if (viewHolder instanceof ViewHolder2) {
            ((ViewHolder2) viewHolder).init(cg, position);
        }
    }


    //获取数据的数量
    @Override
    public int getItemCount() {
        return mlist.size();
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView good_name;
        TextView salemix;
        TextView price;
        TextView number;
        TextView rebateAmount;
        ImageView goods_image;
        CheckBox goodsPrefer_checkbox;
        CheckBox luckCode_checkbox;
        CheckBox reabte_checkbox;
        View line_one;
        View goodsPrefer_layout;
        View luckCode_layout;
        View reabte_layout;

        RelativeLayout layout;

        public ViewHolder(View view) {
            super(view);
            good_name = (TextView) view.findViewById(R.id.good_name);
            salemix = (TextView) view.findViewById(R.id.salemix);
            price = (TextView) view.findViewById(R.id.price);
            number = (TextView) view.findViewById(R.id.number);
            rebateAmount = (TextView) view.findViewById(R.id.rebateAmount);
            goods_image = (ImageView) view.findViewById(R.id.goods_image);
            goodsPrefer_checkbox = (CheckBox) view.findViewById(R.id.goodsPrefer_checkbox);
            luckCode_checkbox = (CheckBox) view.findViewById(R.id.luckCode_checkbox);
            reabte_checkbox = (CheckBox) view.findViewById(R.id.reabte_checkbox);
            line_one = view.findViewById(R.id.line_one);
            goodsPrefer_layout = view.findViewById(R.id.goodsPrefer_layout);
            luckCode_layout = view.findViewById(R.id.luckCode_layout);
            reabte_layout = view.findViewById(R.id.reabte_layout);
        }

        public AppGoodsVo getGoods() {
            AppGoodsVo appGoodsVo = new AppGoodsVo();
            appGoodsVo.isGoodsPrefer = goodsPrefer_checkbox.isChecked();
            appGoodsVo.isTescode = luckCode_checkbox.isChecked();
            appGoodsVo.isReabte = reabte_checkbox.isChecked();
            Suit suit = (Suit) good_name.getTag();
            if (suit != null) {
                appGoodsVo.rebateid = suit.rebateId;
                appGoodsVo.cartid = suit.id;
            }
            return appGoodsVo;
        }


        public void init(final Suit suit, final int position) {
            CartGoods cg = suit.goodsList.get(0);
            good_name.setText(cg.title);
            good_name.setTag(suit);
            salemix.setText(cg.salemix);
            price.setText("￥" + suit.price);//套餐价
            number.setText("X " + String.valueOf(suit.num));

            if (suit.goodsPrefer) {
                goodsPrefer_layout.setVisibility(View.VISIBLE);
                line_one.setVisibility(View.VISIBLE);
                goodsPrefer_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (towObjectParameterInterface != null) {
                            towObjectParameterInterface.Onchange(isChecked ? ConfirmShoppingCartFragment.sub : ConfirmShoppingCartFragment.add, 0, new BigDecimal(suit.goodsPreferAmount));
                        }
                    }
                });
            } else {
                goodsPrefer_layout.setVisibility(View.GONE);
                line_one.setVisibility(View.GONE);
            }

            if (suit.luckCode) {
                luckCode_layout.setVisibility(View.VISIBLE);
                luckCode_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (towObjectParameterInterface != null) {
                            towObjectParameterInterface.Onchange(isChecked ? ConfirmShoppingCartFragment.sub : ConfirmShoppingCartFragment.add, 0, new BigDecimal(suit.maxCount));
                        }
                    }
                });
            } else {
                luckCode_layout.setVisibility(View.GONE);
            }

            if (suit.reabte) {
                reabte_layout.setVisibility(View.VISIBLE);
                SpannableStringBuilder builder = new SpannableStringBuilder("可激活金额：  ￥" + NumberUtil.getString(suit.rebateAmount, 2));
                //ForegroundColorSpan 为文字前景色，BackgroundColorSpan为文字背景色
                ForegroundColorSpan redSpan = new ForegroundColorSpan(context.getResources().getColor(R.color.black));
                builder.setSpan(redSpan, 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                rebateAmount.setText(builder);
                if (!suit.luckCode) {
                    line_one.setVisibility(View.VISIBLE);
                }
            } else {
                reabte_layout.setVisibility(View.GONE);
                if (!suit.goodsPrefer) {
                    line_one.setVisibility(View.GONE);
                }
            }
            ApiClient.ImageLoadersRounde(cg.thumb, goods_image, 8);
        }
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public class ViewHolder2 extends RecyclerView.ViewHolder {

        TextView good_name;
        TextView price;
        TextView number;
        RecyclerView list_view;
        CheckBox goodsPrefer_checkbox;
        CheckBox luckCode_checkbox;
        CheckBox reabte_checkbox;
        View line_one;
        View goodsPrefer_layout;
        View luckCode_layout;
        View reabte_layout;
        TextView rebateAmount;

        public ViewHolder2(View view) {
            super(view);
            good_name = (TextView) view.findViewById(R.id.good_name);
            price = (TextView) view.findViewById(R.id.price);
            number = (TextView) view.findViewById(R.id.number);
            rebateAmount = (TextView) view.findViewById(R.id.rebateAmount);
            list_view = (RecyclerView) view.findViewById(R.id.list_view);
            goodsPrefer_checkbox = (CheckBox) view.findViewById(R.id.goodsPrefer_checkbox);
            luckCode_checkbox = (CheckBox) view.findViewById(R.id.luckCode_checkbox);
            reabte_checkbox = (CheckBox) view.findViewById(R.id.reabte_checkbox);
            line_one = view.findViewById(R.id.line_one);
            goodsPrefer_layout = view.findViewById(R.id.goodsPrefer_layout);
            luckCode_layout = view.findViewById(R.id.luckCode_layout);
            reabte_layout = view.findViewById(R.id.reabte_layout);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
            list_view.setLayoutManager(mLayoutManager);
            list_view.addItemDecoration(new DividerGridItemDecoration(context, LinearLayoutManager.HORIZONTAL, 3, context.getResources().getColor(R.color.line_bg)));
        }

        public AppGoodsVo getGoods() {
            AppGoodsVo appGoodsVo = new AppGoodsVo();
            appGoodsVo.isGoodsPrefer = goodsPrefer_checkbox.isChecked();
            appGoodsVo.isTescode = luckCode_checkbox.isChecked();
            appGoodsVo.isReabte = reabte_checkbox.isChecked();
            Suit suit = (Suit) good_name.getTag();
            appGoodsVo.rebateid = suit.rebateId;
            appGoodsVo.cartid = suit.id;
            return appGoodsVo;
        }

        public void init(final Suit suit, final int position) {
            good_name.setText(suit.name);
            good_name.setTag(suit);

            number.setText("X " + String.valueOf(suit.num));
            price.setText("￥" + suit.price);//套餐价

            if (suit.goodsPrefer) {
                goodsPrefer_layout.setVisibility(View.VISIBLE);
                line_one.setVisibility(View.VISIBLE);
                goodsPrefer_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (towObjectParameterInterface != null) {
                            towObjectParameterInterface.Onchange(isChecked ? ConfirmShoppingCartFragment.sub : ConfirmShoppingCartFragment.add, 0, new BigDecimal(suit.goodsPreferAmount));
                        }
                    }
                });
            } else {
                goodsPrefer_layout.setVisibility(View.GONE);
                line_one.setVisibility(View.GONE);
            }
            if (suit.luckCode) {
                luckCode_layout.setVisibility(View.VISIBLE);
                luckCode_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (towObjectParameterInterface != null) {
                            towObjectParameterInterface.Onchange(isChecked ? ConfirmShoppingCartFragment.sub : ConfirmShoppingCartFragment.add, 0, new BigDecimal(suit.maxCount));
                        }
                    }
                });
            } else {
                luckCode_layout.setVisibility(View.GONE);
            }
            if (suit.reabte) {
                reabte_layout.setVisibility(View.VISIBLE);
                SpannableStringBuilder builder = new SpannableStringBuilder("可激活金额：  ￥" + NumberUtil.getString(suit.rebateAmount, 2));
                //ForegroundColorSpan 为文字前景色，BackgroundColorSpan为文字背景色
                ForegroundColorSpan redSpan = new ForegroundColorSpan(context.getResources().getColor(R.color.black));
                builder.setSpan(redSpan, 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                rebateAmount.setText(builder);
                if (!suit.luckCode) {
                    line_one.setVisibility(View.VISIBLE);
                }
            } else {
                reabte_layout.setVisibility(View.GONE);
                if (!suit.goodsPrefer) {
                    line_one.setVisibility(View.GONE);
                }
            }
            ShoppingConfirmSuitGoodsAdapter adapter = new ShoppingConfirmSuitGoodsAdapter(context, suit.goodsList);
            list_view.setAdapter(adapter);
        }
    }


    private TowObjectParameterInterface towObjectParameterInterface;

    public void setOnCheckBoxChangeListener(TowObjectParameterInterface onCheckBoxChangeListener) {
        this.towObjectParameterInterface = onCheckBoxChangeListener;
    }
}
