package com.cadyd.app.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.cadyd.app.R;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.interfaces.TowObjectParameterInterface;
import com.cadyd.app.model.GoodsTheSun;
import com.cadyd.app.model.GoodsTheSunDataModel;
import com.cadyd.app.model.GoodsTheSunModel;
import com.cadyd.app.model.ImageModel;
import com.cadyd.app.model.LookEvaluateModel;

import org.wcy.android.utils.LogUtil;
import org.wcy.android.utils.adapter.CommonRecyclerAdapter;
import org.wcy.android.utils.adapter.ViewRecyclerHolder;

import java.util.List;
import java.util.Map;

/**
 * Created by xiongmao on 2016/8/5.
 */
public class LookEvaluateRecyclerAdapter extends RecyclerView.Adapter<LookViewHolder> {

    private Activity activity;
    private LayoutInflater inflater;

    private LookEvaluateModel models;


    public LookEvaluateRecyclerAdapter(Activity activity, LookEvaluateModel models) {
        this.activity = activity;
        inflater = LayoutInflater.from(activity);
        this.models = models;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        } else if (position == models.itme.size() - 1) {
            return 2;
        } else {
            return 1;
        }
    }

    @Override
    public LookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LookViewHolder viewHolder;
        View view;
        switch (viewType) {
            case 0:
                view = inflater.inflate(R.layout.evaluate_item_head, null);
                viewHolder = new LookViewHolder(view);
                break;
            case 2:
                view = inflater.inflate(R.layout.evaluate_item_foot, null);
                viewHolder = new LookViewHolder(view);
                break;
            default:
                view = inflater.inflate(R.layout.evaluate_item, null);
                viewHolder = new LookViewHolder(view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(LookViewHolder holder, int position) {
        holder.setData(activity, models, position);
    }

    @Override
    public int getItemCount() {
        return models == null ? 0 : models.itme.size();
    }
}

class LookViewHolder extends RecyclerView.ViewHolder {
    private View view;

    public LookViewHolder(View itemView) {
        super(itemView);
        view = itemView;
    }

    CommonRecyclerAdapter<LookEvaluateModel.ItmeBean.SchFileListBean> adapter;

    public void setData(Activity activity, LookEvaluateModel models, int postion) {
        LookEvaluateModel.ItmeBean item = models.itme.get(postion);
        if (postion == 0) {//设置订单头部
            TextView orderNumber = (TextView) view.findViewById(R.id.order_number);
            TextView creat = (TextView) view.findViewById(R.id.create);
            if (models.itme.get(1) != null) {
                orderNumber.setText("订单号：" + models.itme.get(1).commentGoods.odid);
                creat.setText(models.itme.get(1).commentGoods.created);
            }
        } else if (postion == models.itme.size() - 1) {//设置星级
            RatingBar service = (RatingBar) view.findViewById(R.id.service_ratinbar);
            RatingBar Logistics = (RatingBar) view.findViewById(R.id.logistics_ratinbar);
            service.setRating(models.serviceLevel);
            Logistics.setRating(models.logisticsLevel);
            service.setEnabled(false);
            Logistics.setEnabled(false);

        } else {//设置商品
            ImageView imageView = (ImageView) view.findViewById(R.id.image);
            TextView title = (TextView) view.findViewById(R.id.title);
            TextView number = (TextView) view.findViewById(R.id.number);
            RatingBar roomRatingBar = (RatingBar) view.findViewById(R.id.goods_ratingBar);
            EditText content = (EditText) view.findViewById(R.id.content);
            content.setEnabled(false);
            ApiClient.displayImage(item.orderDetail.thumb, imageView);
            title.setText(item.orderDetail.goodstitle);
            number.setText(item.orderDetail.number + "");
            roomRatingBar.setRating(item.orderDetail.state);
            roomRatingBar.setEnabled(false);
            content.setText(item.commentGoods.content);

            /**设置横向图片列表*/
            RecyclerView imageList = (RecyclerView) view.findViewById(R.id.recyclerView);
            imageList.setLayoutManager(new GridLayoutManager(activity, 4));
            adapter = new CommonRecyclerAdapter<LookEvaluateModel.ItmeBean.SchFileListBean>(activity, item.schFileList, R.layout.small_image_view) {
                @Override
                public void convert(ViewRecyclerHolder helper, LookEvaluateModel.ItmeBean.SchFileListBean item) {
                    ImageView imageView1 = helper.getView(R.id.imageView_image);
                    ApiClient.displayImage(item.thumb, imageView1);
                }
            };
            imageList.setAdapter(adapter);
        }
    }
}
