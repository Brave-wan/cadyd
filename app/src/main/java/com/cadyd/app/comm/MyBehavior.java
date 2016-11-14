package com.cadyd.app.comm;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.cadyd.app.R;

/**
 * Created by xiongmao on 2016/7/21.
 */
public class MyBehavior extends CoordinatorLayout.Behavior {

    public MyBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        return super.onDependentViewChanged(parent, child, dependency);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        RelativeLayout title_bg = (RelativeLayout) parent.findViewById(R.id.title_layout);
        ImageView back = (ImageView) parent.findViewById(R.id.back);
        TextView title = (TextView) parent.findViewById(R.id.title);
        if (dependency.getY() < 0) {
            MyChange.ChangeHandAlpha(title_bg, back, title, (int) Math.abs(dependency.getY()));
        }
        return super.layoutDependsOn(parent, child, dependency);
    }
}
