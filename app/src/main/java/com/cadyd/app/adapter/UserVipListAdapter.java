package com.cadyd.app.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.cadyd.app.R;
import com.cadyd.app.interfaces.OnItemClickListener;
import com.cadyd.app.model.IntegeralListInfo;
import com.cadyd.app.model.IntegralDetailInfo;

import java.util.List;

/**
 * Created by yaoye on 2016-08-06.
 */
public class UserVipListAdapter extends RecyclerView.Adapter<UserVipListAdapter.UserVipHolder> {

    private List<IntegeralListInfo> datas;

    private OnItemClickListener clickListener;

    public UserVipListAdapter(List<IntegeralListInfo> datas) {
        this.datas = datas;
    }

    @Override
    public UserVipHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.user_vip_list_item_layout, parent, false);
        return new UserVipHolder(view);
    }

    @Override
    public void onBindViewHolder(final UserVipHolder holder, int position) {
        IntegeralListInfo info = datas.get(position);
        holder.title.setText(info.getDate());

        createChildView(holder.detail, info.getDetailIntegral());

        if (clickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onItemClick("", holder.itemView);
                }
            });
        }
    }

    private void createChildView(LinearLayout detail, List<IntegralDetailInfo> detailIntegral) {
        if (detailIntegral == null || detailIntegral.size() == 0) {
            return;
        }

        for (IntegralDetailInfo info : detailIntegral) {
            View childView = LayoutInflater.from(detail.getContext()).inflate(R.layout.user_vip_list_item_child_layout, null);
            TextView keyView = (TextView) childView.findViewById(R.id.user_vip_list_item_child_key);
            TextView valueView = (TextView) childView.findViewById(R.id.user_vip_list_item_child_value);

            keyView.setText(info.getDetail());
            valueView.setText(detail.getContext().getString(R.string.user_vip_list_item_integral, info.getContent()));

            detail.addView(childView);
        }
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public static class UserVipHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private LinearLayout detail;

        public UserVipHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.user_vip_list_item_title);
            detail = (LinearLayout) itemView.findViewById(R.id.user_vip_list_item_detail);
        }
    }

    public void setClickListener(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

}
