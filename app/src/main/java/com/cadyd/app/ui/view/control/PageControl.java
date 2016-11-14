package com.cadyd.app.ui.view.control;

import android.content.Context;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.cadyd.app.R;

public class PageControl {

    private LinearLayout layout;
    private ImageView[] textViews;
    private ImageView textView;
    private int pageSize;

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    private int selectedImage = R.drawable.bg_circle_sel;
    private int unselectedImage = R.drawable.bg_circle_white;

    private int currentPage = 0;
    private Context mContext;

    public PageControl(Context context, LinearLayout layout, int pageSize) {
        this.mContext = context;
        this.pageSize = pageSize;
        this.layout = layout;
        initDots();
    }

    void initDots() {
        textViews = new ImageView[pageSize];
        layout.removeAllViews();
        for (int i = 0; i < pageSize; i++) {
            textView = new ImageView(mContext);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(15, 15);
            lp.setMargins(8, 0, 8, 0);
            textView.setLayoutParams(lp);
            textViews[i] = textView;
            if (i == 0) {
                textViews[i].setImageResource(selectedImage);
            } else {
                textViews[i].setImageResource(unselectedImage);
            }
            layout.addView(textViews[i]);
        }
    }

    boolean isFirst() {
        return this.currentPage == 0;
    }

    boolean isLast() {
        return this.currentPage == pageSize;
    }

    public void selectPage(int current) {
        for (int i = 0; i < textViews.length; i++) {
            textViews[current].setImageResource(selectedImage);
            if (current != i) {
                textViews[i].setImageResource(unselectedImage);
            }
        }
    }

    void turnToNextPage() {
        if (!isLast()) {
            currentPage++;
            selectPage(currentPage);
        }
    }

    void turnToPrePage() {
        if (!isFirst()) {
            currentPage--;
            selectPage(currentPage);
        }
    }
}
