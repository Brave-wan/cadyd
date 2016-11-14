package com.cadyd.app.ui.window;

import android.app.Activity;
import android.view.View;
import android.widget.ListView;
import com.cadyd.app.JConstant;
import com.cadyd.app.MyApplication;
import com.cadyd.app.R;
import com.cadyd.app.asynctask.DataLoader;
import com.cadyd.app.asynctask.SimpleAsyncTask;
import com.cadyd.app.model.Certification;
import com.cadyd.app.model.Prefer;
import com.cadyd.app.ui.view.toast.ToastUtils;
import org.wcy.android.utils.adapter.CommonAdapter;
import org.wcy.android.utils.adapter.ViewHolder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 服务信息
 * Created by wcy on 2016/5/25.
 */
public class CerificationPopupWindow extends BottomPushPopupWindow<List<Certification>> {
    ListView listView;


    public CerificationPopupWindow(Activity activity, List<Certification> data) {
        super(activity, data);
    }

    @Override
    protected View generateCustomView(List<Certification> data) {
        View view = View.inflate(bActivity, R.layout.cerification_popuo_window, null);
        listView = (ListView) view.findViewById(R.id.list_view);
        CommonAdapter<Certification> adapter = new CommonAdapter<Certification>(context, data, R.layout.cerification_list_item) {
            @Override
            public void convert(ViewHolder helper, final Certification item) {
                helper.setText(R.id.name, item.name);
                helper.setText(R.id.mark, item.mark);
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
