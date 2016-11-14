package com.cadyd.app.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.cadyd.app.R;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.comm.MyChange;
import com.cadyd.app.interfaces.TowObjectParameterInterface;
import com.cadyd.app.model.RecordModleData;
import com.nostra13.universalimageloader.core.ImageLoader;
import org.wcy.common.utils.StringUtil;

import java.util.List;

/**
 * Created by Administrator on 2016/5/8.
 */
public class RecordAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private List<RecordModleData> list;
    private boolean isPrize;

    private TowObjectParameterInterface towObjectParameterInterface;

    public RecordAdapter(Context context, List<RecordModleData> list, boolean isPrize) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.list = list;
        this.isPrize = isPrize;
    }

    public void setClick(TowObjectParameterInterface towObjectParameterInterface) {
        this.towObjectParameterInterface = towObjectParameterInterface;
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
            view = inflater.inflate(R.layout.record_item, null);
            holder.allNumber = (TextView) view.findViewById(R.id.ui_tesco_item_allnumber);
            holder.bar = (ProgressBar) view.findViewById(R.id.ui_tesco_item_bar);
            holder.content = (TextView) view.findViewById(R.id.ui_tesco_item_content);
            holder.imageView = (ImageView) view.findViewById(R.id.ui_tesco_item_image);
            holder.number = (TextView) view.findViewById(R.id.ui_tesco_item_number);
            holder.surplusNumber = (TextView) view.findViewById(R.id.ui_tesco_item_surplusNumber);

            holder.name = (TextView) view.findViewById(R.id.name);
            holder.luck_number = (TextView) view.findViewById(R.id.luck_number);
            holder.get_Luck_Number = (Button) view.findViewById(R.id.get_luck_number);

            holder.numLayout = (RelativeLayout) view.findViewById(R.id.num_layout);
            holder.numNameLayout = (RelativeLayout) view.findViewById(R.id.num_name_layout);

            holder.get_parent_layout = (RelativeLayout) view.findViewById(R.id.get_parent_layout);
            holder.parent_layout = (LinearLayout) view.findViewById(R.id.record_parent_layout);

            holder.getName = (TextView) view.findViewById(R.id.get_name);
            holder.getNumber = (TextView) view.findViewById(R.id.get_number);
            holder.getAllNumber = (TextView) view.findViewById(R.id.get_all_number);
            holder.getTime = (TextView) view.findViewById(R.id.get_time);
            holder.theSun = (Button) view.findViewById(R.id.the_sun);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        final RecordModleData model = list.get(position);
        ApiClient.displayImage(model.mainimg, holder.imageView);
        holder.content.setText("(第" + model.seq + "期)" + model.title);

        /**
         * 是否为获奖记录
         * */
        if (isPrize) {
            holder.get_parent_layout.setVisibility(View.VISIBLE);
            holder.parent_layout.setVisibility(View.GONE);
            if (!StringUtil.hasText(model.nickname)) {
                model.nickname = "匿名";
            }

            holder.getName.setText(MyChange.ChangeTextColor("获奖者：" + model.nickname, 4, 4 + model.nickname.length(), context.getResources().getColor(R.color.blue)));
            String has = String.valueOf(model.buytims);
            holder.getNumber.setText(MyChange.ChangeTextColor("参与人次：" + has + "人次", 5, 5 + has.length(), Color.RED));
            holder.getAllNumber.setText("总需人次：" + model.price);
            if (!StringUtil.hasText(model.announcedTime)) {
                model.announcedTime = "";
            }
            holder.getTime.setText("揭晓时间：" + model.announcedTime);
            /*
            * 添加晒单跳转
            * */
            holder.theSun.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (towObjectParameterInterface != null) {
                        towObjectParameterInterface.Onchange(1, position, null);
                    }
                }
            });
        } else {
            holder.get_parent_layout.setVisibility(View.GONE);
            holder.parent_layout.setVisibility(View.VISIBLE);
            /**
             * 是否为当前进行中
             **/
            if ("".equals(model.announcedTime) || !StringUtil.hasText(model.announcedTime)) {//false
                holder.numLayout.setVisibility(View.VISIBLE);
                holder.numNameLayout.setVisibility(View.VISIBLE);
                holder.bar.setVisibility(View.VISIBLE);

                holder.name.setVisibility(View.GONE);
                holder.luck_number.setVisibility(View.GONE);
                holder.get_Luck_Number.setVisibility(View.GONE);

                holder.number.setText(model.hasparticipatetimes + "");
                holder.allNumber.setText(model.participatetimes + "");
                holder.surplusNumber.setText((model.participatetimes - model.hasparticipatetimes) + "");
                holder.bar.setMax(model.participatetimes);
                holder.bar.setProgress(Integer.valueOf(model.hasparticipatetimes));
            } else {
                holder.numLayout.setVisibility(View.GONE);
                holder.numNameLayout.setVisibility(View.GONE);
                holder.bar.setVisibility(View.GONE);

                holder.name.setVisibility(View.VISIBLE);
                holder.luck_number.setVisibility(View.VISIBLE);
                holder.get_Luck_Number.setVisibility(View.VISIBLE);

                if (model.nickname == null) {
                    model.nickname = "";
                }
                holder.name.setText(MyChange.ChangeTextColor("获奖者：" + model.nickname, 4, 4 + model.nickname.length(), context.getResources().getColor(R.color.blue)));
                if (model.luckcode == null) {
                    model.luckcode = "";
                }
                holder.luck_number.setText(MyChange.ChangeTextColor("获奖乐购码：" + model.luckcode, 6, 6 + model.luckcode.length(), Color.RED));
                holder.get_Luck_Number.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (towObjectParameterInterface != null) {
                            towObjectParameterInterface.Onchange(0, position, null);
                        }
                    }
                });
            }

        }
        return view;
    }

    private class ViewHolder {
        private ImageView imageView;
        private TextView content;
        private TextView allNumber;
        private TextView surplusNumber;
        private TextView number;
        private ProgressBar bar;

        private TextView name;
        private TextView luck_number;
        private Button get_Luck_Number;

        private RelativeLayout get_parent_layout;
        private LinearLayout parent_layout;

        private RelativeLayout numLayout;
        private RelativeLayout numNameLayout;

        private TextView getName;
        private TextView getNumber;
        private TextView getAllNumber;
        private TextView getTime;
        private Button theSun;
    }
}
