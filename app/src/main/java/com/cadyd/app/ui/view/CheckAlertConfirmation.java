package com.cadyd.app.ui.view;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import com.cadyd.app.R;
import com.cadyd.app.ui.view.dialog.effects.Effectstype;
import org.wcy.common.utils.StringUtil;

/**
 * 公用弹出提示框
 *
 * @author wcy
 */
public class CheckAlertConfirmation {
    NiftyDialogBuilder pgd;
    private String confirm = "确定";
    private String cancel = "取消";
    private boolean cancelable = true;
    private String title;
    private Context context;
    private Effectstype effectstype = Effectstype.RotateBottom;

    /**
     * @param context 上下文
     * @param title   标题
     */
    public CheckAlertConfirmation(Context context, String title) {
        pgd = NiftyDialogBuilder.getInstance(context);
        this.context = context;
        this.title = title;
    }

    /**
     * @param context 上下文
     * @param title   标题
     * @param confirm 确定按钮文字
     * @param cancel  取消按钮文字
     */
    public CheckAlertConfirmation(Context context, String title, String confirm, String cancel) {
        pgd = NiftyDialogBuilder.getInstance(context);
        this.title = title;
        this.confirm = confirm;
        this.cancel = cancel;
    }

    private String content;
    private String leftText = "男";
    private String rightText = "女";

    public void show(final OnClickListeners listerer) {
        pgd.withEffect(effectstype).setCancelable(cancelable);
        View window = View.inflate(context, R.layout.check_alert_confirmation, null);
        TextView title_tv = (TextView) window.findViewById(R.id.title);
        //内容
        RadioGroup radioGroup = (RadioGroup) window.findViewById(R.id.radio_group);
        final RadioButton leftButton = (RadioButton) window.findViewById(R.id.radio_left_button);
        final RadioButton rightButton = (RadioButton) window.findViewById(R.id.radio_right_button);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_left_button:
                        content = leftButton.getText().toString();
                        break;
                    case R.id.radio_right_button:
                        content = rightButton.getText().toString();
                        break;
                }
            }
        });
        final Button confirm_bt = (Button) window.findViewById(R.id.confirm);
        Button cancel_bt = (Button) window.findViewById(R.id.cancel);
        if (StringUtil.hasText(title)) {
            title_tv.setText(title);
        } else {
            title_tv.setVisibility(View.GONE);
        }
        confirm_bt.setText(confirm);
        cancel_bt.setText(cancel);
        leftButton.setText(leftText);
        rightButton.setText(rightText);
        confirm_bt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listerer.ConfirmOnClickListener(content);
            }
        });
        cancel_bt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listerer.CancelOnClickListener();
            }
        });
        pgd.setView(window).show();
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

    public void setCheckButtontext(String left, String right) {
        this.leftText = left;
        this.rightText = right;
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
        public void ConfirmOnClickListener(String string);

        public void CancelOnClickListener();
    }
}
