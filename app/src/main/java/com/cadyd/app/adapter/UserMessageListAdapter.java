package com.cadyd.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cadyd.app.R;

import java.util.List;

/**
 * Created by root on 16-10-11.
 */
public class UserMessageListAdapter extends RecyclerView.Adapter<UserMessageListAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    private List<String> mDatas;
    private Context context;
    public UserMessageListAdapter(Context context, List<String> datats) {
        mInflater = LayoutInflater.from(context);
        this.context=context;
        mDatas = datats;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View arg0) {
            super(arg0);
        }
        TextView name;
        TextView time;
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    /**
     * 创建ViewHolder
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.item_messagelist_listview, null);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.name = (TextView) view.findViewById(R.id.item_messageList_name);
        viewHolder.time = (TextView) view.findViewById(R.id.item_messageList_time);
        return viewHolder;
    }

    /**
     * 设置值
     */
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        //获取屏幕信息
        viewHolder.time.setText(mDatas.get(i));
    }
}
