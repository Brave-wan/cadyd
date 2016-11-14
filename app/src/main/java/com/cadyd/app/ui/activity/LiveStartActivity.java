package com.cadyd.app.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.cadyd.app.R;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.asynctask.retrofitmode.BaseRequestInfo;
import com.cadyd.app.asynctask.retrofitmode.HttpMethods;
import com.cadyd.app.asynctask.retrofitmode.ProgressSubscriber;
import com.cadyd.app.asynctask.retrofitmode.SubscriberOnNextListener;
import com.cadyd.app.comm.KeyBoardUtils;
import com.cadyd.app.comm.MyShare;
import com.cadyd.app.comm.Utils;
import com.cadyd.app.interfaces.TowObjectParameterInterface;
import com.cadyd.app.model.LiveStartInfo;
import com.cadyd.app.model.OSSTokenInfo;
import com.cadyd.app.model.ShareInfo;
import com.cadyd.app.ui.base.BaseActivity;
import com.cadyd.app.ui.push.SWCodecCameraStreamingActivity;
import com.cadyd.app.ui.push.gles.Config;
import com.cadyd.app.ui.view.ProgressDialogUtil;
import com.cadyd.app.ui.window.LiveMenuPopupWindow;
import com.cadyd.app.utils.BitmapUtil;
import com.cadyd.app.utils.CameraHelper;
import com.cadyd.app.utils.GPUImageFilterTools;
import com.cadyd.app.utils.Util;
import com.cadyd.app.utils.alioss.OssService;
import com.cadyd.app.utils.alioss.STSGetter;

import org.wcy.common.utils.DateUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;
import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImageBilateralFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import okhttp3.RequestBody;

public class LiveStartActivity extends BaseActivity {

    @Bind(R.id.relative_layout)
    RelativeLayout relativeLayout;
    @Bind(R.id.surfaceView)
    GLSurfaceView surfaceView;
    @Bind(R.id.live_start_cover)
    ImageView liveStartCover;
    @Bind(R.id.live_start_title)
    EditText liveStartTitle;
    @Bind(R.id.live_start_rag)
    RadioGroup liveStartRag;
    @Bind(R.id.live_start_checkbox)
    CheckBox liveStartCheckbox;

    private GPUImage mGPUImage;
    private LiveMenuPopupWindow menuPopupWindow;

    private final int RESULT_OK = -1;
    private final int PHOTO_REQUEST_TAKEPHOTO = 11;// 拍照
    private final int PHOTO_REQUEST_GALLERY = 12;// 从相册中选择
    // 创建一个以当前时间为名称的文件
    File tempFile = new File(Environment.getExternalStorageDirectory(), getPhotoFileName());

    private CameraLoader mCamera;
    private CameraHelper mCameraHelper;
    private GPUImageFilter mFilter;
    private GPUImageFilterTools.FilterAdjuster mFilterAdjuster;

    //分享平台名称
    private String[] platformToShares = {SinaWeibo.NAME, Wechat.NAME, WechatMoments.NAME, QZone.NAME, QZone.NAME};
    //分享平台标志  0:未选择  1：微博  2：qq  3：qq空间  4：微信  5：朋友圈
    private int shareIndex = 0;

    private LiveStartInfo liveStartInfo;
    private ShareInfo shareInfo;

    private String merchantCode;

    private ProgressDialogUtil dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_start);
        ButterKnife.bind(this);
        initWidget();
    }

    private void initWidget() {
        merchantCode = getIntent().getStringExtra("merchantCode");

        mCameraHelper = new CameraHelper(this);
        mCamera = new CameraLoader();

        mGPUImage = new GPUImage(this);
        mGPUImage.setGLSurfaceView(surfaceView);
        mFilter = new GPUImageBilateralFilter();
        //GPUImageCannyEdgeDetectionFilter GPUImageCombinationFilter GPUImageHSBFilter
        mGPUImage.setFilter(mFilter);
        mFilterAdjuster = new GPUImageFilterTools.FilterAdjuster(mFilter);
        mFilterAdjuster.adjust(50);

        liveStartInfo = new LiveStartInfo();
        shareInfo = new ShareInfo();

        liveStartRag.setOnCheckedChangeListener(checkedChangeListener);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyBoardUtils.closeKeybord(liveStartTitle, activity);
            }
        });
    }

    private RadioGroup.OnCheckedChangeListener checkedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {
            switch (radioGroup.getCheckedRadioButtonId()) {
                case R.id.live_start_rb_sina:
                    shareIndex = 1;
                    break;
                case R.id.live_start_rb_qq:
                    shareIndex = 2;
                    break;
                case R.id.live_start_rb_kongjian:
                    shareIndex = 3;
                    break;
                case R.id.live_start_rb_weixin:
                    shareIndex = 4;
                    break;
                case R.id.live_start_rb_friend:
                    shareIndex = 5;
                    break;
            }
        }
    };

    /**
     * 旋转按钮
     *
     * @param view
     */
    public void RotateClick(View view) {
        if (!mCameraHelper.hasFrontCamera() || !mCameraHelper.hasBackCamera()) {
            toastError("没有前置摄像头!");
            return;
        }
        mCamera.switchCamera();
    }

    /**
     * 关闭按钮
     *
     * @param view
     */
    public void CloseClick(View view) {
        finish();
    }

    /**
     * 更换封面
     *
     * @param view
     */
    public void ChangCoverClick(View view) {
        if (menuPopupWindow == null) {
            List<String> list = new ArrayList<>();
            list.add("相册");
            list.add("拍照");
            menuPopupWindow = new LiveMenuPopupWindow(activity, list, new TowObjectParameterInterface() {
                @Override
                public void Onchange(int type, int postion, Object object) {
                    if (postion == 0) {
                        Intent intent = new Intent(Intent.ACTION_PICK, null);
                        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                        startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
                        menuPopupWindow.dismissWindo();
                    } else if (postion == 1) {
                        Intent cameraintent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        // 指定调用相机拍照后照片的储存路径
                        cameraintent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
                        startActivityForResult(cameraintent, PHOTO_REQUEST_TAKEPHOTO);
                        menuPopupWindow.dismissWindo();
                    }
                }
            });
            menuPopupWindow.showWindo(surfaceView);
        } else {
            menuPopupWindow.showWindo(surfaceView);
        }
    }

    /**
     * 开始直播
     *
     * @param view
     */
    public void StartLiveClick(View view) {
        if (TextUtils.isEmpty(liveStartTitle.getText().toString())) {
            toastError("请先输入直播标题");
            return;
        }
        if (!liveStartCheckbox.isChecked()) {
            toastError("请先阅读并同意第一点直播协议");
            return;
        }
        if (shareIndex == 0) {
            toastError("请先选择分享平台");
            return;
        }
        if (liveStartCover.getTag() == null) {
            toastError("请先选择封面");
            return;
        }
        String path = liveStartCover.getTag().toString();
        updateImg(path);
    }

    private void updateImg(final String path) {
        HttpMethods.getInstance().getFederationToken(new ProgressSubscriber<BaseRequestInfo<OSSTokenInfo>>(new SubscriberOnNextListener<BaseRequestInfo<OSSTokenInfo>>() {
            @Override
            public void onNext(BaseRequestInfo<OSSTokenInfo> o) {
                OSSTokenInfo ossTokenInfo = o.getData();
                OssServiceCreate(ossTokenInfo, path);
            }

            @Override
            public void onError(Throwable e) {
                Log.e("GetSTSTokenFail", e.toString());
            }

            @Override
            public void onLogicError(String msg) {

            }
        }, LiveStartActivity.this));
    }

    public void OssServiceCreate(OSSTokenInfo ossTokenInfo, String path) {
        if (dialog == null){
            dialog = new ProgressDialogUtil(this, "正在上传图片...");
        }
        dialog.show();
        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(5); // 最大并发请求书，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
        OSSCredentialProvider credentialProvider = new STSGetter(ossTokenInfo);
        OSS oss = new OSSClient(getApplicationContext(), ossTokenInfo.getOSS_Url(), credentialProvider, conf);
        OssService ossService = new OssService(oss, ossTokenInfo.getOSS_BucketName());
        ossService.setUploadImageClick(new OssService.UploadImageClick() {
            @Override
            public void onSuccess(String imgUrl) {
                Looper.prepare();
                LiveOn(imgUrl);
                dialog.dismissDialog();
            }

            @Override
            public void onFailure() {
                toastError("封面上传失败");
                dialog.dismissDialog();
            }

        });
        ossService.setCallbackAddress(ossTokenInfo.getOSS_Callback());
        ossService.asyncPutImage(getImageName(ossTokenInfo.getOSS_ImgPath()), path);
    }

    public String getImageName(String imgName) {
        return imgName + "/" + DateUtil.getCurrentDate("yyyy/MM/dd") + "/" + Util.getStringRandom(10) + ".jpg";
    }

    public void LiveOn(String imgPath) {
        Map<Object, Object> map = new HashMap<>();
        map.put("conversationTitle", liveStartTitle.getText().toString());
        map.put("liveRoomCoverPath", imgPath);
        map.put("areaCode", "122");
        if (!TextUtils.isEmpty(merchantCode)) {
            map.put("merchantPassword", merchantCode);
        }
        RequestBody key = ApiClient.getRequestBody(map);
        HttpMethods.getInstance().doLiveOn(new ProgressSubscriber<BaseRequestInfo<LiveStartInfo>>(new SubscriberOnNextListener<BaseRequestInfo<LiveStartInfo>>() {
            @Override
            public void onNext(BaseRequestInfo<LiveStartInfo> o) {
                liveStartInfo = o.getData();
                shareHttp();
            }

            @Override
            public void onError(Throwable e) {
                toastError("开播失败");
            }

            @Override
            public void onLogicError(String msg) {

            }
        }, LiveStartActivity.this), key);
        Looper.loop();
    }

    /**
     * 分享
     */
    public void shareHttp() {
        Map<Object, Object> map = new HashMap<>();
        map.put("shareType", "76eb35ff-9eba-4863-8a4b-36ff75cfef08");
        map.put("param1", liveStartInfo.getRoomCode());
        map.put("param2", !TextUtils.isEmpty(merchantCode) ? 2 : 1);
        RequestBody key = ApiClient.getRequestBody(map);
        HttpMethods.getInstance().getShareInfo(new ProgressSubscriber<BaseRequestInfo<ShareInfo>>(new SubscriberOnNextListener<BaseRequestInfo<ShareInfo>>() {
            @Override
            public void onNext(BaseRequestInfo<ShareInfo> o) {
                shareInfo = o.getData();
                share();
                startSWCodecCamera();
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onLogicError(String msg) {

            }
        }, LiveStartActivity.this), key);
    }

    public void share() {
        MyShare.showShare(this, shareInfo.getTitle(), shareInfo.getContent(), shareInfo.getClickUrl(), shareInfo.getPictureUrl(), platformToShares[shareIndex - 1]);
    }

    /**
     * 跳转直播页面
     */
    private void startSWCodecCamera() {
        Intent intent = new Intent(LiveStartActivity.this, SWCodecCameraStreamingActivity.class);
        String publishUrl = Config.EXTRA_PUBLISH_URL_PREFIX + liveStartInfo.getQiniuParameter().getPushStream();
        intent.putExtra(Config.EXTRA_KEY_PUB_URL, publishUrl);
        intent.putExtra("info", liveStartInfo);
        startActivity(intent);
        finish();
    }

    /**
     * 查看直播协议
     *
     * @param view
     */
    public void AgreementClick(View view) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PHOTO_REQUEST_TAKEPHOTO:
                    try {
                        Bitmap photo = BitmapUtil.getImageThumbnail(tempFile.getPath(), 400, 300);
                        if (photo != null) {
                            liveStartCover.setImageBitmap(photo);
                            liveStartCover.setTag(tempFile.getPath());
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
                                liveStartCover.setImageBitmap(p);
                                liveStartCover.setTag(path);
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
    protected void onResume() {
        super.onResume();
        mCamera.onResume();
    }

    @Override
    protected void onPause() {
        mCamera.onPause();
        super.onPause();
    }

    private class CameraLoader {

        private int mCurrentCameraId = 0;
        private Camera mCameraInstance;

        public void onResume() {
            setUpCamera(mCurrentCameraId);
        }

        public void onPause() {
            releaseCamera();
        }

        public void switchCamera() {
            releaseCamera();
            mCurrentCameraId = (mCurrentCameraId + 1) % mCameraHelper.getNumberOfCameras();
            setUpCamera(mCurrentCameraId);
        }

        private void setUpCamera(final int id) {
            mCameraInstance = getCameraInstance(id);
            Camera.Parameters parameters = mCameraInstance.getParameters();
            // TODO adjust by getting supportedPreviewSizes and then choosing
            // the best one for screen size (best fill screen)
            if (parameters.getSupportedFocusModes().contains(
                    Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            }
            mCameraInstance.setParameters(parameters);

            int orientation = mCameraHelper.getCameraDisplayOrientation(
                    LiveStartActivity.this, mCurrentCameraId);
            CameraHelper.CameraInfo2 cameraInfo = new CameraHelper.CameraInfo2();
            mCameraHelper.getCameraInfo(mCurrentCameraId, cameraInfo);
            boolean flipHorizontal = cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT;
            mGPUImage.setUpCamera(mCameraInstance, orientation, flipHorizontal, false);
        }

        /**
         * A safe way to get an instance of the Camera object.
         */
        private Camera getCameraInstance(final int id) {
            Camera c = null;
            try {
                c = mCameraHelper.openCamera(id);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return c;
        }

        private void releaseCamera() {
            mCameraInstance.setPreviewCallback(null);
            mCameraInstance.release();
            mCameraInstance = null;
        }
    }
}
