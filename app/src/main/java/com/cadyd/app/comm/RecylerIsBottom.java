package com.cadyd.app.comm;

import android.support.v7.widget.RecyclerView;

/**
 * Created by xiongmao on 2016/8/4.
 */
public class RecylerIsBottom {
    public static boolean isSlideToBottom(RecyclerView recyclerView) {
        if (recyclerView == null) {
            return false;
        }
        System.out.println(recyclerView.computeVerticalScrollExtent() + "+and+" + recyclerView.computeVerticalScrollOffset() + "+and+" + recyclerView.computeVerticalScrollRange());
        if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset() >= recyclerView.computeVerticalScrollRange()) {
            return true;
        }
        return false;
    }
}
