package com.cadyd.app.ui.window;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;
import com.cadyd.app.JConstant;
import com.cadyd.app.R;
import com.cadyd.app.adapter.PackageAdapter;
import com.cadyd.app.interfaces.TowObjectParameterInterface;
import com.cadyd.app.model.Sales;
import com.cadyd.app.ui.activity.CommonActivity;
import com.cadyd.app.ui.fragment.mall.SalesPackageFragment;
import com.cadyd.app.ui.view.DividerListItemDecoration;
import org.wcy.common.utils.NumberUtil;

/**
 * 促销
 * Created by wcy on 2016/5/25.
 */
public class SalesPopupWindow extends BottomPushPopupWindow<Sales> {
    RecyclerView listView;
    TextView price;
    TextView number;

    public SalesPopupWindow(Activity activity, Sales data) {
        super(activity, data);
    }

    private TowObjectParameterInterface towObjectParameterInterface;

    @Override
    protected View generateCustomView(final Sales data) {
        View view = View.inflate(bActivity, R.layout.sales_popuo_window, null);
        price = (TextView) view.findViewById(R.id.price);
        number = (TextView) view.findViewById(R.id.number);
        ForegroundColorSpan shop_level_fc = new ForegroundColorSpan(context.getResources().getColor(R.color.red));
        String money = NumberUtil.getString(data.money, 2);
        SpannableStringBuilder shop_level_ssb = new SpannableStringBuilder(String.format("最高省  %s元", money));
        //ForegroundColorSpan 为文字前景色，BackgroundColorSpan为文字背景色
        shop_level_ssb.setSpan(shop_level_fc, 4, 5 + money.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        price.setText(shop_level_ssb);
        number.setText(String.format("共%d款", data.number));

        listView = (RecyclerView) view.findViewById(R.id.list_view);
        listView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        listView.setHasFixedSize(true);
        listView.addItemDecoration(new DividerListItemDecoration(context, LinearLayoutManager.VERTICAL, 60, context.getResources().getColor(R.color.white)));
        PackageAdapter adapter = new PackageAdapter(data.mallpackagelist, context);
        adapter.setClick(new TowObjectParameterInterface() {
            @Override
            public void Onchange(int type, int postion, Object object) {
                dismiss();
                towObjectParameterInterface.Onchange(0, 0, null);
            }
        });
        listView.setAdapter(adapter);
        view.findViewById(R.id.colse).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return view;
    }

    public void setClick(TowObjectParameterInterface towObjectParameterInterface) {
        this.towObjectParameterInterface = towObjectParameterInterface;
    }
}
