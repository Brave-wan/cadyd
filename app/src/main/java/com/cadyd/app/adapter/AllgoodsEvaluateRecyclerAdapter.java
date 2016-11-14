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

import org.wcy.android.utils.adapter.CommonRecyclerAdapter;
import org.wcy.android.utils.adapter.ViewRecyclerHolder;

import java.util.List;
import java.util.Map;

/**
 * Created by xiongmao on 2016/8/5.
 */
public class AllgoodsEvaluateRecyclerAdapter extends RecyclerView.Adapter<RlViewHolder> {

    private Activity activity;
    private LayoutInflater inflater;

    private GoodsTheSunDataModel goodsTheSunDataModel;

    private TowObjectParameterInterface towObjectParameterInterface;

    public AllgoodsEvaluateRecyclerAdapter(Activity activity, GoodsTheSunDataModel goodsTheSunDataModel) {
        this.activity = activity;
        inflater = LayoutInflater.from(activity);
        this.goodsTheSunDataModel = goodsTheSunDataModel;
    }

    public void setClick(TowObjectParameterInterface towObjectParameterInterface) {
        this.towObjectParameterInterface = towObjectParameterInterface;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        } else if (position == goodsTheSunDataModel.list.size() - 1) {
            return 2;
        } else {
            return 1;
        }
    }

    @Override
    public RlViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RlViewHolder viewHolder;
        View view;
        switch (viewType) {
            case 0:
                view = inflater.inflate(R.layout.evaluate_item_head, null);
                viewHolder = new RlViewHolder(view);
                break;
            case 2:
                view = inflater.inflate(R.layout.evaluate_item_foot, null);
                viewHolder = new RlViewHolder(view);
                break;
            default:
                view = inflater.inflate(R.layout.evaluate_item, null);
                viewHolder = new RlViewHolder(view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RlViewHolder holder, int position) {
        holder.setData(activity, goodsTheSunDataModel, position, towObjectParameterInterface);
    }

    @Override
    public int getItemCount() {
        return goodsTheSunDataModel.list == null ? 0 : goodsTheSunDataModel.list.size();
    }
}

class RlViewHolder extends RecyclerView.ViewHolder {
    private View view;

    public RlViewHolder(View itemView) {
        super(itemView);
        view = itemView;
    }

    CommonRecyclerAdapter<Map<String, ImageModel>> adapter;

    public void setData(Activity activity, final GoodsTheSunDataModel goodsTheSunDataModel, final int postion, final TowObjectParameterInterface towObjectParameterInterface) {
        List<GoodsTheSunModel> list = goodsTheSunDataModel.list;
        final GoodsTheSunModel item = list.get(postion);
        final int bigPostion = postion;
        if (postion == 0) {
            TextView orderNumber = (TextView) view.findViewById(R.id.order_number);
            TextView creat = (TextView) view.findViewById(R.id.create);
            orderNumber.setText("订单号：" + goodsTheSunDataModel.tranflowno);
            creat.setText(goodsTheSunDataModel.trantime);
        } else if (postion == list.size() - 1) {
            RatingBar service = (RatingBar) view.findViewById(R.id.service_ratinbar);
            RatingBar Logistics = (RatingBar) view.findViewById(R.id.logistics_ratinbar);
            //获取服务评价
            service.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    item.service = (int) rating;
                }
            });
            //获取物流评价
            Logistics.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    item.Logistics = (int) rating;
                }
            });

        } else {
            ImageView imageView = (ImageView) view.findViewById(R.id.image);
            TextView title = (TextView) view.findViewById(R.id.title);
            TextView number = (TextView) view.findViewById(R.id.number);
            RatingBar roomRatingBar = (RatingBar) view.findViewById(R.id.goods_ratingBar);
            final EditText content = (EditText) view.findViewById(R.id.content);
            /**设置商品的星级*/
            roomRatingBar.setNumStars(item.local_level);
            roomRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    item.local_level = (int) rating;
                }
            });
            /**设置单个商品的评论内容*/
            content.setText(item.local_content);
            content.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    item.local_content = content.getText().toString();
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });

            ApiClient.displayImage(item.thumb, imageView, R.mipmap.defaiut_on);
            title.setText(item.title);
            number.setText("X" + item.number);

            /**设置用户选择的图片*/
            if (item.bitmapList.size() == 0) {
                item.bitmapList.add(0, null);
            }
            RecyclerView imageList = (RecyclerView) view.findViewById(R.id.recyclerView);
//            RecyclerView.LayoutManager manager = new LinearLayoutManager(activity, LinearLayout.HORIZONTAL, false);
            imageList.setLayoutManager(new GridLayoutManager(activity, 4));
            adapter = new CommonRecyclerAdapter<Map<String, ImageModel>>(activity, item.bitmapList, R.layout.small_image_view) {
                @Override
                public void convert(ViewRecyclerHolder helper, Map<String, ImageModel> item) {
                    ImageView image = helper.getView(R.id.imageView_image);
                    if (position == 0) {
                        image.setBackgroundResource(R.mipmap.on_camera);
                        helper.getConvertView().setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (towObjectParameterInterface != null) {
                                    towObjectParameterInterface.Onchange(0, bigPostion, adapter);
                                }
                            }
                        });
                    } else {
                        for (String key : item.keySet()) {
                            image.setImageBitmap(item.get(key).bitmap);
                            return;
                        }
                    }
                }
            };
            imageList.setAdapter(adapter);
        }
    }
}
