package com.cadyd.app.comm;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;

import com.cadyd.app.ui.view.AlertConfirmation;

import java.math.BigDecimal;

/**
 * Created by xiongmao on 2016/7/29.
 */
public class Utils {

    /**
     * 打电话
     */
    public static void tellPhone(final Context context, final String phone, String title) {
        if (TextUtils.isEmpty(phone)) {
            return;
        }

        if (TextUtils.isEmpty(title)) {
            title = "";
        }

        final AlertConfirmation alertConfirmation = new AlertConfirmation(context, title, "您是否要拨打  " + phone + "  此电话？");

        alertConfirmation.show(new AlertConfirmation.OnClickListeners() {
            @Override
            public void ConfirmOnClickListener() {
                //用intent启动拨打电话
                Intent intent = new Intent(Intent.ACTION_CALL);
                String[] tellPhone = phone.split("-");
                String phone = "";
                for (String tp : tellPhone) {
                    phone += tp;
                }
                Log.i(context.getClass().getName(), phone);
                intent.setData(Uri.parse("tel:" + phone));
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                alertConfirmation.hide();
                context.startActivity(intent);
            }

            @Override
            public void CancelOnClickListener() {
                alertConfirmation.hide();
            }
        });


    }

    public static void openUrlToWeb(Context context, String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(Uri.parse(url));
        context.startActivity(intent);
    }

    public static String converUnit(int number) {
        if (number == 0) {
            return number + "";
        }

        String result = number + "";
        if (number / 10000 > 0) {
            BigDecimal source = BigDecimal.valueOf(number);
            BigDecimal target = BigDecimal.valueOf(10000);
            result = source.divide(target, 2, BigDecimal.ROUND_HALF_DOWN).toString() + "万";
        }

        return result;
    }

    public static boolean isSlideToBottom(RecyclerView recyclerView) {
        if (recyclerView == null) {
            return false;
        }

        if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset() >= recyclerView.computeVerticalScrollRange()) {
            return true;
        }
        return false;
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static Bitmap blurBitmap(Bitmap bitmap, Context context) {

        //Let's create an empty bitmap with the same size of the bitmap we want to blur
        Bitmap outBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);

        //Instantiate a new Renderscript
        RenderScript rs = RenderScript.create(context);

        //Create an Intrinsic Blur Script using the Renderscript
        ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));

        //Create the Allocations (in/out) with the Renderscript and the in/out bitmaps
        Allocation allIn = Allocation.createFromBitmap(rs, bitmap);
        Allocation allOut = Allocation.createFromBitmap(rs, outBitmap);

        //Set the radius of the blur
        blurScript.setRadius(25.f);

        //Perform the Renderscript
        blurScript.setInput(allIn);
        blurScript.forEach(allOut);

        //Copy the final bitmap created by the out Allocation to the outBitmap
        allOut.copyTo(outBitmap);

        //recycle the original bitmap
        bitmap.recycle();

        //After finishing everything, we destroy the Renderscript.
        rs.destroy();

        return outBitmap;


    }

}
