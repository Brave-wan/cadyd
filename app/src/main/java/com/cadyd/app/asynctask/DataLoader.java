package com.cadyd.app.asynctask;

public interface DataLoader<T> {
	public void task(String data);
	public void succeed(T data);
	public void error(String message);
}
