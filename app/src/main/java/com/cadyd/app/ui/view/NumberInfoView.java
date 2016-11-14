package com.cadyd.app.ui.view;

import android.content.Context;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import java.util.List;

/**
 * Created by admin on 2016/5/6.
 */
public class NumberInfoView extends HorizontalScrollView {
    List<LinearLayout> linearLayouts;
    public NumberInfoView(Context context) {
        super(context);
    }
    //决定内部子view宽和高
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }
}
