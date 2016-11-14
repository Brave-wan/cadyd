package com.cadyd.app.utils.http;

/**
 * HttpCallBack:httpCallBack interface.
 *
 * @author spengwu
 * @since Feb. 03, 2016
 */
public interface HttpCallBack {

	public void onSuccess(String result);

	public void onFaild(String result);
}
