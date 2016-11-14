package com.cadyd.app.ui.fragment.unitary;

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
import com.cadyd.app.model.RecordModleData;
import com.cadyd.app.ui.base.BaseFragement;
import com.cadyd.app.utils.BitmapUtil;
import com.loopj.android.http.RequestParams;
import org.wcy.android.widget.NoScrollGridView;
import org.wcy.common.utils.DateUtil;
import org.wcy.common.utils.StringUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 发布晒单
 */
public class AddTheSunFragment extends BaseFragement {

    private class CBitmap {
        public Bitmap bitmap;
        public String path;
    }

    private List<CBitmap> bitmaps = new ArrayList<>();
    private LayoutInflater inflater;
    private RecordModleData modleData;

    private static final int IAMGE_DIALOG_CODE = 0;
    private static final int CAMERA_DIALOG_CODE = 1;
    private final int RESULT_OK = -1;
    private final int PHOTO_REQUEST_TAKEPHOTO = 11;// 拍照
    private final int PHOTO_REQUEST_GALLERY = 12;// 从相册中选择
    private String[] mDialogItems = new String[]{"相册", "拍照"};
    // 创建一个以当前时间为名称的文件
    File tempFile = new File(Environment.getExternalStorageDirectory(), getPhotoFileName());

    @Bind(R.id.image)
    ImageView imageView;
    @Bind(R.id.title)
    TextView title;

    @Bind(R.id.add_image_grid)
    NoScrollGridView grid;

    @Bind(R.id.send)
    Button send;
    @Bind(R.id.the_sun_title)
    TextView TheSunTitle;
    @Bind(R.id.the_sun_content)
    TextView TheSunContent;


    public static AddTheSunFragment newInstance(RecordModleData modleData) {
        AddTheSunFragment newFragment = new AddTheSunFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("model", modleData);
        newFragment.setArguments(bundle);
        return newFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            modleData = (RecordModleData) args.getSerializable("model");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        bitmaps.add(new CBitmap());
        return setView(R.layout.fragment_add_the_sun, "晒单", true);
    }

    @Override
    protected void initView() {
        inflater = LayoutInflater.from(activity);
        ApiClient.displayImage(modleData.mainimg, imageView);
        title.setText("(第" + modleData.buytims + "期)" + modleData.title);

        grid.setAdapter(adapter);
    }

    @OnClick(R.id.send)
    public void onSend(View view) {
        String title = TheSunTitle.getText().toString();
        String content = TheSunContent.getText().toString();

        File files[] = new File[bitmaps.size() - 1];
        for (int i = 0; i < bitmaps.size(); i++) {
            if ((bitmaps.size() - 1) != i) {
                files[i] = new File(bitmaps.get(i).path);
            }
        }
//            imgUrl	图片数组
//            productId	产品主键
//            description	描述
//            userId	用户主键

        //  params.put("file", file, "multipart/form-data", "");
        Map<String, Object> map = new HashMap<>();
        if (title == null || !StringUtil.hasText(title)) {
            toast("标题不能为空");
            return;
        }
        map.put("imgUrl", files);
        map.put("productId", modleData.tbid);
        map.put("title", title);
        map.put("description", content);
        ApiClient.send(activity, JConstant.SAVEDOCUMENT_, map, null, new DataLoader<String>() {
            @Override
            public void task(String data) {

            }

            @Override
            public void succeed(String data) {
                toastSuccess("晒单成功");
                finishFramager();
            }

            @Override
            public void error(String message) {

            }
        }, JConstant.SAVEDOCUMENT_);
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
            ViewHolder holder;
            if (view == null) {
                holder = new ViewHolder();
                view = inflater.inflate(R.layout.image_view, null);
                holder.image = (ImageView) view.findViewById(R.id.imageView_image);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
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

    // 使用系统当前日期加以调整作为照片的名称
    private String getPhotoFileName() {
        return "dx" + DateUtil.getCurDate() + ".jpg";
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PHOTO_REQUEST_TAKEPHOTO:
                    try {
                        Bitmap photo = BitmapUtil.getImageThumbnail(tempFile.getPath(), 400, 300);
                        if (photo != null) {
                            CBitmap bitmap = new CBitmap();
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
                                CBitmap bitmap = new CBitmap();
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

    @Override
    public boolean onBackPressed() {
        finishFramager();
        return false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ApiClient.cancelRequest(JConstant.SAVEDOCUMENT_);
    }
}
