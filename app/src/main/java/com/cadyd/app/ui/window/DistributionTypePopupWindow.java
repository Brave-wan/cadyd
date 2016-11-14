package com.cadyd.app.ui.window;

import android.app.Activity;
import android.view.View;
import android.widget.ListView;
import android.widget.RadioButton;

import com.cadyd.app.R;
import com.cadyd.app.model.Freight;
import com.cadyd.app.utils.StringUtils;

import org.wcy.android.utils.adapter.CommonAdapter;
import org.wcy.android.utils.adapter.ViewHolder;
import org.wcy.common.utils.NumberUtil;

import java.util.List;

/**
 * 配送方式
 * Created by wcy on 2016/5/25.
 */
public class DistributionTypePopupWindow extends BottomPushPopupWindow<List<Freight>> {
    ListView listView;
    String id = "'";

    public DistributionTypePopupWindow(Activity activity, List<Freight> data, String id) {
        super(activity, data);
        this.id = id;
    }

    @Override
    protected View generateCustomView(final List<Freight> data) {
        View view = View.inflate(bActivity, R.layout.distribution_type_popuo_window, null);
        listView = (ListView) view.findViewById(R.id.list_view);
        CommonAdapter<Freight> adapter = new CommonAdapter<Freight>(context, data, R.layout.comm_radio_layout) {
            @Override
            public void convert(ViewHolder helper, final Freight item) {
                RadioButton rb = helper.getView(R.id.name);

                rb.setText(item.name + " " + NumberUtil.getString(item.extra, 2) + "元");
                rb.setTag(item.id);
//                if (data.size() == 1) {
//                    rb.setChecked(true);
//                }
                if (rb.getTag().toString().equals(id)) {
                    rb.setChecked(true);
                } else {
                    rb.setChecked(false);
                }
                rb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        id = item.id;
                        notifyDataSetChanged();
                        if (onCheckChangeListener != null) {
                            onCheckChangeListener.onClickListener(item);
                        }
                    }
                });
            }
        };
        listView.setAdapter(adapter);
        view.findViewById(R.id.determine).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        public void onClickListener(Freight data);
    }

}
