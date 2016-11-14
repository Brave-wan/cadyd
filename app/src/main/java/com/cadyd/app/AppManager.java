package com.cadyd.app;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import java.util.Stack;

/**
 * 应用程序Activity管理类：用于Activity管理和应用程序退出
 *
 * @author wangchayong
 */
public class AppManager {

    private static Stack<Activity> activityStack;
    private static Stack<Fragment> fragmentStack;
    private static AppManager instance;

    private AppManager() {
    }

    /**
     * 单一实例
     */
    public static AppManager getAppManager() {
        if (instance == null) {
            instance = new AppManager();
        }
        return instance;
    }

    /**
     * 添加fragment到堆栈
     */
    public void addFragment(Fragment fragment) {
        if (fragmentStack == null) {
            fragmentStack = new Stack<Fragment>();
        }
        fragmentStack.add(fragment);
    }


    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }

        boolean s = activityStack.add(activity);
    }



    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() {
        Activity activity = activityStack.lastElement();
        finishActivity(activity);
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束指定的fragment
     */
    public void finishFragment(Fragment fragment, FragmentTransaction ft) {
        if (fragmentStack != null && fragmentStack.size() > 0) {
            for (int i = 0; i < fragmentStack.size(); i++) {
                Fragment fragment1 = fragmentStack.get(i);
                if (fragment1 != null) {
                    if (fragment1.getClass().equals(fragment)) {
                        ft.remove(fragment);
                        ft.commit();
                        fragmentStack.remove(fragment);
                    }
                }
            }
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        if (activityStack != null && activityStack.size() > 0) {
            for (int i = 0; i < activityStack.size(); i++) {
                Activity activity = activityStack.get(i);
                if (activity != null) {
                    if (activity.getClass().equals(cls)) {
                        activityStack.remove(activity);
                        activity.finish();
                    }
                }
            }
        }
    }

    /**
     * 结束所有Activity,但不包含传入进来的
     */
    public void finishAllActivity(Class<?> cls) {
        if (activityStack != null && activityStack.size() > 0) {
            for (int i = 0; i < activityStack.size(); i++) {
                Activity activity = activityStack.get(i);
                if (activity != null) {
                    if (!activityStack.firstElement().equals(activity)) {
                        activity.finish();
                    }
                }
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
    }

    /**
     * 退出应用程序
     */
    public void AppExit() {
        try {
            finishAllActivity();
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(10);
        } catch (Exception e) {
        }
    }
}