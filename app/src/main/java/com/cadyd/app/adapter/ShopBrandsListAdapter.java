package com.cadyd.app.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.cadyd.app.R;
import com.cadyd.app.interfaces.OnItemClickListener;
import com.cadyd.app.model.ShopBrandsEntry;

import java.util.List;

/**
 * Created by SCH-1 on 2016/8/3.
 */
public class ShopBrandsListAdapter extends RecyclerView.Adapter<ShopBrandsListAdapter.ShopBrandsHolder> {

    private List<ShopBrandsEntry> datas;

    private OnItemClickListener listener;

    public ShopBrandsListAdapter(List<ShopBrandsEntry> datas) {
        this.datas = datas;
    }

    @Override
    public ShopBrandsListAdapter.ShopBrandsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shop_category_list_item, parent, false);
        ShopBrandsListAdapter.ShopBrandsHolder holder = new ShopBrandsListAdapter.ShopBrandsHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ShopBrandsListAdapter.ShopBrandsHolder holder, int position) {
        final ShopBrandsEntry entry = datas.get(position);
        holder.title.setText(entry.getName());

        if (listener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    listener.onItemClick(entry, holder.itemView);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public static class ShopBrandsHolder extends RecyclerView.ViewHolder {

        public TextView title;

        public ShopBrandsHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.shop_category_list_item_name);
        }
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
