package com.cadyd.app.ui.view;

import android.app.AlertDialog;
import android.content.Context;
import android.view.Window;
import android.widget.TextView;
import com.cadyd.app.R;


/**
 * dialog提示框（也包含圆形提示框ProgressDialogUtil）
 * 
 * @author wangchaoyong
 * 
 */
public class ProgressDialogUtil {
	private AlertDialog pgd;
	private String msg;
	/**
	 * 设置提示信息
	 * 
	 * @param msg
	 */
	public void setMsg(String msg) {
		this.msg=msg;
	}

	/**
	 * 是否可以返回
	 * 
	 * @param falg
	 */
	public void setCanselable(boolean falg) {
		pgd.setCancelable(falg);
	}

	/**
	 * 默认是不能返回的
	 * 
	 * @param context
	 * @param msg
	 *            提示信息
	 */
	public ProgressDialogUtil(Context context, String msg) {
		pgd = new AlertDialog.Builder(context).create();
		pgd.setCancelable(false);
		this.msg=msg;
	}
	/**
	 * 构造方法
	 * 
	 * @param context
	 */
	public ProgressDialogUtil(Context context) {
		pgd = new AlertDialog.Builder(context).create();
		pgd.setCancelable(false);
	}
	/**
	 * 显示提示
	 */
	public void show() {
		if (pgd != null){
			pgd.show();
			Window window = pgd.getWindow();
			window.setContentView(R.layout.progress_dialog);
			TextView tv=(TextView)window.findViewById(R.id.progressBarText);
			tv.setText(msg);
		}
	}

	/**
	 * 隐藏提示
	 */
	public void hide() {
		// 显示则隐藏
		if (pgd.isShowing()) {
			pgd.cancel();
		}
	}

	public void dismissDialog(){
		// 显示则隐藏
		if (pgd.isShowing()) {
			pgd.dismiss();
		}
	}
}
