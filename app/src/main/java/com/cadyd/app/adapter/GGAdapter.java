package com.cadyd.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.cadyd.app.R;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.model.liveDetil;

import java.util.List;

/**
 * Created by xiongmao on 2016/9/8.
 */

public class GGAdapter extends BaseAdapter {

    private Context context;
    private List<liveDetil> list;
    private LayoutInflater inflater;

    public GGAdapter(Context context, List<liveDetil> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    public void setData(List<liveDetil> list){
        this.list=list;
        notifyDataSetChanged();

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
        ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = inflater.inflate(R.layout.pl_image_view, null);
            viewHolder.imageView = (ImageView) view.findViewById(R.id.image_view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        ApiClient.displayImage(list.get(position).thumb, viewHolder.imageView);
        return view;
    }

    private class ViewHolder {
        ImageView imageView;
    }
}
