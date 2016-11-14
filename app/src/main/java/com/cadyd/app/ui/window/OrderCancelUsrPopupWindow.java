package com.cadyd.app.ui.window;

import android.app.Activity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import com.cadyd.app.R;
import com.cadyd.app.model.Usr;
import org.wcy.android.utils.adapter.CommonAdapter;
import org.wcy.android.utils.adapter.ViewHolder;

import java.util.List;

/**
 * 取消原因
 * Created by wcy on 2016/5/25.
 */
public class OrderCancelUsrPopupWindow extends BottomPushPopupWindow<List<Usr>> {


    ListView listView;
    List<Usr> data;
    int positionList = -1;
    TextView title;

    public OrderCancelUsrPopupWindow(Activity activity, List<Usr> data) {
        super(activity, data);
        this.data = data;
    }

    @Override
    protected View generateCustomView(final List<Usr> data) {

        View view = View.inflate(bActivity, R.layout.prefer_use_popuo_window, null);
        listView = (ListView) view.findViewById(R.id.list_view);
        title = (TextView) view.findViewById(R.id.title);
        title.setText("取消订单原因");
        CommonAdapter<Usr> adapter = new CommonAdapter<Usr>(context, data, R.layout.comm_checkbox_layout) {
            @Override
            public void convert(final ViewHolder helper, final Usr item) {
                final CheckBox rb = helper.getView(R.id.name);
                rb.setText(item.name);
                rb.setTag(item.val);
                if (helper.getPosition() == positionList) {
                    rb.setChecked(true);
                } else {
                    rb.setChecked(false);
                }
                rb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (rb.isChecked()) {
                            positionList = helper.getPosition();
                            notifyDataSetChanged();
                        } else {
                            positionList = -1;
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
                    if (positionList > -1) {
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
        public void onClickListener(Usr data, boolean isChecked);
    }

}
