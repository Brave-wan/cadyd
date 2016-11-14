package com.cadyd.app.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.cadyd.app.R;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.interfaces.TowObjectParameterInterface;
import com.cadyd.app.model.Good;
import com.cadyd.app.model.SalesPackage;
import com.cadyd.app.ui.view.DividerListItemDecoration;
import org.wcy.common.utils.NumberUtil;

import java.util.List;

/**
 * 套装
 * Created by wcy on 2016/6/1.
 */
public class SalesPackageAdapter extends RecyclerView.Adapter<SalesPackageAdapter.ViewHolder> {
    public List<SalesPackage> datas = null;
    private TowObjectParameterInterface towObjectParameterInterface;
    private Context context;
    private int check_position = 0;

    public SalesPackageAdapter(List<SalesPackage> datas, Context context) {
        this.datas = datas;
        this.context = context;
    }

    public void setClick(TowObjectParameterInterface towObjectParameterInterface) {
        this.towObjectParameterInterface = towObjectParameterInterface;
    }

    //创建新View，被LayoutManager所调用
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.sales_package_list_iteam, viewGroup, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        SalesPackage n = datas.get(position);
        viewHolder.bindData(n);
        viewHolder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check_position != position) {
                    check_position = position;
                    notifyDataSetChanged();
                } else {
                    check_position = -1;
                    viewHolder.package_layout.setVisibility(View.GONE);
                    viewHolder.arrow_image.setImageResource(R.mipmap.arrow_down);
                    viewHolder.price.setVisibility(View.VISIBLE);
                    viewHolder.recyclerView.setVisibility(View.VISIBLE);
                    viewHolder.package_view_h.setVisibility(View.GONE);
                }
            }
        });

    }

    //获取数据的数量
    @Override
    public int getItemCount() {
        return datas.size();
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public class ViewHolder extends RecyclerView.ViewHolder {
        RecyclerView recyclerView;
        TextView name;
        TextView price;
        TextView price_bottom;
        TextView thrift;
        Button add_cart;
        TextView entity_price;
        RelativeLayout title;
        ImageView arrow_image;
        LinearLayout package_layout;
        RecyclerView package_view_h;

        public ViewHolder(View view) {
            super(view);
            title = (RelativeLayout) view.findViewById(R.id.title);
            package_layout = (LinearLayout) view.findViewById(R.id.package_layout);
            arrow_image = (ImageView) view.findViewById(R.id.arrow_image);
            name = (TextView) view.findViewById(R.id.name);
            price = (TextView) view.findViewById(R.id.price);
            price_bottom = (TextView) view.findViewById(R.id.price_bottom);
            thrift = (TextView) view.findViewById(R.id.thrift);
            entity_price = (TextView) view.findViewById(R.id.entity_price);
            add_cart = (Button) view.findViewById(R.id.add_cart);
            recyclerView = (RecyclerView) view.findViewById(R.id.package_view);
            package_view_h = (RecyclerView) view.findViewById(R.id.package_view_h);
            recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            recyclerView.addItemDecoration(new DividerListItemDecoration(context, LinearLayoutManager.VERTICAL, R.mipmap.division_ico));
            package_view_h.setLayoutManager(new LinearLayoutManager(context));
            package_view_h.addItemDecoration(new DividerListItemDecoration(context, LinearLayoutManager.HORIZONTAL, 1, context.getResources().getColor(R.color.line_bg)));
        }

        public void bindData(final SalesPackage itemInfo) {
            name.setText(itemInfo.tname);
            recyclerView.setAdapter(new PackageChildAdapter(itemInfo.getMallgoods()));
            package_view_h.setAdapter(new PackageChildHAdapter(itemInfo.getMallgoods()));
            price.setText(String.format("套餐价格：￥%s", NumberUtil.getString(itemInfo.price, 2)));
            ForegroundColorSpan redSpan = new ForegroundColorSpan(context.getResources().getColor(R.color.text_primary_gray));
            SpannableStringBuilder salenumb = new SpannableStringBuilder(String.format("套餐价格：￥%s", NumberUtil.getString(itemInfo.price, 2)));
            salenumb.setSpan(redSpan, 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            price_bottom.setText(salenumb);
            thrift.setText(String.format("立省：￥%s", NumberUtil.getString(itemInfo.lisheng, 2)));
            entity_price.setText(String.format("实体店价格：￥%s", NumberUtil.getString(itemInfo.yprice, 2)));
            entity_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            if (getPosition() == check_position) {
                package_view_h.setVisibility(View.VISIBLE);
                package_layout.setVisibility(View.VISIBLE);
                price.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
                arrow_image.setImageResource(R.mipmap.arrow_up);
            } else {
                package_layout.setVisibility(View.GONE);
                arrow_image.setImageResource(R.mipmap.arrow_down);
                price.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
                package_view_h.setVisibility(View.GONE);
            }
            add_cart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (towObjectParameterInterface != null) {
                        towObjectParameterInterface.Onchange(0, 0, itemInfo);
                    }
                }
            });
        }
    }

    public class PackageChildAdapter extends RecyclerView.Adapter<PackageChildAdapter.ViewHolder> {
        public List<Good> datas = null;

        public PackageChildAdapter(List<Good> datas) {
            this.datas = datas;
        }


        //创建新View，被LayoutManager所调用
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.sales_package_list_iteam_goods, viewGroup, false);
            ViewHolder vh = new ViewHolder(view);
            return vh;
        }

        //将数据与界面进行绑定的操作
        @Override
        public void onBindViewHolder(ViewHolder viewHolder, final int position) {
            Good n = datas.get(position);
            viewHolder.bindData(n);
        }

        //获取数据的数量
        @Override
        public int getItemCount() {
            return datas.size();
        }

        //自定义的ViewHolder，持有每个Item的的所有界面元素
        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;

            public ViewHolder(View view) {
                super(view);
                imageView = (ImageView) view.findViewById(R.id.goods_image);
            }

            public void bindData(Good itemInfo) {
                ApiClient.displayImage(itemInfo.thumb, imageView);
            }
        }
    }

    public class PackageChildHAdapter extends RecyclerView.Adapter<PackageChildHAdapter.ViewHolder> {
        public List<Good> datas = null;

        public PackageChildHAdapter(List<Good> datas) {
            this.datas = datas;
        }


        //创建新View，被LayoutManager所调用
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.sales_package_list_h_iteam_goods, viewGroup, false);
            ViewHolder vh = new ViewHolder(view);
            return vh;
        }

        //将数据与界面进行绑定的操作
        @Override
        public void onBindViewHolder(ViewHolder viewHolder, final int position) {
            Good n = datas.get(position);
            viewHolder.bindData(n);
        }

        //获取数据的数量
        @Override
        public int getItemCount() {
            return datas.size();
        }

        //自定义的ViewHolder，持有每个Item的的所有界面元素
        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;
            TextView good_name;

            public ViewHolder(View view) {
                super(view);
                imageView = (ImageView) view.findViewById(R.id.goods_image);
                good_name = (TextView) view.findViewById(R.id.good_name);
            }

            public void bindData(Good itemInfo) {
                ApiClient.displayImage(itemInfo.thumb, imageView);
                good_name.setText(itemInfo.mallname);
            }
        }
    }
}