package com.cadyd.app.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.cadyd.app.R;
import com.cadyd.app.model.MyOrder;

import java.util.List;

/**
 * 订单套餐商品列表
 */
public class MyGoodsPackageAdapter extends RecyclerView.Adapter<MyGoodsPackageAdapter.ViewHolder> {
    public List<MyOrder.DetailListEntity> goodsMlist = null;
    public Integer state;
    private Context context;
    private MyGoodsAdapter.OnItemClickListener onItemClickListener;
    private boolean type;//用于判断是从订单列表还是从订单详情进入的 false列表 true详情

    public MyGoodsPackageAdapter(Context context, List<MyOrder.DetailListEntity> datas, Integer state, boolean type) {
        this.goodsMlist = datas;
        this.state = state;
        this.context = context;
        this.type = type;
    }

    //创建新View，被LayoutManager所调用
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.package_list_item, null);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.setData(position);
    }

    //获取数据的数量
    @Override
    public int getItemCount() {
        return goodsMlist == null ? 0 : goodsMlist.size();
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public class ViewHolder extends RecyclerView.ViewHolder {
        public RecyclerView recyclerView;

        public ViewHolder(View view) {
            super(view);
            recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        }

        public void setData(int position) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            if (type) {
                MyGoodsAdapter adapter = new MyGoodsAdapter(goodsMlist.get(position).orderDetail, position, state);
                adapter.setOnItemClickListener(new MyGoodsAdapter.OnItemClickListener() {
                    @Override
                    public void ItemClickListener(int index, int position) {
                        if (onItemClickListener != null) {
                            onItemClickListener.ItemClickListener(index, position);
                        }
                    }
                });
                recyclerView.setAdapter(adapter);
            } else {
                MyGoodsAdapter adapter = new MyGoodsAdapter(goodsMlist.get(position).orderDetail, position, state);
                adapter.setOnItemClickListener(new MyGoodsAdapter.OnItemClickListener() {
                    @Override
                    public void ItemClickListener(int index, int position) {
                        if (onItemClickListener != null) {
                            onItemClickListener.ItemClickListener(index, position);
                        }
                    }
                });
                recyclerView.setAdapter(adapter);
            }

        }


    }

    public void setOnItemClickListener(MyGoodsAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}