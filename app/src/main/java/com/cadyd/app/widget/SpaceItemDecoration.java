package com.cadyd.app.widget;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by rance on 2016/10/4.
 * recyclerView 间距
 */
public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

    private int space;
    private int spaceCount;

    public SpaceItemDecoration(int space, int spaceCount) {
        this.space = space;
        this.spaceCount =spaceCount;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        //不是第一个的格子都设一个左边和底部的间距
        outRect.left = space;
        outRect.bottom = space;
        if (parent.getChildLayoutPosition(view) % spaceCount == 0) {
            outRect.left = 0;
        }
    }

}
