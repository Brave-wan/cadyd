package com.cadyd.app.adapter;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.cadyd.app.R;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.comm.MyChange;
import com.cadyd.app.interfaces.TowObjectParameterInterface;
import com.cadyd.app.model.OneGoodsModel;

import java.util.List;

/**
 * Created by Administrator on 2016/5/8.
 */
public class AllGoodsGridAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private List<OneGoodsModel> list;
    private TowObjectParameterInterface towObjectParameterInterface;

    public AllGoodsGridAdapter(Context context, List<OneGoodsModel> list) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.list = list;
    }

    public void setClick(TowObjectParameterInterface towObjectParameterInterface) {
        this.towObjectParameterInterface = towObjectParameterInterface;
    }

    @Override
    public int getCount() {

        return list.size() != 0 ? list.size() : 0;
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
            view = inflater.inflate(R.layout.all_goods_content_item, null);
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
        if (model.hastimes == null) {
            model.hastimes = "";
        }
        holder.content.setText("(第" + model.hastimes + "期)" + model.title);

        SpannableStringBuilder allString = MyChange.ChangeTextColor("总需:" + model.participatetimes + "人次", 3, 3 + model.participatetimes.length(), context.getResources().getColor(R.color.red));
        holder.allNumber.setText(allString);//总需人次
        String string = (Integer.valueOf(model.participatetimes) - Integer.valueOf(model.hasparticipatetimes)) + "";
        SpannableStringBuilder surplusStr = MyChange.ChangeTextColor("剩余:" + string + "人次", 3, 3 + string.length(), context.getResources().getColor(R.color.red));
        holder.surplusNumber.setText(surplusStr);//剩余人次
        //进度条
        holder.bar.setMax(Integer.parseInt(model.participatetimes));
        holder.bar.setProgress(Integer.valueOf(model.hasparticipatetimes));

        ApiClient.displayImage(model.mainImg, holder.imageView);
        //查看详情
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                towObjectParameterInterface.Onchange(0, position, "");
            }
        });
        if ("10".equals(model.average)) {
            holder.SmallimageView.setVisibility(View.VISIBLE);
        } else {
            holder.SmallimageView.setVisibility(View.GONE);
        }
        //加入购物车
        holder.addCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                towObjectParameterInterface.Onchange(1, position, "");
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
