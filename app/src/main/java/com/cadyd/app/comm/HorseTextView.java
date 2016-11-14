package com.cadyd.app.comm;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by xiongmao on 2016/6/15.
 * 自定义跑马灯
 */
public class HorseTextView extends TextView implements Runnable {

    private int currentScrollX;// 当前滚动的位置
    private boolean isStop = false;
    private boolean isMeasure = false;
    private int textWidth;

    public HorseTextView(Context context) {
        super(context);
    }

    public HorseTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HorseTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * textview的绘制方法
     **/
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!isMeasure) {// 文字宽度只需获取一次就可以了
            textWidth = getTextWidth();
            isMeasure = true;
        }
    }

    /**
     * 获取文字宽度
     */
    private int getTextWidth() {
        Paint paint = this.getPaint();
        String str = this.getText().toString();
        return (int) paint.measureText(str);
    }

    @Override
    public void run() {
        currentScrollX += 3;// 滚动速度
        scrollTo(currentScrollX, 0);
        if (isStop) {
            return;
        }
        if (getScrollX() >= textWidth) {
            scrollTo(-textWidth, 0);
            currentScrollX = -textWidth;
        }
        postDelayed(this, 42);//run方法的自动调用方法
    }

    // 开始滚动
    public void startScroll() {
        isStop = false;
        this.removeCallbacks(this);
        post(this);
    }

    // 停止滚动
    public void stopScroll() {
        isStop = true;
    }

    // 从头开始滚动
    public void startFor0() {
        currentScrollX = 0;
        startScroll();
    }

    @Override
    public boolean isFocused() {
        return true;
    }
}
