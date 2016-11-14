package com.cadyd.app.widget;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
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
import android.widget.TextView;
import android.widget.Toast;

import com.cadyd.app.JConstant;
import com.cadyd.app.MyApplication;
import com.cadyd.app.R;
import com.cadyd.app.adapter.ChatListAdatper;
import com.cadyd.app.adapter.GGAdapter;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.asynctask.retrofitmode.BaseRequestInfo;
import com.cadyd.app.asynctask.retrofitmode.HttpMethods;
import com.cadyd.app.asynctask.retrofitmode.ProgressSubscriber;
import com.cadyd.app.asynctask.retrofitmode.SubscriberOnNextListener;
import com.cadyd.app.comm.KeyBoardUtils;
import com.cadyd.app.model.ChatMesgInfo;
import com.cadyd.app.model.LiveModel;
import com.cadyd.app.model.LivePersonalDetailIfon;
import com.cadyd.app.model.PeopleSayInfo;
import com.cadyd.app.model.SendDataMsgInfo;
import com.cadyd.app.model.SendGiftInfo;
import com.cadyd.app.model.SignInModel;
import com.cadyd.app.model.SocketMode;
import com.cadyd.app.model.liveDetil;
import com.cadyd.app.service.VideoService;
import com.cadyd.app.ui.activity.PersonalCenterActivity;
import com.cadyd.app.ui.activity.SendGiftActivtiy;
import com.cadyd.app.ui.fragment.live.LiveDetailActivity;
import com.cadyd.app.ui.fragment.live.ProductsFeaturedActivity;
import com.cadyd.app.ui.push.gles.Config;
import com.cadyd.app.ui.view.AlertConfirmation;
import com.cadyd.app.ui.view.CircleImageView;
import com.cadyd.app.ui.view.DividerGridItemDecoration;
import com.cadyd.app.ui.window.KidsLiveWindow;
import com.cadyd.app.ui.window.SharePopupWindow;
import com.cadyd.app.utils.HttpAnalyze;
import com.cadyd.app.utils.SocketSingleton;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;
import com.pili.pldroid.player.AVOptions;
import com.pili.pldroid.player.PLMediaPlayer;
import com.pili.pldroid.player.widget.PLVideoView;
import com.ypy.eventbus.EventBus;

import org.json.JSONObject;
import org.wcy.android.utils.adapter.CommonRecyclerAdapter;
import org.wcy.android.utils.adapter.ViewRecyclerHolder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.RequestBody;

import static com.cadyd.app.R.id.live_radio_Rproducts;
import static com.cadyd.app.ui.push.gles.Config.MESSAGE_ID_RECONNECTING;

/**
 * This is a demo activity of PLVideoView
 */
public class PLVideoViewActivity extends FragmentActivity implements View.OnClickListener {

    private LivePersonalDetailIfon info;//房间信息模型
    private String shopId;
    private static final String TAG = PLVideoViewActivity.class.getSimpleName();
    @Bind(R.id.media_back)
    ImageView backImage;
    private MediaController mMediaController;
    @Bind(R.id.SurfaceView)
    SurfaceView mSurfaceView;
    private Toast mToast = null;
    private String mVideoPath = null;
    @Bind(R.id.voido_persont)
    RelativeLayout personCenter;
    private int mDisplayAspectRatio = PLVideoView.ASPECT_RATIO_FIT_PARENT;
    private boolean mIsActivityPaused = true;
    @Bind(R.id.video_number)
    TextView mVideo_number;

    //    private LinearLayout mVideo_personCenter;
    @Bind(R.id.video_headImage)
    CircleImageView mVideo_headImage;
    @Bind(R.id.video_name)
    TextView mVideo_name;
    @Bind(R.id.chat_list)
    ListView mChat_list;
    @Bind(R.id.input_text)
    LinearLayout mInput_text;
    @Bind(R.id.video_attentionNumber)
    TextView mAttentionNumber;
    //头像
    CircleImageView headImage;
    //主播名
    TextView userName;
    ListView chatList;
    @Bind(R.id.void_content)
    EditText contentMsg;
    //弹幕信息
    private ArrayList<ChatMesgInfo> chatMeslist = new ArrayList<>();
    private ChatListAdatper adatper;
    private MyApplication application;
    private SignInModel signInModel;
    @Bind(R.id.voide_send)
    TextView sendMsg;
    private LiveModel model;
    @Bind(R.id.void_rootView)
    RelativeLayout VideoRootView;
    @Bind(R.id.voideo_share)
    TextView share;
    @Bind(R.id.rootView)
    RelativeLayout rootView;
    private List<liveDetil> list = new ArrayList<>();
    private GGAdapter ggAdapter;
    private CommonRecyclerAdapter<LivePersonalDetailIfon.OnlineListBean> recyclerAdapter;
    @Bind(R.id.live_radio_group)
    RadioGroup mGroup;
    private RadioButton mRadioMsg, mRadioShare, mRadioMerchandise, mRadioGift, mRadioProducts;
    @Bind(R.id.msg_back)
    ImageView msgBack;
    private String conversationId;
    private int liveType;
    @Bind(R.id.lookNumber)
    TextView lookNumber;
    @Bind(R.id.Plvideo_roomNumber)
    TextView roomNumber;
    @Bind(R.id.gg_list)
    RecyclerView gg_listView;
    @Bind(R.id.live_leave)
    Button leave;
    @Bind(R.id.live_empty)
    View liveEmpty;
    private AVOptions mAVOptions;
    private PLMediaPlayer mMediaPlayer;
    View loadingView;
    @Bind(R.id.live_empty_text)
    TextView liveEmptyTx;

    private SharePopupWindow sharePopupWindow;
    private KidsLiveWindow window;
    private int attentionState = 1;
    private boolean mIsStopped = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onCreate(savedInstanceState);
        mFragmentManager = getSupportFragmentManager();
        setContentView(R.layout.activity_pl_video_view);
        EventBus.getDefault().register(PLVideoViewActivity.this);

        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        mFragmentManager = getSupportFragmentManager();
        mGroup = (RadioGroup) findViewById(R.id.live_radio_group);
        mRadioGift = (RadioButton) findViewById(R.id.live_radio_gift);
        mRadioShare = (RadioButton) findViewById(R.id.live_radio_share);
        mRadioMsg = (RadioButton) findViewById(R.id.live_radio_msg);
        mRadioProducts = (RadioButton) findViewById(live_radio_Rproducts);
        mRadioMerchandise = (RadioButton) findViewById(R.id.live_radio_merchandise);
        userName = (TextView) findViewById(R.id.video_name);
        chatList = (ListView) findViewById(R.id.chat_list);
        headImage = (CircleImageView) findViewById(R.id.video_headImage);

        application = (MyApplication) getApplication();
        signInModel = application.model;

        adatper = new ChatListAdatper(PLVideoViewActivity.this, chatMeslist);
        loadingView = findViewById(R.id.LoadingView);
        conversationId = getIntent().getStringExtra("conversationId");
        liveType = getIntent().getIntExtra("type", 1);
        backImage.setOnClickListener(this);
        personCenter.setOnClickListener(this);
        sendMsg.setOnClickListener(this);
        share.setOnClickListener(this);
        rootView.setOnClickListener(this);
        personCenter.setOnClickListener(this);
        leave.setOnClickListener(this);
        msgBack.setOnClickListener(this);
        mGroup.setOnCheckedChangeListener(mOnCheckedChangeListener);

        chatList.setAdapter(adatper);


        mAVOptions = new AVOptions();
        mAVOptions.setInteger(AVOptions.KEY_PREPARE_TIMEOUT, 10 * 1000);
        mAVOptions.setInteger(AVOptions.KEY_GET_AV_FRAME_TIMEOUT, 10 * 1000);
        mAVOptions.setInteger(AVOptions.KEY_LIVE_STREAMING, 0);
        mAVOptions.setInteger(AVOptions.KEY_MEDIACODEC, 0);
        mAVOptions.setInteger(AVOptions.KEY_START_ON_PREPARED, 0);

        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.requestAudioFocus(null, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);

        ViewGroup.LayoutParams params = gg_listView.getLayoutParams();
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        gg_listView.setLayoutManager(linearLayoutManager);
        gg_listView.addItemDecoration(new DividerGridItemDecoration(this, GridLayout.VERTICAL, 6, getResources().getColor(R.color.transparent)));

        recyclerAdapter = new CommonRecyclerAdapter<LivePersonalDetailIfon.OnlineListBean>(PLVideoViewActivity.this, onlineList, R.layout.pl_image_view) {
            @Override
            public void convert(ViewRecyclerHolder helper, final LivePersonalDetailIfon.OnlineListBean item) {
                Log.i("Wan", "headUrl----" + item.getHeadImageUrl());

                ImageView imageView = helper.getView(R.id.image_view);
                ApiClient.displayCirleImage(item.getHeadImageUrl(), imageView, R.mipmap.user_default);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(PLVideoViewActivity.this, PersonalCenterActivity.class);
                        intent.putExtra("userId", item.getUserId());
                        startActivity(intent);
                    }
                });
            }
        };

        gg_listView.setAdapter(recyclerAdapter);
        Log.i("wan", "直播类型" + liveType);
        if (liveType == 1) {
            getPersonalCenterDetail(conversationId);
            mRadioProducts.setVisibility(View.GONE);
//            mRadioMerchandise.setVisibility(View.VISIBLE);
        } else {
            getBusinessCenterDetail(conversationId);
            mRadioProducts.setVisibility(View.VISIBLE);
//            mRadioMerchandise.setVisibility(View.GONE);
        }
    }

    private int mSurfaceWidth = 0;
    private int mSurfaceHeight = 0;

    private SurfaceHolder.Callback mCallback = new SurfaceHolder.Callback() {

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            prepare();
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            mSurfaceWidth = width;
            mSurfaceHeight = height;
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
        }
    };


    private void prepare() {

        if (mMediaPlayer != null) {
            mMediaPlayer.setDisplay(mSurfaceView.getHolder());
            return;
        }
        try {
            mMediaPlayer = new PLMediaPlayer(mAVOptions);
            mMediaPlayer.setOnPreparedListener(mOnPreparedListener);
            mMediaPlayer.setOnVideoSizeChangedListener(mOnVideoSizeChangedListener);
            mMediaPlayer.setOnCompletionListener(mOnCompletionListener);
            mMediaPlayer.setOnErrorListener(mOnErrorListener);
            mMediaPlayer.setOnInfoListener(mOnInfoListener);
            mMediaPlayer.setOnBufferingUpdateListener(mOnBufferingUpdateListener);
            mMediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);

            if (info != null) {
                mMediaPlayer.setDataSource(info.getQiniuParameter().getRtmpUrl());
                Log.i("wan", "url-------" + info.getQiniuParameter().getRtmpUrl());
            }
            mMediaPlayer.setDisplay(mSurfaceView.getHolder());
            mMediaPlayer.prepareAsync();

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //在线观看人数
    List<LivePersonalDetailIfon.OnlineListBean> onlineList = new ArrayList<>();

    /**
     * 获取个人直播详情
     *
     * @param ConversationID
     */
    public void getPersonalCenterDetail(@NonNull String ConversationID) {
        Map<Object, Object> map = new HashMap<>();
        map.put("conversationId", ConversationID);
        RequestBody key = ApiClient.getRequestBody(map);
        HttpMethods.getInstance().getLivePersonalDetailInfo(new ProgressSubscriber<BaseRequestInfo<LivePersonalDetailIfon>>(new SubscriberOnNextListener<BaseRequestInfo<LivePersonalDetailIfon>>() {
            @Override
            public void onNext(BaseRequestInfo<LivePersonalDetailIfon> o) {
                info = o.getData();
                if (info != null) {
                    onlineList.clear();
                    onlineList.addAll(info.getOnlineList());
                    ApiClient.displayImage(info.getHeadImageUrl(), mVideo_headImage);
                    Log.i("Wan", "tcp----address" + info.getChatParameter().getServerId());
                    //初始链接
                    initConnect();
                    info.setConversationId(conversationId);
                    mAttentionNumber.setText(info.getFoucsCount() + "");
                    lookNumber.setText(info.getOnlineCount() + "");
                    roomNumber.setText(info.getRoomCode() + "");
                    mVideoPath = info.getQiniuParameter().getRtmpUrl();

                    Log.i("wan", "七牛播放地址" + Config.EXTRA_PUBLISH_URL_PREFIX + info.getQiniuParameter().getRtmpUrl());
                    mHandler.sendEmptyMessage(1000);

                    //（1=已关注，2=未关注，3=不支持）
                    switch (info.getFocusStatus()) {

                        case 1:
                            share.setText("已关注");
                            break;

                        case 2:
                            share.setText("未关注");
                            break;

                        case 3:
                            share.setText("不支持");
                            break;
                    }

                    Log.i("wan", "在线观看人数" + onlineList.size());
                    gg_listView.setVisibility(onlineList.size() > 0 ? View.VISIBLE : View.GONE);
                    recyclerAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onLogicError(String msg) {

            }
        }, PLVideoViewActivity.this), key);
    }

    private FragmentManager mFragmentManager;
    private LivePersonalDetailIfon mBusinessLiveDetailInfo;
    private SocketSingleton socketSingleton;
    private Socket mSocket;

    /**
     * 直播商家详情页
     *
     * @param ConversationID
     */
    public void getBusinessCenterDetail(String ConversationID) {
        Map<Object, Object> map = new HashMap<>();
        map.put("conversationId", ConversationID);
        RequestBody key = ApiClient.getRequestBody(map);
        HttpMethods.getInstance().getLiveBusinessDetailsInfo(new ProgressSubscriber<BaseRequestInfo<LivePersonalDetailIfon>>(new SubscriberOnNextListener<BaseRequestInfo<LivePersonalDetailIfon>>() {
            @Override
            public void onNext(BaseRequestInfo<LivePersonalDetailIfon> o) {
                info = o.getData();
                Log.i("wan", "直播个人中心");
                if (info != null) {
                    info = o.getData();
                    //初始链接
                    initConnect();
                    info.setConversationId(conversationId);
                    Log.i("Wan", "tcp----address" + info.getChatParameter().getServerId());
                    ApiClient.displayImage(info.getHeadImageUrl(), mVideo_headImage);
                    mAttentionNumber.setText(info.getFoucsCount() + "");
                    lookNumber.setText(info.getOnlineCount() + "");
                    roomNumber.setText(info.getRoomCode() + "");
                    mVideoPath = info.getQiniuParameter().getRtmpUrl();
                    onlineList.clear();
                    onlineList.addAll(info.getOnlineList());
                    //（1=已关注，2=未关注，3=不支持）
                    switch (info.getFocusStatus()) {
                        case 1:
                            share.setText("已关注");
                            break;

                        case 2:
                            share.setText("未关注");
                            break;
                        case 3:
                            share.setText("不支持");
                            break;
                    }
                    Log.i("wan", "七牛播放地址" + Config.EXTRA_PUBLISH_URL_PREFIX + info.getQiniuParameter().getRtmpUrl());
                    mHandler.sendEmptyMessage(1000);
                    gg_listView.setVisibility(onlineList.size() > 0 ? View.VISIBLE : View.GONE);
                    recyclerAdapter.notifyDataSetChanged();
                    mBusinessLiveDetailInfo = info;
                    if (ApiClient.isLogin(PLVideoViewActivity.this))
                        startLiveDetail(info);
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onLogicError(String msg) {

            }
        }, PLVideoViewActivity.this), key);
    }


    public void startLiveDetail(LivePersonalDetailIfon info) {
        LiveDetailActivity liveEndActivity = null;
        if (liveEndActivity == null) {
            liveEndActivity = new LiveDetailActivity();
            Bundle bundle = new Bundle();
            bundle.putSerializable("info", info);
            liveEndActivity.setArguments(bundle);
            liveEndActivity.show(mFragmentManager, "dilaog");
        } else {
            liveEndActivity.show(mFragmentManager, "dilaog");
        }
    }


    RadioGroup.OnCheckedChangeListener mOnCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            mInput_text.setVisibility(View.GONE);
            mGroup.setVisibility(View.VISIBLE);

            switch (checkedId) {
                case live_radio_Rproducts:
                    if (!ApiClient.isLogin(PLVideoViewActivity.this)) return;
                    startLiveDetail(mBusinessLiveDetailInfo);
                    mRadioProducts.setChecked(false);
                    break;

                case R.id.live_radio_gift://打开礼物界面
                    if (!ApiClient.isLogin(PLVideoViewActivity.this)) return;
                    if (info != null) {
                        SendGiftActivtiy sendGiftActivtiy = new SendGiftActivtiy();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("info", info);
                        sendGiftActivtiy.setArguments(bundle);
                        sendGiftActivtiy.show(mFragmentManager, "sendGif");
                        mRadioGift.setChecked(false);
                    }
                    break;
                //商品推荐
                case R.id.live_radio_merchandise:
                    mRadioMerchandise.setChecked(false);
                    if (!ApiClient.isLogin(PLVideoViewActivity.this)) return;
                    ProductsFeaturedActivity productsFeaturedActivity = new ProductsFeaturedActivity();
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("conversationId", conversationId);
                    productsFeaturedActivity.setArguments(bundle1);
                    productsFeaturedActivity.show(mFragmentManager, "product");
                    break;

                case R.id.live_radio_msg:
                    mRadioMsg.setChecked(false);
                    mInput_text.setVisibility(View.VISIBLE);
                    mGroup.setVisibility(View.GONE);
                    break;

                case R.id.live_radio_share:
                    mRadioShare.setChecked(false);
                    if (!ApiClient.isLogin(PLVideoViewActivity.this)) return;
                    //直播分享
                    if (info != null) {
                        sharePopupWindow = new SharePopupWindow(PLVideoViewActivity.this, "76eb35ff-9eba-4863-8a4b-36ff75cfef08", info.getRoomCode(), liveType);
                        sharePopupWindow.showPop(group);
                    }
                    break;
            }
        }
    };


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.live_leave:
                finish();
                break;
            //主播的个人中心
            case R.id.voido_persont:
                if (!ApiClient.isLogin(PLVideoViewActivity.this)) return;

                Intent intent = new Intent(PLVideoViewActivity.this, PersonalCenterActivity.class);
                startActivity(intent);
                break;

            case R.id.msg_back:
                mInput_text.setVisibility(View.GONE);
                mGroup.setVisibility(View.VISIBLE);
                KeyBoardUtils.closeKeybord(contentMsg, PLVideoViewActivity.this);
                break;
            case R.id.rootView:
                if (window != null) {
                    window.dismiss();
                }
                //关闭软键盘
                KeyBoardUtils.closeKeybord(contentMsg, PLVideoViewActivity.this);
                break;
            case R.id.voideo_share:
                if (!ApiClient.isLogin(PLVideoViewActivity.this)) return;

                if (info != null) {
                    //（1=已关注，2=未关注，3=不支持）
                    switch (info.getFocusStatus()) {
                        case 1:
                            cancelFoucs(info.getUserId());
                            break;
                        case 2:
                            userAddAttention(info.getUserId());
                            break;
                        case 3:
                            break;
                    }
                }
                break;
            //返回
            case R.id.media_back:
                if (info != null) {
                    if (mSocket.connected()) {
                        SignInModel model = MyApplication.getInstance().getSignInModel();
                        if (model.name == null) {
                            JSONObject jsonObject = socketSingleton.getUserSocketJson(info.getChatParameter().getChannelId(), " ", "离开房间");
                            mSocket.emit("UN_CONNECT", jsonObject);
                        } else {
                            JSONObject jsonObject = socketSingleton.getUserSocketJson(info.getChatParameter().getChannelId(), model.name, "离开房间");
                            mSocket.emit("UN_CONNECT", jsonObject);
                        }
                    } else {
                        mHandler.sendEmptyMessage(Config.SOCKETRESTART);
                    }

                }
                finish();
                break;
            //发送消息
            case R.id.voide_send:
                if (!ApiClient.isLogin(PLVideoViewActivity.this)) return;
                sendMsg();
                break;
        }

    }

    private SubscriberOnNextListener mOnNextListener;

    /**
     * 关注用户
     *
     * @param userId
     */
    public void userAddAttention(String userId) {

        mOnNextListener = new SubscriberOnNextListener<BaseRequestInfo>() {
            @Override
            public void onNext(BaseRequestInfo baseRequestInfo) {
                Log.i("wan", "关注用户成功");
                share.setText("已关注");
                if (info != null && conversationId != null) {
                    info.setFocusStatus(1);
                    if (mSocket.connected()) {
                        SignInModel model = MyApplication.getInstance().model;
                        JSONObject jsonObject = socketSingleton.getUserSocketJson(info.getChatParameter().getChannelId(), model.name, "添加了关注");
                        mSocket.emit("CONCERN", jsonObject);
                    } else {
                        mHandler.sendEmptyMessage(Config.SOCKETRESTART);
                    }
                }
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onLogicError(String msg) {

            }
        };

        Map<Object, Object> map = new HashMap<>();
        map.put("userId", userId);
        RequestBody body = ApiClient.getRequestBody(map);
        HttpMethods.getInstance().getUserAttention(new ProgressSubscriber<BaseRequestInfo>(mOnNextListener, PLVideoViewActivity.this), body);
    }

    /**
     * 取消关注
     */
    private void cancelFoucs(String userId) {
        Map<Object, Object> map = new HashMap<>();
        map.put("userId", userId);
        RequestBody body = ApiClient.getRequestBody(map);
        HttpMethods.getInstance().doCancelFoucs(new ProgressSubscriber<BaseRequestInfo>(new SubscriberOnNextListener<BaseRequestInfo>() {
            @Override
            public void onNext(BaseRequestInfo o) {
                Log.i("wan", "取消关注用户成功");
                share.setText("关注");
                if (info != null && conversationId != null) {
                    info.setFocusStatus(2);
                    if (mSocket.connected()) {
                        SignInModel model = MyApplication.getInstance().model;
                        JSONObject jsonObject = socketSingleton.getUserSocketJson(info.getChatParameter().getChannelId(), model.name, "取消了关注");
                        mSocket.emit("UN_CONCERN", jsonObject);
                    } else {
                        mHandler.sendEmptyMessage(Config.SOCKETRESTART);
                    }
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onLogicError(String msg) {

            }
        }, PLVideoViewActivity.this), body);
    }

    /**
     * 发送消息
     */
    public void sendMsg() {
        if (contentMsg.getText().toString().trim().equals("")) {
            Toast.makeText(PLVideoViewActivity.this, "发送内容不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (info != null) {
            if (mSocket.connected()) {
                SignInModel model = MyApplication.getInstance().getSignInModel();
                JSONObject jsonObject = socketSingleton.getUserSocketJson(info.getChatParameter().getChannelId(), model.name, contentMsg.getText().toString().trim());
                mSocket.emit("CHATMSG", jsonObject);
            } else {
                mHandler.sendEmptyMessage(Config.SOCKETRESTART);
            }

        }
        contentMsg.setText("");
    }

    @Override
    protected void onResume() {
        super.onResume();
        mIsActivityPaused = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        mToast = null;
        mIsActivityPaused = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        release();
        if (socketSingleton != null && mSocket != null)
            socketSingleton.disConnect();
        mHandler.removeMessages(Config.SOCKETRESTART);
        ButterKnife.unbind(this);
        ApiClient.cancelRequest(JConstant.QUERYSHOPIDTOLISTGOODS);
        ApiClient.cancelRequest(JConstant.COLLECT_);
        ApiClient.cancelRequest(JConstant.CANCEL_COLLECT_);
        ApiClient.cancelRequest(JConstant.LIVEDETIL);
        ApiClient.cancelRequest(JConstant.LIVEWINDOW);
    }

    private PLMediaPlayer.OnInfoListener mOnInfoListener = new PLMediaPlayer.OnInfoListener() {
        @Override
        public boolean onInfo(PLMediaPlayer plMediaPlayer, int what, int extra) {
            Log.i(TAG, "OnInfo, what = " + what + ", extra = " + extra);
            switch (what) {
                case PLMediaPlayer.MEDIA_INFO_BUFFERING_START:
                    loadingView.setVisibility(View.VISIBLE);
                    break;
                case PLMediaPlayer.MEDIA_INFO_BUFFERING_END:
                case PLMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
                    loadingView.setVisibility(View.GONE);
                    break;
                default:
                    break;
            }
            return true;
        }
    };

    private PLMediaPlayer.OnPreparedListener mOnPreparedListener = new PLMediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(PLMediaPlayer mp) {
            Log.i(TAG, "On Prepared !");
            mMediaPlayer.start();
            mIsStopped = false;
        }
    };

    private PLMediaPlayer.OnErrorListener mOnErrorListener = new PLMediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(PLMediaPlayer plMediaPlayer, int errorCode) {
            Log.e(TAG, "Error happened, errorCode = " + errorCode);
            boolean isNeedReconnect = false;
            switch (errorCode) {
                case PLMediaPlayer.ERROR_CODE_INVALID_URI:
                    Log.i("wan", "Invalid URL !");
                    break;
                case PLMediaPlayer.ERROR_CODE_404_NOT_FOUND:
                    Log.i("wan", "404 resource not found !");
                    break;
                case PLMediaPlayer.ERROR_CODE_CONNECTION_REFUSED:
                    Log.i("wan", "Connection refused !");
                    break;
                case PLMediaPlayer.ERROR_CODE_CONNECTION_TIMEOUT:
                    Log.i("wan", "Connection timeout !");
                    isNeedReconnect = true;
                    break;
                case PLMediaPlayer.ERROR_CODE_EMPTY_PLAYLIST:
                    Log.i("wan", "Empty playlist !");
                    break;
                case PLMediaPlayer.ERROR_CODE_STREAM_DISCONNECTED:
                    Log.i("wan", "Stream disconnected !");
                    isNeedReconnect = true;
                    break;
                case PLMediaPlayer.ERROR_CODE_IO_ERROR:
                    Log.i("wan", "Network IO Error !");
                    isNeedReconnect = true;
                    break;
                case PLMediaPlayer.ERROR_CODE_UNAUTHORIZED:
                    Log.i("wan", "Unauthorized Error !");
                    break;
                case PLMediaPlayer.ERROR_CODE_PREPARE_TIMEOUT:
                    Log.i("wan", "Prepare timeout !");
                    isNeedReconnect = true;
                    break;
                case PLMediaPlayer.ERROR_CODE_READ_FRAME_TIMEOUT:
                    Log.i("wan", "Read frame timeout !");
                    isNeedReconnect = true;
                    break;
                case PLMediaPlayer.MEDIA_ERROR_UNKNOWN:
                    break;
                default:
                    Log.i("wan", "unknown error !");
                    break;
            }

            release();
            if (isNeedReconnect) {
                sendReconnectMessage();
            }
            return true;
        }
    };


    private void sendReconnectMessage() {
        Log.i("wan", "正在重连...");
        loadingView.setVisibility(View.VISIBLE);
        mHandler.sendEmptyMessage(MESSAGE_ID_RECONNECTING);
    }

    private PLMediaPlayer.OnCompletionListener mOnCompletionListener = new PLMediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(PLMediaPlayer plMediaPlayer) {
            Log.d(TAG, "Play Completed !");
            showToastTips("Play Completed !");
            finish();
        }
    };

    private PLMediaPlayer.OnBufferingUpdateListener mOnBufferingUpdateListener = new PLMediaPlayer.OnBufferingUpdateListener() {
        @Override
        public void onBufferingUpdate(PLMediaPlayer plMediaPlayer, int precent) {
            Log.d(TAG, "onBufferingUpdate:" + precent);
        }
    };


    private PLMediaPlayer.OnVideoSizeChangedListener mOnVideoSizeChangedListener = new PLMediaPlayer.OnVideoSizeChangedListener() {
        @Override
        public void onVideoSizeChanged(PLMediaPlayer plMediaPlayer, int width, int height) {
            Log.d(TAG, "onVideoSizeChanged: " + width + "," + height);
        }
    };

    private void showToastTips(final String tips) {
        if (mIsActivityPaused) {
            return;
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mToast != null) {
                    mToast.cancel();
                }
                mToast = Toast.makeText(PLVideoViewActivity.this, tips, Toast.LENGTH_SHORT);
                mToast.show();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //监听用户按返回建，悬浮框观看视频
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            showWindow();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    AlertConfirmation confirmation;

    /**
     * 确认是否继续直播
     */
    public void showWindow() {
        confirmation = new AlertConfirmation(PLVideoViewActivity.this, "温馨提醒", "是否继续观看直播");
        confirmation.show(new AlertConfirmation.OnClickListeners() {
            @Override
            public void ConfirmOnClickListener() {
                confirmation.hide();
                try {
                    Config.isLive = true;
                    Log.i("wan", "---------mVideoPath---------" + mVideoPath);
                    if (mVideoPath != null) {
                        Intent intent = new Intent(PLVideoViewActivity.this, VideoService.class);
                        intent.putExtra("videoPath", mVideoPath);
                        intent.putExtra("conversationId", conversationId);
                        intent.putExtra("type", liveType);
                        startService(intent);
                        finish();
                    }
                } catch (Exception e) {
                    Log.i("wan", e.toString());
                }
            }

            @Override
            public void CancelOnClickListener() {
                confirmation.hide();
                finish();
            }
        });
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
                mHandler.sendMessage(message);
                EventBus.getDefault().post(socketMode);
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
                mHandler.sendMessage(message);
                EventBus.getDefault().post(socketMode);
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
                mHandler.sendMessage(message);
                EventBus.getDefault().post(socketMode);
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
                mHandler.sendMessage(message);
                EventBus.getDefault().post(socketMode);
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
                mHandler.sendMessage(message);
                EventBus.getDefault().post(socketMode);
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
                message.what = Config.SOCKETCONNECT;
                message.obj = socketMode;
                mHandler.sendMessage(message);
                EventBus.getDefault().post(socketMode);
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
                message.what = Config.SOCKETSTOPPED;
                message.obj = socketMode;
                mHandler.sendMessage(message);
            } else {
                Log.i("wan", "数据解析异常");
            }
        }
    };

    /**
     * 建立tcp链接
     */
    private void initConnect() {
        if (info != null) {
            socketSingleton = SocketSingleton.getInstance(info.getChatParameter().getServerId());
            mSocket = socketSingleton.getSocket();
            mSocket.on("CREATE_ROOM", emitterListener);
            mSocket.on("CHATGIFT", CHATGIFTListener);
            mSocket.on("CHATMSG", CHATMSGListener);
            mSocket.on("UN_CONNECT", UN_CONNECTListener);
            mSocket.on("CONCERN", CONCERNListener);
            mSocket.on("UN_CONCERN", UN_CONCERNListener);
            mSocket.on("STOPPED", STOPPEDListener);
            mHandler.sendEmptyMessage(Config.SOCKETRESTART);

        }
    }

    public int initTcpNumber = 0;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            SocketMode socketMode;
            switch (msg.what) {
                case MESSAGE_ID_RECONNECTING:
                    prepare();
                    break;
                case 1000:
                    mSurfaceView.getHolder().addCallback(mCallback);
                    prepare();
                    break;
                case Config.SOCKETRESTART:
                    initTcpNumber++;
//                    if (initTcpNumber < 30)
                    initSocketContent();
                    break;

                case Config.SOCKETCONNECT:
                    socketMode = (SocketMode) msg.obj;
                    socketMode.setEvenType(Config.SOCKETCONNECT);
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
                    socketMode.setEvenType(Config.SOCKETCONCERN);
                    RefurbishListView(socketMode);
                    break;

                case Config.SOCKETUNCONCERN:
                    socketMode = (SocketMode) msg.obj;
                    socketMode.setEvenType(Config.SOCKETUNCONCERN);
                    RefurbishListView(socketMode);
                    break;

                case Config.SOCKETSTOPPED:
                    liveEmpty.setVisibility(View.VISIBLE);
                    liveEmptyTx.setText("主播已结束直播");
                    break;

                case Config.SOCKETUNCONNECT:
                    socketMode = (SocketMode) msg.obj;
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
            chatMesgInfo.setEvenType(socketMode.getEvenType());
            chatMesgInfo.setContent(msgContent);
            chatMeslist.add(chatMesgInfo);
            adatper.setData(chatMeslist);
            //让listView滑动到底部
            chatList.setSelection(chatList.getBottom());
        }
    }

    public void initSocketContent() {
        if (mSocket.connected()) {
            Log.i("wan", "链接成功。。。");
            SignInModel model = MyApplication.getInstance().getSignInModel();
            String msgContent = model.name == null ? "欢迎 游客 进入直播间" : "欢迎" + model.name + "进入直播间";
            JSONObject jsonObject = socketSingleton.getUserSocketJson(info.getChatParameter().getChannelId(), " ", msgContent);
            mSocket.emit("CREATE_ROOM", jsonObject);
        } else {
            mHandler.sendEmptyMessage(Config.SOCKETRESTART);
            Log.i("wan", "链接失败，正在重连");
        }
    }

    public void release() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    /**
     * 赠送礼物
     *
     * @param event
     */
    public void onEventMainThread(SendGiftInfo event) {
        Log.i("wan", "赠送礼物");
        if (mSocket.connected()) {
            SignInModel model = MyApplication.getInstance().getSignInModel();
            JSONObject jsonObject = socketSingleton.getUserSocketJson(info.getChatParameter().getChannelId(), model.name, " 给主播赠送了礼物");
            mSocket.emit("CHATGIFT", jsonObject);
        } else {
            mHandler.sendEmptyMessage(Config.SOCKETRESTART);
        }
    }

    /**
     * 大家说发送消息
     *
     * @param peopleSayInfo
     */
    public void onEventMainThread(PeopleSayInfo peopleSayInfo) {
        SignInModel model = MyApplication.getInstance().getSignInModel();
        if (info != null) {
            JSONObject jsonObject = socketSingleton.getUserSocketJson(info.getChatParameter().getChannelId(), model.name, peopleSayInfo.getMsg());
            mSocket.emit("CHATMSG", jsonObject);
        }
    }

    /**
     * 开启悬浮框
     */
    public void onEventMainThread(SendDataMsgInfo sendDataMsgInfo) {
        try {
            Config.isLive = true;
            Log.i("wan", "---------开启悬浮框---------" + mVideoPath);
            if (mVideoPath != null) {
                Intent intent = new Intent(PLVideoViewActivity.this, VideoService.class);
                intent.putExtra("videoPath", mVideoPath);
                intent.putExtra("conversationId", conversationId);
                intent.putExtra("type", liveType);
                startService(intent);
                finish();
            }
        } catch (Exception e) {
            Log.i("wan", e.toString());
        }
    }
}
