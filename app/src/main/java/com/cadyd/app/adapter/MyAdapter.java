package com.cadyd.app.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.cadyd.app.R;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.interfaces.TowObjectParameterInterface;
import com.cadyd.app.model.NewAnnounce;
import com.cadyd.app.ui.view.countdown.CountdownView;

import java.util.List;

/**
 * Created by wcy on 2016/6/1.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    public List<NewAnnounce> datas = null;
    private TowObjectParameterInterface towObjectParameterInterface;

    public MyAdapter(List<NewAnnounce> datas) {
        this.datas = datas;
    }

    public void setClick(TowObjectParameterInterface towObjectParameterInterface) {
        this.towObjectParameterInterface = towObjectParameterInterface;
    }

    //创建新View，被LayoutManager所调用
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_new_announce_item, viewGroup, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        NewAnnounce n = datas.get(position);
        viewHolder.bindData(n);
    }

    //获取数据的数量
    @Override
    public int getItemCount() {
        return datas.size();
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;
        public ImageView mImageView;
        public CountdownView mTimeTextView;
        public View public_View;
        public TextView countdown_toast;
        private NewAnnounce mItemInfo;

        public ViewHolder(View view) {
            super(view);
            mTextView = (TextView) view.findViewById(R.id.name);
            mImageView = (ImageView) view.findViewById(R.id.image);
            mTimeTextView = (CountdownView) view.findViewById(R.id.timeTextView);
            countdown_toast = (TextView) view.findViewById(R.id.countdown_toast);
            public_View = view;
        }

        public void bindData(NewAnnounce itemInfo) {
            mItemInfo = itemInfo;
            mTimeTextView.setOnCountdownEndListener(new CountdownView.OnCountdownEndListener() {
                @Override
                public void onEnd(CountdownView cv) {
                    mTimeTextView.setVisibility(View.GONE);
                    countdown_toast.setVisibility(View.VISIBLE);
                }
            });
            mTextView.setText(itemInfo.title);
            public_View.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (towObjectParameterInterface != null) {
                        towObjectParameterInterface.Onchange(0, getPosition(), mItemInfo);
                    }
                }
            });
            ApiClient.displayImage(itemInfo.mainimg, mImageView);
            if (itemInfo.countdown > 0) {
                mTimeTextView.setVisibility(View.VISIBLE);
                countdown_toast.setVisibility(View.GONE);
                mTimeTextView.start(itemInfo.countdown);
            } else {
                mTimeTextView.setVisibility(View.GONE);
                countdown_toast.setVisibility(View.VISIBLE);
            }
        }
    }
}