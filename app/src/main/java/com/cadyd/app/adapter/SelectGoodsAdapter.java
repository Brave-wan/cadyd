package com.cadyd.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cadyd.app.JConstant;
import com.cadyd.app.R;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.model.SelectGoodsModleData;
import com.cadyd.app.ui.activity.CommonActivity;
import com.cadyd.app.ui.activity.GoodsDetailActivity;
import com.cadyd.app.ui.activity.ShopHomeActivity;
import com.cadyd.app.ui.view.AutoLoadRecyclerView;
import com.cadyd.app.ui.view.toast.ToastUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.wcy.android.utils.adapter.CommonRecyclerAdapter;
import org.wcy.android.utils.adapter.ViewRecyclerHolder;
import org.wcy.common.utils.NumberUtil;
import org.wcy.common.utils.StringUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2016/5/25.
 */
public class SelectGoodsAdapter extends BaseAdapter {

    private List<SelectGoodsModleData> list;
    private Context context;
    private LayoutInflater inflater;

    public SelectGoodsAdapter(Context context, List<SelectGoodsModleData> list, String name) {
        this.context = context;
        this.list = list;
        this.name = name;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
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
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.goods_select_item, null);
            holder.imageView = (ImageView) view.findViewById(R.id.select_image);
            holder.title = (TextView) view.findViewById(R.id.select_title);
            holder.NewMoney = (TextView) view.findViewById(R.id.select_new_money);
            holder.oldMoney = (TextView) view.findViewById(R.id.select_old_money);
            holder.goodProportion = (TextView) view.findViewById(R.id.good_proportion);
            holder.numberOfPraise = (TextView) view.findViewById(R.id.number_of_praise);
            holder.goods_layout = (LinearLayout) view.findViewById(R.id.goods_layout);
            /**店铺控件*/
            holder.shop_imageView = (ImageView) view.findViewById(R.id.select_shop_image);
            holder.shop_title = (TextView) view.findViewById(R.id.select_shop_title);
            holder.shop_content = (TextView) view.findViewById(R.id.shop_proportion);
            holder.go_to_shopping = (TextView) view.findViewById(R.id.go_to_shopping);
            holder.shop_image_recycler = (RecyclerView) view.findViewById(R.id.shop_image_recycler);
            holder.shop_layout = (LinearLayout) view.findViewById(R.id.shop_layout);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        final SelectGoodsModleData data = list.get(position);
        if (PRE.equals(name)) {
            holder.goods_layout.setVisibility(View.VISIBLE);
            holder.shop_layout.setVisibility(View.GONE);
            ApiClient.displayImage(data.thumb, holder.imageView, R.mipmap.defaiut_on);
            holder.title.setText(data.title);
            holder.NewMoney.setText("￥" + NumberUtil.getString(data.price, 2));
            holder.oldMoney.setText("￥" + NumberUtil.getString(data.original, 2));
            holder.goodProportion.setText("好评：" + data.rate);
            holder.numberOfPraise.setText(data.evalnum + " 人");
            holder.oldMoney.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (data != null && StringUtil.hasText(data.id)) {
                        Intent intent = new Intent(context, GoodsDetailActivity.class);
                        intent.putExtra("gid", data.id);
                        context.startActivity(intent);
                    } else {
                        ToastUtils.show(context, "数据异常", ToastUtils.ERROR_TYPE);
                    }
                }
            });
        } else if (SHOP.equals(name)) {
            holder.goods_layout.setVisibility(View.GONE);
            holder.shop_layout.setVisibility(View.VISIBLE);
            ApiClient.displayImage(data.logo, holder.shop_imageView, R.mipmap.defaiut_on);
            holder.shop_title.setText(data.name);
            holder.shop_content.setText("销量" + data.salenum + "  共" + data.goodsnum + "件宝贝");
            holder.go_to_shopping.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ShopHomeActivity.class);
                    intent.putExtra("shopId", data.shopId);
                    context.startActivity(intent);
                }
            });
            LinearLayoutManager layoutManager = new LinearLayoutManager(context);
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            holder.shop_image_recycler.setLayoutManager(layoutManager);
            String[] stringList;
            if (data.goodsthumbs == null) {
                stringList = new String[0];
            } else {
                stringList = data.goodsthumbs.split(",");
            }
            List<String> ImageList = new ArrayList<>();
            Collections.addAll(ImageList, stringList);

            CommonRecyclerAdapter<String> adapter = new CommonRecyclerAdapter<String>(context, ImageList, R.layout.search_image_view) {
                @Override
                public void convert(ViewRecyclerHolder helper, String item) {
                    ImageView imageView = helper.getView(R.id.imageView_image);
                    ApiClient.displayImage(item, imageView, R.mipmap.defaiut_on);
                }
            };
            holder.shop_image_recycler.setAdapter(adapter);
        }
        return view;
    }

    private class ViewHolder {
        private ImageView imageView;
        private TextView title;
        private TextView NewMoney;
        private TextView oldMoney;
        private TextView goodProportion;//好评比例
        private TextView numberOfPraise;//好评人数
        private LinearLayout goods_layout;
        /**
         * 店铺列表控件
         */
        private ImageView shop_imageView;
        private TextView shop_title;
        private TextView shop_content;
        private TextView go_to_shopping;//这是一个按钮
        private LinearLayout shop_layout;
        private RecyclerView shop_image_recycler;
    }

    private String PRE = "pre";//宝贝
    private String SHOP = "shop";//店铺
    private String name = "pre";

    public void notifyDataSetChanged(String name) {
        this.name = name;
        notifyDataSetChanged();
    }
}
