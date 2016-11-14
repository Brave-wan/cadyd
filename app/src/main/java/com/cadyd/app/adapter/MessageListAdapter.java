package com.cadyd.app.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.cadyd.app.R;
import com.cadyd.app.interfaces.OnItemClickListener;
import com.cadyd.app.model.MessageInfoEntry;

import java.util.List;

/**
 * Created by SCH-1 on 2016/7/28.
 */
public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.MessageViewHolder> {

    private List<MessageInfoEntry> datas;

    private OnItemClickListener listener;

    public MessageListAdapter(List<MessageInfoEntry> datas) {
        this.datas = datas;
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item_layout, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MessageViewHolder holder, int position) {
        final MessageInfoEntry entry = datas.get(position);
        holder.content.setText(entry.getText());
        holder.date.setText(entry.getCreatedata());

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

    public static class MessageViewHolder extends RecyclerView.ViewHolder {

        public TextView content;
        public TextView date;

        public MessageViewHolder(View itemView) {
            super(itemView);

            content = (TextView) itemView.findViewById(R.id.message_item_content);
            date = (TextView) itemView.findViewById(R.id.message_item_date);
        }
    }


    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
