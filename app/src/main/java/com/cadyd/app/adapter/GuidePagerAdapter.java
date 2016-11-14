package com.cadyd.app.adapter;

import java.util.List;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

/**
 * 自定义PagerAdapter，实现引导页
 * 
 * @author wangchaoyong
 * 
 */
public class GuidePagerAdapter extends PagerAdapter {
	private final String TAG = "GuidePagerAdapter";
	/** 界面列表 */
	private List<View> views = null;
	/** 上下文内容 */

	public GuidePagerAdapter(List<View> views, Context context) {
		this.views = views;
	}

	/*
	 * 销毁arg1位置的界面(non-Javadoc)
	 * 
	 * @see android.support.v4.view.PagerAdapter#destroyItem(android.view.View,
	 * int, java.lang.Object)
	 */
	@Override
	public void destroyItem(View container, int position, Object object) {
		((ViewPager) container).removeView(views.get(position));
	}

	/*
	 * 获得当前界面数(non-Javadoc)
	 * 
	 * @see android.support.v4.view.PagerAdapter#getCount()
	 */
	@Override
	public int getCount() {
		return views.size();
	}

	/*
	 * 初始化arg1位置的界面(non-Javadoc)
	 * 
	 * @see android.support.v4.view.PagerAdapter#instantiateItem(android.view
	 * .View, int)
	 */
	@Override
	public Object instantiateItem(View container, int position) {
		Log.i(TAG, "==instantiateItem()");
		((ViewPager) container).addView(views.get(position), 0);
		if (position == views.size() - 1) {
			
		}
		return views.get(position);
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return (arg0 == arg1);
	}
}
