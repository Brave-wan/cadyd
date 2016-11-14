package com.cadyd.app.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.cadyd.app.R;
import com.cadyd.app.model.ExpressPathInfo;

import java.util.List;

/**
 * Created by SCH-1 on 2016/8/9.
 */
public class LookLogisticsListAdapter extends BaseAdapter {

    private List<ExpressPathInfo> paths;

    public LookLogisticsListAdapter(List<ExpressPathInfo> paths) {
        this.paths = paths;
    }

    @Override
    public int getCount() {
        return paths == null ? 0 : paths.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.look_logistics_expree_list_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.up = (TextView) convertView.findViewById(R.id.look_logistics_list_item_up);
            viewHolder.down = (TextView) convertView.findViewById(R.id.look_logistics_list_item_down);
            viewHolder.time = (TextView) convertView.findViewById(R.id.look_logistics_list_item_time);
            viewHolder.path = (TextView) convertView.findViewById(R.id.look_logistics_list_item_path);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ExpressPathInfo info = paths.get(position);
        viewHolder.time.setText(info.getAcceptTime());
        viewHolder.path.setText(info.getAcceptStation());

        if (paths.size() > 1) {
            if (position == 0) {
                viewHolder.up.setVisibility(View.INVISIBLE);
                viewHolder.down.setVisibility(View.VISIBLE);
            } else if ((position + 1) < paths.size()) {
                viewHolder.up.setVisibility(View.VISIBLE);
                viewHolder.down.setVisibility(View.VISIBLE);
            } else if ((position + 1) == paths.size()) {
                viewHolder.up.setVisibility(View.VISIBLE);
                viewHolder.down.setVisibility(View.INVISIBLE);
            }
        }
        return convertView;
    }

    private class ViewHolder {
        private TextView up;
        private TextView down;
        private TextView time;
        private TextView path;
    }

}
