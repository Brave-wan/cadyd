package com.cadyd.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.cadyd.app.R;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.comm.ForPxTp;
import com.cadyd.app.interfaces.TowObjectParameterInterface;
import com.cadyd.app.model.GoodsTheSunData;
import com.cadyd.app.ui.view.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by Administrator on 2016/5/13.
 */
public class TheSunAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private List<GoodsTheSunData> goodsTheSunDatas;
    private TowObjectParameterInterface towObjectParameterInterface;

    public TheSunAdapter(Context context, List<GoodsTheSunData> goodsTheSunDatas) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.goodsTheSunDatas = goodsTheSunDatas;
    }

    public void setOnClick(TowObjectParameterInterface towObjectParameterInterface) {
        this.towObjectParameterInterface = towObjectParameterInterface;
    }

    @Override
    public int getCount() {
        return goodsTheSunDatas != null ? goodsTheSunDatas.size() : 0;
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
            view = inflater.inflate(R.layout.the_sun_item, null);
            holder.head = (CircleImageView) view.findViewById(R.id.the_sun_item_head);
            holder.name = (TextView) view.findViewById(R.id.the_sun_item_name);
            holder.title = (TextView) view.findViewById(R.id.the_sun_item_title);
            holder.ListImage = (LinearLayout) view.findViewById(R.id.the_sun_list_image);
            holder.time = (TextView) view.findViewById(R.id.the_sun_item_time);
            holder.share = (TextView) view.findViewById(R.id.the_sun_share);//分享
            holder.Zambia = (TextView) view.findViewById(R.id.the_sun_zambia);//点赞
            holder.comment = (TextView) view.findViewById(R.id.the_sun_comment);//评论
            holder.horizontal_line = view.findViewById(R.id.horizontal_line);
            holder.layout = (LinearLayout) view.findViewById(R.id.layout);
            holder.view = view.findViewById(R.id.view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        final GoodsTheSunData goodsTheSunData = goodsTheSunDatas.get(position);
        ApiClient.displayImage(goodsTheSunData.photo, holder.head, R.mipmap.user_default);//头像
        holder.name.setText(goodsTheSunData.nickname);

        if (goodsTheSunData.des == null || "".equals(goodsTheSunData.des)) {
            holder.title.setVisibility(View.GONE);
        } else {
            holder.title.setText(goodsTheSunData.des);
        }


        if (goodsTheSunData.imgList != null && goodsTheSunData.imgList.size() > 0) {
            holder.ListImage.removeAllViews();
            holder.ListImage.setVisibility(View.VISIBLE);
            holder.horizontal_line.setVisibility(View.VISIBLE);
            for (int i = 0; i < goodsTheSunData.imgList.size(); i++) {
                ImageView imageView = new ImageView(context);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ForPxTp.dip2px(context, 110.0f), ForPxTp.dip2px(context, 150.0f));
                params.setMargins(ForPxTp.dip2px(context, 10), 0, 0, 0);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                imageView.setLayoutParams(params);
                imageView.setTag(i);//设置标签
                ApiClient.displayImage(goodsTheSunData.imgList.get(i).path, imageView);
                //进入详情
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        towObjectParameterInterface.Onchange(3, position, v.getTag());
                    }
                });
                holder.ListImage.addView(imageView);
            }
        } else {
            holder.horizontal_line.setVisibility(View.GONE);
            holder.ListImage.setVisibility(View.GONE);
        }

        holder.time.setText(goodsTheSunData.createTime);
        holder.Zambia.setText(goodsTheSunData.praiseCount);//点赞次数
        holder.Zambia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//点赞
                if (towObjectParameterInterface != null) {
                    towObjectParameterInterface.Onchange(1, position, null);
                }
            }
        });
        holder.comment.setText(String.valueOf(goodsTheSunData.commentCount));//评论次数

        if (position == 0) {
            holder.view.setVisibility(View.GONE);
        }

        //进入所有评论界面
        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                towObjectParameterInterface.Onchange(2, position, null);
            }
        });


        return view;
    }

    private class ViewHolder {
        private CircleImageView head;
        private TextView name;
        private TextView title;
        private LinearLayout ListImage;
        private TextView time;
        private TextView share;
        private TextView Zambia;
        private TextView comment;
        private View horizontal_line;
        private View view;
        private LinearLayout layout;//最大的那个布局
    }
}
