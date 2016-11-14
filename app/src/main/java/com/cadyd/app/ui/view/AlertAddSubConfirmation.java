package com.cadyd.app.ui.view;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.cadyd.app.R;
import com.cadyd.app.ui.view.dialog.effects.Effectstype;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 公用弹出提示框
 *
 * @author wcy
 */
public class AlertAddSubConfirmation {
    NiftyDialogBuilder pgd;
    private String confirm = "确定";
    private String cancel = "取消";
    private boolean cancelable = true;
    private Context context;
    private Effectstype effectstype = Effectstype.RotateBottom;

    /**
     * @param context 上下文
     */
    public AlertAddSubConfirmation(Context context) {
        pgd = NiftyDialogBuilder.getInstance(context);
        this.context = context;
    }

    /**
     * @param context 上下文
     * @param confirm 确定按钮文字
     * @param cancel  取消按钮文字
     */
    public AlertAddSubConfirmation(Context context, String confirm, String cancel) {
        pgd = NiftyDialogBuilder.getInstance(context);
        this.context = context;
        this.confirm = confirm;
        this.cancel = cancel;
    }

    EditText editText;

    public void show(int number, final OnClickListeners listerer) {
        pgd.withEffect(effectstype).setCancelable(cancelable);
        View window = View.inflate(context, R.layout.alert_add_sub, null);
        final AddAndSubView addAndSubView = (AddAndSubView) window.findViewById(R.id.add_sub_view);
        addAndSubView.setNum(number);
        editText = addAndSubView.editText;
        TextView title_tv = (TextView) window.findViewById(R.id.title);
        Button confirm_bt = (Button) window.findViewById(R.id.confirm);
        Button cancel_bt = (Button) window.findViewById(R.id.cancel);
        confirm_bt.setText(confirm);
        cancel_bt.setText(cancel);
        confirm_bt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listerer.ConfirmOnClickListener(addAndSubView.getNum());
            }
        });
        cancel_bt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listerer.CancelOnClickListener();
            }
        });
        pgd.setView(window).show();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                showKeyboard();
            }
        }, 1000);
    }

    public void showKeyboard() {
        if (editText != null) {
            //设置可获得焦点
            editText.setFocusable(true);
            editText.setFocusableInTouchMode(true);
            //请求获得焦点
            editText.requestFocus();
            //调用系统输入法
            InputMethodManager inputManager = (InputMethodManager) editText
                    .getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.showSoftInput(editText, 0);
        }
    }

    public void setConfirm(String confirm) {
        this.confirm = confirm;
    }

    public void setCancel(String cancel) {
        this.cancel = cancel;
    }


    /**
     * 隐藏提示
     */
    public void hide() {
        // 显示则隐藏
        if (pgd != null && pgd.isShowing()) {
            pgd.dismiss();
        }
    }


    /**
     * 按钮点击接口
     */
    public interface OnClickListeners {
        public void ConfirmOnClickListener(int number);

        public void CancelOnClickListener();
    }
}
