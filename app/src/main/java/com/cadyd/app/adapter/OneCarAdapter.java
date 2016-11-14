package com.cadyd.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.alipay.sdk.authjs.a;
import com.cadyd.app.R;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.interfaces.BooleanOneByOneInterface;
import com.cadyd.app.interfaces.TowObjectParameterInterface;
import com.cadyd.app.interfaces.TowParameterInterface;
import com.cadyd.app.model.OneCarGoodsData;
import com.cadyd.app.ui.view.AddAndSubView;
import com.cadyd.app.ui.view.CustListView;
import com.nostra13.universalimageloader.core.ImageLoader;
import org.wcy.android.utils.ActivityUtil;

import java.util.List;

/**
 * Created by Administrator on 2016/5/18.
 */
public class OneCarAdapter extends BaseAdapter {
    private List<OneCarGoodsData> datas;
    private LayoutInflater inflater;

    private TowObjectParameterInterface towObjectParameterInterface;
    private BooleanOneByOneInterface byOneInterface;

    public OneCarAdapter(Activity activity, List<OneCarGoodsData> datas) {
        this.datas = datas;
        inflater = LayoutInflater.from(activity);
    }

    public void setOnClick(TowObjectParameterInterface towObjectParameterInterface, BooleanOneByOneInterface byOneInterface) {
        this.towObjectParameterInterface = towObjectParameterInterface;
        this.byOneInterface = byOneInterface;
    }

    @Override
    public int getCount() {
        return datas == null ? 0 : datas.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View view, final ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.one_shopping_car_item, null);
            holder.imageView = (ImageView) view.findViewById(R.id.one_car_image);
            holder.content = (TextView) view.findViewById(R.id.one_car_content);
            holder.checkBox = (CheckBox) view.findViewById(R.id.one_car_is_shopping);
            holder.addAndSubView = (AddAndSubView) view.findViewById(R.id.addandsubview);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        final OneCarGoodsData goodsData = datas.get(position);

        ApiClient.displayImage(goodsData.mainImg, holder.imageView);
        holder.content.setText(goodsData.title);
        holder.addAndSubView.setMaxNum(Integer.parseInt(goodsData.participatetimes));
        holder.addAndSubView.setNum(goodsData.buytimes);
        holder.checkBox.setChecked(goodsData.checked);

        //单选按钮
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                byOneInterface.Onchange(position, holder.checkBox.isChecked());
            }
        });

        //加减监听事件
        holder.addAndSubView.setOnNumChangeListener(new AddAndSubView.OnNumChangeListener() {
            @Override
            public void onNumChange(View view, int type, int num) {
                towObjectParameterInterface.Onchange(0, position, String.valueOf(num));
            }
        });

        return view;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    private class ViewHolder {
        private CheckBox checkBox;
        private ImageView imageView;
        private TextView content;
        private AddAndSubView addAndSubView;
    }
}
