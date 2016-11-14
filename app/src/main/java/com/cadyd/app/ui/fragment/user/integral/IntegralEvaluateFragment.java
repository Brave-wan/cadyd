package com.cadyd.app.ui.fragment.user.integral;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import butterknife.Bind;
import butterknife.OnClick;
import com.cadyd.app.JConstant;
import com.cadyd.app.R;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.asynctask.DataLoader;
import com.cadyd.app.comm.CommaSplice;
import com.cadyd.app.model.IntegralListData;
import com.cadyd.app.ui.base.BaseFragement;
import com.cadyd.app.ui.view.SelectFixGridLayout;
import com.cadyd.app.utils.BitmapUtil;
import org.wcy.android.utils.ActivityUtil;
import org.wcy.common.utils.DateUtil;
import org.wcy.common.utils.NumberUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 积分商城商品评价
 */
public class IntegralEvaluateFragment extends BaseFragement {

    private IntegralListData.OrderIntegralGoods model;
    private String orderId;

    public static IntegralEvaluateFragment newInstance(IntegralListData.OrderIntegralGoods model , String orderId) {
        IntegralEvaluateFragment newFragment = new IntegralEvaluateFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("model", model);
        bundle.putString("orderId" ,orderId);
        newFragment.setArguments(bundle);
        return newFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            this.model = (IntegralListData.OrderIntegralGoods) args.getSerializable("model");
            this.orderId = args.getString("orderId");
        }
    }

    @Bind(R.id.add_label)
    SelectFixGridLayout gridLayout;
    @Bind(R.id.room_ratingbar)
    RatingBar room_ratingbar;
    @Bind(R.id.goods_image)
    ImageView imageView;
    @Bind(R.id.content)
    EditText contentText;
    @Bind(R.id.libe)
    EditText libe;
    @Bind(R.id.libe_line)
    View libe_line ;

    private class CBitmap {
        public Bitmap bitmap;
        public String path;
    }

    private List<IntegralEvaluateFragment.CBitmap> bitmaps = new ArrayList<>();
    private LayoutInflater inflater;
    private static final int IAMGE_DIALOG_CODE = 0;
    private static final int CAMERA_DIALOG_CODE = 1;
    private final int RESULT_OK = -1;
    private final int PHOTO_REQUEST_TAKEPHOTO = 11;// 拍照
    private final int PHOTO_REQUEST_GALLERY = 12;// 从相册中选择
    private String[] mDialogItems = new String[]{"相册", "拍照"};
    // 创建一个以当前时间为名称的文件
    File tempFile = new File(Environment.getExternalStorageDirectory(), getPhotoFileName());

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        bitmaps.add(new IntegralEvaluateFragment.CBitmap());//为bitmap默认一个空的item用来当作添加按钮
        return setView(R.layout.fragment_shop_goods_the_sun, "评价", true);
    }

    @Override
    protected void initView() {
        inflater = LayoutInflater.from(activity);
        libe.setVisibility(View.VISIBLE);
        libe_line.setVisibility(View.VISIBLE);


        getHttp();//获得商品信息
    }

    //发送评论
    @OnClick(R.id.send)
    public void onSend(View view) {
        if (room_ratingbar.getRating() == 0) {
            toast("请选择评分");
            return;
        } else if (!ActivityUtil.isEditTextNull(contentText)) {
            toast("请输入评论内容");
            contentText.requestFocus();
            return;
        }else if (!ActivityUtil.isEditTextNull(libe)){
            toast("请输入标签");
            libe.requestFocus();
        }
        //获得用户的标题(暂时不支持上传用户的标题)
        StringBuffer buffer;
        List<String> stringList = gridLayout.getChicedsString();
        buffer = CommaSplice.Splice(stringList);

        String content = contentText.getText().toString();
        //获得用户添加的图片们
        File files[] = new File[bitmaps.size() - 1];
        for (int j = 0; j < bitmaps.size(); j++) {
            if ((bitmaps.size() - 1) != j) {
                File file = new File(bitmaps.get(j).path);
                files[j] = file;
            }
        }
//      params.put("file", files, "multipart/form-data", "");
        Map<String, Object> map = new HashMap<>();
        map.put("content", content);//评论内容
        map.put("startlevel", NumberUtil.getInteger(room_ratingbar.getRating()));//星级
        map.put("goodsid", model.goodsId);//商品id
        map.put("odid", orderId);//订单详情id
        map.put("libe", libe.getText().toString());//标签
        map.put("file[]", files);//图片集
        sendHttp(map);
    }

    private void getHttp() {
        ApiClient.displayImage(model.path, imageView);//设置图片
    }

    private void sendHttp(Map<String, Object> map) {

        ApiClient.send(activity, JConstant.SAVEINTEGRALCOMMENT, map, null, new DataLoader<String>() {
            @Override
            public void task(String data) {

            }

            @Override
            public void succeed(String data) {
                toastSuccess("晒单成功");
                activity.setResult(Activity.RESULT_OK);
                finishActivity();
            }

            @Override
            public void error(String message) {

            }
        }, JConstant.SAVEINTEGRALCOMMENT);
    }

    //打开图片选择
    private void addImage() {
        new AlertDialog.Builder(activity)
                .setTitle("图片选择")
                .setItems(mDialogItems, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case IAMGE_DIALOG_CODE:
                                Intent intent = new Intent(Intent.ACTION_PICK, null);
                                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                                startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
                                break;
                            case CAMERA_DIALOG_CODE:
                                Intent cameraintent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                // 指定调用相机拍照后照片的储存路径
                                cameraintent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
                                startActivityForResult(cameraintent, PHOTO_REQUEST_TAKEPHOTO);
                                break;
                        }
                    }
                })
                .setNegativeButton(
                        "取消",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PHOTO_REQUEST_TAKEPHOTO:
                    try {
                        Bitmap photo = BitmapUtil.getImageThumbnail(tempFile.getPath(), 400, 300);
                        if (photo != null) {
                            IntegralEvaluateFragment.CBitmap bitmap = new IntegralEvaluateFragment.CBitmap();
                            bitmap.bitmap = photo;
                            bitmap.path = tempFile.getPath();
                            bitmaps.add(0, bitmap);
                        }
                    } catch (Exception e) {
                        toast("请重新选择");
                    }
                    break;
                case PHOTO_REQUEST_GALLERY:
                    if (data != null && data.getData() != null) {
                        try {
                            String path = BitmapUtil.getImageAbsolutePath(activity, data.getData());
                            Bitmap p = BitmapUtil.getImageThumbnail(path, 400, 300);
                            if (p != null) {
                                IntegralEvaluateFragment.CBitmap bitmap = new IntegralEvaluateFragment.CBitmap();
                                bitmap.bitmap = p;
                                bitmap.path = path;
                                bitmaps.add(0, bitmap);
                            }
                        } catch (Exception e) {
                            toast("请重新选择");
                        }
                    }
                    break;
            }
        }
    }

    // 使用系统当前日期加以调整作为照片的名称
    private String getPhotoFileName() {
        return "dx" + DateUtil.getCurDate() + ".jpg";
    }

    @Override
    public boolean onBackPressed() {
        finishFramager();
        return false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ApiClient.cancelRequest(JConstant.SAVEINTEGRALCOMMENT);
    }
}
