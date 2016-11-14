package com.cadyd.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import com.cadyd.app.R;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.interfaces.TowObjectParameterInterface;
import com.cadyd.app.model.ShoppingCollectionData;
import com.cadyd.app.model.ShoppingCollectionShopData;
import com.cadyd.app.ui.activity.GoodsDetailActivity;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by Administrator on 2016/5/27.
 */
public class UserShoppingCollectionAdapter extends BaseAdapter {

    private Boolean TYPE = false;

    private List<ShoppingCollectionData> collectionDatas;
    private List<ShoppingCollectionShopData> collectionShopDatas;
    private Context context;
    private LayoutInflater inflater;
    private boolean isCheckShow = false;
    private TowObjectParameterInterface parameterInterface;

    public UserShoppingCollectionAdapter(Context context, List<ShoppingCollectionData> collectionDatas) {
        this.context = context;
        this.collectionDatas = collectionDatas;
        inflater = LayoutInflater.from(context);
    }


    public UserShoppingCollectionAdapter(Context context, List<ShoppingCollectionShopData> collectionShopDatas, String str) {
        this.context = context;
        this.collectionShopDatas = collectionShopDatas;
        inflater = LayoutInflater.from(context);
        TYPE = true;
    }


    public void isCheckShow(Boolean bool) {
        this.isCheckShow = bool;
        notifyDataSetChanged();
    }

    public Boolean isCheckShow() {
        return this.isCheckShow;
    }

    public void setClick(TowObjectParameterInterface parameterInterface) {
        this.parameterInterface = parameterInterface;
    }


    @Override
    public int getCount() {
        int count = 0;
        if (TYPE) {
            count = collectionShopDatas == null ? 0 : collectionShopDatas.size();
        } else {
            count = collectionDatas == null ? 0 : collectionDatas.size();
        }
        return count;
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
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.user_shopping_collection_item, null);
            holder.imageView = (ImageView) view.findViewById(R.id.user_shopping_collection_image);
            holder.title = (TextView) view.findViewById(R.id.user_shopping_collection_title);
            holder.money = (TextView) view.findViewById(R.id.user_shopping_collection_money);
            holder.checkBox = (CheckBox) view.findViewById(R.id.user_shopping_collection_check);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        if (isCheckShow) {
            holder.checkBox.setVisibility(View.VISIBLE);
        } else {
            holder.checkBox.setVisibility(View.GONE);
        }
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (parameterInterface != null) {
                    parameterInterface.Onchange(0, position, holder.checkBox.isChecked());
                }
            }
        });

        if (TYPE) {
            final ShoppingCollectionShopData collectionShopDatas1 = collectionShopDatas.get(position);
            holder.checkBox.setChecked(collectionShopDatas1.checked);
            ApiClient.displayImage(collectionShopDatas1.logo, holder.imageView);
            holder.title.setText(collectionShopDatas1.name);
            holder.money.setVisibility(View.GONE);
        } else {
            final ShoppingCollectionData collectionData = collectionDatas.get(position);
            holder.checkBox.setChecked(collectionData.checked);
            ApiClient.displayImage(collectionData.thumb, holder.imageView);
            holder.title.setText(collectionData.title);
            holder.money.setText("￥" + collectionData.price);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, GoodsDetailActivity.class);
                    intent.putExtra("gid", collectionData.goodsId);
                    context.startActivity(intent);
                }
            });
        }
        return view;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    private class ViewHolder {
        private ImageView imageView;
        private TextView title;
        private TextView money;
        private CheckBox checkBox;
    }
}
