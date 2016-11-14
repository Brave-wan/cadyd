package com.cadyd.app.asynctask.retrofitmode;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.cadyd.app.MyApplication;
import com.cadyd.app.ui.activity.SignInActivity;
import com.cadyd.app.ui.view.AlertConfirmation;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import rx.Subscriber;

import static com.cadyd.app.asynctask.retrofitmode.ApiException.AUTHORIZATION_EXPIRED;
import static com.cadyd.app.asynctask.retrofitmode.ApiException.BUSINESSLEVEL_ERRORS;
import static com.cadyd.app.asynctask.retrofitmode.ApiException.BUSINESS_SUCCESS;
import static com.cadyd.app.asynctask.retrofitmode.ApiException.SYSTEM_ERROR;
import static com.cadyd.app.asynctask.retrofitmode.ApiException.confirmation;

/**
 * 用于在Http请求开始时，自动显示一个ProgressDialog
 * 在Http请求结束是，关闭ProgressDialog
 * 调用者自己对请求数据进行处理
 * 作者：Jerry
 * 邮箱：185214487@qq.com
 *
 * @time 16-9-28 下午3:23
 */
public class ProgressSubscriber<T> extends Subscriber<T> implements ProgressCancelListener {

    private SubscriberOnNextListener mSubscriberOnNextListener;
    private ProgressDialogHandler mProgressDialogHandler;

    private static Context context;

    public ProgressSubscriber(SubscriberOnNextListener mSubscriberOnNextListener, Context context) {
        this.mSubscriberOnNextListener = mSubscriberOnNextListener;
        this.context = context;
        mProgressDialogHandler = new ProgressDialogHandler(context, this, true);
    }

    private void showProgressDialog() {
        if (mProgressDialogHandler != null) {
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.SHOW_PROGRESS_DIALOG).sendToTarget();
        }
    }

    private void dismissProgressDialog() {
        if (mProgressDialogHandler != null) {
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.DISMISS_PROGRESS_DIALOG).sendToTarget();
            mProgressDialogHandler = null;
        }
    }

    /**
     * @author Jerry
     * @Description: 订阅开始时调用, 显示ProgressDialog
     */
    @Override
    public void onStart() {
        showProgressDialog();
    }

    /**
     * @author Jerry
     * @Description: 完成，隐藏ProgressDialog
     */
    @Override
    public void onCompleted() {
        dismissProgressDialog();
    }

    /**
     * @author Jerry
     * @Description: 对错误进行统一处理, 隐藏ProgressDialog
     */
    @Override
    public void onError(Throwable e) {
        if (e instanceof SocketTimeoutException) {
            Toast.makeText(context, "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
            Log.i("wan", "------请求超时------");
        } else if (e instanceof ConnectException) {
            Toast.makeText(context, "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
        } else {
            Log.i("wan", "请求error：" + e.getMessage());
        }
        mSubscriberOnNextListener.onError(e);
        dismissProgressDialog();

    }

    /**
     * @param t 创建Subscriber时的泛型类型
     * @author Jerry
     * @Description: 将onNext方法中的返回结果交给Activity或Fragment自己处理
     */
    @Override
    public void onNext(T t) {
        int code = ((BaseRequestInfo) t).getState();
        if (code != 100) {
            requestState((BaseRequestInfo) t);
        } else {
            if (mSubscriberOnNextListener != null) {
                Log.i("wan", "-----T------" + t.toString());
                mSubscriberOnNextListener.onNext(t);
            }
        }
    }

    /**
     * @author Jerry
     * @Description: 取消ProgressDialog的时候，取消对observable的订阅，同时也取消了http请求
     */
    @Override
    public void onCancelProgress() {
        if (!this.isUnsubscribed()) {
            this.unsubscribe();
        }
    }

    /**
     * 状态业务逻辑
     *
     * @param info
     */
    public void requestState(BaseRequestInfo info) {
        String message = info.getErrorMsg();
        int code = info.getState();
        switch (code) {
            //业务正确执行
            case BUSINESS_SUCCESS:
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
                mSubscriberOnNextListener.onLogicError(message);
                Toast.makeText(MyApplication.getInstance().getApplicationContext(), message != null ? message : "网络请求失败", Toast.LENGTH_SHORT).show();
                break;
        }

    }

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