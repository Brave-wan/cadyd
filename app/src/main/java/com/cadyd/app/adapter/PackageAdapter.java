package com.cadyd.app.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.cadyd.app.R;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.interfaces.TowObjectParameterInterface;
import com.cadyd.app.model.Good;
import com.cadyd.app.model.SalesPackage;
import com.cadyd.app.ui.view.DividerGridItemDecoration;
import com.cadyd.app.ui.view.DividerListItemDecoration;

import java.util.List;

/**
 * 套装
 * Created by wcy on 2016/6/1.
 */
public class PackageAdapter extends RecyclerView.Adapter<PackageAdapter.ViewHolder> {
    public List<SalesPackage> datas = null;
    private TowObjectParameterInterface towObjectParameterInterface;
    private Context context;

    public PackageAdapter(List<SalesPackage> datas, Context context) {
        this.datas = datas;
        this.context = context;
    }

    public void setClick(TowObjectParameterInterface towObjectParameterInterface) {
        this.towObjectParameterInterface = towObjectParameterInterface;
    }

    //创建新View，被LayoutManager所调用
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.sales_list_iteam, viewGroup, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (towObjectParameterInterface != null) {
                    towObjectParameterInterface.Onchange(0, 0, null);
                }
            }
        });
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        SalesPackage n = datas.get(position);
        viewHolder.bindData(n);
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

        public ViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            recyclerView = (RecyclerView) view.findViewById(R.id.package_view);
            recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            recyclerView.setHasFixedSize(true);
            recyclerView.addItemDecoration(new DividerListItemDecoration(context, LinearLayoutManager.VERTICAL, R.mipmap.division_ico));
        }

        public void bindData(SalesPackage itemInfo) {
            name.setText(itemInfo.tname);
            PackageChildAdapter packageChildAdapter = new PackageChildAdapter(itemInfo.getMallgoods());
            recyclerView.setAdapter(packageChildAdapter);
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
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.sales_list_iteam_goods, viewGroup, false);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (towObjectParameterInterface != null) {
                        towObjectParameterInterface.Onchange(0, 0, null);
                    }
                }
            });
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
}