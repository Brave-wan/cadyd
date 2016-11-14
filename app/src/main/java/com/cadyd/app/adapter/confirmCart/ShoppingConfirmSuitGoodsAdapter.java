package com.cadyd.app.adapter.confirmCart;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.cadyd.app.R;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.model.CartGoods;
import org.wcy.common.utils.NumberUtil;

import java.util.List;

/**
 * 购物车，商户下面的商品信息
 * Created by wcy on 2016/5/8.
 */
public class ShoppingConfirmSuitGoodsAdapter extends RecyclerView.Adapter<ShoppingConfirmSuitGoodsAdapter.ViewHolder> {
    private Context context;
    private List<CartGoods> mlist;

    public ShoppingConfirmSuitGoodsAdapter(Context context, List<CartGoods> list) {
        this.context = context;
        mlist = list;
    }

    //创建新View，被LayoutManager所调用
    @Override
    public ShoppingConfirmSuitGoodsAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.shopping_confirm_list_iteam_suit, viewGroup, false);
        ShoppingConfirmSuitGoodsAdapter.ViewHolder vh = new ShoppingConfirmSuitGoodsAdapter.ViewHolder(view);
        return vh;
    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(final ShoppingConfirmSuitGoodsAdapter.ViewHolder viewHolder, final int position) {
        CartGoods cg = mlist.get(position);
        viewHolder.init(cg);
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
            view.setBackgroundResource(R.color.transparent);
            goods_image = (ImageView) view.findViewById(R.id.goods_image);
            good_name = (TextView) view.findViewById(R.id.good_name);
            salemix = (TextView) view.findViewById(R.id.salemix);
            price = (TextView) view.findViewById(R.id.price);
        }

        public void init(CartGoods cg) {
            good_name.setText(cg.title);
            salemix.setText(cg.salemix);
            price.setText("￥" + NumberUtil.getString(cg.price, 2));
            ApiClient.ImageLoadersRounde(cg.thumb, goods_image, 8);
        }
    }

}
