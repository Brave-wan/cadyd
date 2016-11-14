package com.cadyd.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cadyd.app.R;
import com.cadyd.app.model.LiveNewInfo;
import com.cadyd.app.model.SpendMoneyModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by rance on 2016/9/30.
 * 热门直播adapter
 */
public class SpendMoneyAdapter extends RecyclerView.Adapter<SpendMoneyAdapter.ViewHolder> {

    private Context mContext;
    private List<SpendMoneyModel.LiveAmountMoneyInfoListBean> listBean;

    public SpendMoneyAdapter(Context mContext, List<SpendMoneyModel.LiveAmountMoneyInfoListBean> listBean) {
        this.mContext = mContext;
        this.listBean = listBean;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_spend_money, null);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.setContent(listBean.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.ItemClickListener(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listBean == null ? 0 : listBean.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.item_spend_money)
        TextView itemSpendMoney;//花币量
        @Bind(R.id.item_spend_money_rmb)
        TextView itemSpendMoneyRmb;//钱量

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setContent(SpendMoneyModel.LiveAmountMoneyInfoListBean listBean) {
            itemSpendMoney.setText(listBean.getPetalAmount() + "");
            itemSpendMoneyRmb.setText(listBean.getAmountMoney() + "");
        }
    }

    /**
     * item点击事件
     */
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void ItemClickListener(int position);
    }
}
