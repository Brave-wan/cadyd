package com.cadyd.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.cadyd.app.R;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.comm.IsVisual;
import com.cadyd.app.comm.MyChange;
import com.cadyd.app.interfaces.TowParameterInterface;
import com.cadyd.app.model.OneGoodsModel;

import java.util.List;

/**
 * Created by Administrator on 2016/5/8.
 */
public class TescoAdapter extends BaseAdapter {

    private Activity context;
    private LayoutInflater inflater;
    private List<OneGoodsModel> list;
    private TowParameterInterface anInterface;

    public TescoAdapter(Activity context, List<OneGoodsModel> list) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.list = list;
    }


    public void setClick(TowParameterInterface anInterface) {
        this.anInterface = anInterface;
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
            view = inflater.inflate(R.layout.one_yuan_tesco_item, null);
            holder.addCar = (Button) view.findViewById(R.id.ui_tesco_item_addcar);
            holder.allNumber = (TextView) view.findViewById(R.id.ui_tesco_item_allnumber);
            holder.bar = (ProgressBar) view.findViewById(R.id.ui_tesco_item_bar);
            holder.content = (TextView) view.findViewById(R.id.ui_tesco_item_content);
            holder.imageView = (ImageView) view.findViewById(R.id.ui_tesco_item_image);
            holder.surplusNumber = (TextView) view.findViewById(R.id.ui_tesco_item_surplusNumber);
            holder.SmallimageView = (ImageView) view.findViewById(R.id.ui_tesco_item_small_image);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        OneGoodsModel model = list.get(position);
        holder.content.setText("(第" + model.hastimes + "期)" + model.title);
        int start;
        int end;
        StringBuffer buffer = new StringBuffer();
        buffer.append("总需:");
        start = buffer.length();
        buffer.append(model.participatetimes);
        end = buffer.length();
        buffer.append("人次");
        holder.allNumber.setText(MyChange.ChangeTextColor(buffer.toString(), start, end, context.getResources().getColor(R.color.red)));//总需人次

        buffer = new StringBuffer();
        buffer.append("剩余:");
        start = buffer.length();
        buffer.append((Integer.valueOf(model.participatetimes) - Integer.valueOf(model.hasparticipatetimes)));
        end = buffer.length();
        buffer.append("人次");
        holder.surplusNumber.setText(MyChange.ChangeTextColor(buffer.toString(), start, end, context.getResources().getColor(R.color.red)));//剩余人次
        //进度条
        holder.bar.setMax(Integer.parseInt(model.participatetimes));
        holder.bar.setProgress(Integer.valueOf(model.hasparticipatetimes));

        ApiClient.displayImage(model.mainImg, holder.imageView);

        if ("10".equals(model.average)) {
            holder.SmallimageView.setVisibility(View.VISIBLE);
        } else {
            holder.SmallimageView.setVisibility(View.GONE);
        }

        //跳转详情
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                anInterface.Onchange(0, String.valueOf(position));
            }
        });

        //加入购物车
        holder.addCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                anInterface.Onchange(1, String.valueOf(position));
            }
        });

        return view;
    }

    private class ViewHolder {
        private ImageView imageView;
        private TextView content;
        private TextView allNumber;
        private TextView surplusNumber;
        private ProgressBar bar;
        private ImageView SmallimageView;
        private Button addCar;
    }
}
