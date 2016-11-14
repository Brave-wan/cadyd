package com.cadyd.app.comm;

import android.graphics.Bitmap;
import android.view.View;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.wcy.android.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiongmao on 2016/8/29.
 */

public class ImageLodingListener implements ImageLoadingListener {

    private List<Bitmap> bitmaps = new ArrayList<>();

    public void cleanBitmap() {
        if (bitmaps != null && bitmaps.size() > 0) {
            for (Bitmap b : bitmaps) {
                LogUtil.i(ImageLodingListener.class, "----------开始查找图片" + (b == null ? 0 : b.getByteCount()));
                if (b != null && !b.isRecycled()) {
                    LogUtil.i(ImageLodingListener.class, "-------------开始释放图片");
                    b.recycle();
                }
            }
        }
    }

    @Override
    public void onLoadingStarted(String s, View view) {

    }

    @Override
    public void onLoadingFailed(String s, View view, FailReason failReason) {

    }

    @Override
    public void onLoadingComplete(String s, View view, Bitmap bitmap) {
        bitmaps.add(bitmap);
    }

    @Override
    public void onLoadingCancelled(String s, View view) {

    }
}
