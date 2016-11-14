package com.cadyd.app.ui.fragment.mall;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.Bind;
import butterknife.OnClick;

import com.cadyd.app.JConstant;
import com.cadyd.app.R;
import com.cadyd.app.adapter.AllgoodsEvaluateRecyclerAdapter;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.asynctask.DataLoader;
import com.cadyd.app.interfaces.TowObjectParameterInterface;
import com.cadyd.app.model.*;
import com.cadyd.app.ui.base.BaseFragement;
import com.cadyd.app.utils.BitmapUtil;
import com.cadyd.app.utils.FileUploadUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.wcy.android.utils.LogUtil;
import org.wcy.common.utils.DateUtil;
import org.wcy.common.utils.StringUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Handler;

/**
 * zjh
 * 订单中的所有的商品评论
 */
public class AllGoodsEvaluateFragment extends BaseFragement {

    private String orderid;
    private String type;
    private GoodsTheSunDataModel goodsTheSunDataModel;

    private static final int IAMGE_DIALOG_CODE = 0;
    private static final int CAMERA_DIALOG_CODE = 1;
    private final int RESULT_OK = -1;
    private final int PHOTO_REQUEST_TAKEPHOTO = 11;// 拍照
    private final int PHOTO_REQUEST_GALLERY = 12;// 从相册中选择
    private String[] mDialogItems = new String[]{"相册", "拍照"};
    // 创建一个以当前时间为名称的文件
    File tempFile = new File(Environment.getExternalStorageDirectory(), getPhotoFileName());


    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    public static AllGoodsEvaluateFragment newInstance(String orderid, String type) {
        AllGoodsEvaluateFragment newFragment = new AllGoodsEvaluateFragment();
        Bundle bundle = new Bundle();
        bundle.putString("orderid", orderid);
        bundle.putString("type", type);
        newFragment.setArguments(bundle);
        return newFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            this.orderid = args.getString("orderid");
            this.type = args.getString("type");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return setView(R.layout.fragment_all_goods_evaluate, "商品晒单", true);
    }

    @Override
    protected void initView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        getGoodsList();
    }

    /**
     * 获取商品信息
     */
    private void getGoodsList() {
        Map<String, Object> map = new HashMap<>();
        map.put("oid", orderid);
        ApiClient.send(activity, JConstant.UNCOMMENT_, map, GoodsTheSunDataModel.class, new DataLoader<GoodsTheSunDataModel>() {
            @Override
            public void task(String data) {

            }

            @Override
            public void succeed(GoodsTheSunDataModel data) {
                if (data != null) {
                    goodsTheSunDataModel = data;
                    setAdapter();
                }
            }

            @Override
            public void error(String message) {

            }
        }, JConstant.UNCOMMENT_);
    }

    /**
     * 设置adapter
     */
    private int mpostion;
    private RecyclerView.Adapter madapter;

    private AllgoodsEvaluateRecyclerAdapter adapter;

    private void setAdapter() {
        goodsTheSunDataModel.list.add(new GoodsTheSunModel());
        goodsTheSunDataModel.list.add(0, new GoodsTheSunModel());
        adapter = new AllgoodsEvaluateRecyclerAdapter(activity, goodsTheSunDataModel);
        adapter.setClick(new TowObjectParameterInterface() {
            @Override
            public void Onchange(int type, int postion, Object object) {
                madapter = (RecyclerView.Adapter) object;
                mpostion = postion;
                addImage(mpostion);
            }
        });
        recyclerView.setAdapter(adapter);
    }

    /**
     * 提交晒单
     */
    @OnClick(R.id.send)
    public void onSend() {
        List<SendEvaluateModel> list = new ArrayList<>();
        int service = 0;
        int Logistics = 0;
        for (int i = goodsTheSunDataModel.list.size() - 1; i >= 0; i--) {
            boolean isAdd = false;
            SendEvaluateModel model = new SendEvaluateModel();
            Commentgoods cgoods = new Commentgoods();
            List<ImageModelImage> imageList = new ArrayList<>();
            if (i == 0) {
            } else if (i == (goodsTheSunDataModel.list.size() - 1)) {
                System.out.println("总的 service：" + goodsTheSunDataModel.list.get(i).service);
                System.out.println("总的 Logistics：" + goodsTheSunDataModel.list.get(i).Logistics);
            } else {
                isAdd = true;
                System.out.println("星级：" + goodsTheSunDataModel.list.get(i).local_level);
                System.out.println("内容：" + goodsTheSunDataModel.list.get(i).local_content);
                if (!StringUtil.hasText(goodsTheSunDataModel.list.get(i).local_content)) {
                    toast("请评论完所有商品");
                    return;
                }
                for (Map<String, ImageModel> m : goodsTheSunDataModel.list.get(i).bitmapList) {
                    if (m != null) {
                        ImageModelImage image = new ImageModelImage();
                        for (String str : m.keySet()) {
                            System.out.println("图片地址:" + m.get(str).thumb);
                            image.filename = m.get(str).filename;
                            image.path = m.get(str).path;
                            image.thumb = m.get(str).thumb;
                        }
                        imageList.add(image);
                    }
                }
                cgoods.content = goodsTheSunDataModel.list.get(i).local_content;
                cgoods.startlevel = goodsTheSunDataModel.list.get(i).local_level;
                cgoods.logisticsLevel = Logistics;
                cgoods.serviceLevel = service;
                cgoods.odid = goodsTheSunDataModel.list.get(i).odid;
                cgoods.goodsid = goodsTheSunDataModel.list.get(i).goodsId;
            }
            model.commentGoods = cgoods;
            model.imageList = imageList;
            if (isAdd) {
                list.add(model);
            }
        }

        ApiClient.send(activity, JConstant.SAVEPIC_, list, null, new DataLoader<String>() {
            @Override
            public void task(String data) {

            }

            @Override
            public void succeed(String data) {
                toastSuccess("评论成功");
                finishFramager();
            }

            @Override
            public void error(String message) {

            }
        }, JConstant.SAVEPIC_);
    }

    //打开图片选择
    private void addImage(int postion) {
        if (goodsTheSunDataModel.list.get(postion).bitmapList.size() >= 4) {
            toast("选择的图片最多为三张");
            return;
        }
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
                            UpLoder(tempFile, photo);
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
                                UpLoder(new File(path), p);
                            }
                        } catch (Exception e) {
                            toast("请重新选择");
                        }
                    }
                    break;
            }
        }
    }

    private void UpLoder(File file, final Bitmap bitmap) {
        FileUploadUtils.getInstance().post(activity, JConstant.URlP + "/ossFileUpload", JConstant.OSSFILEUOLPAD, file, new FileUploadUtils.UploadFileListener() {
            @Override
            public void onSuccess(String data) {
                ImageModel imageModel = null;
                try {
                    JSONObject object = new JSONObject(data);
                    if (object.getInt("state") == 0) {
                        object = object.getJSONObject("data");
                        JSONArray array = object.getJSONArray("SchFile");
                        for (int i = 0; i < array.length(); i++) {
                            object = array.getJSONObject(i);
                            imageModel = new ImageModel();
                            imageModel.filename = object.getString("filename");
                            imageModel.path = object.getString("path");
                            imageModel.thumb = object.getString("thumb");//获得压缩图片地址
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Map<String, ImageModel> map = new HashMap<String, ImageModel>();
                imageModel.bitmap = bitmap;
                map.put(imageModel.thumb, imageModel);
                goodsTheSunDataModel.list.get(mpostion).bitmapList.add(map);
                handler.sendEmptyMessage(0);
            }

            @Override
            public void onFailure(String msg) {
                LogUtil.i(AllGoodsEvaluateFragment.class, msg);
                handler.sendEmptyMessage(1);
            }
        });
    }

    android.os.Handler handler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    toastSuccess("完成");
                    madapter.notifyDataSetChanged();
                    break;
                case 1:
                    toastError("上传失败");
                    break;
            }
        }
    };

    @Override
    public boolean onBackPressed() {
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ApiClient.cancelRequest(JConstant.SAVEPIC_);
        ApiClient.cancelRequest(JConstant.UNCOMMENT_);
    }
}
