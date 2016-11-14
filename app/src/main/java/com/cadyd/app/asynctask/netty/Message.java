package com.cadyd.app.asynctask.netty;

import java.util.HashMap;
import java.util.Map;

public class Message {

	// 消息头
	private Head head = new Head();

	// 消息体
	private Map<String, Object> data = new HashMap<String, Object>();
	private String sign;

	public Head getHead() {
		return head;
	}

	public Message setHead(Head head) {
		this.head = head;
		return this;
	}

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public int optInt(String key) {
		try {
			return Integer.parseInt(optStr(key));
		} catch (Exception e) {
			return 0;
		}
	}

	public int optInt(String key, int defValue) {
		try {
			return Integer.parseInt(optStr(key));
		} catch (Exception e) {
			return defValue;
		}
	}

	public Long optLong(String key) {
		try {
			return Long.parseLong(optStr(key));
		} catch (Exception e) {
			return 0L;
		}
	}

	public boolean optBoolean(String key) {
		try {
			return Boolean.parseBoolean("" + this.data.get(key));
		} catch (Exception e) {
			return false;
		}
	}

	public String optStr(String key) {
		if (this.data.get(key) == null) {
			return "";
		}
		return this.data.get(key).toString();
	}

}
