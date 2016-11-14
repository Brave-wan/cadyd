package com.cadyd.app.ui.view;


import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import com.cadyd.app.R;
import com.cadyd.app.interfaces.OneParameterInterface;
import com.cadyd.app.model.SaleInfo;
import com.cadyd.app.ui.window.BabyPopWindow;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义自动换行LinearLayout
 *
 * @author wcy 2016-5-13
 */
public class SelectFixGridLayout extends ViewGroup {
    private SaleInfo saleInfo;

    private SaleInfo parentSale;

    public SelectFixGridLayout(Context context) {
        super(context);
    }

    public SelectFixGridLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SelectFixGridLayout(Context context, AttributeSet attrs, int defStyle) {
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
     * @param parentSale
     */
    public void setList(SaleInfo parentSale) {
        this.parentSale = parentSale;
        for (SaleInfo cf : parentSale.list) {
            System.out.println( parentSale.list.size()+"---------------------setList");
            addItemView(cf, parentSale.list.size());
        }
    }

    /**
     * 添加选项
     *
     * @param info
     * @param size
     */
    private void addItemView(final SaleInfo info, int size) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_checkbox, null);
        final CheckBox tvItem = (CheckBox) view.findViewById(R.id.checkbox);
        if (size == 1) {
            saleInfo = info;
            tvItem.setChecked(true);
        }
        tvItem.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvItem.isChecked()) {
                    saleInfo = info;
                } else {
                    saleInfo = null;
                }
                Refresh();
                if (onItemClickListener != null) {
                    onItemClickListener.onClickOKPop();
                }
            }
        });
        tvItem.setText(info.attrvname);
        tvItem.setTag(info.vid);
        addView(view);
    }

    /**
     * zhou
     * 手动添加view
     */
    private int index = 0;
    private List<CheckBox> checkBoxList = new ArrayList<>();

    public void addItemView(String string, final OneParameterInterface oneParameterInterface) {
        index++;
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_checkbox, null);
        final CheckBox tvItem = (CheckBox) view.findViewById(R.id.checkbox);
        tvItem.setChecked(true);
        tvItem.setText(string);
        addView(view, 0);
        if (index == 1) {
            tvItem.setBackgroundResource(R.drawable.round_red_untransparent);
            tvItem.setTextColor(getResources().getColor(R.color.black));
            tvItem.setBackgroundResource(R.drawable.round_gray_untransparent);
            tvItem.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    oneParameterInterface.Onchange(0);
                }
            });
        } else {
            checkBoxList.add(tvItem);
        }
    }

    public List<String> getChicedsString() {
        List<String> stringList = new ArrayList<>();
        for (int i = 0; i < checkBoxList.size(); i++) {
            if (checkBoxList.get(i).isChecked()) {
                stringList.add(checkBoxList.get(i).getText().toString());
            }
        }
        return stringList;
    }

    /**
     * 刷新选择
     */
    private void Refresh() {
        for (int i = 0; i < getChildCount(); i++) {
            ViewGroup view = (ViewGroup) getChildAt(i);
            CheckBox checkBox = (CheckBox) view.getChildAt(0);
            if (saleInfo != null) {
                if (checkBox.getTag().toString().equals(saleInfo.vid)) {
                    checkBox.setChecked(true);
                } else {
                    checkBox.setChecked(false);
                }
            } else {
                checkBox.setChecked(false);
            }
        }
    }

    /**
     * 当前选中
     *
     * @return
     */
    public SaleInfo getSelect() {
        return saleInfo;
    }

    public SaleInfo getParentSale() {
        return parentSale;
    }

    /**
     * 是否选择
     *
     * @return
     */
    public boolean isSelect() {
        return saleInfo == null ? false : true;
    }

    BabyPopWindow.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(BabyPopWindow.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}