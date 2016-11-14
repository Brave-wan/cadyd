package com.cadyd.app.ui.window;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.*;

import com.cadyd.app.R;
import com.cadyd.app.model.Freight;
import com.cadyd.app.model.Prefer;

import org.wcy.android.utils.adapter.CommonAdapter;
import org.wcy.android.utils.adapter.ViewHolder;
import org.wcy.common.utils.NumberUtil;
import org.wcy.common.utils.StringUtil;

import java.math.BigDecimal;
import java.util.List;

/**
 * 配送方式
 * Created by wcy on 2016/5/25.
 */
public class PreferUsePopupWindow extends BottomPushPopupWindow<List<Prefer>> {
    ListView listView;
    String id = "";
    private BigDecimal numMoney;
    List<Prefer> data;
    int positionList = -1;

    public PreferUsePopupWindow(Activity activity, List<Prefer> data, String id, BigDecimal numMoney) {
        super(activity, data);
        this.id = id;
        this.numMoney = numMoney;
        this.data = data;
    }


    @Override
    protected View generateCustomView(final List<Prefer> data) {

        View view = View.inflate(bActivity, R.layout.prefer_use_popuo_window, null);
        listView = (ListView) view.findViewById(R.id.list_view);
        CommonAdapter<Prefer> adapter = new CommonAdapter<Prefer>(context, data, R.layout.comm_checkbox_layout) {
            @Override
            public void convert(final ViewHolder helper, final Prefer item) {
                final CheckBox rb = helper.getView(R.id.name);
                rb.setText(item.money + "元优惠券  " + "(满" + item.condition + "元  " + item.des + ")");
                rb.setTag(item.id);
                //     positionList = -1;
                if (item.condition.compareTo(numMoney) == 1) {
                    rb.setEnabled(false);
//                    rb.setClickable(false);
                    rb.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.check_shopping_cart_dis, 0);
                } else {
                    rb.setClickable(true);
                    rb.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.shopping_cart_checked_selector, 0);
                    if (rb.getTag().toString().equals(id)) {
                        positionList = helper.getPosition();
                        rb.setChecked(true);
                    } else {
                        rb.setChecked(false);
                    }
                }
                rb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (rb.isChecked()) {
                            positionList = helper.getPosition();
                            id = item.id;
                            rb.setTag(item.id);
                            notifyDataSetChanged();
                        } else {
                            positionList = -1;
                            id = "";
                        }
                    }
                });

            }
        };
        listView.setAdapter(adapter);
        view.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        view.findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onCheckChangeListener != null) {
                    if (positionList == -1) {
                        onCheckChangeListener.onClickListener(null, false);
                    } else {
                        onCheckChangeListener.onClickListener(data.get(positionList), true);
                    }

                }
                dismiss();
            }
        });
        return view;
    }

    private OnCheckChangeListener onCheckChangeListener;

    public void setOnCheckChangeListener(OnCheckChangeListener onCheckChangeListener) {
        this.onCheckChangeListener = onCheckChangeListener;
    }

    public interface OnCheckChangeListener {
        public void onClickListener(Prefer data, boolean isChecked);
    }

}
