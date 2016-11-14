package com.cadyd.app.ui.window;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.cadyd.app.R;
import com.cadyd.app.interfaces.TowObjectParameterInterface;
import com.cadyd.app.ui.activity.ReportActivity;

import org.wcy.android.utils.adapter.CommonAdapter;
import org.wcy.android.utils.adapter.ViewHolder;

import java.util.List;

/**
 * Created by xiongmao on 2016/10/6.
 */

public class LiveMenuPopupWindow {

    private Context context;
    private LayoutInflater inflater;
    private PopupWindow popupWindow;

    private List<String> itemList;
    private TowObjectParameterInterface towObjectParameterInterface;

    public LiveMenuPopupWindow(Context context, List<String> itemList, TowObjectParameterInterface towObjectParameterInterface) {
        this.context = context;
        this.itemList = itemList;
        this.towObjectParameterInterface = towObjectParameterInterface;
        setWindo();
    }

    private void setWindo() {
        inflater = LayoutInflater.from(context);
        popupWindow = new PopupWindow(GridLayoutManager.LayoutParams.MATCH_PARENT, GridLayoutManager.LayoutParams.MATCH_PARENT);
        popupWindow.setAnimationStyle(R.style.popup_window_bottombar);
        final View popopView = inflater.inflate(R.layout.popup_personal_center, null);
        popopView.setOnClickListener(new View.OnClickListener() {//取消
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        TextView cancel = (TextView) popopView.findViewById(R.id.cancel);//取消
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        ListView listView = (ListView) popopView.findViewById(R.id.list_view);
        CommonAdapter<String> adapter = new CommonAdapter<String>(context, itemList, R.layout.menu_item) {
            @Override
            public void convert(final ViewHolder helper, String item) {
                TextView textView = helper.getView(R.id.text);
                textView.setText(item);
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        towObjectParameterInterface.Onchange(0, helper.getPosition(), null);
                    }
                });
            }
        };
        listView.setAdapter(adapter);
        popupWindow.setContentView(popopView);
    }

    public void showWindo(View view) {
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

    public void dismissWindo() {
        if (popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
    }

}


