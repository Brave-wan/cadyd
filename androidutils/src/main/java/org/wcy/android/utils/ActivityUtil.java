package org.wcy.android.utils;

import org.wcy.common.utils.StringUtil;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * :Activity特性辅助工具
 *
 * @author wangchaoyong
 * @version 1.0
 * @created 2014-9-4
 */
public class ActivityUtil {
    /**
     * 应用于Activity的获取控件实例
     *
     * @param activity
     * @param id
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T extends View> T findViewById(Activity activity, int id) {
        return (T) activity.findViewById(id);
    }

    /**
     * 应用于View的获取控件实例
     *
     * @param view
     * @param id
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T extends View> T findViewById(View view, int id) {
        return (T) view.findViewById(id);
    }


    /**
     * title : 设置Activity全屏显示 description :设置Activity全屏显示。
     *
     * @param activity Activity引用
     * @param isFull   true为全屏，false为非全屏
     */
    public static void setFullScreen(Activity activity, boolean isFull) {
        Window window = activity.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        if (isFull) {
            params.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
            window.setAttributes(params);
            window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        } else {
            params.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
            window.setAttributes(params);
            window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }

    /**
     * title : 隐藏系统标题栏 description :隐藏Activity的系统默认标题栏
     *
     * @param activity Activity对象
     */
    public static void hideTitleBar(Activity activity) {
        activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    /**
     * title : 设置Activity的显示方向为垂直方向 description :强制设置Actiity的显示方向为垂直方向。
     *
     * @param activity Activity对象
     */
    public static void setScreenVertical(Activity activity) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    /**
     * title : 隐藏软件输入法 description :隐藏软件输入法 time : 2012-7-12 下午7:20:00
     *
     * @param activity
     */
    public static void hideSoftInput(Activity activity) {
        if (activity.getCurrentFocus() == null) {
            return;
        }

        ((InputMethodManager) activity.getSystemService(activity.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(activity
                .getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }


    /**
     * 取得edittext的值转换成Integer
     *
     * @param et
     * @return
     */
    public static Integer getInt(EditText et) {
        String str = et.getText().toString();
        if (StringUtil.hasText(str)) {
            return Integer.parseInt(str);
        } else {
            return 0;
        }
    }

    /**
     * 判断输入框是否为null
     *
     * @param et
     * @return
     */
    public static boolean isEditTextNull(EditText et) {
        return StringUtil.hasText(et.getText().toString());
    }
}
