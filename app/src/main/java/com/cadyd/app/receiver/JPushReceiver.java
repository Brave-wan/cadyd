package com.cadyd.app.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by SCH-1 on 2016/8/12.
 */
public class JPushReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("cn.jpush.android.intent.MESSAGE_RECEIVED")) {
            //自定义消息
            String msg = intent.getExtras().getString(JPushInterface.EXTRA_MESSAGE);
            System.out.println(msg);
            Log.i("wan","JpushInterface-------"+msg);
        } else if (intent.getAction().equals("cn.jpush.android.intent.REGISTRATION")) {
            String registerId = intent.getExtras().getString(JPushInterface.EXTRA_REGISTRATION_ID);
        } else if (intent.getAction().equals("cn.jpush.android.intent.NOTIFICATION_OPENED")) {
            //TODO 用户点击通知栏后跳转
        }
    }
}
