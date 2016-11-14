package com.cadyd.app.asynctask.netty;


/**
 * 返回页面结果集
 * 
 * @author wubin
 * 
 */
public class ResultData {
	/**
	 * 数据集
	 */
	private Object data;
	/**
	 * 状态
	 */
	private int state;
	/**
	 * 错误消息
	 */
	private String errorMsg;

	public void setState(int state) {
		this.state = state;
	}

	public int getState() {
		return state;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public void setState(int state, String msg) {
		if (msg.substring(0, 3).matches("^[a-zA-Z]*")) {
			msg = "数据异常";
		}
		this.state = state;
		this.errorMsg = msg;
	}

}
