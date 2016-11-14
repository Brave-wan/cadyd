package com.cadyd.app.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.cadyd.app.R;
import com.cadyd.app.handle.InterFace;
import com.cadyd.app.model.MyOrder;
import com.cadyd.app.ui.view.DividerGridItemDecoration;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 我的订单 主界面
 * Created by wcy on 2016/6/1.
 */
public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.ViewHolder> {
    public List<MyOrder> datas = null;
    private boolean isEdit = false;
    private Context mcontext;
    private Map<String, Boolean> map;

    public MyOrderAdapter(List<MyOrder> datas, Context context) {
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
        final MyOrder n = datas.get(position);
        if (isEdit) {
            viewHolder.shop_name.setCompoundDrawablesWithIntrinsicBounds(R.drawable.shopping_cart_checked_selector, 0, 0, 0);
            viewHolder.shop_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean c = viewHolder.shop_name.isChecked();
                    map.put(n.shopid, c);
                    if (c) {
                        operateClickInterface.isPay(true);
                    } else {
                        operateClickInterface.isPay(IsCheck());
                    }
                }
            });
        }
        viewHolder.status_name.setText(InterFace.OrderStatus.format(n.state).ename);//设置状态名字
        //计算商品的总价，和总数量
        BigDecimal bigDecimal = new BigDecimal(0);
        int num = 0;
        for (MyOrder.DetailListEntity deta : n.suitList) {
            for (MyOrder.NewDetailListEntity newdeta : deta.orderDetail) {
                num += Integer.valueOf(newdeta.number);
                bigDecimal = bigDecimal.add(new BigDecimal(String.valueOf(newdeta.total)));
            }
        }
        //加上商品的邮费
        bigDecimal = bigDecimal.add(new BigDecimal(String.valueOf(n.mailcost)));
        int start;
        int end;
        StringBuffer buffer = new StringBuffer();
        buffer.append(num);//数量，计算来的
        buffer.append("件  合计：￥");
        start = buffer.length();
        buffer.append(bigDecimal.toString());//加邮费的价格
        end = buffer.length();
        buffer.append("(含运费￥");
        buffer.append(n.mailcost);
        buffer.append(")");
        SpannableStringBuilder buildero = new SpannableStringBuilder(buffer);//总价//合计统计
        buildero.setSpan(new AbsoluteSizeSpan(40), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        viewHolder.subtotal.setText(buildero);

        /**判断这个订单是否能够退款*/
        if (n.refundFlag) {
            viewHolder.operate_layout.setVisibility(View.VISIBLE);
            viewHolder.refund.setVisibility(View.VISIBLE);
            viewHolder.refund.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    operateClickInterface.refound(n);
                }
            });
        }

        if (n.state.equals(InterFace.OrderStatus.SUCCEED.ecode)) {//交易成功
            viewHolder.operate_layout.setVisibility(View.VISIBLE);
            viewHolder.line_view.setVisibility(View.VISIBLE);
            viewHolder.delete_order.setVisibility(View.VISIBLE);
            if (n.commentstate == 0) {//能够晒单
                viewHolder.evaluate_order.setVisibility(View.VISIBLE);
            } else {//查看晒单
                viewHolder.look_evaluate_order.setVisibility(View.VISIBLE);
                viewHolder.look_evaluate_order.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        operateClickInterface.lookEvaluate(n);
                    }
                });
            }
        } else if (n.state.equals(InterFace.OrderStatus.PAYMENT.ecode)) {
            viewHolder.operate_layout.setVisibility(View.VISIBLE);
            viewHolder.line_view.setVisibility(View.VISIBLE);
            viewHolder.cancel_order.setVisibility(View.VISIBLE);
            viewHolder.pay_order.setVisibility(View.VISIBLE);
            viewHolder.contact_shop.setVisibility(View.VISIBLE);
        } else if (n.state.equals(InterFace.OrderStatus.CLOSE.ecode)) {
            viewHolder.operate_layout.setVisibility(View.VISIBLE);
            viewHolder.line_view.setVisibility(View.VISIBLE);
            viewHolder.delete_order.setVisibility(View.VISIBLE);
        } else if (n.state.equals(InterFace.OrderStatus.REFUND.ecode)) {//待收货
            viewHolder.operate_layout.setVisibility(View.VISIBLE);
            viewHolder.receipt_order.setVisibility(View.VISIBLE);
            viewHolder.look_logistics.setVisibility(View.VISIBLE);
        } else if (n.state.equals((InterFace.OrderStatus.DENIAL.ecode))) {//拒绝退款原因
            viewHolder.operate_layout.setVisibility(View.VISIBLE);
            viewHolder.denial_reason.setVisibility(View.VISIBLE);
            viewHolder.delete_order.setVisibility(View.VISIBLE);
            viewHolder.denial_reason.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    operateClickInterface.denialReason(n);
                }
            });
        } else if (n.state.equals((InterFace.OrderStatus.REFUNDSUCCESSFULLY.ecode))) {//退款成功
            viewHolder.operate_layout.setVisibility(View.VISIBLE);
            viewHolder.denial_reason.setVisibility(View.VISIBLE);
            viewHolder.delete_order.setVisibility(View.VISIBLE);
            viewHolder.denial_reason.setText("退款原因");
            viewHolder.denial_reason.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    operateClickInterface.denialReason(n);
                }
            });
        }
        /**
         * 正在施工
         ***/
        MyGoodsPackageAdapter adapter = new MyGoodsPackageAdapter(mcontext, n.suitList, n.state, false);
        adapter.setOnItemClickListener(new MyGoodsAdapter.OnItemClickListener() {
            @Override
            public void ItemClickListener(int index, int position) {
                operateClickInterface.detail(n);
            }
        });
        viewHolder.list_view.setAdapter(adapter);

        viewHolder.shop_name.setText(n.shopname);
        viewHolder.delete_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operateClickInterface.delete(n);
            }
        });
        viewHolder.cancel_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operateClickInterface.cansel(n);
            }
        });
        viewHolder.contact_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operateClickInterface.contact(n);
            }
        });
        viewHolder.evaluate_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operateClickInterface.evaluate(n);
            }
        });
        viewHolder.receipt_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operateClickInterface.receipt(n);
            }
        });
        viewHolder.pay_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operateClickInterface.pay(n);
            }
        });
        viewHolder.look_logistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operateClickInterface.lookLogistics(n);
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
        public TextView refund;//退款
        public TextView look_logistics;//查看物流
        public RecyclerView list_view;
        public View line_view;
        public TextView denial_reason;//查看拒绝退款原因
        public TextView look_evaluate_order;//查看评价

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
            receipt_order = (TextView) view.findViewById(R.id.receipt_order);
            pay_order = (TextView) view.findViewById(R.id.pay_order);
            refund = (TextView) view.findViewById(R.id.Refund);
            list_view = (RecyclerView) view.findViewById(R.id.goods_list_view);
            line_view = view.findViewById(R.id.line_view);
            look_logistics = (TextView) view.findViewById(R.id.look_logistics);
            denial_reason = (TextView) view.findViewById(R.id.denial_reason);
            look_evaluate_order = (TextView) view.findViewById(R.id.look_evaluate_order);
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
            refund.setVisibility(View.GONE);
            look_logistics.setVisibility(View.GONE);
            denial_reason.setVisibility(View.GONE);
            look_evaluate_order.setVisibility(View.GONE);
        }
    }

    private OperateClickInterface operateClickInterface;

    public void setOperateClickInterface(OperateClickInterface operateClickInterface) {
        this.operateClickInterface = operateClickInterface;
    }

    public interface OperateClickInterface {
        public void delete(MyOrder order);

        public void cansel(MyOrder order);

        public void pay(MyOrder order);

        public void contact(MyOrder order);

        public void evaluate(MyOrder order);

        public void isPay(boolean ispay);

        public void detail(MyOrder order);

        public void receipt(MyOrder order);

        public void refound(MyOrder order);

        public void lookLogistics(MyOrder order);

        public void denialReason(MyOrder order);

        public void lookEvaluate(MyOrder order);
    }


}