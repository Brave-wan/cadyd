package com.cadyd.app.asynctask.netty;

public class Head {
	/**
	 * 用户ID
	 */
//	public String userid;
	/**
	 * 执行方法入口
	 */
	private String operation;

	/**
	 * 请求类型
	 */
	private String contentType;

	/**
	 * token
	 */
	private String security;
	/**
	 * 版本号
	 */
	private int ver;
	/**
	 * 设备类型 1 ios,2 android
	 */
	private int deviceType;
	/**
	 * 设备版本 Android4.4，IOS9
	 */
	private String deviceVer;
	/**
	 * 设备ID唯一标识
	 */
	private String deviceId;
	/**
	 * 打包渠道Id
	 */
	private int channelId;

	/**
	 * 来宾用户，临时用户
	 */
	private boolean guest;
	/**
	 * 请求时间戳，检查用取位毫秒
	 */
	private Long time;
	/**
	 * 内部请求SOCKET时间戳，检查用取位毫秒
	 */
	private Long innerTime;

	private String ip;

	public int getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(int deviceType) {
		this.deviceType = deviceType;
	}

	public String getDeviceVer() {
		return deviceVer;
	}

	public void setDeviceVer(String deviceVer) {
		this.deviceVer = deviceVer;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public boolean isGuest() {
		return guest;
	}

	public void setGuest(boolean guest) {
		this.guest = guest;
	}

	public int getChannelId() {
		return channelId;
	}

	public void setChannelId(int channelId) {
		this.channelId = channelId;
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	public Long getInnerTime() {
		return innerTime;
	}

	public void setInnerTime(Long innerTime) {
		this.innerTime = innerTime;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getSecurity() {
		return security;
	}

	public void setSecurity(String security) {
		this.security = security;
	}

	public int getVer() {
		return ver;
	}

	public void setVer(int ver) {
		this.ver = ver;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

//	public String getUserid() {
//		return userid;
//	}
//
//	public void setUserid(String userid) {
//		this.userid = userid;
//	}

}
