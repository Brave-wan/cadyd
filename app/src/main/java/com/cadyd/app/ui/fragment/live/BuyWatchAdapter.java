package com.cadyd.app.ui.fragment.live;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cadyd.app.R;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.model.ProductsFeaturedInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: ${todo}(这里用一句话描述这个方法的作用)}$
 * 作者：Jerry
 * 邮箱：185214487@qq.com
 */
public class BuyWatchAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private Context mContext;
    private List<ProductsFeaturedInfo.ProductsFeatBean> mBuyWatchingInfoList = new ArrayList<>();

    public BuyWatchAdapter(Context mContext, List<ProductsFeaturedInfo.ProductsFeatBean> mBuyWatchingInfoList) {
        this.mBuyWatchingInfoList = mBuyWatchingInfoList;
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);

    }

    @Override
    public int getCount() {
        return mBuyWatchingInfoList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public void setData(List<ProductsFeaturedInfo.ProductsFeatBean> mBuyWatchingInfoList) {
        this.mBuyWatchingInfoList = mBuyWatchingInfoList;
        notifyDataSetChanged();

    }

    @Override
    public Object getItem(int position) {
        return mBuyWatchingInfoList.get(position);
    }

    private viewHolder mViewHolder;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_buywatching_listview, null);
            mViewHolder = new viewHolder();
            mViewHolder.buywacth_image = (ImageView) convertView.findViewById(R.id.buywacth_image);
            mViewHolder.buywacth_title = (TextView) convertView.findViewById(R.id.buywacth_title);
            mViewHolder.buywacth_price = (TextView) convertView.findViewById(R.id.buywacth_price);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (viewHolder) convertView.getTag();
        }
        ProductsFeaturedInfo.ProductsFeatBean info = mBuyWatchingInfoList.get(position);
        ApiClient.displayImage(info.getProductImageUrl(), mViewHolder.buywacth_image, R.mipmap.defaiut_on);
        mViewHolder.buywacth_title.setText(info.getProductName());
        mViewHolder.buywacth_price.setText("￥ " + info.getCashPrice());

        return convertView;
    }

    class viewHolder {
        ImageView buywacth_image;
        TextView buywacth_title;
        TextView buywacth_price;


    }
}
