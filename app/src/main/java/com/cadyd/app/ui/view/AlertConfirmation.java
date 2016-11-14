package com.cadyd.app.ui.view;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.cadyd.app.R;
import com.cadyd.app.ui.view.dialog.effects.Effectstype;

import org.wcy.common.utils.StringUtil;

/**
 * 公用弹出提示框
 *
 * @author wcy
 */
public class AlertConfirmation {
    NiftyDialogBuilder pgd;
    private String confirm = "确定";
    private String cancel = "取消";
    private boolean cancelable = true;
    private String title;
    private String content;
    private Context context;
    private Effectstype effectstype = Effectstype.RotateBottom;

    public Context getContext() {
        return context;
    }

    /**
     * @param context 上下文
     * @param title   标题
     * @param content 内容
     */
    public AlertConfirmation(Context context, String title, String content) {
        pgd = NiftyDialogBuilder.getInstance(context);
        this.context = context;
        this.title = title;
        this.content = content;
    }

    /**
     * @param context 上下文
     * @param title   标题
     * @param content 内容
     * @param confirm 确定按钮文字
     * @param cancel  取消按钮文字
     */
    public AlertConfirmation(Context context, String title, String content, String confirm, String cancel) {
        pgd = NiftyDialogBuilder.getInstance(context);
        this.context = context;
        this.title = title;
        this.content = content;
        this.confirm = confirm;
        this.cancel = cancel;
    }

    public void show(final OnClickListeners listerer) {
        pgd.withEffect(effectstype).setCancelable(cancelable);
        View window = View.inflate(context, R.layout.alert_confirmation, null);
        TextView title_tv = (TextView) window.findViewById(R.id.title);
        TextView content_tv = (TextView) window.findViewById(R.id.content);
        Button confirm_bt = (Button) window.findViewById(R.id.confirm);
        Button cancel_bt = (Button) window.findViewById(R.id.cancel);
        if (StringUtil.hasText(title)) {
            title_tv.setText(title);
        } else {
            title_tv.setVisibility(View.GONE);
        }
        content_tv.setText(content);
        confirm_bt.setText(confirm);
        cancel_bt.setText(cancel);
        confirm_bt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listerer.ConfirmOnClickListener();
            }
        });
        cancel_bt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listerer.CancelOnClickListener();
            }
        });
        pgd.setView(window);
        if (!pgd.isShowing()) {
            pgd.show();
        }
    }

    public void setEffectstype(Effectstype effectstype) {
        this.effectstype = effectstype;
    }

    public void setConfirm(String confirm) {
        this.confirm = confirm;
    }

    public void setCancel(String cancel) {
        this.cancel = cancel;
    }

    public void setCancelable(boolean cancelable) {
        this.cancelable = cancelable;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
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


    public boolean isShow() {
        if (pgd == null&&!pgd.isShowing()) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 按钮点击接口
     */
    public interface OnClickListeners {
        public void ConfirmOnClickListener();

        public void CancelOnClickListener();
    }
}
