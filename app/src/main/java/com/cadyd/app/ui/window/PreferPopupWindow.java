package com.cadyd.app.ui.window;

import android.app.Activity;
import android.view.View;
import android.widget.ListView;
import com.cadyd.app.JConstant;
import com.cadyd.app.MyApplication;
import com.cadyd.app.R;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.asynctask.DataLoader;
import com.cadyd.app.asynctask.SimpleAsyncTask;
import com.cadyd.app.model.Prefer;
import com.cadyd.app.ui.view.ToastView;
import com.cadyd.app.ui.view.toast.ToastUtils;
import org.wcy.android.utils.adapter.CommonAdapter;
import org.wcy.android.utils.adapter.ViewHolder;
import org.wcy.common.utils.StringUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 优惠卷领取
 * Created by wcy on 2016/5/25.
 */
public class PreferPopupWindow extends BottomPushPopupWindow<List<Prefer>> {
    ListView listView;


    public PreferPopupWindow(Activity activity, List<Prefer> data) {
        super(activity, data);
    }

    @Override
    protected View generateCustomView(List<Prefer> data) {
        View view = View.inflate(bActivity, R.layout.prefer_popuo_window, null);
        listView = (ListView) view.findViewById(R.id.list_view);
        CommonAdapter<Prefer> adapter = new CommonAdapter<Prefer>(context, data, R.layout.prefer_list_item) {
            @Override
            public void convert(ViewHolder helper, final Prefer item) {
                helper.setText(R.id.money, item.money + "元");
                helper.setText(R.id.condition, item.des);
                helper.setText(R.id.valid, item.valid + item.invalid);
                helper.getView(R.id.get_btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("userid", MyApplication.getInstance().model.id);
                        map.put("preferid", item.id);
                        ApiClient.send(context, JConstant.PREFER_RECEIVE_, map, true, null, new DataLoader<Object>() {
                            @Override
                            public void task(String data) {

                            }

                            @Override
                            public void succeed(Object data) {
                                ToastUtils.show(bActivity, "领取成功", ToastUtils.SUCCESS_TYPE);
                            }

                            @Override
                            public void error(String message) {

                            }
                        }, JConstant.PREFER_RECEIVE_);
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


}
