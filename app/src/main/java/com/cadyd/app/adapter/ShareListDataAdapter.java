package com.cadyd.app.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.cadyd.app.R;
import com.cadyd.app.handle.InterFace;


/**
 * 分享分类
 *
 * @author wangchaoyong
 */
public class ShareListDataAdapter extends BaseAdapter {
    Activity mContext;
    InterFace.ShareType[] shares;

    public ShareListDataAdapter(Activity paramContext) {
        this.mContext = paramContext;
        shares = InterFace.ShareType.values();
    }

    public int getCount() {
        return this.shares.length;
    }

    public InterFace.ShareType getItem(int paramInt) {
        return shares[paramInt];
    }

    public long getItemId(int paramInt) {
        return paramInt;
    }

    public View getView(final int paramInt, View paramView, ViewGroup paramViewGroup) {
        ViewHolder localViewHolder;
        if (paramView == null) {
            localViewHolder = new ViewHolder();
            paramView = LayoutInflater.from(this.mContext).inflate(R.layout.share_list_rows, null);
            localViewHolder.name = ((TextView) paramView.findViewById(R.id.name));
            paramView.setTag(localViewHolder);
        } else {
            localViewHolder = (ViewHolder) paramView.getTag();
        }
        InterFace.ShareType t = getItem(paramInt);
        localViewHolder.name.setText(t.ename);
        localViewHolder.name.setCompoundDrawablesWithIntrinsicBounds(0, t.resid, 0, 0);
        localViewHolder.name.setTag(t);
        return paramView;

    }

    class ViewHolder {
        public TextView name;
    }
}