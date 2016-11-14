package com.cadyd.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cadyd.app.R;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.model.ProductsFeaturedInfo;
import com.cadyd.app.model.SendDataMsgInfo;
import com.cadyd.app.ui.activity.GoodsDetailActivity;
import com.ypy.eventbus.EventBus;

import java.util.List;

/**
 * Created by root on 16-10-11.
 */
public class ProductsFeaturedAdapter extends RecyclerView.Adapter<ProductsFeaturedAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    private List<ProductsFeaturedInfo.ProductsFeatBean> mDatas;
    private WindowManager wm;
    private Context context;
    int width;

    public ProductsFeaturedAdapter(Context context, List<ProductsFeaturedInfo.ProductsFeatBean> datats, WindowManager wm) {
        mInflater = LayoutInflater.from(context);
        this.wm = wm;
        this.context = context;
        width = wm.getDefaultDisplay().getWidth() / 3;
        mDatas = datats;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View arg0) {
            super(arg0);
        }

        ImageView imageView;
        TextView name;
        TextView price;
    }


    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    /**
     * 创建ViewHolder
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.item_productsfeatured_listview, null);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.imageView = (ImageView) view.findViewById(R.id.item_prudect_image);
        viewHolder.name = (TextView) view.findViewById(R.id.item_prudect_name);
        viewHolder.price = (TextView) view.findViewById(R.id.item_prudect_price);
        final ProductsFeaturedInfo.ProductsFeatBean buyWatchingInfo = mDatas.get(i);
        viewHolder.name.setText(buyWatchingInfo.getProductName());
        viewHolder.price.setText("￥" + buyWatchingInfo.getCashPrice());
        ApiClient.displayImage("http://www.everrich-group.com/Upload/fcd42f03-236f-4211-93b4-d1b31fee2fb8/TC/01.jpg", viewHolder.imageView, R.mipmap.defaiut_on);
        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, GoodsDetailActivity.class);
                intent.putExtra("gid", buyWatchingInfo.getProductId());
                context.startActivity(intent);
                EventBus.getDefault().post(new SendDataMsgInfo());
            }
        });
        return viewHolder;
    }

    /**
     * 设置值
     */
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        //获取屏幕信息
        viewHolder.imageView.setLayoutParams(new RelativeLayout.LayoutParams(width, 200));
    }
}
