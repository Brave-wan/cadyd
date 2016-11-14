package com.cadyd.app.adapter;

import android.content.Context;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import com.cadyd.app.R;
import com.cadyd.app.ui.view.ToastView;

import java.util.List;

/**
 * Created by Administrator on 2016/5/19.
 */
public class OneTextAdapter extends BaseAdapter {

    private List<String> lists;
    private LayoutInflater inflater;
    private Context context;
    private EditText editText;
    private int type = -1;
    private String money;

    public OneTextAdapter(Context context, List<String> lists) {
        this.lists = lists;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }


    public String getNumber() {
        if (type == -1) {
            money = "-1";
        } else if (type == 0) {
            money = editText.getText().toString();
        } else {
            money = lists.get(type);
        }
        if (money == null || "".equals(money)) {
            money = "0";
        }
        return money;
    }

    @Override
    public int getCount() {
        return lists == null ? 0 : lists.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private ViewHolder holder;

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.big_text_view, null);
            holder.editText = (EditText) view.findViewById(R.id.text_view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        if (type == position) {
            holder.editText.setBackgroundResource(R.drawable.square_rad_transparent);
            holder.editText.setTextColor(context.getResources().getColor(R.color.red));
        } else {
            holder.editText.setBackgroundResource(R.drawable.square_gray_transparent);
            holder.editText.setTextColor(context.getResources().getColor(R.color.radio_nor));
        }

        if (position == 0) {
            if (editText == null) {
                editText = holder.editText;
            }
            holder.editText.setFocusable(true);
            holder.editText.setHint(lists.get(position));
            holder.editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            holder.editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    type = position;
                    notifyDataSetChanged();
                }
            });
        } else {
            holder.editText.setText(lists.get(position));
            holder.editText.setFocusable(false);
        }
        holder.editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = position;
                notifyDataSetChanged();
            }
        });


        return view;
    }

    private class ViewHolder {
        private EditText editText;
    }
}
