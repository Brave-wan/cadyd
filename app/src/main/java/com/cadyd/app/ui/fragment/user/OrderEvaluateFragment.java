package com.cadyd.app.ui.fragment.user;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.cadyd.app.asynctask.SimpleAsyncTask;
import com.cadyd.app.comm.CommaSplice;
import com.cadyd.app.interfaces.OneParameterInterface;
import com.cadyd.app.model.GoodsTheSunModel;
import com.cadyd.app.ui.base.BaseFragement;
import com.cadyd.app.ui.view.EditAlertConfirmation;
import com.cadyd.app.ui.view.SelectFixGridLayout;
import com.cadyd.app.utils.BitmapUtil;
import com.loopj.android.http.RequestParams;
import org.wcy.android.utils.ActivityUtil;
import org.wcy.android.widget.NoScrollGridView;
import org.wcy.common.utils.DateUtil;
import org.wcy.common.utils.NumberUtil;
import org.wcy.common.utils.StringUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商城商品评价
 */
public class OrderEvaluateFragment extends BaseFragement {

    GoodsTheSunModel model;

    public static OrderEvaluateFragment newInstance(GoodsTheSunModel model) {
        OrderEvaluateFragment newFragment = new OrderEvaluateFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("model", model);
        newFragment.setArguments(bundle);
        return newFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            this.model = (GoodsTheSunModel) args.getSerializable("model");
        }
    }

    private EditAlertConfirmation aler;//popup
    @Bind(R.id.add_label)
    SelectFixGridLayout gridLayout;
    @Bind(R.id.add_image_grid)
    NoScrollGridView gridView;
    @Bind(R.id.room_ratingbar)
    RatingBar room_ratingbar;
    private CheckBox[] starsList;
    @Bind(R.id.goods_image)
    ImageView imageView;
    @Bind(R.id.content)
    EditText contentText;

    private class CBitmap {
        public Bitmap bitmap;
        public String path;
    }

    private List<OrderEvaluateFragment.CBitmap> bitmaps = new ArrayList<>();
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
        bitmaps.add(new OrderEvaluateFragment.CBitmap());//为bitmap默认一个空的item用来当作添加按钮
        return setView(R.layout.fragment_shop_goods_the_sun, "评价", true);
    }

    @Override
    protected void initView() {
        aler = new EditAlertConfirmation(activity, "添加内容", "请输入内容");
        inflater = LayoutInflater.from(activity);
        //添加默认的添加标题
        gridLayout.addItemView("+添加", new OneParameterInterface() {
            @Override
            public void Onchange(int type) {
                aler.show(new EditAlertConfirmation.OnClickListeners() {
                    @Override
                    public void ConfirmOnClickListener(String string) {
                        if (!StringUtil.hasText(string)) {
                            toast("添加内容不能为空");
                            return;
                        } else if (string.length() > 5) {
                            toast("添加内容不能大于5个");
                            return;
                        }
                        gridLayout.addItemView(string, new OneParameterInterface() {
                            @Override
                            public void Onchange(int type) {

                            }
                        });
                        aler.hide();
                    }

                    @Override
                    public void CancelOnClickListener() {
                        aler.hide();
                    }
                });
            }
        });

        gridView.setAdapter(adapter);
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
        }
        //获得用户的标题
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

//        RequestParams params
//      params.put("file", files, "multipart/form-data", "");
        Map<String, Object> map = new HashMap<>();
        map.put("content", content);//评论内容
        map.put("startlevel", NumberUtil.getInteger(room_ratingbar.getRating()));//星级
        map.put("goodsid", model.goodsId);//商品id
        map.put("odid", model.odid);//订单详情id
        map.put("libe", "");//标签
        map.put("file[]", files);//图片集
        sendHttp(map);
    }

    private void getHttp() {
        ApiClient.displayImage(model.thumb, imageView);//设置图片
        if (model.label != null) {
            String lable[] = model.label.split(",");
            for (int i = 0; i < lable.length; i++) {
                gridLayout.addItemView(lable[i], new OneParameterInterface() {
                    @Override
                    public void Onchange(int type) {

                    }
                });
            }
        }
    }

    private void sendHttp(Map<String, Object> map) {

        ApiClient.send(activity, JConstant.SAVEPIC_, map, null, new DataLoader<String>() {
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
        }, JConstant.SAVEPIC_);
    }

    private BaseAdapter adapter = new BaseAdapter() {
        @Override
        public int getCount() {
            return bitmaps.size() == 0 ? 1 : bitmaps.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            OrderEvaluateFragment.ViewHolder holder;
            if (view == null) {
                holder = new OrderEvaluateFragment.ViewHolder();
                view = inflater.inflate(R.layout.image_view, null);
                holder.image = (ImageView) view.findViewById(R.id.imageView_image);
                view.setTag(holder);
            } else {
                holder = (OrderEvaluateFragment.ViewHolder) view.getTag();
            }
            if (position == bitmaps.size() - 1) {
                //添加监听
                holder.image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addImage();
                    }
                });
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.add_image);
                holder.image.setImageBitmap(bitmap);
            } else {
                //用于覆盖添加监听
                holder.image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                holder.image.setImageBitmap(bitmaps.get(position).bitmap);
            }
            return view;
        }
    };

    private class ViewHolder {
        private ImageView image;
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
                            OrderEvaluateFragment.CBitmap bitmap = new OrderEvaluateFragment.CBitmap();
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
                                OrderEvaluateFragment.CBitmap bitmap = new OrderEvaluateFragment.CBitmap();
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
            adapter.notifyDataSetChanged();
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
        ApiClient.cancelRequest(JConstant.SAVEPIC_);
    }
}
