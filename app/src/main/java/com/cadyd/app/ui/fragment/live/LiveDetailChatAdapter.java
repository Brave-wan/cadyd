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
import com.cadyd.app.model.SocketMode;

import org.wcy.common.utils.DateUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: ${todo}(这里用一句话描述这个方法的作用)}$
 * 作者：Jerry
 * 邮箱：185214487@qq.com
 */
public class LiveDetailChatAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private Context mContext;
    private List<SocketMode> list = new ArrayList<>();

    public LiveDetailChatAdapter(Context mContext, List<SocketMode> list) {
        this.list = list;
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    public void setData(List<SocketMode> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    private viewHolder mViewHolder;
    private leftViewHolder mLeftViewHolder;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SocketMode info = list.get(position);
        if (info != null) {
            if (!info.getLiveUser()) {
                convertView = mInflater.inflate(R.layout.item_peoplesay_chat_right, null);
                mViewHolder = new viewHolder();
                TextView name = (TextView) convertView.findViewById(R.id.chat_right_name);
                TextView rightMsg = (TextView) convertView.findViewById(R.id.chat_right_msg);
                ImageView head = (ImageView) convertView.findViewById(R.id.chat_right_head);
                TextView time = (TextView) convertView.findViewById(R.id.chat_right_day);
                String data = DateUtil.getSpaceTime(info.getSendDate());
                time.setText(data);
                name.setText(info.getUserName());
                rightMsg.setText(info.getMessage());
                ApiClient.displayCirleImage(info.getHeadUrl(), head, R.mipmap.user_default);

            } else {
                convertView = mInflater.inflate(R.layout.item_peoplesay_chat_left, null);
                TextView name = (TextView) convertView.findViewById(R.id.chat_left_name);
                TextView leftMsg = (TextView) convertView.findViewById(R.id.chat_left_msg);
                ImageView head = (ImageView) convertView.findViewById(R.id.chat_right_head);
                TextView time = (TextView) convertView.findViewById(R.id.chat_left_day);

                String data = DateUtil.getSpaceTime(info.getSendDate());
                time.setText(data);
                name.setText(info.getUserName());
                leftMsg.setText(info.getMessage());
                ApiClient.displayCirleImage(info.getHeadUrl(), head, R.mipmap.user_default);
            }

        }
        return convertView;
    }

    class viewHolder {
        private TextView name;
        private TextView rightMsg;
        private ImageView rightImage;
    }

    class leftViewHolder {
        private TextView name;
        private TextView leftMsg;
        private ImageView leftImage;
    }
}
