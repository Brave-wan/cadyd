package com.cadyd.app.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.cadyd.app.R;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.comm.MyChange;
import com.cadyd.app.interfaces.TowParameterInterface;
import com.cadyd.app.model.OneGoodsModel;

import java.util.List;

/**
 * Created by Administrator on 2016/5/8.
 */
public class RTescoAdapter extends RecyclerView.Adapter<XViewHolder> {


    private Activity context;
    private LayoutInflater inflater;
    private List<OneGoodsModel> list;
    private TowParameterInterface anInterface;

    public RTescoAdapter(Activity context, List<OneGoodsModel> list) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.list = list;
    }

    public void setClick(TowParameterInterface anInterface) {
        this.anInterface = anInterface;
    }

    @Override
    public XViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.one_yuan_tesco_item, null);
        XViewHolder viewHolder = new XViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(XViewHolder holder, int position) {
        holder.setData(context, list, position, anInterface, holder.itemView);
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }
}

class XViewHolder extends RecyclerView.ViewHolder {
    private ImageView imageView;
    private TextView content;
    private TextView allNumber;
    private TextView surplusNumber;
    private ProgressBar bar;
    private ImageView SmallimageView;
    private Button addCar;

    public XViewHolder(View view) {
        super(view);
        addCar = (Button) view.findViewById(R.id.ui_tesco_item_addcar);
        allNumber = (TextView) view.findViewById(R.id.ui_tesco_item_allnumber);
        bar = (ProgressBar) view.findViewById(R.id.ui_tesco_item_bar);
        content = (TextView) view.findViewById(R.id.ui_tesco_item_content);
        imageView = (ImageView) view.findViewById(R.id.ui_tesco_item_image);
        surplusNumber = (TextView) view.findViewById(R.id.ui_tesco_item_surplusNumber);
        SmallimageView = (ImageView) view.findViewById(R.id.ui_tesco_item_small_image);
    }

    public void setData(Activity context, List<OneGoodsModel> list, final int position, final TowParameterInterface anInterface, View view) {
        OneGoodsModel model = list.get(position);
        content.setText("(第" + model.hastimes + "期)" + model.title);
        int start;
        int end;
        StringBuffer buffer = new StringBuffer();
        buffer.append("总需:");
        start = buffer.length();
        buffer.append(model.participatetimes);
        end = buffer.length();
        buffer.append("人次");
        allNumber.setText(MyChange.ChangeTextColor(buffer.toString(), start, end, context.getResources().getColor(R.color.red)));//总需人次

        buffer = new StringBuffer();
        buffer.append("剩余:");
        start = buffer.length();
        buffer.append((Integer.valueOf(model.participatetimes) - Integer.valueOf(model.hasparticipatetimes)));
        end = buffer.length();
        buffer.append("人次");
        surplusNumber.setText(MyChange.ChangeTextColor(buffer.toString(), start, end, context.getResources().getColor(R.color.red)));//剩余人次
        //进度条
        bar.setMax(Integer.parseInt(model.participatetimes));
        bar.setProgress(Integer.valueOf(model.hasparticipatetimes));

        ApiClient.displayImage(model.mainImg, imageView);

        if ("10".equals(model.average)) {
            SmallimageView.setVisibility(View.VISIBLE);
        } else {
            SmallimageView.setVisibility(View.GONE);
        }

        //跳转详情
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                anInterface.Onchange(0, String.valueOf(position));
            }
        });

        //加入购物车
        addCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                anInterface.Onchange(1, String.valueOf(position));
            }
        });
    }
}
