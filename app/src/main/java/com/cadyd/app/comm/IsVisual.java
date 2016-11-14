package com.cadyd.app.comm;

import android.app.Activity;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.View;
import android.widget.ImageView;
import com.cadyd.app.R;

import java.util.Arrays;

/**
 * Created by xiongmao on 2016/7/9.
 * 控件是否可视
 */
public class IsVisual {

    public static void IsView(Activity activity, View view) {
        int screenWidth;
        int screenHeight;
        Point p = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(p);
        screenWidth = p.x;
        screenHeight = p.y;

        Rect rect = new Rect(0, 0, screenWidth, screenHeight);

        int[] location = new int[2];
        view.getLocationInWindow(location);

        System.out.println(Arrays.toString(location));
        // Rect ivRect=new Rect(imageView.getLeft(),imageView.getTop(),imageView.getRight(),imageView.getBottom());
        if (view.getLocalVisibleRect(rect)) {/*rect.contains(ivRect)*/
            System.out.println("---------控件在屏幕可见区域-----显现-----------------");
        } else {
            System.out.println("---------控件已不在屏幕可见区域（已滑出屏幕）-----隐去-----------------");
        }
    }

    public static void IsView(Activity activity, ImageView imageView) {
        int screenWidth;
        int screenHeight;
        Point p = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(p);
        screenWidth = p.x;
        screenHeight = p.y;

        Rect rect = new Rect(0, 0, screenWidth, screenHeight);

        int[] location = new int[2];
        imageView.getLocationInWindow(location);

        System.out.println(Arrays.toString(location));
        // Rect ivRect=new Rect(imageView.getLeft(),imageView.getTop(),imageView.getRight(),imageView.getBottom());
        if (imageView.getLocalVisibleRect(rect)) {/*rect.contains(ivRect)*/
            System.out.println("---------控件在屏幕可见区域-----显现-----------------");
        } else {
            imageView.setBackgroundResource(R.mipmap.penny);
            System.out.println("---------控件已不在屏幕可见区域（已滑出屏幕）-----隐去-----------------");
        }
    }
}
