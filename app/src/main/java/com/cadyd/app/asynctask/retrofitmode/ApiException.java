package com.cadyd.app.asynctask.retrofitmode;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.cadyd.app.MyApplication;
import com.cadyd.app.ui.activity.SignInActivity;
import com.cadyd.app.ui.view.AlertConfirmation;

/**
 * @Description: 请求异常封装
 * 作者：Jerry
 * 邮箱：185214487@qq.com
 * @time 16-9-28 下午3:28
 */
public class ApiException extends RuntimeException {

    public static final int USER_NOT_EXIST = 100;
    public static final int WRONG_PASSWORD = 101;

    public static final int BUSINESS_SUCCESS = 100;

    public static final int SYSTEM_ERROR = -100;

    public static final int AUTHORIZATION_EXPIRED = 401;

    public static final int BUSINESSLEVEL_ERRORS = 10000;

    public ApiException(@Nullable BaseRequestInfo info) {
        this(getApiExceptionMessage(info));
    }

    public ApiException(String detailMessage) {
        super(detailMessage);
    }

    /**
     * @author Jerry
     * @Description: 由于服务器传递过来的错误信息直接给用户看的话，用户未必能够理解,需要根据错误码对错误信息进行一个转换，在显示给用户
     * @time 16-9-28 下午3:29
     */
    private static String getApiExceptionMessage(@Nullable BaseRequestInfo info) {
        String message = info.getErrorMsg();
        int code = info.getState();
        switch (code) {
            //业务正确执行
            case BUSINESS_SUCCESS:
                Reregister();
                break;
            //系统严重错误
            case SYSTEM_ERROR:
                Toast.makeText(MyApplication.getInstance().getApplicationContext(), "提示网络异常，用户耐心等待", Toast.LENGTH_SHORT).show();
                break;

            //登录授权过期或者需要登录授权
            case AUTHORIZATION_EXPIRED:
                Reregister();
                break;

            //业务级错误，要求用户按照业务要求执行,服务端的错误信息，使用弹出框弹出给用户
            case BUSINESSLEVEL_ERRORS:
                Toast.makeText(MyApplication.getInstance().getApplicationContext(), message != null ? message : "网络请求失败", Toast.LENGTH_SHORT).show();
                break;

        }
        return message;
    }

    static AlertConfirmation confirmation;
    static Context context;

    /**
     * 重新登录
     */
    public static void Reregister() {
        if (confirmation != null && !context.equals(confirmation.getContext())) {
            confirmation = null;
        }
        if (confirmation == null) {
            confirmation = new AlertConfirmation(context, "掉线通知", "您已掉线是否重新登录!");
        }
        confirmation.show(new AlertConfirmation.OnClickListeners() {
            @Override
            public void ConfirmOnClickListener() {
                confirmation.hide();
                Intent intent = new Intent(context, SignInActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                context.startActivity(intent);
            }

            @Override
            public void CancelOnClickListener() {
                confirmation.hide();
            }
        });
    }
}

