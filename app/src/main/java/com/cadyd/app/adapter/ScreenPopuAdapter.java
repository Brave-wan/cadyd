package com.cadyd.app.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cadyd.app.R;
import com.cadyd.app.interfaces.TowParameterInterface;
import com.cadyd.app.model.Key;

import java.util.List;

/**
 * Created by Administrator on 2016/5/25.
 */
public class ScreenPopuAdapter extends BaseAdapter {

    private int Type = 0;
    private List<Key> list;
    private Context context;
    private LayoutInflater inflater;
    private TowParameterInterface parameterInterface;

    public ScreenPopuAdapter(Context context, List<Key> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    public void setClick(TowParameterInterface parameterInterface) {
        this.parameterInterface = parameterInterface;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
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
    public View getView(final int position, View view, final ViewGroup parent) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.screen_popu_item, null);
            holder.textView = (TextView) view.findViewById(R.id.screen_popu_text);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        if ((list.get(position).end == null || "".equals(list.get(position).end)) && (list.get(position).start != null || "".equals(list.get(position).start))) {
            holder.textView.setText(list.get(position).start + "以上");
        } else if (list.get(position).start == null || "".equals(list.get(position).start)) {
            holder.textView.setText("全部");
        } else {
            holder.textView.setText(list.get(position).start + "-" + list.get(position).end);
        }

        if (Type == position) {
            Resources res = context.getResources();
            Bitmap bmp = BitmapFactory.decodeResource(res, R.mipmap.rad_gou);
            Drawable drawable = new BitmapDrawable(bmp);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            holder.textView.setCompoundDrawables(null, null, drawable, null);
            holder.textView.setTextColor(context.getResources().getColor(R.color.red));
        } else {
            holder.textView.setTextColor(context.getResources().getColor(R.color.text_primary_gray));
            holder.textView.setCompoundDrawables(null, null, null, null);
        }

        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (parameterInterface != null) {
                    String start = list.get(position).start;
                    String end = list.get(position).end;
                    if (start == null && end == null) {
                        start = "-1";
                        end = "-1";
                    } else if (start != null && end == null) {
                        end = "-1";
                    }
                    parameterInterface.Onchange(Integer.valueOf(start), end);
                    Type = position;
                }

            }
        });

        return view;
    }

    private class ViewHolder {
        private TextView textView;
    }
}
