package com.cadyd.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.cadyd.app.JConstant;
import com.cadyd.app.R;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.model.Good;
import com.cadyd.app.model.NewAnnounce;
import com.cadyd.app.ui.activity.CommonActivity;
import com.cadyd.app.ui.view.TimeTextView;
import com.cadyd.app.ui.view.countdown.CountdownView;

import java.util.ArrayList;
import java.util.List;

/**
 * 最新揭晓
 * Created by wcy on 2016/5/8.
 */
public class NewAnnounceAdapter extends BaseAdapter {
    private List<NewAnnounce> mList;// 定义一个list对象
    private Context mContext;// 上下文

    public static final int APP_PAGE_SIZE = 4;// 每一页装载数据的大小

    public NewAnnounceAdapter(Context context, List<NewAnnounce> list, int page) {
        this.mContext = context;
        this.page = page;
        mList = new ArrayList<NewAnnounce>();
        // 根据当前页计算装载的应用，每页只装载4个
        int i = page * APP_PAGE_SIZE;// 当前页的其实位置
        int iEnd = i + APP_PAGE_SIZE;// 所有数据的结束位置
        while ((i < list.size()) && (i < iEnd)) {
            mList.add(list.get(i));
            i++;
        }
    }

    private int page;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return 2;
    }

    @Override
    public NewAnnounce getItem(int arg0) {
        // TODO Auto-generated method stub
        return mList.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(final int arg0, View convertView, ViewGroup arg2) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.gridview_new_announce_item, null);
            viewHolder = new ViewHolder();
            viewHolder.image = (ImageView) convertView.findViewById(R.id.image);
            viewHolder.goodName = (TextView) convertView.findViewById(R.id.name);
            viewHolder.countdown_toast = (TextView) convertView.findViewById(R.id.countdown_toast);
            //特卖倒计时控件
            viewHolder.mTimeText = (CountdownView) convertView.findViewById(R.id.timeTextView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            resetViewHolder(viewHolder);
        }
        if (arg0 < mList.size()) {
            final NewAnnounce n = getItem(arg0);
            viewHolder.goodName.setText(n.title);
            ApiClient.displayImage(n.mainimg, viewHolder.image);
            viewHolder.mTimeText.setOnCountdownEndListener(new CountdownView.OnCountdownEndListener() {
                @Override
                public void onEnd(CountdownView cv) {
                    viewHolder.mTimeText.setVisibility(View.GONE);
                    viewHolder.countdown_toast.setVisibility(View.VISIBLE);
                }
            });
            if (n.countdown > 0) {
                viewHolder.mTimeText.setVisibility(View.VISIBLE);
                viewHolder.countdown_toast.setVisibility(View.GONE);
                viewHolder.mTimeText.start(n.countdown);
            } else {
                viewHolder.mTimeText.setVisibility(View.GONE);
                viewHolder.countdown_toast.setVisibility(View.VISIBLE);
            }
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, CommonActivity.class);
                    intent.putExtra(JConstant.EXTRA_INDEX, CommonActivity.NEW_ANNOUNCED);
                    intent.putExtra("newModel", n);
                    mContext.startActivity(intent);
                }
            });
        }else{
            viewHolder.image.setVisibility(View.INVISIBLE);
            viewHolder.goodName.setVisibility(View.INVISIBLE);
            viewHolder.countdown_toast.setVisibility(View.INVISIBLE);
            viewHolder.mTimeText.setVisibility(View.INVISIBLE);

        }
        return convertView;
    }

    class ViewHolder {
        ImageView image;
        TextView goodName;
        CountdownView mTimeText;
        public TextView countdown_toast;
    }

    protected void resetViewHolder(ViewHolder viewHolder) {
        viewHolder.image.setImageBitmap(null);
        viewHolder.goodName.setText("");

    }
}
