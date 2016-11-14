package com.cadyd.app.ui.view;


import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import com.cadyd.app.R;
import com.cadyd.app.model.CommentCount;
import com.cadyd.app.model.SaleInfo;
import com.cadyd.app.ui.window.BabyPopWindow;

/**
 * 自定义自动换行LinearLayout
 *
 * @author wcy 2016-5-13
 */
public class CommentFixGridLayout extends ViewGroup {
    public CommentFixGridLayout(Context context) {
        super(context);
    }

    public CommentFixGridLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CommentFixGridLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * 控制子控件的换行
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int childCount = getChildCount();
        int maxWidth = r - l;
        int x = 0;
        int y = 0;
        int row = 0;
        for (int i = 0; i < childCount; i++) {
            final View child = this.getChildAt(i);
            if (child.getVisibility() != View.GONE) {
                int width = child.getMeasuredWidth();
                int height = child.getMeasuredHeight();
                x += width;
                y = row * height + height;
                if (x > maxWidth) {
                    x = width;
                    row++;
                    y = row * height + height;
                }
                child.layout(x - width, y - height, x, y);
            }
        }
    }

    /**
     * 计算控件及子控件所占区域
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int maxWidth = MeasureSpec.getSize(widthMeasureSpec);
        int childCount = getChildCount();
        int x = 0;
        int y = 0;
        int row = 0;

        for (int index = 0; index < childCount; index++) {
            final View child = getChildAt(index);
            if (child.getVisibility() != View.GONE) {
                child.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
                // 此处增加onlayout中的换行判断，用于计算所需的高度
                int width = child.getMeasuredWidth();
                int height = child.getMeasuredHeight();
                x += width;
                y = row * height + height;
                if (x > maxWidth) {
                    x = width;
                    row++;
                    y = row * height + height;
                }
            }
        }
        // 设置容器所需的宽度和高度
        setMeasuredDimension(maxWidth, y);
    }

    /**
     * 设置选项
     *
     * @param val
     */
    public void setList(String val) {
        String[] strs = val.split(",");
        addItemView("全部");
        for (String str : strs) {
            addItemView(str);
        }
    }

    /**
     * 添加选项
     *
     * @param info
     */
    private void addItemView(String info) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_checkbox, null);
        final CheckBox tvItem = (CheckBox) view.findViewById(R.id.checkbox);
        tvItem.setText(info);
        if (info.indexOf("(") != -1) {
            info = info.substring(0, info.indexOf("("));
        }
        if (info.equals("全部")) {
            tvItem.setChecked(true);
        }
        tvItem.setTag(info);
        tvItem.setBackgroundResource(R.drawable.corners_comment_btn_selector);
        final String finalTag = info;
        tvItem.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                lable = finalTag;
                if (onItemClickListener != null) {
                    onItemClickListener.onClickOKPop();
                }
                Refresh();
            }
        });
        addView(view);
    }

    /**
     * 刷新选择
     */
    private void Refresh() {
        for (int i = 0; i < getChildCount(); i++) {
            ViewGroup view = (ViewGroup) getChildAt(i);
            CheckBox checkBox = (CheckBox) view.getChildAt(0);
            if (checkBox.getTag().toString().equals(lable)) {
                checkBox.setChecked(true);
            } else {
                checkBox.setChecked(false);
            }

        }
    }

    private String lable = "";

    public String getLable() {
        return lable;
    }

    BabyPopWindow.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(BabyPopWindow.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}