package com.cadyd.app.ui.view;

import android.content.Context;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.cadyd.app.JConstant;
import com.cadyd.app.R;
import com.cadyd.app.comm.ForPxTp;


/**
 * 安卓自定义Toast，支持多种情景
 *
 * @author wangchaoyong
 */
public class ToastView {
    private static Handler handler = new Handler();
    private static Toast toast;
    private static Runnable run = new Runnable() {
        public void run() {
            toast.cancel();
        }
    };

    public static void show(Context context, CharSequence text) {
        if (null == toast) {
            toast = new Toast(context);
            toast.setGravity(Gravity.TOP, 0, ForPxTp.dip2px(context, 50));
            toast.setDuration(JConstant.toast_time);
        }
        toast.setView(createTextView(context, text));
        handler.postDelayed(run, JConstant.toast_time);
        toast.show();
    }

    public static void show(Context context, int resId) {
        String text = context.getResources().getString(resId);
        show(context, text);
    }

    public static int getScreenW(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        dm = context.getResources().getDisplayMetrics();
        int w = dm.widthPixels;
        return w;
    }

    private static View createTextView(Context context, CharSequence text) {
        View view = View.inflate(context, R.layout.toast_view, null);
        TextView textView = (TextView) view.findViewById(R.id.message);
        textView.setText(text);
        textView.getBackground().setAlpha(150);
        return view;
    }
}
