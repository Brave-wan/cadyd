package com.cadyd.app.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.cadyd.app.R;
import com.cadyd.app.interfaces.OneParameterInterface;
import com.cadyd.app.model.QueryClassIficationCategorylist;

import java.util.List;

/**
 * Created by Administrator on 2016/5/9.
 */
public class TextAdapter extends BaseAdapter {

    private List<QueryClassIficationCategorylist> list;
    private Context context;
    private LayoutInflater inflater;

    private OneParameterInterface oneParameterInterface;
    public int type = 0;

    public TextAdapter(Context context, List<QueryClassIficationCategorylist> list, int type) {
        inflater = LayoutInflater.from(context);
        this.list = list;
        this.context = context;

        if (type == 10) {
            for (int i = 0; i < list.size(); i++) {
                if ("十元专区".equals(list.get(i).name)) {
                    this.type = i;
                    break;
                }
            }
        }

    }

    public void SetClickListener(OneParameterInterface oneParameterInterface) {
        this.oneParameterInterface = oneParameterInterface;
    }

    @Override
    public int getCount() {
        return list != null ? list.size() : 0;
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
    public View getView(final int position, View view, ViewGroup parent) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.list_text_item, null);
            holder.text = (TextView) view.findViewById(R.id.list_item_text);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.text.setText(list.get(position).name);
        if (type == position) {
            view.setBackgroundResource(android.R.color.white);
            holder.text.setTextColor(Color.RED);
        } else {
            view.setBackgroundResource(android.R.color.transparent);
            holder.text.setTextColor(context.getResources().getColor(R.color.text_primary_gray));
        }
        holder.text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = position;
                if (oneParameterInterface != null) {
                    oneParameterInterface.Onchange(type);
                }
                notifyDataSetChanged();
            }
        });
        return view;
    }

    private class ViewHolder {
        private TextView text;
    }
}
