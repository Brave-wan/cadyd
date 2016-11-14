package com.cadyd.app.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.android.volley.toolbox.ImageLoader;
import com.cadyd.app.R;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.comm.MyChange;
import com.cadyd.app.model.QueryBaseRecordData;
import com.cadyd.app.ui.view.CircleImageView;
import java.util.List;

/**
 * Created by Administrator on 2016/5/11.
 */
public class OneForTwoAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<QueryBaseRecordData> queryBaseRecordDates;

    public OneForTwoAdapter(Context context, List<QueryBaseRecordData> queryBaseRecordDates) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.queryBaseRecordDates = queryBaseRecordDates;
    }

    @Override
    public int getCount() {
        return queryBaseRecordDates != null ? queryBaseRecordDates.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.one_yuan_details_item, null);
            holder.imageView = (CircleImageView) view.findViewById(R.id.image);
            holder.top_textView = (TextView) view.findViewById(R.id.top_text);
            holder.down_textView = (TextView) view.findViewById(R.id.down_text);
            holder.centerTextView = (TextView) view.findViewById(R.id.center_text);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        ApiClient.displayImage(queryBaseRecordDates.get(position).headimg, holder.imageView, R.mipmap.user_default);
        //queryBaseRecordDates.get(position).nickname
        if (queryBaseRecordDates.get(position).nickname == null) {
            queryBaseRecordDates.get(position).nickname = "";
        }
        holder.top_textView.setText(MyChange.ChangeTextColor(queryBaseRecordDates.get(position).nickname + "(" + queryBaseRecordDates.get(position).loginip + ")", 0, queryBaseRecordDates.get(position).nickname.length(), context.getResources().getColor(R.color.A111)));
        if (queryBaseRecordDates.get(position).buytims == null) {
            queryBaseRecordDates.get(position).buytims = "0";
        }
        holder.centerTextView.setText(MyChange.ChangeTextColor("参与" + queryBaseRecordDates.get(position).buytims + "人次", 2, 2 + queryBaseRecordDates.get(position).buytims.length(), Color.RED));
        if (queryBaseRecordDates.get(position).createtime == null) {
            queryBaseRecordDates.get(position).createtime = "0";
        }
        holder.down_textView.setText(queryBaseRecordDates.get(position).createtime);
        return view;
    }

    private class ViewHolder {
        private CircleImageView imageView;
        private TextView top_textView;
        private TextView centerTextView;
        private TextView down_textView;
    }
}
