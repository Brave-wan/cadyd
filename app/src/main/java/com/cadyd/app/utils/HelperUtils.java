package com.cadyd.app.utils;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by root on 16-9-3.
 */

public class HelperUtils {
    /**
     * 显示进度框
     */
    public static ProgressDialog showProgressDialog(
            ProgressDialog progressDialog, Context context, String keyWord) {
        if (progressDialog == null)
            progressDialog = new ProgressDialog(context, ProgressDialog.THEME_HOLO_LIGHT);
        // progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(keyWord);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(true);
        return progressDialog;
    }

}
