package com.cadyd.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cadyd.app.R;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.model.LiveNewInfo;
import com.cadyd.app.utils.Util;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by rance on 2016/9/30.
 * 热门直播adapter
 */
public class LiveNewRecyclerAdapter extends RecyclerView.Adapter<LiveNewRecyclerAdapter.ViewHolder> {

    private Context mContext;
    private List<LiveNewInfo.DataBean> liveNewInfos;

    public LiveNewRecyclerAdapter(Context mContext) {
        this.mContext = mContext;
        liveNewInfos = new ArrayList<>();
    }

    public void setData(List<LiveNewInfo.DataBean> liveNewInfos) {
        this.liveNewInfos = liveNewInfos;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_live_new, null);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        ViewGroup.LayoutParams ps = holder.itemLiveNewCover.getLayoutParams();
        ps.height = (Util.getScreenWidth(mContext) - 15) / 2;
        holder.itemLiveNewCover.setLayoutParams(ps);

        LiveNewInfo.DataBean liveNewInfo = liveNewInfos.get(position);
        holder.itemLiveNewCover.setTag(position);
        ApiClient.displayImage(liveNewInfo.getConversationUrl(), holder.itemLiveNewCover, R.mipmap.defaiut_on);
        holder.itemLiveNewTitle.setText(liveNewInfo.getConversationTitle());

        switch (liveNewInfo.getLiveType()) {
            case 1:
                holder.itemLiveNewUserType.setImageResource(0);
                break;
            case 2:
                holder.itemLiveNewUserType.setImageResource(R.mipmap.live_bus_icon);
                break;
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.ItemClickListener(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return liveNewInfos == null ? 0 : liveNewInfos.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.item_live_new_cover)
        ImageView itemLiveNewCover;
        @Bind(R.id.item_live_new_title)
        TextView itemLiveNewTitle;
        @Bind(R.id.item_live_new_user_type)
        ImageView itemLiveNewUserType;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
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
