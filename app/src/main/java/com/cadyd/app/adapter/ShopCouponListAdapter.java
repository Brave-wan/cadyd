package com.cadyd.app.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.cadyd.app.R;
import com.cadyd.app.interfaces.OnItemClickListener;
import com.cadyd.app.model.CouponInfo;

import java.util.List;

/**
 * Created by SCH-1 on 2016/8/9.
 */
public class ShopCouponListAdapter extends RecyclerView.Adapter<ShopCouponListAdapter.ShopCouponHolder> {

    private List<CouponInfo> datas;

    private OnItemClickListener<CouponInfo> listener;

    public void setListener(OnItemClickListener<CouponInfo> listener) {
        this.listener = listener;
    }

    public ShopCouponListAdapter(List<CouponInfo> datas) {
        this.datas = datas;
    }

    @Override
    public ShopCouponHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shop_coupon_list_item_layout, parent, false);
        return new ShopCouponHolder(view);
    }

    @Override
    public void onBindViewHolder(final ShopCouponHolder holder, int position) {
        final CouponInfo info = datas.get(position);

        holder.condtion.setText(holder.itemView.getContext().getString(R.string.shop_coupon_list_item_condtion, info.getPreCondition()));
        holder.money.setText("￥" + info.getMoney());
        holder.title.setText(info.getTitle());
        holder.validate.setText("有效期：" + info.getInvalid());

        if (info.getMoney() <= 20) {
            holder.bg.setBackgroundResource(R.mipmap.league_prefer_one);
            holder.obtain.setBackgroundResource(R.drawable.league_prefer_one_bg);
            holder.obtain.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.league_prefer_one));
        } else if (info.getMoney() > 20 && info.getMoney() <= 50) {
            holder.bg.setBackgroundResource(R.mipmap.league_prefer_two);
            holder.obtain.setBackgroundResource(R.drawable.league_prefer_two_bg);
            holder.obtain.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.league_prefer_two));
        } else if (info.getMoney() > 50 && info.getMoney() <= 100) {
            holder.bg.setBackgroundResource(R.mipmap.league_prefer_three);
            holder.obtain.setBackgroundResource(R.drawable.league_prefer_three_bg);
            holder.obtain.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.league_prefer_three));
        } else {
            holder.bg.setBackgroundResource(R.mipmap.league_prefer_four);
            holder.obtain.setBackgroundResource(R.drawable.league_prefer_four_bg);
            holder.obtain.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.league_prefer_four));
        }

        if (listener != null) {
            holder.obtain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(info, holder.itemView);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public static class ShopCouponHolder extends RecyclerView.ViewHolder {

        private TextView condtion;
        private TextView money;
        private TextView title;
        private TextView validate;
        private Button obtain;

        private LinearLayout bg;

        public ShopCouponHolder(View itemView) {
            super(itemView);

            condtion = (TextView) itemView.findViewById(R.id.shop_coupon_list_item_condition);
            money = (TextView) itemView.findViewById(R.id.shop_coupon_list_item_money);
            title = (TextView) itemView.findViewById(R.id.shop_coupon_list_item_title);
            validate = (TextView) itemView.findViewById(R.id.shop_coupon_list_item_invald);
            obtain = (Button) itemView.findViewById(R.id.shop_coupon_list_item_obtain);

            bg = (LinearLayout) itemView.findViewById(R.id.shop_coupon_list_item_bg);
        }
    }
}
