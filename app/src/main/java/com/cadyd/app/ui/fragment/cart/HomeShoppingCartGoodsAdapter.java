package com.cadyd.app.ui.fragment.cart;

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
import com.cadyd.app.adapter.cart.ShoppingCartGoodsAdapter;
import com.cadyd.app.adapter.cart.ShoppingCartSuitGoodsAdapter;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.asynctask.DataLoader;
import com.cadyd.app.interfaces.CartCheckBoxChangeListener;
import com.cadyd.app.interfaces.TowObjectParameterInterface;
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
public class HomeShoppingCartGoodsAdapter extends RecyclerView.Adapter<HomeShoppingCartGoodsAdapter.ViewHolder> {
    private Context context;
    private List<Suit> mlist;
    private TowObjectParameterInterface towObjectParameterInterface;

    //建立枚举 2个item 类型
    public enum ITEM_TYPE {
        ITEM1,
        ITEM2
    }

    public HomeShoppingCartGoodsAdapter(Context context, List<Suit> list) {
        this.context = context;
        mlist = list;
    }

    public void setClick(TowObjectParameterInterface towObjectParameterInterface) {
        this.towObjectParameterInterface = towObjectParameterInterface;
    }

    //获取数据的数量
    @Override
    public int getItemCount() {
        return mlist.size();
    }

    //设置ITEM类型，可以自由发挥，这里设置item position单数显示item1 偶数显示item2
    @Override
    public int getItemViewType(int position) {
        Suit suit = mlist.get(position);
        //Enum类提供了一个ordinal()方法，返回枚举类型的序数，这里ITEM_TYPE.ITEM1.ordinal()代表0， ITEM_TYPE.ITEM2.ordinal()代表1套餐
        return suit.suit ? ITEM_TYPE.ITEM1.ordinal() : ITEM_TYPE.ITEM2.ordinal();
    }

    //创建新View，被LayoutManager所调用
    @Override
    public HomeShoppingCartGoodsAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view;
        if (viewType == ITEM_TYPE.ITEM1.ordinal()) {
            view = LayoutInflater.from(context).inflate(R.layout.shopping_cart_list_iteam_list_two, viewGroup, false);//套餐的view
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.shopping_cart_list_iteam_list, viewGroup, false);//单品的view
        }
        return new HomeShoppingCartGoodsAdapter.ViewHolder(view);
    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(HomeShoppingCartGoodsAdapter.ViewHolder viewHolder, int position) {
        Suit cg = mlist.get(position);
        viewHolder.init(cg, position, towObjectParameterInterface);
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public class ViewHolder extends RecyclerView.ViewHolder {
        /**
         * 单品的控件
         */
        ImageView goods_image;
        TextView salemix;
        View itemView;
        /**
         * 套餐的控件
         */
        RecyclerView list_view;

        /**
         * 相同的控件
         */
        TextView good_name;
        AddAndSubView add_sub_view;
        TextView price;
        CheckBox goods_checkbox;

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
            //套餐的独有特性
            list_view = (RecyclerView) view.findViewById(R.id.list_view);
        }

        public void init(final Suit suit, final int position, final TowObjectParameterInterface towObjectParameterInterface) {
            final CartGoods cg = suit.goodsList.get(0);
            good_name.setText(cg.title);
            salemix.setText(cg.salemix);
            price.setText("￥" + suit.price);
            ApiClient.ImageLoadersRounde(cg.thumb, goods_image, 8);
            add_sub_view.setNum(suit.num);
            /**改变数量*/
            add_sub_view.setOnNumChangeListener(new AddAndSubView.OnNumChangeListener() {
                @Override
                public void onNumChange(View view, int bnum, int num) {
                    updateNumber(suit, num, (AddAndSubView) view, bnum, position);
                }
            });
            goods_checkbox.setChecked(suit.isCheck);
            /**选择商品*/
            goods_checkbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    towObjectParameterInterface.Onchange(HomeShoppingCartFragment.CHICKGOODS, position, goods_checkbox.isChecked());
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
            //如果是套餐则加上套餐的特性
            if (suit.suit) {
                LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
                list_view.setLayoutManager(mLayoutManager);
                list_view.addItemDecoration(new DividerGridItemDecoration(context, LinearLayoutManager.HORIZONTAL, 0, context.getResources().getColor(R.color.transparent)));
                ShoppingCartSuitGoodsAdapter adapter = new ShoppingCartSuitGoodsAdapter(context, suit.goodsList);
                list_view.setAdapter(adapter);
            }
        }
    }

    /**
     * 修改商品的数量
     *
     * @param suit 对象模型
     * @param number    改变的数量
     * @param view      显示数量的控件
     * @param num       改变前的数量
     * @param position  改变的数量的商品的位置
     */
    public void updateNumber(Suit suit, final int number, final AddAndSubView view, final int num, final int position) {
        final Map<String, Object> map = new HashMap<>();
        List<ObjArr> list = new ArrayList<>();
        ObjArr oa = new ObjArr();
        oa.id = suit.id;
        oa.num = number;
        list.add(oa);
        map.put("objArr", list);
        ApiClient.send(context, JConstant.UPDATE_CART_, map, true, null, new DataLoader<Object>() {
            @Override
            public void task(String data) {

            }

            @Override
            public void succeed(Object data) {
                if (towObjectParameterInterface != null) {
                    towObjectParameterInterface.Onchange(HomeShoppingCartFragment.CHANGENUMBER, position, number);
                }
            }

            @Override
            public void error(String message) {
                view.setNum(num);
                ToastUtils.show(context, "修改失败", ToastUtils.ERROR_TYPE);
            }
        }, JConstant.UPDATE_CART_);
    }

    public class ObjArr {
        public String id;
        public int num;
    }
}
