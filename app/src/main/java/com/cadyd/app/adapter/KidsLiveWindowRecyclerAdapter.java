package com.cadyd.app.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cadyd.app.R;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.interfaces.TowObjectParameterInterface;
import com.cadyd.app.model.OneGoodsModel;

import org.wcy.android.utils.adapter.CommonRecyclerAdapter;
import org.wcy.android.utils.adapter.ViewRecyclerHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiongmao on 2016/8/25.
 */

public class KidsLiveWindowRecyclerAdapter extends RecyclerView.Adapter<ViewHolder> {

    private TowObjectParameterInterface towObjectParameterInterface;

    private Context context;
    private LayoutInflater inflater;
    private List<OneGoodsModel> list;
    private List<OneGoodsModel> hotList;

    public void setClick(TowObjectParameterInterface towObjectParameterInterface) {
        this.towObjectParameterInterface = towObjectParameterInterface;
    }

    public KidsLiveWindowRecyclerAdapter(Context context, List<OneGoodsModel> list, List<OneGoodsModel> hotList) {
        this.context = context;
        this.list = list;
        this.hotList = hotList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemViewType(int position) {
        int typt;//0为头部， 1 以为内容
        if (position == 0) {
            typt = 0;
        } else {
            typt = 1;
        }
        return typt;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        ViewHolder viewHolder;
        if (viewType == 0) {
            view = inflater.inflate(R.layout.kids_live_window_head, null);
        } else {
            view = inflater.inflate(R.layout.kids_live_window_content, null);
        }
        viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setContent(hotList, list, position, context, towObjectParameterInterface);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }
}

class ViewHolder extends RecyclerView.ViewHolder {

    private View itemView;
    /**
     * 头部控件
     */
    RecyclerView recyclerView;
    TextView textView;//全部宝贝
    /**
     * 中间控件
     */
    ImageView imageView;//商品图片
    TextView title;//商品标题
    TextView money;//商品价格
    TextView number;//购买人次
    Button addCar;//添加购物车

    public ViewHolder(View itemView) {
        super(itemView);
        this.itemView = itemView;
    }

    public void setContent(final List<OneGoodsModel> hotList, List<OneGoodsModel> AllList, final int postion, Context context, final TowObjectParameterInterface towObjectParameterInterface) {
        if (postion == 0) {
            recyclerView = (RecyclerView) itemView.findViewById(R.id.hot_recyclerView);
            textView = (TextView) itemView.findViewById(R.id.all_baby_number);
            CommonRecyclerAdapter<OneGoodsModel> adapter = new CommonRecyclerAdapter<OneGoodsModel>(context, hotList, R.layout.base_hot_item) {

                @Override
                public void convert(ViewRecyclerHolder helper, final OneGoodsModel item) {
                    ImageView imageView = helper.getView(R.id.image);
                    ApiClient.displayImage(item.thumb, imageView , R.mipmap.defaiut_on);
                    helper.setText(R.id.money, "￥ " + item.price);
                    helper.getConvertView().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //TODO 最热推荐商品点击监听 TYPE=0
                            towObjectParameterInterface.Onchange(0, postion, item);
                        }
                    });
                }
            };
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(adapter);
        } else {
            imageView = (ImageView) itemView.findViewById(R.id.image);//商品图片
            title = (TextView) itemView.findViewById(R.id.title);//商品标题
            money = (TextView) itemView.findViewById(R.id.money);//商品价格
            number = (TextView) itemView.findViewById(R.id.buy_number);//购买人次
            addCar = (Button) itemView.findViewById(R.id.add_car);//添加购物车

            final OneGoodsModel oneGoodsModel = AllList.get(postion);
            ApiClient.displayImage(oneGoodsModel.thumb, imageView , R.mipmap.defaiut_on);
            title.setText(oneGoodsModel.title);
            money.setText("￥ " + oneGoodsModel.price);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO 全部商品的商品点击监听 TYPE=1
                    towObjectParameterInterface.Onchange(1, postion, oneGoodsModel);
                }
            });
            addCar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO 全部商品加入购物车的点击监听 TYPE=2
                    towObjectParameterInterface.Onchange(2, postion, oneGoodsModel);
                }
            });
        }

    }
}
