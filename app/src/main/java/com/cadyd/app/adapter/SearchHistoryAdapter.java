package com.cadyd.app.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.cadyd.app.R;

/**
 * 搜索历史
 * @author wangchaoyong
 *
 */
public class SearchHistoryAdapter extends BaseAdapter {
	Context mContext;
	List<String> mList;

	public SearchHistoryAdapter(List<String> paramList, Context paramContext) {
		this.mList = paramList;
		this.mContext = paramContext;
	}

	public int getCount() {
		return this.mList.size();
	}

	public String getItem(int paramInt) {
		return  this.mList.get(paramInt);
	}

	public long getItemId(int paramInt) {
		return paramInt;
	}

	public View getView(int paramInt, View paramView, ViewGroup paramViewGroup) {
		ViewHolder localViewHolder;
		if (paramView == null) {
			localViewHolder = new ViewHolder();
			paramView = LayoutInflater.from(this.mContext).inflate(R.layout.search_history_list_rows, null);
			localViewHolder.name = (TextView)paramView;
			paramView.setTag(localViewHolder);
		} else {
			localViewHolder = (ViewHolder) paramView.getTag();
		}
		localViewHolder.name.setText(getItem(paramInt));
		return paramView;

	}

	class ViewHolder {
		public TextView name;
	}
}