package com.cadyd.app.ui.fragment.user;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
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
import com.cadyd.app.model.ImageModel;
import com.cadyd.app.model.UserMassge;
import com.cadyd.app.ui.base.BaseFragement;
import com.cadyd.app.ui.fragment.SignatureFragment;
import com.cadyd.app.ui.fragment.UserVipInformationFragment;
import com.cadyd.app.ui.view.AlertConfirmation;
import com.cadyd.app.ui.view.CheckAlertConfirmation;
import com.cadyd.app.utils.BitmapUtil;
import com.cadyd.app.utils.FileUploadUtils;

import org.json.JSONArray;
import org.json.JSONObject;
import org.wcy.common.utils.DateUtil;
import org.wcy.common.utils.StringUtil;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户信息编辑
 */
public class UserInformationFragment extends BaseFragement {

    private UserMassge massge;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            this.massge = (UserMassge) args.getSerializable("message");
        }
    }

    @Bind(R.id.ui_user_information_head_image)
    ImageView headImage;

    @Bind(R.id.ui_user_information_name)
    EditText name;

    @Bind(R.id.ui_user_information_address_text)
    TextView addressText;

    @Bind(R.id.ui_user_information_vip_text)
    TextView vipText;

    @Bind(R.id.user_massage)
    LinearLayout massageLinearLayout;

    @Bind(R.id.check_sex)
    ToggleButton toggleButton;

    @Bind(R.id.signature_content)
    TextView signatureContent;//签名


    private int sexType = 0;

    private CheckAlertConfirmation alre;

    private static final int IAMGE_DIALOG_CODE = 0;
    private static final int CAMERA_DIALOG_CODE = 1;
    private final int RESULT_OK = -1;
    private final int PHOTO_REQUEST_TAKEPHOTO = 11;// 拍照
    private final int PHOTO_REQUEST_GALLERY = 12;// 从相册中选择
    private String[] mDialogItems;
    // 创建一个以当前时间为名称的文件
    File tempFile = new File(Environment.getExternalStorageDirectory(), getPhotoFileName());
    AlertConfirmation alert;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        alert = new AlertConfirmation(activity, "是否退出登录", "确认退出登录!");
        mDialogItems = new String[]{
                "相册",
                "拍照"};
        return setView(R.layout.activity_user_information, "个人信息", true);
    }

    @Override
    protected void initView() {
        getMassge();
    }

    /**
     * 设置数据到界面上
     */
    public void serContent() {
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    sexType = 0;
                } else {
                    sexType = 1;
                }
            }
        });
        if (massge != null) {
            ApiClient.displayImage(massge.photo, headImage, R.mipmap.user_default);
            name.setText(massge.name);
            //性别设置
            if ("0".equals(massge.sex)) {
                toggleButton.setChecked(true);
                sexType = 0;
            } else {
                toggleButton.setChecked(false);
                sexType = 1;
            }
            addressText.setText(massge.oftenaddress);//常住地
            //设置会员等级
            float index = Float.valueOf(massge.vipgrade);
            int s = (int) (index * 10) % 10;
            int g = (int) (index % 10);
            switch (g) {
                case 1://花童
                    vipText.setText("  花童  ");
                    break;
                case 2://花学士
                    vipText.setText("  花学士  ");
                    break;
                case 3://花博士
                    vipText.setText("  花博士  ");
                    break;
                case 4://花仙
                    vipText.setText("  花仙  ");
                    break;
            }
            //删除以前的花
            int num = massageLinearLayout.getChildCount();
            if (num >= 1) {
                for (int i = 0; i < num - 1; i++) {
                    massageLinearLayout.removeViewAt(i + 1);
                }
            }
            //添加花
            for (int j = 0; j < s; j++) {
                ImageView imageView = new ImageView(activity);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                imageView.setLayoutParams(params);
                imageView.setImageResource(R.mipmap.vip_hua);
                massageLinearLayout.addView(imageView, 1);
            }
            signatureContent.setText(massge.signature);//顯示用戶簽名
        }
    }

    //保存信息
    private void sendHttp() {
        Map<String, Object> object = new HashMap<>();
        object.put("id", massge.id);
        object.put("name", name.getText().toString());
        object.put("sex", sexType);
        object.put("oftenaddress", addressText.getText().toString());//常住地

        ApiClient.send(activity, JConstant.CHANGEUSERMESSAGE_, object, null, new DataLoader<String>() {
            @Override
            public void task(String data) {

            }

            @Override
            public void succeed(String data) {
                // TODO
                toastSuccess("保存成功");
                application.model.name = name.getText().toString();
                application.model.sex = String.valueOf(sexType);
            }

            @Override
            public void error(String message) {

            }
        }, JConstant.CHANGEUSERMESSAGE_);
    }

    @OnClick(R.id.ui_user_information_head_image)
    public void OnHead(View view) {
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

    private void ChangeHead() {
        String path = headImage.getTag().toString();
        FileUploadUtils.getInstance().post(activity, JConstant.URlP + "/ossFileUpload", JConstant.CHANGEHEADIMAGE_, new File(path), new FileUploadUtils.UploadFileListener() {
            @Override
            public void onSuccess(String data) {
                System.out.println("返回参数：" + data);
                try {
                    JSONObject object = new JSONObject(data);
                    if (object.getInt("state") == 0) {
                        handler.sendEmptyMessage(0);
                    } else {
                        toastSuccess(1);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String msg) {
                toastError(msg);
                handler.sendEmptyMessage(1);
                System.out.println(msg);
            }
        });
    }

    @OnClick(R.id.ui_user_information_vip)
    public void onGetVip(View view) {
        //TODO 查询会员信息
        replaceFragment(R.id.common_frame, new UserVipInformationFragment());
    }

    @OnClick(R.id.ui_user_information_sign_out)
    public void OnSignOut(View view) {
        //TODO 保存信息
        sendHttp();
    }

    @OnClick(R.id.signature)
    public void onSignature(View view) {
        //TODO 修改签名
        replaceFragment(R.id.common_frame, new SignatureFragment());
    }

    /**
     * 读取用户信息
     */
    private void getMassge() {
        ApiClient.send(baseContext, JConstant.SELECUSERBYDE_, null, UserMassge.class, new DataLoader<UserMassge>() {
            @Override
            public void task(String data) {
            }

            @Override
            public void succeed(UserMassge data) {
                if (data != null) {
                    massge = data;
                    serContent();
                }
            }

            @Override
            public void error(String message) {
            }
        }, JConstant.SELECUSERBYDE_);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PHOTO_REQUEST_TAKEPHOTO:
                    try {
                        Bitmap photo = BitmapUtil.getImageThumbnail(tempFile.getPath(), 400, 300);
                        if (photo != null) {
                            headImage.setImageBitmap(photo);
                            headImage.setTag(tempFile.getPath());
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
                                headImage.setImageBitmap(p);
                                headImage.setTag(path);
                            }
                        } catch (Exception e) {
                            toast("请重新选择");
                        }
                    }
                    break;
            }
            ChangeHead();
        }
    }

    // 使用系统当前日期加以调整作为照片的名称
    private String getPhotoFileName() {
        return "dx" + DateUtil.getCurDate() + ".jpg";
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    toastSuccess("头像修改成功");
                    break;
                case 1:
                    toastError("头像修改失败");
                    break;
            }
        }
    };

    @Override
    public boolean onBackPressed() {
        finishFramager();
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ApiClient.cancelRequest(JConstant.CHANGEUSERMESSAGE_);
    }
}
