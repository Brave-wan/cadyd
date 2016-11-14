package org.wcy.android.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;
/**
 * 解决多个gridview同时存在的问题
 * @author wangchaoyong
 *
 */
public class NoScrollGridView extends GridView{
    public NoScrollGridView(Context context) {
        super(context);
    }


	 public NoScrollGridView(Context context, AttributeSet attrs){  
         super(context, attrs);  
    }  
 
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec){  
         int mExpandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);  
         super.onMeasure(widthMeasureSpec, mExpandSpec);  
    }  
}
