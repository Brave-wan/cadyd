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
import com.cadyd.app.model.MyOrder;

import org.wcy.common.utils.NumberUtil;

import java.util.List;

/**
 * 订单商品列表
 * Created by wcy on 2016/6/7.
 */
public class MyGoodsAdapter extends RecyclerView.Adapter<MyGoodsAdapter.ViewHolder> {
    public List<MyOrder.NewDetailListEntity> goodsMlist = null;
    public Integer state;
    public int index;

    public MyGoodsAdapter(List<MyOrder.NewDetailListEntity> datas, int index, Integer state) {
        this.goodsMlist = datas;
        this.index = index;
        this.state = state;
    }

    //创建新View，被LayoutManager所调用
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.my_order_iteam_list, null);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.Gone();
        MyOrder.NewDetailListEntity entity = goodsMlist.get(position);
        /**   **/

        ApiClient.displayImage(entity.thumb, viewHolder.image_view);
        viewHolder.name.setText(entity.goodstitle);
        viewHolder.number.setText("X" + entity.number);
        viewHolder.price.setText("￥" + NumberUtil.getString(entity.goodsprice, 2));
        viewHolder.salemix.setText(entity.salemix);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.ItemClickListener(index, position);
                }
            }
        });
        /**
         * 正在施工
         * */
        if (InterFace.GoodsStatus.REFUND.ecode.equals(entity.state)) {
            viewHolder.status.setVisibility(View.VISIBLE);
            viewHolder.status.setText(InterFace.GoodsStatus.format(Integer.parseInt(entity.state)).ename);
        } else if (InterFace.OrderStatus.REFUND.ecode.equals(state)) {
            viewHolder.status.setText(InterFace.OrderStatus.REFUND.ename);
        }
    }

    //获取数据的数量
    @Override
    public int getItemCount() {
        return goodsMlist == null ? 0 : goodsMlist.size();
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView image_view;
        public TextView name;
        public TextView salemix;
        public TextView price;
        public TextView number;
        public TextView status;

        public ViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            salemix = (TextView) view.findViewById(R.id.salemix);
            price = (TextView) view.findViewById(R.id.price);
            number = (TextView) view.findViewById(R.id.number);
            status = (TextView) view.findViewById(R.id.status);//隐藏退款中的标识
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
        void ItemClickListener(int index, int position);
    }
}