package com.cadyd.app.adapter.cart;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.cadyd.app.JConstant;
import com.cadyd.app.R;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.asynctask.DataLoader;
import com.cadyd.app.interfaces.CartCheckBoxChangeListener;
import com.cadyd.app.model.CartGoods;
import com.cadyd.app.model.Suit;
import com.cadyd.app.ui.activity.GoodsDetailActivity;
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
 * 购物车，商户下面的商品信息
 * Created by wcy on 2016/5/8.
 */
public class ShoppingCartGoodsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<Suit> mlist;
    // 用来控制CheckBox的选中状况
    private ArrayList<Boolean> isSelected;

    //建立枚举 2个item 类型
    public enum ITEM_TYPE {
        ITEM1,
        ITEM2
    }

    public ShoppingCartGoodsAdapter(Context context, List<Suit> list, boolean isChecked) {
        this.context = context;
        mlist = list;
        isSelected = new ArrayList<Boolean>();
        initDate(isChecked);
    }

    //创建新View，被LayoutManager所调用
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == ITEM_TYPE.ITEM1.ordinal()) {
            View view = LayoutInflater.from(context).inflate(R.layout.shopping_cart_list_iteam_list_two, viewGroup, false);
            return new ShoppingCartGoodsAdapter.ViewHolder2(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.shopping_cart_list_iteam_list, viewGroup, false);
            return new ShoppingCartGoodsAdapter.ViewHolder(view);
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
        ImageView goods_image;
        TextView good_name;
        TextView salemix;
        AddAndSubView add_sub_view;
        TextView price;
        CheckBox goods_checkbox;
        View itemView;

        public ViewHolder(View view) {
            super(view);
            itemView = view;
            goods_image = (ImageView) view.findViewById(R.id.goods_image);
            good_name = (TextView) view.findViewById(R.id.good_name);
            salemix = (TextView) view.findViewById(R.id.salemix);
            price = (TextView) view.findViewById(R.id.price);
            add_sub_view = (AddAndSubView) view.findViewById(R.id.add_sub_view);
            goods_checkbox = (CheckBox) view.findViewById(R.id.goods_checkbox);
            add_sub_view.setIsEditable(false);
        }

        public void init(final Suit suit, final int position) {
            final CartGoods cg = suit.goodsList.get(0);
            good_name.setText(cg.title);
            salemix.setText(cg.salemix);
            price.setText("￥" + suit.price);
            ApiClient.ImageLoadersRounde(cg.thumb, goods_image, 8);
            add_sub_view.setNum(suit.num);
            add_sub_view.setOnNumChangeListener(new AddAndSubView.OnNumChangeListener() {
                @Override
                public void onNumChange(View view, int bnum, int num) {
                    updateNumber(suit.id, bnum, num, (AddAndSubView) view, position, goods_checkbox.isChecked());
                }
            });
            goods_checkbox.setChecked(isSelected.get(position));
            goods_checkbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean ischecked = goods_checkbox.isChecked();
                    isSelected.set(position, ischecked);
                    onCheckBoxChangeListener.onCheckChange(ischecked == true ? isAllCheck() : false);
                    BigDecimal b = new BigDecimal(String.valueOf(suit.price));
                    b = b.multiply(new BigDecimal(add_sub_view.getNum()));
                    onCheckBoxChangeListener.onCheckChileChange(ischecked, null, b, suit.id);
                    onCheckBoxChangeListener.onCheckNumber(ischecked, 0, 1);
                }
            });
            //跳转到商品详情
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, GoodsDetailActivity.class);
                    intent.putExtra("gid", cg.goodsid);
                    context.startActivity(intent);
                }
            });
        }
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public class ViewHolder2 extends RecyclerView.ViewHolder {
        TextView good_name;
        AddAndSubView add_sub_view;
        TextView price;
        CheckBox goods_checkbox;
        RecyclerView list_view;

        public ViewHolder2(View view) {
            super(view);
            good_name = (TextView) view.findViewById(R.id.good_name);
            list_view = (RecyclerView) view.findViewById(R.id.list_view);
            price = (TextView) view.findViewById(R.id.price);
            add_sub_view = (AddAndSubView) view.findViewById(R.id.add_sub_view);
            goods_checkbox = (CheckBox) view.findViewById(R.id.goods_checkbox);
            add_sub_view.setIsEditable(false);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
            list_view.setLayoutManager(mLayoutManager);
            list_view.addItemDecoration(new DividerGridItemDecoration(context, LinearLayoutManager.HORIZONTAL, 0, context.getResources().getColor(R.color.transparent)));
        }

        public void init(final Suit suit, final int position) {
            good_name.setText(suit.name);
            price.setText("套装价格：￥" + NumberUtil.getString(suit.price, 2));
            add_sub_view.setNum(suit.num);
            add_sub_view.setOnNumChangeListener(new AddAndSubView.OnNumChangeListener() {
                @Override
                public void onNumChange(View view, int bnum, int num) {
                    System.out.println(bnum + ":" + "--->" + num);
                    updateNumber(suit.id, bnum, num, (AddAndSubView) view, position, goods_checkbox.isChecked());
                }
            });
            goods_checkbox.setChecked(isSelected.get(position));
            goods_checkbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean ischecked = goods_checkbox.isChecked();
                    isSelected.set(position, ischecked);
                    onCheckBoxChangeListener.onCheckChange(ischecked == true ? isAllCheck() : false);
                    BigDecimal b = new BigDecimal(suit.price);
                    b = b.multiply(new BigDecimal(add_sub_view.getNum()));
                    onCheckBoxChangeListener.onCheckChileChange(ischecked, null, b, suit.id);
                    onCheckBoxChangeListener.onCheckNumber(ischecked, 0, add_sub_view.getNum());
                }
            });
            ShoppingCartSuitGoodsAdapter adapter = new ShoppingCartSuitGoodsAdapter(context, suit.goodsList);
            list_view.setAdapter(adapter);
        }
    }

    /**
     * 数量增加
     *
     * @param id
     * @param number
     */
    public void updateNumber(final String id, final int bnum, final int number, final AddAndSubView view, final int position, final boolean ischeck) {
        final Map<String, Object> map = new HashMap<>();
        List<ShoppingCartGoodsAdapter.ObjArr> list = new ArrayList<>();
        ShoppingCartGoodsAdapter.ObjArr oa = new ShoppingCartGoodsAdapter.ObjArr();
        oa.id = id;
        oa.num = number;
        list.add(oa);
        map.put("objArr", list);
        ApiClient.send(context, JConstant.UPDATE_CART_, map, true, null, new DataLoader<Object>() {
            @Override
            public void task(String data) {

            }

            @Override
            public void succeed(Object data) {
                mlist.get(position).num = number;
                if (ischeck) {
                    BigDecimal b = new BigDecimal(String.valueOf(mlist.get(position).price));
                    Log.i("xiongmao", "数量变化后：" + b.multiply(new BigDecimal(bnum)) + "and" + b.multiply(new BigDecimal(number)));
                    onCheckBoxChangeListener.onCheckChileChange(ischeck, b.multiply(new BigDecimal(bnum)), b.multiply(new BigDecimal(number)), id);
                    onCheckBoxChangeListener.onCheckNumber(ischeck, bnum, number);
                }
            }

            @Override
            public void error(String message) {
                view.setNum(bnum);
                ToastUtils.show(context, "修改失败", ToastUtils.ERROR_TYPE);
            }
        }, JConstant.UPDATE_CART_);
    }

    // 初始化isSelected的数据
    private void initDate(boolean isCheck) {
        isSelected.clear();
        for (int i = 0; i < mlist.size(); i++) {
            isSelected.add(i, isCheck);
        }
    }

    public void isAllCheck(boolean isCheck) {
        for (int i = 0; i < mlist.size(); i++) {
            isSelected.set(i, isCheck);
        }
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

    private CartCheckBoxChangeListener onCheckBoxChangeListener;

    public void setOnCheckBoxChangeListener(CartCheckBoxChangeListener onCheckBoxChangeListener) {
        this.onCheckBoxChangeListener = onCheckBoxChangeListener;
    }

    public class ObjArr {
        public String id;
        public int num;
    }
}
