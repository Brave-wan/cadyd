package com.cadyd.app.asynctask.retrofitmode;


/**
 * @Description: ${todo}(这里用一句话描述这个方法的作用)}$
 * 作者：Jerry
 * 邮箱：185214487@qq.com
 * @time 16-9-28 下午4:11
 */
public interface SubscriberOnNextListener<T> {
    void onNext(T t);
    void onError(Throwable e);
    void onLogicError(String msg);
}
