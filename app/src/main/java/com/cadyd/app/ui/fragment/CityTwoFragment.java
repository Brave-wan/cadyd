package com.cadyd.app.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import butterknife.Bind;
import com.cadyd.app.R;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.model.AreaInfo;
import com.cadyd.app.model.CityInfo;
import com.cadyd.app.ui.base.BaseFragement;
import org.wcy.android.utils.PreferenceUtils;
import org.wcy.android.utils.adapter.CommonAdapter;
import org.wcy.android.utils.adapter.ViewHolder;

import java.util.List;

/**
 * 城市选择
 *
 * @author wangchaoyong
 */
public class CityTwoFragment extends BaseFragement {
    AreaInfo areainfo;
    boolean isallcity;
    @Bind(R.id.listview)
    ListView listview;
    private CityFragment.ICustomDialogEventListener mCustomDialogEventListener;

    public  void setListener(CityFragment.ICustomDialogEventListener listener){
        mCustomDialogEventListener = listener;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = setView(R.layout.serieinfo, areainfo.name, true);
        return view;
    }

    @Override
    protected void initView() {
        List<CityInfo> list = ApiClient.getCity(areainfo.id);
        if (!isallcity) {
            CityInfo ci = new CityInfo();
            ci.areaid = String.valueOf(areainfo.id);
            ci.name = "不限";
            list.add(0, ci);
        }
        if (list != null) {
            CommonAdapter<CityInfo> adapter = new CommonAdapter<CityInfo>(activity, list, R.layout.series_list_rows) {
                @Override
                public void convert(ViewHolder helper, final CityInfo item) {
                    helper.setText(R.id.name, item.name);
                    helper.getView(R.id.name).setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!isallcity) {
                                PreferenceUtils.setObject(activity, item);
                            }
                            mCustomDialogEventListener.customDialogEvent(item);
                        }
                    });

                }
            };
            listview.setAdapter(adapter);
        }
    }
    @Override
    public boolean onBackPressed() {
        finishFramager();
        return false;
    }

}
