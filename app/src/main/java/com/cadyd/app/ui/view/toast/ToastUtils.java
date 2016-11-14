package com.cadyd.app.ui.view.toast;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cadyd.app.JConstant;
import com.cadyd.app.R;
import com.cadyd.app.ui.view.ToastView;


/**
 * 安卓自定义Toast，支持多种情景
 *
 * @author wangchaoyong
 */
public class ToastUtils {
    private static Toast toast;

    public static final int ERROR_TYPE = 1;
    public static final int SUCCESS_TYPE = 2;
    public static final int WARNING_TYPE = 3;
    public static final int NO = 0;
    private static Animation mSuccessBowAnim;
    private static AnimationSet mSuccessLayoutAnimSet;
    private static Animation mErrorInAnim;
    private static AnimationSet mErrorXInAnim;

    public static void show(Context context, CharSequence text, int type) {
        View view;
        if (toast == null) {
            toast = new Toast(context);
            mSuccessBowAnim = OptAnimationLoader.loadAnimation(context, R.anim.success_bow_roate);
            mSuccessLayoutAnimSet = (AnimationSet) OptAnimationLoader.loadAnimation(context, R.anim.success_mask_layout);
            mErrorInAnim = OptAnimationLoader.loadAnimation(context, R.anim.error_frame_in);
            mErrorXInAnim = (AnimationSet) OptAnimationLoader.loadAnimation(context, R.anim.error_x_in);
            view = View.inflate(context, R.layout.alert_toast, null);
            toast.setGravity(Gravity.CENTER,0,0);
        } else {
            view = toast.getView();
        }
        if (type == NO) {
            ToastView.show(context, text);
        } else {
            toast.setDuration(JConstant.toast_time);
            toast.setView(createTextView(context, view, text, type));
            toast.show();
        }

    }

    public static void show(Context context, int resId, int type) {
        String text = context.getResources().getString(resId);
        show(context, text, type);
    }

    public static int getScreenW(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        dm = context.getResources().getDisplayMetrics();
        int w = dm.widthPixels;
        return w;
    }

    private static View createTextView(Context context, View view, CharSequence text, int type) {
        FrameLayout error_frame = (FrameLayout) view.findViewById(R.id.error_frame);
        FrameLayout success_frame = (FrameLayout) view.findViewById(R.id.success_frame);
        FrameLayout warning_frame = (FrameLayout) view.findViewById(R.id.warning_frame);
        View mSuccessLeftMask = view.findViewById(R.id.mask_left);
        View mSuccessRightMask = view.findViewById(R.id.mask_right);
        ImageView mErrorX = (ImageView) view.findViewById(R.id.error_x);
        // 成功
        SuccessTickView mSuccessTick = (SuccessTickView) view.findViewById(R.id.success_tick);
        error_frame.clearAnimation();
        mErrorX.clearAnimation();
        mSuccessLeftMask.clearAnimation();
        mSuccessTick.clearAnimation();
        mSuccessRightMask.clearAnimation();
        TextView textView = (TextView) view.findViewById(R.id.message);
        textView.setText(text);
        switch (type) {
            case ERROR_TYPE:
                success_frame.setVisibility(View.GONE);
                warning_frame.setVisibility(View.GONE);
                error_frame.setVisibility(View.VISIBLE);
                // 失败
                error_frame.startAnimation(mErrorInAnim);
                mErrorX.startAnimation(mErrorXInAnim);
                break;
            case SUCCESS_TYPE:
                warning_frame.setVisibility(View.GONE);
                error_frame.setVisibility(View.GONE);
                success_frame.setVisibility(View.VISIBLE);
                mSuccessLeftMask.startAnimation(mSuccessLayoutAnimSet.getAnimations().get(0));
                // mSuccessRightMask.startAnimation(mSuccessLayoutAnimSet.getAnimations().get(1));
                mSuccessTick.startTickAnim();
                mSuccessRightMask.startAnimation(mSuccessBowAnim);
                break;
            case WARNING_TYPE:
                success_frame.setVisibility(View.GONE);
                error_frame.setVisibility(View.GONE);
                warning_frame.setVisibility(View.VISIBLE);
                break;
            case NO:
                success_frame.setVisibility(View.GONE);
                error_frame.setVisibility(View.GONE);
                warning_frame.setVisibility(View.GONE);
                break;
        }

        return view;
    }
}
