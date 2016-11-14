package com.cadyd.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cadyd.app.R;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.model.LiveModel;
import com.cadyd.app.ui.view.CircleImageView;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

/**
 * Author       : yanbo
 * Date         : 2015-06-02
 * Time         : 09:47
 * Description  :
 */
public class DirectRoomViewAdapter extends BaseAdapter {
    private LayoutInflater inflater;

    private Context mContext;
    private List<LiveModel> liveModels;

    public DirectRoomViewAdapter(Context mContext, List<LiveModel> liveModels) {
        this.mContext = mContext;
        inflater = LayoutInflater.from(mContext);
        this.liveModels = liveModels;
    }

    @Override
    public int getCount() {
        return liveModels.size();
    }

    @Override
    public Object getItem(int position) {
        return liveModels.get(position);
    }

    public void setData(List<LiveModel> liveModels) {
        this.liveModels = liveModels;
        notifyDataSetChanged();

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            holder = new HolderView();
            convertView = inflater.inflate(R.layout.directroom_list_item, null);
            holder.name = (TextView) convertView.findViewById(R.id.direct_item_name);
            holder.mode = (TextView) convertView.findViewById(R.id.direct_item_mode);
            holder.backImage = (ImageView) convertView.findViewById(R.id.direct_item_backgroup);
            holder.danmu = (TextView) convertView.findViewById(R.id.direct_item_danmu);
            holder.item_number = (TextView) convertView.findViewById(R.id.direct_item_number);
            holder.circleImageView = (CircleImageView) convertView.findViewById(R.id.direct_item_head);
            convertView.setTag(holder);
        } else {
            holder = (HolderView) convertView.getTag();
        }
        LiveModel info = liveModels.get(position);
        holder.name.setText(info.nickname);
//        try {
//            String subject = URLDecoder.decode(info.subject, "UTF-8");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        holder.mode.setText(info.subject);
        ApiClient.displayImage(info.coverUrl, holder.backImage, R.drawable.goods_image_default);
        ApiClient.displayImage(info.photo, holder.circleImageView, R.drawable.goods_image_default);

        return convertView;
    }

    private HolderView holder;


    class HolderView {
        //姓名
        TextView name;
        //心情
        TextView mode;
        ImageView backImage;
        //弹幕
        TextView danmu;
        TextView item_number;
        CircleImageView circleImageView;


    }


}
