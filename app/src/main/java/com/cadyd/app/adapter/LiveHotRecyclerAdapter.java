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
import com.cadyd.app.model.LiveHotInfo;
import com.cadyd.app.utils.Util;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by rance on 2016/9/30.
 * 热门直播adapter
 */
public class LiveHotRecyclerAdapter extends RecyclerView.Adapter<LiveHotRecyclerAdapter.ViewHolder> {

    private Context mContext;
    private List<LiveHotInfo.DataBean> liveHotInfos;

    public LiveHotRecyclerAdapter(Context mContext) {
        this.mContext = mContext;
        liveHotInfos = new ArrayList<>();
    }

    public void setData(List<LiveHotInfo.DataBean> liveHotInfos) {
        this.liveHotInfos = liveHotInfos;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_live_hot, null);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        ViewGroup.LayoutParams ps = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Util.getScreenWidth(mContext) * 3/2);
        holder.itemView.setLayoutParams(ps);

        LiveHotInfo.DataBean liveHotInfo = liveHotInfos.get(position);
        holder.itemLiveHotHeader.setTag(position);
        ApiClient.displayImage(liveHotInfo.getHeadImageUrl(), holder.itemLiveHotHeader, R.mipmap.user_account);
        holder.itemLiveHotCover.setTag(position);
        ApiClient.displayImage(liveHotInfo.getConversationUrl(), holder.itemLiveHotCover, R.mipmap.defaiut_on);
        holder.itemLiveHotTitle.setText(liveHotInfo.getConversationTitle());
        holder.itemLiveHotCity.setText(liveHotInfo.getCity());
        holder.itemLiveHotNum.setText(liveHotInfo.getOnlineUserCount() + "人观看");
        switch (liveHotInfo.getLiveType()) {
            case 1:
                holder.itemLiveHotUserType.setImageResource(0);
                break;
            case 2:
                holder.itemLiveHotUserType.setImageResource(R.mipmap.live_bus_icon);
                break;
        }
        switch (liveHotInfo.getFocusStatus()) {
            case 1:
                holder.itemLiveHotFollow.setBackgroundResource(R.mipmap.button_list_yiguanzhu);
                holder.itemLiveHotFollow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onFollowClickListener.unFollowClickListener(position, view);
                    }
                });
                break;
            case 2:
                holder.itemLiveHotFollow.setBackgroundResource(R.mipmap.button_list_guanzhu);
                holder.itemLiveHotFollow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onFollowClickListener.FollowClickListener(position, view);
                    }
                });
                break;
            case 3:
                holder.itemLiveHotFollow.setBackgroundResource(R.mipmap.button_list_guanzhu);
                holder.itemLiveHotFollow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onFollowClickListener.goLoginClickListener();
                    }
                });
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
        return liveHotInfos == null ? 0 : liveHotInfos.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.item_live_hot_header)
        ImageView itemLiveHotHeader;
        @Bind(R.id.item_live_hot_title)
        TextView itemLiveHotTitle;
        @Bind(R.id.item_live_hot_city)
        TextView itemLiveHotCity;
        @Bind(R.id.item_live_hot_num)
        TextView itemLiveHotNum;
        @Bind(R.id.item_live_hot_follow)
        TextView itemLiveHotFollow;
        @Bind(R.id.item_live_hot_cover)
        ImageView itemLiveHotCover;
        @Bind(R.id.item_live_hot_user_type)
        ImageView itemLiveHotUserType;


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

    /**
     * 点击关注按钮监听
     */
    private OnFollowClickListener onFollowClickListener;

    public void setOnFollowClickListener(OnFollowClickListener onFollowClickListener) {
        this.onFollowClickListener = onFollowClickListener;
    }

    public interface OnFollowClickListener {
        // focusStatus状态为2  关注回调
        void FollowClickListener(int position, View view);

        // focusStatus状态为1  取消关注回调
        void unFollowClickListener(int position, View view);

        // focusStatus状态为3  提醒登录回调
        void goLoginClickListener();
    }
}
