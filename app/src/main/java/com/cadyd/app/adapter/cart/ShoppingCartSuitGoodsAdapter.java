package com.cadyd.app.adapter.cart;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cadyd.app.R;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.model.CartGoods;
import com.cadyd.app.ui.activity.GoodsDetailActivity;

import org.wcy.common.utils.NumberUtil;

import java.util.List;

/**
 * 购物车，商户下面的商品信息
 * Created by wcy on 2016/5/8.
 */
public class ShoppingCartSuitGoodsAdapter extends RecyclerView.Adapter<ShoppingCartSuitGoodsAdapter.ViewHolder> {
    private Context context;
    private List<CartGoods> mlist;

    public ShoppingCartSuitGoodsAdapter(Context context, List<CartGoods> list) {
        this.context = context;
        mlist = list;
    }

    //创建新View，被LayoutManager所调用
    @Override
    public ShoppingCartSuitGoodsAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.shopping_cart_list_iteam_suit, viewGroup, false);
        ShoppingCartSuitGoodsAdapter.ViewHolder vh = new ShoppingCartSuitGoodsAdapter.ViewHolder(view);
        return vh;
    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(final ShoppingCartSuitGoodsAdapter.ViewHolder viewHolder, final int position) {
        final CartGoods cg = mlist.get(position);
        viewHolder.good_name.setText(cg.title);
        viewHolder.salemix.setText(cg.salemix);
        viewHolder.price.setText("￥" + NumberUtil.getString(cg.price, 2));
        ApiClient.ImageLoadersRounde(cg.thumb, viewHolder.goods_image, 8);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, GoodsDetailActivity.class);
                intent.putExtra("gid", cg.goodsid);
                context.startActivity(intent);
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
        TextView price;

        public ViewHolder(View view) {
            super(view);
            goods_image = (ImageView) view.findViewById(R.id.goods_image);
            good_name = (TextView) view.findViewById(R.id.good_name);
            salemix = (TextView) view.findViewById(R.id.salemix);
            price = (TextView) view.findViewById(R.id.price);
        }
    }

}
