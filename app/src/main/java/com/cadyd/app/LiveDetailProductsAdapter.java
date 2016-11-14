package com.cadyd.app;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cadyd.app.api.ApiClient;
import com.cadyd.app.model.LivePersonalDetailIfon;
import com.cadyd.app.ui.activity.CommWebViewActivity;
import com.cadyd.app.ui.activity.GoodsDetailActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 16-10-11.
 */
public class LiveDetailProductsAdapter extends RecyclerView.Adapter<LiveDetailProductsAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    private List<String> mDatas;
    private WindowManager wm;
    private Context context;
    List<LivePersonalDetailIfon.advertListBean> advertList = new ArrayList<>();
    int width;
    private int height;

    public LiveDetailProductsAdapter(Context context, List<LivePersonalDetailIfon.advertListBean> advertList, WindowManager wm) {
        mInflater = LayoutInflater.from(context);
        this.wm = wm;
        this.context = context;
        height = wm.getDefaultDisplay().getHeight() / 8;
        width = wm.getDefaultDisplay().getWidth() / 3;
        this.advertList = advertList;
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
        return advertList.size();
    }

    /**
     * 创建ViewHolder
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.item_livedetailproducts_listview, null);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.imageView = (ImageView) view.findViewById(R.id.item_liveDetails_image);
        return viewHolder;
    }

    /**
     * 设置值
     */
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        final LivePersonalDetailIfon.advertListBean bean = advertList.get(i);
        ApiClient.displayImage(bean.getAdvertImageUrl(), viewHolder.imageView);
        //获取屏幕信息
        viewHolder.imageView.setLayoutParams(new LinearLayout.LayoutParams(width, height));
        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (bean.getAdvertType()) {
                    // 商家广告 =2
                    case 2:
                        if (bean.getOperateType() == 1) {//1=网页
                            Intent intent = new Intent(context, CommWebViewActivity.class);
                            intent.putExtra("url", bean.getLinkUrl());
                            context.startActivity(intent);
                        } else {// 2=商家店铺
                            Intent intent = new Intent(context, GoodsDetailActivity.class);
                            intent.putExtra("gid", bean.getLinkUrl());
                            context.startActivity(intent);
                        }
                        break;
                }


            }
        });
    }
}
