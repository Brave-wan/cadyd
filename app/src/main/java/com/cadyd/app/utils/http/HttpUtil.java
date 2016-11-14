package com.cadyd.app.utils.http;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.UnsupportedEncodingException;

/**
 * HttpUtil.
 * @author wcy
 */
public class HttpUtil {

	private static AsyncHttpClient asyncHttpClient = null;

	private static AsyncHttpClient getInstance() {
		if (asyncHttpClient == null) {
			asyncHttpClient = new AsyncHttpClient();
		}
		return asyncHttpClient;
	}

	/**
	 *http post 文件上传
	 * @param url 接口地址
	 * @param requestParams 请求参数
	 * @param httpCallBack 回调函数
	 */
	public static void post(String url, RequestParams requestParams, final HttpCallBack httpCallBack) {
		AsyncHttpClient asyncHttpClient = getInstance();
		asyncHttpClient.setTimeout(10*60*1000);
		asyncHttpClient.post(url, requestParams, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int i, org.apache.http.Header[] headers, byte[] bytes) {

				try {
					String msg = new String(bytes, "utf-8");
					httpCallBack.onSuccess(msg);

				} catch (UnsupportedEncodingException e) {
					httpCallBack.onFaild(e.getMessage());
				}
			}

			@Override
			public void onFailure(int i, org.apache.http.Header[] headers, byte[] bytes, Throwable throwable) {
				try {
					httpCallBack.onFaild(new String(bytes, "utf-8"));
				} catch (UnsupportedEncodingException e) {
					httpCallBack.onFaild(e.getMessage());
				}
			}
		});
	}


}
