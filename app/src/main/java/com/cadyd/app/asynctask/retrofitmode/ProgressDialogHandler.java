package com.cadyd.app.asynctask.retrofitmode;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.cadyd.app.ui.view.ProgressDialogUtil;


/**
 * 作者：Jerry
 * 邮箱：185214487@qq.com
 *
 * @time 16-9-28 下午4:01
 */
public class ProgressDialogHandler extends Handler {

    public static final int SHOW_PROGRESS_DIALOG = 1;
    public static final int DISMISS_PROGRESS_DIALOG = 2;
    private ProgressDialogUtil dialog;
    private ProgressDialog pd;

    private Context context;
    private boolean cancelable;
    private ProgressCancelListener mProgressCancelListener;

    public ProgressDialogHandler(Context context, ProgressCancelListener mProgressCancelListener,
                                 boolean cancelable) {
        super();
        this.context = context;
        this.mProgressCancelListener = mProgressCancelListener;
        this.cancelable = cancelable;
    }

    private void initProgressDialog() {
        dialog = new ProgressDialogUtil(context, "加载中...");
        dialog.setCanselable(cancelable);
        dialog.show();
    }


    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case SHOW_PROGRESS_DIALOG:
                initProgressDialog();
                break;
            case DISMISS_PROGRESS_DIALOG:
                if (dialog != null) {
                    dialog.hide();
                }
                break;
        }
    }

}
