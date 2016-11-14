package com.cadyd.app.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.cadyd.app.R;
import com.cadyd.app.model.Flip;

import java.util.List;

/**
 * Created by wcy on 2016/5/6.
 */
public class FlipViewAdapter extends BaseAdapter {
    Activity mContext;
    List<Flip> mlist;
    private float height;

    public FlipViewAdapter(List<Flip> paramList, Activity paramContext) {
        this.mlist = paramList;
        this.mContext = paramContext;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return Integer.MAX_VALUE;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(this.mContext).inflate(
                    R.layout.flip_view, null);
            viewHolder.typeOne = (TextView) convertView.findViewById(R.id.typeOne);
            viewHolder.typeTwo = (TextView) convertView.findViewById(R.id.typeTwo);
            viewHolder.nameOne = (TextView) convertView.findViewById(R.id.nameOne);
            viewHolder.nameTwo = (TextView) convertView.findViewById(R.id.nameTwo);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final int index = position % mlist.size();// 计算当前显示的itemview
        Flip f = mlist.get(index);
        viewHolder.typeOne.setText(f.typeOne);
        viewHolder.typeTwo.setText(f.typeTwo);
        viewHolder.nameOne.setText(f.nameOne);
        viewHolder.nameTwo.setText(f.nameTwo);
        height = convertView.getHeight();
        return convertView;
    }


    public class ViewHolder {
        public TextView typeOne;
        public TextView nameOne;
        public TextView typeTwo;
        public TextView nameTwo;
    }
}
