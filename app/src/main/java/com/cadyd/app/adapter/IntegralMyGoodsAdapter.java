package com.cadyd.app.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.cadyd.app.R;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.handle.InterFace;
import com.cadyd.app.model.IntegralListData;
import com.cadyd.app.model.MyOrder;
import org.wcy.common.utils.NumberUtil;

import java.util.List;

/**
 * 订单商品列表
 * Created by wcy on 2016/6/7.
 */
public class IntegralMyGoodsAdapter extends RecyclerView.Adapter<IntegralMyGoodsAdapter.ViewHolder> {
    public List<IntegralListData.OrderIntegralGoods> goodsMlist = null;
    public Integer state;

    public IntegralMyGoodsAdapter(List<IntegralListData.OrderIntegralGoods> datas, Integer state) {
        this.goodsMlist = datas;
        this.state = state;
    }

    //创建新View，被LayoutManager所调用
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.integral_my_order_iteam_list, viewGroup, false);
        ViewHolder vh = new ViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.ItemClickListener();
                }
            }
        });
        return vh;
    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.Gone();
        IntegralListData.OrderIntegralGoods IntegralGoods = goodsMlist.get(position);
        /**   **/

        ApiClient.displayImage(IntegralGoods.path, viewHolder.image_view);
        viewHolder.name.setText(IntegralGoods.goodsName);
        viewHolder.number.setText("X" + IntegralGoods.number);
        StringBuffer buffer = new StringBuffer();
        buffer.append(IntegralGoods.integral);
        buffer.append("\t+\t");
        buffer.append("￥");
        buffer.append(IntegralGoods.price);
        viewHolder.price.setText(buffer);

        /**
         * 正在施工
         * */
//        if (DetailListEntity.detailList.get(0).state.equals(InterFace.GoodsStatus.REFUND.ecode)) {
//            viewHolder.status.setVisibility(View.VISIBLE);
//            viewHolder.status.setText(InterFace.GoodsStatus.format(Integer.parseInt(DetailListEntity.detailList.get(0).state)).ename);
//        } else if (state.equals(InterFace.OrderStatus.REFUND.ecode)) {
//            viewHolder.status.setText(InterFace.OrderStatus.REFUND.ename);
//        }
    }

    //获取数据的数量
    @Override
    public int getItemCount() {
        return goodsMlist.size();
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView image_view;
        public TextView name;
        public TextView price;
        public TextView number;
        public TextView status;

        public ViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            price = (TextView) view.findViewById(R.id.price);
            number = (TextView) view.findViewById(R.id.number);
            status = (TextView) view.findViewById(R.id.status);
            image_view = (ImageView) view.findViewById(R.id.image_view);

        }

        public void Gone() {
            status.setVisibility(View.GONE);
        }
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        public void ItemClickListener();
    }
}