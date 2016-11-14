package com.cadyd.app.comm;

import android.content.Context;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import com.cadyd.app.interfaces.OnLoginListener;
import com.cadyd.app.model.UserInfo;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;

public class LoginApi implements Callback {
	private static final int MSG_AUTH_CANCEL = 1;
	private static final int MSG_AUTH_ERROR= 2;
	private static final int MSG_AUTH_COMPLETE = 3;

	private OnLoginListener loginListener;
	private String platform;
	private Context context;
	private Handler handler;

	public LoginApi() {
		handler = new Handler(Looper.getMainLooper(), this);
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public void setOnLoginListener(OnLoginListener login){
		this.loginListener=login;
	}

	public void login(Context context) {
		this.context = context.getApplicationContext();
		if (platform == null) {
			return;
		}
		//初始化SDK
		ShareSDK.initSDK(context);
		Platform plat = ShareSDK.getPlatform(platform);
		if (plat == null) {
			return;
		}

		if (plat.isAuthValid()) {
			plat.removeAccount(true);
			return;
		}

		//使用SSO授权，通过客户单授权
		plat.SSOSetting(true);
		plat.setPlatformActionListener(new PlatformActionListener() {
			public void onComplete(Platform plat, int action, HashMap<String, Object> res) {
				if (action == Platform.ACTION_USER_INFOR) {
					Message msg = new Message();
					msg.what = MSG_AUTH_COMPLETE;
					msg.arg2 = action;
					msg.obj =  new Object[] {plat.getName(), res};
					handler.sendMessage(msg);
				}
			}

			public void onError(Platform plat, int action, Throwable t) {
				if (action == Platform.ACTION_USER_INFOR) {
					Message msg = new Message();
					msg.what = MSG_AUTH_ERROR;
					msg.arg2 = action;
					msg.obj = t;
					handler.sendMessage(msg);
				}
				t.printStackTrace();
			}

			public void onCancel(Platform plat, int action) {
				if (action == Platform.ACTION_USER_INFOR) {
					Message msg = new Message();
					msg.what = MSG_AUTH_CANCEL;
					msg.arg2 = action;
					msg.obj = plat;
					handler.sendMessage(msg);
				}
			}
		});
		plat.showUser(null);
	}

	/**处理操作结果*/
	public boolean handleMessage(Message msg) {
		switch(msg.what) {
			case MSG_AUTH_CANCEL: {
				// 取消
				Toast.makeText(context, "取消登录", Toast.LENGTH_SHORT).show();
			} break;
			case MSG_AUTH_ERROR: {
				// 失败
				Throwable t = (Throwable) msg.obj;
				String text = "登录失败: " + t.getMessage();
				Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
				t.printStackTrace();
			} break;
			case MSG_AUTH_COMPLETE: {
				// 成功
				Object[] objs = (Object[]) msg.obj;
				String plat = (String) objs[0];
				Platform platform1 = ShareSDK.getPlatform(plat);
				UserInfo userInfo = new UserInfo();
				String gender = platform1.getDb().getUserGender();
				if("m".equals(gender)){
					userInfo.setUserGender("男");
				}else{
					userInfo.setUserGender("女");
				}
				userInfo.setUserIcon(platform1.getDb().getUserIcon());
				userInfo.setUserName(platform1.getDb().getUserName());
				userInfo.setToken(platform1.getDb().getToken());
				loginListener.onLogin(userInfo);
			} break;
		}
		return false;
	}

}
