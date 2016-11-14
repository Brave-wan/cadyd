package com.cadyd.app.ui.push;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cadyd.app.MyApplication;
import com.cadyd.app.R;
import com.cadyd.app.adapter.ChatListAdatper;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.comm.KeyBoardUtils;
import com.cadyd.app.comm.MyShare;
import com.cadyd.app.interfaces.SocketConnectListener;
import com.cadyd.app.model.ChatMesgInfo;
import com.cadyd.app.model.LiveStartInfo;
import com.cadyd.app.model.SocketMode;
import com.cadyd.app.model.liveDetil;
import com.cadyd.app.ui.activity.LiveEndActivity;
import com.cadyd.app.ui.push.gles.CameraPreviewFrameView;
import com.cadyd.app.ui.push.gles.Config;
import com.cadyd.app.ui.push.gles.FBO;
import com.cadyd.app.ui.view.CircleImageView;
import com.cadyd.app.ui.view.DividerGridItemDecoration;
import com.cadyd.app.utils.HttpAnalyze;
import com.cadyd.app.utils.SocketSingleton;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;
import com.qiniu.android.dns.DnsManager;
import com.qiniu.android.dns.IResolver;
import com.qiniu.android.dns.NetworkInfo;
import com.qiniu.android.dns.http.DnspodFree;
import com.qiniu.android.dns.local.AndroidDnsServer;
import com.qiniu.android.dns.local.Resolver;
import com.qiniu.pili.droid.streaming.AVCodecType;
import com.qiniu.pili.droid.streaming.AudioSourceCallback;
import com.qiniu.pili.droid.streaming.CameraStreamingSetting;
import com.qiniu.pili.droid.streaming.CameraStreamingSetting.CAMERA_FACING_ID;
import com.qiniu.pili.droid.streaming.MediaStreamingManager;
import com.qiniu.pili.droid.streaming.MicrophoneStreamingSetting;
import com.qiniu.pili.droid.streaming.StreamStatusCallback;
import com.qiniu.pili.droid.streaming.StreamingPreviewCallback;
import com.qiniu.pili.droid.streaming.StreamingProfile;
import com.qiniu.pili.droid.streaming.StreamingSessionListener;
import com.qiniu.pili.droid.streaming.StreamingState;
import com.qiniu.pili.droid.streaming.StreamingStateChangedListener;
import com.qiniu.pili.droid.streaming.SurfaceTextureCallback;

import org.json.JSONObject;
import org.wcy.android.utils.adapter.CommonRecyclerAdapter;
import org.wcy.android.utils.adapter.ViewRecyclerHolder;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import static com.cadyd.app.R.id.video_attentionNumber;

/**
 * Created by jerikc on 15/7/6.
 */
public class StreamingBaseActivity extends Activity implements
        View.OnLayoutChangeListener,
        StreamStatusCallback,
        StreamingPreviewCallback,
        SurfaceTextureCallback,
        AudioSourceCallback,
        CameraPreviewFrameView.Listener,
        StreamingSessionListener,
        StreamingStateChangedListener, SocketConnectListener {

    private static final String TAG = "StreamingBaseActivity";

    private static final int ZOOM_MINIMUM_WAIT_MILLIS = 33; //ms

    private Context mContext;

    protected Button mShutterButton;
    //    private Button mTorchBtn;
    private Button mEncodingOrientationSwitcherBtn;
    private RotateLayout mRotateLayout;


    protected TextView mSatusTextView;
    private TextView mLogTextView;
    private TextView mStreamStatus;

    protected boolean mShutterButtonPressed = false;
    private boolean mIsTorchOn = false;
    private boolean mIsNeedMute = false;
    private boolean mIsNeedFB = false;
    private boolean isEncOrientationPort = true;

    protected static final int MSG_START_STREAMING = 0;
    protected static final int MSG_STOP_STREAMING = 1;
    private static final int MSG_SET_ZOOM = 2;
    private static final int MSG_MUTE = 3;
    private static final int MSG_FB = 4;

    protected String mStatusMsgContent;

    protected String mLogContent = "\n";

    private View mRootView;

    protected MediaStreamingManager mMediaStreamingManager;
    protected CameraStreamingSetting mCameraStreamingSetting;
    protected MicrophoneStreamingSetting mMicrophoneStreamingSetting;
    protected StreamingProfile mProfile;
    protected JSONObject mJSONObject;
    private boolean mOrientationChanged = false;

    protected boolean mIsReady = false;

    private int mCurrentZoom = 2;
    private int mMaxZoom = 0;
    private FBO mFBO = new FBO();
    private ListView mListView;
    private Switcher mSwitcher = new Switcher();
    private int mCurrentCamFacingIndex;
    private View LiveEmpty;

    protected Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_START_STREAMING:
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            mMediaStreamingManager.startStreaming();
                            Log.i("wan", "开启直播");
                        }
                    }).start();
                    break;
                case MSG_STOP_STREAMING:
                    boolean isStart = mMediaStreamingManager.stopStreaming();
                    if (isStart) {
                        Toast.makeText(StreamingBaseActivity.this, "暂停播放", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 200:
//                    liveEmptyText.setText("直播内容涉及违规，已停播");
//                    LiveEmpty.setVisibility(View.VISIBLE);
                    break;

                case Config.SOCKETCONNECT:
                    initSocketContent();
                    break;
                default:
                    Log.e(TAG, "Invalid message");
                    break;
            }
        }
    };


    private RelativeLayout beautyView;
    private String conversationID = "0b94a421417748859e801d52c5d6f764";
    LiveStartInfo info;

    private TextView liveEmptyText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        } else {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
        }
        super.onCreate(savedInstanceState);
        //保持屏幕唤醒
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        if (Config.SCREEN_ORIENTATION == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            isEncOrientationPort = true;
        } else if (Config.SCREEN_ORIENTATION == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            isEncOrientationPort = false;
        }

        setRequestedOrientation(Config.SCREEN_ORIENTATION);
        setContentView(R.layout.activity_camera_streaming);


        String publishUrlFromServer = getIntent().getStringExtra(Config.EXTRA_KEY_PUB_URL);
        info = (LiveStartInfo) getIntent().getSerializableExtra("info");
        if (info != null) {
            channelId = info.getChatParameter().getChannelID();
            serverId = info.getChatParameter().getServerID();
            conversationID = info.getConversationID();
        }

        if (serverId != null) {
            Config.serverId = serverId;
            Log.i("wan", "Config.serverId-------" + serverId);
        }
        Log.i("wan", "publishUrlFromServer:" + publishUrlFromServer);
        mContext = this;
        StreamingProfile.AudioProfile aProfile = new StreamingProfile.AudioProfile(44100, 96 * 1024);
        StreamingProfile.VideoProfile vProfile = new StreamingProfile.VideoProfile(30, 1000 * 1024, 48);
        StreamingProfile.AVProfile avProfile = new StreamingProfile.AVProfile(vProfile, aProfile);
        mProfile = new StreamingProfile();
        if (publishUrlFromServer.startsWith(Config.EXTRA_PUBLISH_URL_PREFIX)) {
            // publish url
            try {
                mProfile.setPublishUrl(publishUrlFromServer.substring(Config.EXTRA_PUBLISH_URL_PREFIX.length()));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        } else if (publishUrlFromServer.startsWith(Config.EXTRA_PUBLISH_JSON_PREFIX)) {
            try {
                mJSONObject = new JSONObject(publishUrlFromServer.substring(Config.EXTRA_PUBLISH_JSON_PREFIX.length()));
                StreamingProfile.Stream stream = new StreamingProfile.Stream(mJSONObject);
                mProfile.setStream(stream);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "Invalid Publish Url", Toast.LENGTH_LONG).show();
        }

        mProfile.setVideoQuality(StreamingProfile.VIDEO_QUALITY_HIGH3)
                .setAudioQuality(StreamingProfile.AUDIO_QUALITY_MEDIUM2)
                .setEncodingSizeLevel(Config.ENCODING_LEVEL)
                .setEncoderRCMode(StreamingProfile.EncoderRCModes.QUALITY_PRIORITY)
                .setAVProfile(avProfile)
                .setDnsManager(getMyDnsManager())
                .setStreamStatusConfig(new StreamingProfile.StreamStatusConfig(3))
//                .setEncodingOrientation(StreamingProfile.ENCODING_ORIENTATION.PORT)
                .setSendingBufferProfile(new StreamingProfile.SendingBufferProfile(0.2f, 0.8f, 3.0f, 20 * 1000));

        CAMERA_FACING_ID cameraFacingId = chooseCameraFacingId();
        mCurrentCamFacingIndex = cameraFacingId.ordinal();
        mCameraStreamingSetting = new CameraStreamingSetting();
        mCameraStreamingSetting.setCameraId(Camera.CameraInfo.CAMERA_FACING_BACK)
                .setContinuousFocusModeEnabled(true)
                .setRecordingHint(false)
                .setCameraFacingId(cameraFacingId)
                .setBuiltInFaceBeautyEnabled(true)
                .setResetTouchFocusDelayInMs(3000)
                .setFocusMode(CameraStreamingSetting.FOCUS_MODE_CONTINUOUS_PICTURE)
                .setCameraPrvSizeLevel(CameraStreamingSetting.PREVIEW_SIZE_LEVEL.SMALL)
                .setCameraPrvSizeRatio(CameraStreamingSetting.PREVIEW_SIZE_RATIO.RATIO_16_9)
                .setFaceBeautySetting(new CameraStreamingSetting.FaceBeautySetting(1.0f, 1.0f, 0.8f))
                .setVideoFilter(CameraStreamingSetting.VIDEO_FILTER_TYPE.VIDEO_FILTER_BEAUTY);
        mIsNeedFB = true;
        mMicrophoneStreamingSetting = new MicrophoneStreamingSetting();
        mMicrophoneStreamingSetting.setBluetoothSCOEnabled(false);

        initUIs();
    }


    @Override
    protected void onResume() {
        super.onResume();
        mMediaStreamingManager.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        mIsReady = false;
        mShutterButtonPressed = false;
        mHandler.removeCallbacksAndMessages(null);
        mMediaStreamingManager.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMediaStreamingManager.destroy();
        if (socketSingleton != null && mSocket != null)
            socketSingleton.disConnect();
    }


    protected void startStreaming() {
        mHandler.removeCallbacksAndMessages(null);
        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_START_STREAMING), 50);
    }

    protected void stopStreaming() {
        mHandler.removeCallbacksAndMessages(null);
        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_STOP_STREAMING), 50);
    }

    @Override
    public boolean onRecordAudioFailedHandled(int err) {
        mMediaStreamingManager.updateEncodingType(AVCodecType.SW_VIDEO_CODEC);
        mMediaStreamingManager.startStreaming();
        return true;
    }

    @Override
    public boolean onRestartStreamingHandled(int err) {
        Log.i(TAG, "onRestartStreamingHandled");
        return mMediaStreamingManager.startStreaming();
    }

    @Override
    public Camera.Size onPreviewSizeSelected(List<Camera.Size> list) {
        Camera.Size size = null;
        if (list != null) {
            for (Camera.Size s : list) {
                if (s.height >= 480) {
                    size = s;
                    break;
                }
            }
        }
        return size;
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        Log.i(TAG, "onSingleTapUp X:" + e.getX() + ",Y:" + e.getY());

        if (mIsReady) {
            setFocusAreaIndicator();
            mMediaStreamingManager.doSingleTapUp((int) e.getX(), (int) e.getY());
            return true;
        }
        return false;
    }

    @Override
    public boolean onZoomValueChanged(float factor) {
        if (mIsReady && mMediaStreamingManager.isZoomSupported()) {
            mCurrentZoom = (int) (mMaxZoom * factor);
            mCurrentZoom = Math.min(mCurrentZoom, mMaxZoom);
            mCurrentZoom = Math.max(0, mCurrentZoom);

            Log.d(TAG, "zoom ongoing, scale: " + mCurrentZoom + ",factor:" + factor + ",maxZoom:" + mMaxZoom);
            if (!mHandler.hasMessages(MSG_SET_ZOOM)) {
                mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ZOOM), ZOOM_MINIMUM_WAIT_MILLIS);
                return true;
            }
        }
        return false;
    }

    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        Log.i(TAG, "view!!!!:" + v);
    }

    @Override
    public void onPreviewFrame(byte[] bytes, Camera camera) {

    }

    @Override
    public boolean onPreviewFrame(byte[] bytes, int width, int height) {
        return true;
    }

    @Override
    public void onSurfaceCreated() {
        Log.i(TAG, "onSurfaceCreated");
        mFBO.initialize(this);
    }

    @Override
    public void onSurfaceChanged(int width, int height) {
        Log.i(TAG, "onSurfaceChanged width:" + width + ",height:" + height);
        mFBO.updateSurfaceSize(width, height);
    }

    @Override
    public void onSurfaceDestroyed() {
        Log.i(TAG, "onSurfaceDestroyed");
        mFBO.release();
    }

    @Override
    public int onDrawFrame(int texId, int texWidth, int texHeight, float[] transformMatrix) {
        int newTexId = mFBO.drawFrame(texId, texWidth, texHeight);
        return newTexId;
    }

    @Override
    public void onAudioSourceAvailable(ByteBuffer byteBuffer, int size, boolean eof) {

    }

    @Override
    public void notifyStreamStatusChanged(final StreamingProfile.StreamStatus streamStatus) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mStreamStatus.setText("bitrate:" + streamStatus.totalAVBitrate / 1024 + " kbps"
                        + "\naudio:" + streamStatus.audioFps + " fps"
                        + "\nvideo:" + streamStatus.videoFps + " fps");

            }
        });
    }

    @Override
    public void onStateChanged(StreamingState streamingState, Object extra) {
        switch (streamingState) {
            case PREPARING:
                mStatusMsgContent = getString(R.string.string_state_preparing);
                Log.i("wan", "PREPARING");
                break;
            case READY:
                Log.i("wan", "READY");
                mIsReady = true;
                mMaxZoom = mMediaStreamingManager.getMaxZoom();
                mStatusMsgContent = getString(R.string.string_state_ready);
                // start streaming when READY
                startStreaming();
                break;
            case CONNECTING:
                Log.i("wan", "CONNECTING");
                mStatusMsgContent = getString(R.string.string_state_connecting);
                break;
            case STREAMING:
                Log.i("wan", "STREAMING");
                mStatusMsgContent = getString(R.string.string_state_streaming);
                break;
            case SHUTDOWN:
                Log.i("wan", "SHUTDOWN");
                mStatusMsgContent = getString(R.string.string_state_ready);
                if (mOrientationChanged) {
                    mOrientationChanged = false;
                    startStreaming();
                }
                break;
            case IOERROR:
                Log.i("wan", "IOERROR");
                mLogContent += "IOERROR\n";
                mStatusMsgContent = getString(R.string.string_state_ready);
                break;
            case UNKNOWN:
                Log.i("wan", "UNKNOWN");
                mStatusMsgContent = getString(R.string.string_state_ready);
                break;
            case SENDING_BUFFER_EMPTY:
                Log.i("wan", "SENDING_BUFFER_EMPTY");
                break;
            case SENDING_BUFFER_FULL:
                Log.i("wan", "SENDING_BUFFER_FULL");
                break;
            case AUDIO_RECORDING_FAIL:
                Log.i("wan", "PREPARING");
                break;
            case OPEN_CAMERA_FAIL:
                Log.e(TAG, "Open Camera Fail. id:" + extra);
                break;
            case DISCONNECTED:
                Log.i("wan", "DISCONNECTED");
                mLogContent += "DISCONNECTED\n";
                mHandler.sendEmptyMessage(200);
                break;
            case INVALID_STREAMING_URL:
                Log.e(TAG, "Invalid streaming url:" + extra);
                break;
            case UNAUTHORIZED_STREAMING_URL:
                Log.e(TAG, "Unauthorized streaming url:" + extra);
                mLogContent += "Unauthorized Url\n";
                break;
            case CAMERA_SWITCHED:
//                mShutterButtonPressed = false;
                if (extra != null) {
                    Log.i(TAG, "current camera id:" + (Integer) extra);
                }
                Log.i(TAG, "camera switched");

                break;
            case TORCH_INFO:
                if (extra != null) {
                    final boolean isSupportedTorch = (Boolean) extra;
                    Log.i(TAG, "isSupportedTorch=" + isSupportedTorch);
                }
                break;
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mLogTextView != null) {
                    mLogTextView.setText(mLogContent);

                }
                mSatusTextView.setText(mStatusMsgContent);
            }
        });
    }


    SeekBar seekBarBeauty;

    //    private LiveBackInfo liveBackInfo;
    //姓名
    private TextView name;
    //关注人数
    private TextView liveNumber;
    //观看人数
    private TextView guanNumber;
    private ImageView backImage;
    //進度條
    private ProgressDialog dialog;
    private ChatListAdatper adatper;
    ArrayList<ChatMesgInfo> chatMeslist = new ArrayList<>();
    private RadioGroup mGroup;
    private RadioButton mCanering, mMeiyan, mMsg;

    private RecyclerView gg_list;
    private CommonRecyclerAdapter<liveDetil> recyclerAdapter;
    private List<liveDetil> list = new ArrayList<>();
    private TextView mSuer;
    private ImageView msgBakc;
    private LinearLayout inputMsgView;
    private CircleImageView mCircleImageView;
    private TextView roomNumber;
    private TextView attentionNumber;
    private String channelId;
    private String serverId;
    private TextView liveShare;
    private EditText void_content;
    private TextView voide_send;
    private Button live_leave;


    private void initUIs() {
        LiveEmpty = findViewById(R.id.steaming_live_empty);
        live_leave = (Button) findViewById(R.id.live_leave);
        voide_send = (TextView) findViewById(R.id.voide_send);
        liveEmptyText = (TextView) findViewById(R.id.live_empty_text);
        void_content = (EditText) findViewById(R.id.void_content);
        liveShare = (TextView) findViewById(R.id.voideo_share);
        attentionNumber = (TextView) findViewById(video_attentionNumber);
        roomNumber = (TextView) findViewById(R.id.video_roomNumber);
        mCircleImageView = (CircleImageView) findViewById(R.id.video_headImage);
        inputMsgView = (LinearLayout) findViewById(R.id.input_text);

        mSuer = (TextView) findViewById(R.id.liveBeauty_sure);
        msgBakc = (ImageView) findViewById(R.id.msg_back);

        beautyView = (RelativeLayout) findViewById(R.id.include_beauty_view);
        adatper = new ChatListAdatper(StreamingBaseActivity.this, chatMeslist);
//        liveBackInfo = (LiveBackInfo) getIntent().getExtras().get("info");
        mRootView = findViewById(R.id.content);
        mListView = (ListView) findViewById(R.id.chat_list);
        mRootView.addOnLayoutChangeListener(this);
        liveNumber = (TextView) findViewById(R.id.video_liveNumber);
        guanNumber = (TextView) findViewById(R.id.video_number);
        name = (TextView) findViewById(R.id.video_name);
        backImage = (ImageView) findViewById(R.id.media_back);

        mShutterButton = (Button) findViewById(R.id.toggleRecording_button);
        mSatusTextView = (TextView) findViewById(R.id.streamingStatus);

        mLogTextView = (TextView) findViewById(R.id.log_info);
        mStreamStatus = (TextView) findViewById(R.id.stream_status);

        mGroup = (RadioGroup) findViewById(R.id.live_radio_group);
        mCanering = (RadioButton) findViewById(R.id.live_radio_canerming);
        mMeiyan = (RadioButton) findViewById(R.id.live_radio_merchandise);
        mMsg = (RadioButton) findViewById(R.id.live_radio_msg);
        mGroup.setOnCheckedChangeListener(mOnCheckedChangeListener);
        backImage.setOnClickListener(mOnClickListener);
        liveShare.setOnClickListener(mOnClickListener);

        gg_list = (RecyclerView) findViewById(R.id.gg_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        gg_list.setLayoutManager(linearLayoutManager);
        gg_list.addItemDecoration(new DividerGridItemDecoration(this, GridLayout.VERTICAL, 6, getResources().getColor(R.color.transparent)));

        recyclerAdapter = new CommonRecyclerAdapter<liveDetil>(this, list, R.layout.pl_image_view) {
            @Override
            public void convert(ViewRecyclerHolder helper, final liveDetil item) {
                ImageView imageView = helper.getView(R.id.image_view);
                ApiClient.displayCirleImage(item.url, imageView, R.mipmap.user_default);

//                imageView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent = new Intent(StreamingBaseActivity.this, PersonalCenterActivity.class);
//                        intent.putExtra("userId", item.id);
//                        startActivity(intent);
//                    }
//                });
            }
        };

        gg_list.setAdapter(recyclerAdapter);
        mListView.setAdapter(adatper);

        msgBakc.setOnClickListener(mOnClickListener);
        mSuer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                beautyView.setVisibility(View.GONE);
            }
        });


        mShutterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mShutterButtonPressed) {
                    stopStreaming();
                } else {
                    startStreaming();
                }
            }
        });

        seekBarBeauty = (SeekBar) findViewById(R.id.beautyLevel_seekBar);
        seekBarBeauty.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                CameraStreamingSetting.FaceBeautySetting fbSetting = mCameraStreamingSetting.getFaceBeautySetting();
                fbSetting.beautyLevel = progress / 100.0f;
                fbSetting.whiten = progress / 100.0f;
                fbSetting.redden = progress / 100.0f;
                Log.i("wan", "fbSetting----" + fbSetting);
                mMediaStreamingManager.updateFaceBeautySetting(fbSetting);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        voide_send.setOnClickListener(mOnClickListener);
        mRootView.setOnClickListener(mOnClickListener);
        live_leave.setOnClickListener(mOnClickListener);

        initButtonText();
    }


    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.live_leave:
                    finish();
                    break;
                case R.id.content:
                    KeyBoardUtils.closeKeybord(void_content, StreamingBaseActivity.this);
                    break;
                case R.id.voide_send:

                    if (void_content.getText().toString().trim().equals("")) {
                        Toast.makeText(StreamingBaseActivity.this, "消息不能为空", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        if (info != null) {
                            if (mSocket.connected()) {
                                Log.i("wan", "链接成功");
                                String contentMsg = void_content.getText().toString().trim();
                                JSONObject jsonObject = socketSingleton.getSocketJson(info.getChatParameter().getChannelID(), "主播", contentMsg);
                                mSocket.emit("CHATMSG", jsonObject);
                                void_content.setText("");
                            } else {
                                mHandler.sendEmptyMessage(Config.SOCKETCONNECT);
                                Log.i("wan", "正在重连");
                            }
                        }
                    }

                    break;
                case R.id.media_back:
                    if (info == null) return;
                    if (mSocket.connected()) {
                        JSONObject jsonObject = socketSingleton.getSocketJson(info.getChatParameter().getChannelID(), "主播", "直播结束关闭房间");
                        mSocket.emit("UN_CONNECT", jsonObject);
                    } else {
                        mHandler.sendEmptyMessage(Config.SOCKETCONNECT);
                    }
                    startActivity(new Intent(StreamingBaseActivity.this, LiveEndActivity.class));
                    finish();
                    break;
                case R.id.msg_back:
                    mGroup.setVisibility(View.VISIBLE);
                    inputMsgView.setVisibility(View.GONE);
                    KeyBoardUtils.closeKeybord(void_content, StreamingBaseActivity.this);

                    break;
                case R.id.voideo_share:

                    if (info != null) {
                        MyShare.showShare(StreamingBaseActivity.this, "好店铺正在直播，快来围观。", "巷子里的就少年", "http://schapp.cadyd.com/live.html?anchorToken=", info.getHeadImageUrl());
                    }
                    break;
            }
        }
    };

    RadioGroup.OnCheckedChangeListener mOnCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.live_radio_canerming:
                    mCanering.setChecked(false);
                    mHandler.removeCallbacks(mSwitcher);
                    mHandler.postDelayed(mSwitcher, 100);
                    break;

                case R.id.live_radio_merchandise:
                    beautyView.setVisibility(mMeiyan.isChecked() ? View.VISIBLE : View.GONE);
                    mMeiyan.setChecked(false);
                    break;

                case R.id.live_radio_msg:
                    mMsg.setChecked(false);
                    inputMsgView.setVisibility(View.VISIBLE);
                    mGroup.setVisibility(View.GONE);
                    break;
            }

        }
    };


    private void initButtonText() {
        if (info != null) {
            guanNumber.setText(" " + info.getFoucsCount());
            roomNumber.setText(" " + info.getRoomCode());
            attentionNumber.setText(info.getFoucsCount() + "");
            ApiClient.displayImage(MyApplication.getInstance().getSignInModel().photo, mCircleImageView);
            socketSingleton = SocketSingleton.getInstance(Config.serverId);
            mSocket = socketSingleton.getSocket();
            mSocket = socketSingleton.getSocket();
            //注册监听
            mSocket.on("CREATE_ROOM", emitterListener);
            mSocket.on("CHATGIFT", CHATGIFTListener);
            mSocket.on("CHATMSG", CHATMSGListener);
            mSocket.on("UN_CONNECT", UN_CONNECTListener);
            mSocket.on("CONCERN", CONCERNListener);
            mSocket.on("UN_CONCERN", UN_CONCERNListener);
            mSocket.on("STOPPED", STOPPEDListener);
            mHandler.sendEmptyMessage(Config.SOCKETCONNECT);
        }
    }


    protected void setFocusAreaIndicator() {
        if (mRotateLayout == null) {
            mRotateLayout = (RotateLayout) findViewById(R.id.focus_indicator_rotate_layout);
            mMediaStreamingManager.setFocusAreaIndicator(mRotateLayout,
                    mRotateLayout.findViewById(R.id.focus_indicator));
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return true;
    }

    private static DnsManager getMyDnsManager() {
        IResolver r0 = new DnspodFree();
        IResolver r1 = AndroidDnsServer.defaultResolver();
        IResolver r2 = null;
        try {
            r2 = new Resolver(InetAddress.getByName("119.29.29.29"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return new DnsManager(NetworkInfo.normal, new IResolver[]{r0, r1, r2});
    }

    private CAMERA_FACING_ID chooseCameraFacingId() {
        if (CameraStreamingSetting.hasCameraFacing(CAMERA_FACING_ID.CAMERA_FACING_3RD)) {
            return CAMERA_FACING_ID.CAMERA_FACING_3RD;
        } else if (CameraStreamingSetting.hasCameraFacing(CAMERA_FACING_ID.CAMERA_FACING_FRONT)) {
            return CAMERA_FACING_ID.CAMERA_FACING_FRONT;
        } else {
            return CAMERA_FACING_ID.CAMERA_FACING_BACK;
        }
    }


    private class Switcher implements Runnable {
        @Override
        public void run() {
            mCurrentCamFacingIndex = (mCurrentCamFacingIndex + 1) % CameraStreamingSetting.getNumberOfCameras();

            CAMERA_FACING_ID facingId;
            if (mCurrentCamFacingIndex == CAMERA_FACING_ID.CAMERA_FACING_BACK.ordinal()) {
                facingId = CAMERA_FACING_ID.CAMERA_FACING_BACK;
            } else if (mCurrentCamFacingIndex == CAMERA_FACING_ID.CAMERA_FACING_FRONT.ordinal()) {
                facingId = CAMERA_FACING_ID.CAMERA_FACING_FRONT;
            } else {
                facingId = CAMERA_FACING_ID.CAMERA_FACING_3RD;
            }
            Log.i(TAG, "switchCamera:" + facingId);
            mMediaStreamingManager.switchCamera(facingId);
        }
    }


    private SocketSingleton socketSingleton;
    private Socket mSocket;


    public void initSocketContent() {
        if (mSocket.connected()) {
            Log.i("wan", "链接成功。。。");
            JSONObject jsonObject = socketSingleton.getSocketJson(info.getChatParameter().getChannelID(), "主播", " 直播中...");
            mSocket.emit("CREATE_ROOM", jsonObject);
        } else {
            mHandler.sendEmptyMessage(Config.SOCKETCONNECT);
            Log.i("wan", "链接失败，正在重连");
        }
    }

    private Emitter.Listener CHATGIFTListener = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject data = (JSONObject) args[0];
            Log.i("wan", "CHATGIFTListener------" + data.toString());
            SocketMode socketMode = new HttpAnalyze().getParseSocket(data.toString());
            if (socketMode != null) {
                Message message = new Message();
                message.what = Config.SOCKETCHATGIFT;
                message.obj = socketMode;
                connetHandler.sendMessage(message);
            } else {
                Log.i("wan", "数据解析异常");
            }
        }
    };
    private Emitter.Listener UN_CONNECTListener = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject data = (JSONObject) args[0];
            Log.i("wan", "UN_CONNECTListener------" + data.toString());
            SocketMode socketMode = new HttpAnalyze().getParseSocket(data.toString());
            if (socketMode != null) {
                Message message = new Message();
                message.what = Config.SOCKETUNCONNECT;
                message.obj = socketMode;
                connetHandler.sendMessage(message);
            } else {
                Log.i("wan", "数据解析异常");
            }
        }
    };
    private Emitter.Listener CONCERNListener = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject data = (JSONObject) args[0];
            Log.i("wan", "CONCERNListener------" + data.toString());
            SocketMode socketMode = new HttpAnalyze().getParseSocket(data.toString());
            if (socketMode != null) {
                Message message = new Message();
                message.what = Config.SOCKETCONCERN;
                message.obj = socketMode;
                connetHandler.sendMessage(message);
            } else {
                Log.i("wan", "数据解析异常");
            }
        }
    };
    private Emitter.Listener UN_CONCERNListener = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject data = (JSONObject) args[0];
            Log.i("wan", "UN_CONCERNListener------" + data.toString());
            SocketMode socketMode = new HttpAnalyze().getParseSocket(data.toString());
            if (socketMode != null) {
                Message message = new Message();
                message.what = Config.SOCKETUNCONCERN;
                message.obj = socketMode;
                connetHandler.sendMessage(message);
            } else {
                Log.i("wan", "数据解析异常");
            }
        }
    };


    private Emitter.Listener STOPPEDListener = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject data = (JSONObject) args[0];
            Log.i("wan", "STOPPEDListener------" + data.toString());
            SocketMode socketMode = new HttpAnalyze().getParseSocket(data.toString());
            if (socketMode != null) {
                Message message = new Message();
                message.what = Config.SOCKETUNCONCERN;
                message.obj = socketMode;
                connetHandler.sendMessage(message);
            } else {
                Log.i("wan", "数据解析异常");
            }
        }
    };

    private Emitter.Listener CHATMSGListener = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject data = (JSONObject) args[0];
            Log.i("wan", "CHATMSGListener------" + data.toString());
            SocketMode socketMode = new HttpAnalyze().getParseSocket(data.toString());
            if (socketMode != null) {
                Message message = new Message();
                message.what = Config.SOCKETCHATMSG;
                message.obj = socketMode;
                connetHandler.sendMessage(message);
            } else {
                Log.i("wan", "数据解析异常");
            }
        }
    };

    private Emitter.Listener emitterListener = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject data = (JSONObject) args[0];
            Log.i("wan", "emitterListener------" + data.toString());
            SocketMode socketMode = new HttpAnalyze().getParseSocket(data.toString());
            if (socketMode != null) {
                Message message = new Message();
                message.what = Config.SOCKETCREATEROOM;
                message.obj = socketMode;
                connetHandler.sendMessage(message);
            } else {
                Log.i("wan", "数据解析异常");
            }
        }
    };

    Handler connetHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            SocketMode socketMode;
            int actionNumber;
            switch (msg.what) {
                case Config.SOCKETCREATEROOM:
                    socketMode = (SocketMode) msg.obj;
                    actionNumber = Integer.valueOf(guanNumber.getText().toString().trim());
                    guanNumber.setText((actionNumber + 1) + "");
                    liveDetil liveDetil = new liveDetil();
                    liveDetil.url = socketMode.getHeadUrl();
                    liveDetil.id = socketMode.getUserId();
                    list.add(liveDetil);
                    socketMode.setEvenType(Config.SOCKETCREATEROOM);
                    recyclerAdapter.notifyDataSetChanged();

                    RefurbishListView(socketMode);
                    break;
                case Config.SOCKETCHATGIFT:
                    socketMode = (SocketMode) msg.obj;
                    socketMode.setEvenType(Config.SOCKETCHATGIFT);
                    RefurbishListView(socketMode);
                    break;
                case Config.SOCKETCHATMSG:
                    socketMode = (SocketMode) msg.obj;
                    socketMode.setEvenType(Config.SOCKETCHATMSG);
                    RefurbishListView(socketMode);
                    break;
                case Config.SOCKETCONCERN:
                    socketMode = (SocketMode) msg.obj;
                    actionNumber = Integer.valueOf(attentionNumber.getText().toString().trim());
                    attentionNumber.setText((actionNumber + 1) + "");
                    socketMode.setEvenType(Config.SOCKETCONCERN);
                    RefurbishListView(socketMode);
                    break;
                case Config.SOCKETCONNECT:
                    socketMode = (SocketMode) msg.obj;
                    socketMode.setEvenType(Config.SOCKETCONNECT);
                    RefurbishListView(socketMode);
                    break;
                case Config.SOCKETUNCONCERN:
                    socketMode = (SocketMode) msg.obj;
                    actionNumber = Integer.valueOf(attentionNumber.getText().toString().trim());
                    attentionNumber.setText((actionNumber - 1) + "");
                    socketMode.setEvenType(Config.SOCKETUNCONCERN);
                    RefurbishListView(socketMode);
                    break;
                case Config.SOCKETUNCONNECT:
                    socketMode = (SocketMode) msg.obj;
                    actionNumber = Integer.valueOf(guanNumber.getText().toString().trim());
                    guanNumber.setText((actionNumber - 1) + "");
                    socketMode.setEvenType(Config.SOCKETUNCONNECT);
                    RefurbishListView(socketMode);
                    break;
            }
        }
    };

    /**
     * 刷新消息列表
     *
     * @param socketMode
     */
    public void RefurbishListView(SocketMode socketMode) {
        if (socketMode != null) {
            ChatMesgInfo chatMesgInfo = new ChatMesgInfo();
            String msgContent = socketMode.getUserName().equals(" ") ? socketMode.getMessage() : socketMode.getUserName() + ": " + socketMode.getMessage();
            chatMesgInfo.setContent(msgContent);
            chatMesgInfo.setEvenType(socketMode.getEvenType());
            chatMeslist.add(chatMesgInfo);
            adatper.setData(chatMeslist);
            mListView.setSelection(mListView.getBottom());
        }
    }

    @Override
    public void onConnect() {
        Log.i("wan", "链接超时，正在重新链接");
        mHandler.sendEmptyMessage(Config.SOCKETCONNECT);
    }
}
