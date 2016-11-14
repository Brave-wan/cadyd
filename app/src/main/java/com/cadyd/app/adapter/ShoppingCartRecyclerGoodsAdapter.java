package com.cadyd.app.adapter;

import android.support.v7.widget.RecyclerView;
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
import com.cadyd.app.ui.fragment.cart.ShoppingCartFragment2;
import com.cadyd.app.ui.view.AddAndSubView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 购物车，商户下面的商品信息
 * Created by wcy on 2016/5/8.
 */
public class ShoppingCartRecyclerGoodsAdapter extends RecyclerView.Adapter<ShoppingCartRecyclerGoodsAdapter.ViewHolder> {
    private ShoppingCartFragment2 context;
    private List<CartGoods> mlist;
    // 用来控制CheckBox的选中状况
    private ArrayList<Boolean> isSelected;

    public ShoppingCartRecyclerGoodsAdapter(ShoppingCartFragment2 context, List<CartGoods> list, boolean isChecked) {
        this.context = context;
        mlist = list;
        isSelected = new ArrayList<Boolean>();
        initDate(isChecked);
    }

    //创建新View，被LayoutManager所调用
    @Override
    public ShoppingCartRecyclerGoodsAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context.getContext()).inflate(R.layout.shopping_cart_list_iteam_list, viewGroup, false);
        ShoppingCartRecyclerGoodsAdapter.ViewHolder vh = new ShoppingCartRecyclerGoodsAdapter.ViewHolder(view);
        return vh;
    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(final ShoppingCartRecyclerGoodsAdapter.ViewHolder viewHolder, final int position) {
        final CartGoods cg = mlist.get(position);
        viewHolder.good_name.setText(cg.title);
        viewHolder.salemix.setText(cg.salemix);
        viewHolder.price.setText("￥" + cg.price);
        ApiClient.ImageLoadersRounde(cg.thumb, viewHolder.goods_image, 8);
        viewHolder.add_sub_view.setNum(Integer.parseInt(cg.num));
        viewHolder.add_sub_view.setOnNumChangeListener(new AddAndSubView.OnNumChangeListener() {
            @Override
            public void onNumChange(View view, int bnum, int num) {
                System.out.println(bnum + ":" + "--->" + num);
                updateNumber(cg.id, bnum, num, (AddAndSubView) view, position, viewHolder.goods_checkbox.isChecked());
            }
        });
        viewHolder.goods_checkbox.setChecked(isSelected.get(position));
        viewHolder.goods_checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean ischecked = viewHolder.goods_checkbox.isChecked();
                isSelected.set(position, ischecked);
                onCheckBoxChangeListener.onCheckChange(ischecked == true ? isAllCheck() : false);
                BigDecimal b = new BigDecimal(cg.price);
                b = b.multiply(new BigDecimal(viewHolder.add_sub_view.getNum()));
                System.out.println(b.doubleValue() + ":onCheckedChanged" + "--->" + viewHolder.add_sub_view.getNum() + "////" + ischecked);
                onCheckBoxChangeListener.onCheckChileChange(ischecked, null, b, cg.id);
                onCheckBoxChangeListener.onCheckNumber(ischecked, 0, viewHolder.add_sub_view.getNum());
            }
        });
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

        public ViewHolder(View view) {
            super(view);
            goods_image = (ImageView) view.findViewById(R.id.goods_image);
            good_name = (TextView) view.findViewById(R.id.good_name);
            salemix = (TextView) view.findViewById(R.id.salemix);
            price = (TextView) view.findViewById(R.id.price);
            add_sub_view = (AddAndSubView) view.findViewById(R.id.add_sub_view);
            goods_checkbox = (CheckBox) view.findViewById(R.id.goods_checkbox);
            add_sub_view.setIsEditable(false);
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
        List<ShoppingCartRecyclerGoodsAdapter.ObjArr> list = new ArrayList<>();
        ShoppingCartRecyclerGoodsAdapter.ObjArr oa = new ShoppingCartRecyclerGoodsAdapter.ObjArr();
        oa.id = id;
        oa.num = number;
        list.add(oa);
        map.put("objArr", list);
        ApiClient.send(context.getActivity(), JConstant.UPDATE_CART_, map, true, null, new DataLoader<Object>() {
            @Override
            public void task(String data) {

            }

            @Override
            public void succeed(Object data) {
                mlist.get(position).num = String.valueOf(number);
                if (ischeck) {
                    BigDecimal b = new BigDecimal(mlist.get(position).price);
                    onCheckBoxChangeListener.onCheckChileChange(ischeck, b.multiply(new BigDecimal(bnum)), b.multiply(new BigDecimal(number)), id);
                    onCheckBoxChangeListener.onCheckNumber(ischeck, bnum, number);
                }
            }

            @Override
            public void error(String message) {
                view.setNum(bnum);
                context.toastError("修改失败");
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
