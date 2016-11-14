package com.cadyd.app.comm;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cadyd.app.R;

/**
 * Created by Administrator on 2016/5/12.
 */
public class MyChange {

    /**
     * 字体改变方法
     **/
    public static SpannableStringBuilder ChangeTextColor(String text, int start, int end, int color) {
        SpannableStringBuilder builder = new SpannableStringBuilder(text.toString());
//ForegroundColorSpan 为文字前景色，BackgroundColorSpan为文字背景色
        ForegroundColorSpan ColorSpan = new ForegroundColorSpan(color);
        builder.setSpan(ColorSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return builder;
    }

    /**
     * 顶部视图改变
     **/
    private static int alphaMax = 160;

    //有标题，没有购物车
    public static void ChangeHandAlpha(View view, ImageView backView, TextView textView, int scrollY) {

//        backView.getBackground().setAlpha(0);
//        backView.getBackground().setAlpha(alphaMax);
//        textView.setAlpha(1 - alphaMax / 100);

        if (scrollY > 200 && scrollY <= 510) {
            view.getBackground().setAlpha(scrollY / 2);
            textView.setAlpha(1 - alphaMax / scrollY / 2);
        } else if (scrollY <= 100) {
            view.getBackground().setAlpha(0);
            textView.setAlpha(1);
        } else if (scrollY > 510) {
            view.getBackground().setAlpha(255);
            textView.setAlpha(1 - 0.255f);
        }
        if (scrollY > 200 && scrollY <= 510) {
            int alphaReverse = alphaMax - (scrollY - 200);
            if (alphaReverse < 0) {
                alphaReverse = 0;
            }
            backView.getBackground().setAlpha(alphaReverse);
            backView.setImageResource(R.mipmap.black_back);
            textView.setAlpha(1 - alphaReverse / 100);
        } else if (scrollY <= 200) {
            backView.setImageResource(R.mipmap.back_left_ico);
            backView.getBackground().setAlpha(alphaMax);
            textView.setAlpha(1 - alphaMax / 100);
        } else if (scrollY > 510) {
            backView.getBackground().setAlpha(0);
            textView.setAlpha(1);
        }
    }

    //没有标题，没有购物车
    public static void ChangeHandAlpha(View view, ImageView backView, int scrollY) {
        if (scrollY > 200 && scrollY <= 510) {
            view.getBackground().setAlpha(scrollY / 2);
        } else if (scrollY <= 100) {
            view.getBackground().setAlpha(0);
        } else if (scrollY > 510) {
            view.getBackground().setAlpha(255);
        }
        if (scrollY > 200 && scrollY <= 510) {
            int alphaReverse = alphaMax - (scrollY - 200);
            if (alphaReverse < 0) {
                alphaReverse = 0;
            }
            backView.getBackground().setAlpha(alphaReverse);
        } else if (scrollY <= 200) {
            backView.getBackground().setAlpha(alphaMax);
        } else if (scrollY > 510) {
            backView.getBackground().setAlpha(0);
        }
    }
}
