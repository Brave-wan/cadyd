package com.cadyd.app.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.cadyd.app.R;
import com.cadyd.app.handle.InterFace;
import com.cadyd.app.model.IntegralListData;
import com.cadyd.app.ui.view.DividerGridItemDecoration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 我的订单 主界面
 * Created by wcy on 2016/6/1.
 */
public class IntegralMyOrderAdapter extends RecyclerView.Adapter<IntegralMyOrderAdapter.ViewHolder> {
    public List<IntegralListData> datas = null;
    private boolean isEdit = false;
    private Context mcontext;
    private Map<String, Boolean> map;

    public IntegralMyOrderAdapter(List<IntegralListData> datas, Context context) {
        this.datas = datas;
        this.mcontext = context;
        map = new HashMap<>();
    }

    public void ClearCheck() {
        map.clear();
    }

    public boolean IsCheck() {
        for (String key : map.keySet()) {
            if (map.get(key)) {
                return true;
            }
        }
        return false;
    }

    public String getOrderId() {
        StringBuffer sb = new StringBuffer();
        for (String key : map.keySet()) {
            if (map.get(key)) {
                sb.append(map.get(key));
                sb.append(",");
            }
        }
        if (sb.length() > 0) {
            sb.delete(sb.length() - 1, sb.length());
        }
        return sb.toString();
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
    }

    //创建新View，被LayoutManager所调用
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.my_order_list_iteam, viewGroup, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {
        viewHolder.Gone();
        final IntegralListData listData = datas.get(position);

        //多选结算
//        if (isEdit) {
//            viewHolder.shop_name.setCompoundDrawablesWithIntrinsicBounds(R.drawable.shopping_cart_checked_selector, 0, 0, 0);
//            viewHolder.shop_name.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    boolean c = viewHolder.shop_name.isChecked();
//                    map.put(listData.flowno, c);
//                    if (c) {
//                        operateClickInterface.isPay(true);
//                    } else {
//                        operateClickInterface.isPay(IsCheck());
//                    }
//                }
//            });
//        }

        StringBuffer buffer = new StringBuffer();
        buffer.append(listData.number);
        buffer.append("件  合计：");
        buffer.append(listData.aggregate);
        buffer.append("\t+\t￥");
        buffer.append(listData.price);

        viewHolder.subtotal.setText(buffer.toString());

        viewHolder.shop_name.setText(InterFace.OrderStatus.format(Integer.valueOf(listData.state)).ename);//单选按钮

        if (listData.state.equals(InterFace.OrderStatus.SUCCEED.ecode.toString())) {
            //交易完成
            viewHolder.operate_layout.setVisibility(View.VISIBLE);
            viewHolder.line_view.setVisibility(View.VISIBLE);
            viewHolder.delete_order.setVisibility(View.VISIBLE);
        } else if (listData.state.equals(InterFace.OrderStatus.PAYMENT.ecode.toString())) {
            //待付款
            viewHolder.operate_layout.setVisibility(View.VISIBLE);
            viewHolder.line_view.setVisibility(View.VISIBLE);
            viewHolder.cancel_order.setVisibility(View.VISIBLE);
            viewHolder.pay_order.setVisibility(View.VISIBLE);
        } else if (listData.state.equals(InterFace.OrderStatus.REFUND.ecode.toString())) {
            //等收货
            viewHolder.operate_layout.setVisibility(View.VISIBLE);
            viewHolder.line_view.setVisibility(View.VISIBLE);
            viewHolder.look_logistics.setVisibility(View.VISIBLE);
            viewHolder.receipt_order.setVisibility(View.VISIBLE);
        }
        /**
         * 正在施工
         ***/
        IntegralMyGoodsAdapter adapter = new IntegralMyGoodsAdapter(listData.orderIntegralGoods, Integer.valueOf(listData.state));


        adapter.setOnItemClickListener(new IntegralMyGoodsAdapter.OnItemClickListener() {
            @Override
            public void ItemClickListener() {
                operateClickInterface.detail(listData);
            }
        });
        viewHolder.list_view.setAdapter(adapter);
        viewHolder.status_name.setText(listData.created);//状态提示(现在显示时间)
        viewHolder.delete_order.setOnClickListener(new View.OnClickListener() {//删除订单
            @Override
            public void onClick(View v) {
                operateClickInterface.delete(listData);
            }
        });
        viewHolder.cancel_order.setOnClickListener(new View.OnClickListener() {//取消订单
            @Override
            public void onClick(View v) {
                operateClickInterface.cansel(listData);
            }
        });
        viewHolder.contact_shop.setOnClickListener(new View.OnClickListener() {//联系卖家
            @Override
            public void onClick(View v) {
                operateClickInterface.contact(listData);
            }
        });
        viewHolder.evaluate_order.setOnClickListener(new View.OnClickListener() {//晒单评价
            @Override
            public void onClick(View v) {
                operateClickInterface.evaluate(listData);
            }
        });
        viewHolder.pay_order.setOnClickListener(new View.OnClickListener() {//付款
            @Override
            public void onClick(View v) {
                operateClickInterface.pay(listData);
            }
        });
        viewHolder.look_logistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 查看物流
                operateClickInterface.lookLogistics(listData);
            }
        });
        viewHolder.receipt_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 确认收货
                operateClickInterface.confirmationReceipt(listData);
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
        public CheckBox shop_name;//店铺名称
        public TextView status_name;//订单状态
        public TextView subtotal;//合计统计
        public LinearLayout operate_layout;//操作
        public TextView contact_shop; //联系卖家
        public TextView cancel_order;//取消订单
        public TextView delete_order;//删除订单
        public TextView evaluate_order;//评价晒单
        public TextView pay_order;//付款
        public TextView receipt_order;//确认收货
        public TextView look_logistics;//查看物流
        public RecyclerView list_view;
        public View line_view;

        public ViewHolder(View view) {
            super(view);
            shop_name = (CheckBox) view.findViewById(R.id.shop_name);
            status_name = (TextView) view.findViewById(R.id.status_name);
            subtotal = (TextView) view.findViewById(R.id.subtotal);
            operate_layout = (LinearLayout) view.findViewById(R.id.operate_layout);
            contact_shop = (TextView) view.findViewById(R.id.contact_shop);
            cancel_order = (TextView) view.findViewById(R.id.cancel_order);
            delete_order = (TextView) view.findViewById(R.id.delete_order);
            evaluate_order = (TextView) view.findViewById(R.id.evaluate_order);
            pay_order = (TextView) view.findViewById(R.id.pay_order);
            receipt_order = (TextView) view.findViewById(R.id.receipt_order);
            look_logistics = (TextView) view.findViewById(R.id.look_logistics);
            list_view = (RecyclerView) view.findViewById(R.id.goods_list_view);
            line_view = view.findViewById(R.id.line_view);
            list_view.setLayoutManager(new LinearLayoutManager(mcontext));
            list_view.setFocusable(true);
            list_view.setClickable(true);
            list_view.addItemDecoration(new DividerGridItemDecoration(mcontext, LinearLayoutManager.HORIZONTAL, 10, mcontext.getResources().getColor(R.color.white)));
        }

        public void Gone() {
            shop_name.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            operate_layout.setVisibility(View.GONE);
            contact_shop.setVisibility(View.GONE);
            cancel_order.setVisibility(View.GONE);
            delete_order.setVisibility(View.GONE);
            evaluate_order.setVisibility(View.GONE);
            pay_order.setVisibility(View.GONE);
            receipt_order.setVisibility(View.GONE);
            look_logistics.setVisibility(View.GONE);
        }
    }

    private OperateClickInterface operateClickInterface;

    public void setOperateClickInterface(OperateClickInterface operateClickInterface) {
        this.operateClickInterface = operateClickInterface;
    }

    public interface OperateClickInterface {
        public void delete(IntegralListData order);

        public void cansel(IntegralListData order);

        public void pay(IntegralListData order);

        public void contact(IntegralListData order);

        public void evaluate(IntegralListData order);

        public void isPay(boolean ispay);

        public void detail(IntegralListData order);

        public void lookLogistics(IntegralListData order);

        public void confirmationReceipt(IntegralListData order);
    }


}