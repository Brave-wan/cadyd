package com.cadyd.app.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cadyd.app.JConstant;
import com.cadyd.app.MyApplication;
import com.cadyd.app.R;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.asynctask.retrofitmode.BaseRequestInfo;
import com.cadyd.app.asynctask.retrofitmode.HttpMethods;
import com.cadyd.app.asynctask.retrofitmode.ProgressSubscriber;
import com.cadyd.app.asynctask.retrofitmode.SubscriberOnNextListener;
import com.cadyd.app.interfaces.TowObjectParameterInterface;
import com.cadyd.app.model.UserCenterInfo;
import com.cadyd.app.model.UserCentervideoInfo;
import com.cadyd.app.ui.base.BaseActivity;
import com.cadyd.app.ui.view.CircleImageView;
import com.cadyd.app.ui.window.LiveMenuPopupWindow;
import com.cadyd.app.ui.window.SharePopupWindow;
import com.cadyd.app.utils.EmptyRecyclerView;
import com.cadyd.app.widget.PLMediaPlayerActivity;

import org.wcy.android.utils.adapter.CommonRecyclerAdapter;
import org.wcy.android.utils.adapter.ViewRecyclerHolder;
import org.wcy.common.utils.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.RequestBody;

/**
 * xiongmao
 * 直播個人中心
 */
public class PersonalCenterActivity extends BaseActivity {

    @Bind(R.id.video_number)
    TextView videoNumber;
    @Bind(R.id.back)
    TextView back;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.center_mall)
    ImageView centerMall;
    @Bind(R.id.title_layout)
    RelativeLayout titleLayout;
    @Bind(R.id.user_image)
    ImageView userImage;
    @Bind(R.id.user_name)
    TextView userName;
    @Bind(R.id.user_lv)
    TextView userLv;
    @Bind(R.id.LinearLayout)
    android.widget.LinearLayout LinearLayout;
    @Bind(R.id.user_content)
    TextView userContent;
    @Bind(R.id.user_attention)
    TextView userAttention;
    @Bind(R.id.user_fans)
    TextView userFans;
    @Bind(R.id.number_linearLayout)
    android.widget.LinearLayout numberLinearLayout;
    @Bind(R.id.add_attention)
    TextView addAttention;
    @Bind(R.id.empty_recycler)
    EmptyRecyclerView emptyRecycler;
    @Bind(R.id.activity_personal_center)
    RelativeLayout activityPersonalCenter;
    @Bind(R.id.visitors_recycler)
    RecyclerView visitors_recycler;
    @Bind(R.id.RecentVisitorsRelative)
    RelativeLayout RecentVisitorsRelative;
    @Bind(R.id.editor_image)
    ImageView editorImage;

    private CommonRecyclerAdapter<UserCentervideoInfo.DataBean> adapter;
    private CommonRecyclerAdapter<UserCenterInfo.Visitors> mVisitorsAdapter;

    private LiveMenuPopupWindow menuPopupWindow;
    //最近访客
    private List<UserCenterInfo.Visitors> astVisitors = new ArrayList<>();

    private UserCenterInfo info;//用户基础信息

    // cd0133187bd144a0a741fa26dc1c38ea (18580402350)
    //(18315058038)
    String userid = "cd0133187bd144a0a741fa26dc1c38ea";
    private List<String> list = new ArrayList<>(); //更多选项数组

    private SharePopupWindow sharePopupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_center);
        ButterKnife.bind(this);
        emptyRecycler.setLayoutManager(new GridLayoutManager(this, 2));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        visitors_recycler.setLayoutManager(layoutManager);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            userid = bundle.getString("userId", "");
        }
        initView();
    }

    private void initView() {
        MyApplication application = (MyApplication) getApplication();
        if (userid.equals(application.model.id)) {//如果相等则为自己的个人中心
            editorImage.setVisibility(View.VISIBLE);
            addAttention.setVisibility(View.GONE);
            list.add("分享");
        } else {
            list.add("分享");
            list.add("举报");
        }

        adapter = new CommonRecyclerAdapter<UserCentervideoInfo.DataBean>(this, videoList, R.layout.item_live) {
            @Override
            public void convert(ViewRecyclerHolder helper, final UserCentervideoInfo.DataBean item) {
                ImageView imageView = helper.getView(R.id.item_live_new_cover);
                ApiClient.displayImage(item.getCoverUrl(), imageView, R.mipmap.defaiut_on);
                helper.setText(R.id.item_live_new_title, item.getTitle());
                ImageView tabl = helper.getView(R.id.item_live_new_type);
                if (item.getLiveStatus() == 2) {//2=回看 note_porfile_huifang
                    tabl.setBackgroundResource(R.mipmap.note_porfile_huifang);
                } else if (item.getLiveStatus() == 1) {//1=直播中
                    tabl.setBackgroundResource(R.mipmap.note_porfile_zhibo);
                }
                helper.getConvertView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(PersonalCenterActivity.this, PLMediaPlayerActivity.class);
                        intent.putExtra("mediaCodec", 0);//軟解
                        intent.putExtra("liveStreaming", 0);//點播
//                        intent.putExtra("videoPath", "rtmp://live.hkstv.hk.lxdns.com/live/hks");
                        intent.putExtra("videoPath", item.getSationUrl());
                        startActivity(intent);

                    }
                });
            }
        };
        emptyRecycler.setAdapter(adapter);
        /**
         * 设置最近访客
         */
        mVisitorsAdapter = new CommonRecyclerAdapter<UserCenterInfo.Visitors>(this, astVisitors, R.layout.item_personal_center_visitor) {
            @Override
            public void convert(ViewRecyclerHolder helper, UserCenterInfo.Visitors item) {
                CircleImageView imageView = helper.getView(R.id.circleImageView);
                ApiClient.displayImage(item.getHeadImageUrl(), imageView, R.mipmap.user_default);
            }
        };
        visitors_recycler.setAdapter(mVisitorsAdapter);
        /**
         * 跳转到最近访客列表页面
         */
        RecentVisitorsRelative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonalCenterActivity.this, RecentVisitorsActivity.class);
                intent.putExtra("userId", userid);
                startActivity(intent);
            }
        });
        getNetWork();
        getLiveAndVideo();
    }

    @OnClick(R.id.center_mall)
    public void onMall(final View view) {
        if (menuPopupWindow == null) {
            menuPopupWindow = new LiveMenuPopupWindow(activity, list, new TowObjectParameterInterface() {
                @Override
                public void Onchange(int type, int postion, Object object) {
                    if (postion == 0) {
                        if (sharePopupWindow == null) {
                            sharePopupWindow = new SharePopupWindow(PersonalCenterActivity.this, "510cb344-d125-47d3-a1b6-837f25d94c3b", userid, -1);
                        }
                        sharePopupWindow.showPop(view);
                        menuPopupWindow.dismissWindo();
                    } else if (postion == 1) {
                        Intent intent = new Intent(PersonalCenterActivity.this, ReportActivity.class);
                        if (info == null) info = new UserCenterInfo();
                        intent.putExtra("userId", userid);
                        startActivity(intent);
                        menuPopupWindow.dismissWindo();
                    }
                }
            });
            menuPopupWindow.showWindo(activityPersonalCenter);
        } else {
            menuPopupWindow.showWindo(activityPersonalCenter);
        }
    }

    /**
     * 关注
     */
    @OnClick(R.id.user_attention)
    public void userAttention(View view) {
        Intent intent = new Intent(PersonalCenterActivity.this, AttentionAndFansActivity.class);
        if (info == null)
            info = new UserCenterInfo();
        intent.putExtra("userId", userid);
        intent.putExtra("Type", AttentionAndFansActivity.Attention);
        startActivity(intent);
    }

    /**
     * 粉丝
     */
    @OnClick(R.id.user_fans)
    public void userFans(View v) {
        Intent intent = new Intent(PersonalCenterActivity.this, AttentionAndFansActivity.class);
        if (info == null)
            info = new UserCenterInfo();
        intent.putExtra("userId", userid);
        intent.putExtra("Type", AttentionAndFansActivity.Fans);
        startActivity(intent);
    }

    @OnClick(R.id.editor_image)
    public void onEditor(View view) {
        Intent intent = new Intent(activity, CommonActivity.class);
        intent.putExtra(JConstant.EXTRA_INDEX, CommonActivity.MYHEAD);
        startActivity(intent);
    }

    private SubscriberOnNextListener mOnNextListener;

    /**
     * 获取用户中心的数据
     */
    public void getNetWork() {
        mOnNextListener = new SubscriberOnNextListener<BaseRequestInfo<UserCenterInfo>>() {
            @Override
            public void onNext(BaseRequestInfo<UserCenterInfo> baseRequestInfo) {
                Log.i("wan", "获取用户中心的数据---" + baseRequestInfo.toString());
                info = baseRequestInfo.getData();
                if (info == null) return;
                ApiClient.displayImage(info.getUserBasicInfo().getHeadImageUrl(), userImage);//用户头像
                userName.setText(info.getUserBasicInfo().getNickName());//用户名
                MyImage(Float.valueOf(info.getUserBasicInfo().getLevel()));//设置会员等级
                if (StringUtil.isEmpty(info.getUserBasicInfo().getDes())) {
                    userContent.setVisibility(View.GONE);
                } else {
                    userContent.setText(info.getUserBasicInfo().getDes());//用户描述
                }
                userAttention.setText(info.getFocusCount() + "关注");
                userFans.setText(info.getFansCount() + "粉丝");
                videoNumber.setText("视屏" + info.getVideoCount());

                switch (info.getFocusStatus()) {
                    //关注状态 （当前操作用户相对于该用户的关注，1=已关注，2=未关注，3=不支持）
                    case 1:
                        addAttention.setBackground(getResources().getDrawable(R.drawable.oval_yellow_untransparent));
                        addAttention.setText("已关注");
                        addAttention.setTextColor(getResources().getColor(R.color.white));
                        break;

                    case 2:
                        addAttention.setBackground(getResources().getDrawable(R.drawable.oval_yellow_transparent));
                        addAttention.setText("+关注");
                        addAttention.setTextColor(getResources().getColor(R.color.Orange));
                        break;

                    case 3:
                        addAttention.setBackground(getResources().getDrawable(R.drawable.oval_grad_untransparent));
                        addAttention.setText("不支持");
                        addAttention.setTextColor(getResources().getColor(R.color.gray));
                        break;
                }
                if (astVisitors != null) {
                    astVisitors.clear();
                    astVisitors.addAll(info.getLastVisitors());
                }
                if (astVisitors.size() <= 0) {
                    RecentVisitorsRelative.setVisibility(View.GONE);
                } else {
                    RecentVisitorsRelative.setVisibility(View.VISIBLE);
                    mVisitorsAdapter.notifyDataSetChanged();
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
        map.put("userId", userid);
        RequestBody body = ApiClient.getRequestBody(map);
        HttpMethods.getInstance().getUserCenterInfo(new ProgressSubscriber<BaseRequestInfo<UserCenterInfo>>(mOnNextListener, PersonalCenterActivity.this), body);
    }

    private int pageIndex = 0;
    List<UserCentervideoInfo.DataBean> videoList = new ArrayList<>();

    /**
     * 获取直播和录像列表
     */
    public void getLiveAndVideo() {
        mOnNextListener = new SubscriberOnNextListener<BaseRequestInfo<UserCentervideoInfo>>() {
            @Override
            public void onNext(BaseRequestInfo<UserCentervideoInfo> userCentervideoInfo) {
                Log.i("wan", "获取直播和录像列表---" + userCentervideoInfo.toString());
                videoList.addAll(userCentervideoInfo.getData().getData());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onLogicError(String msg) {

            }
        };
        Map<Object, Object> map = new HashMap<>();
        map.put("userId", userid);
        map.put("pageIndex", pageIndex);
        map.put("pageSize", 10);
        RequestBody body = ApiClient.getRequestBody(map);
        HttpMethods.getInstance().getUserCenterVideo(new ProgressSubscriber<BaseRequestInfo<UserCentervideoInfo>>(mOnNextListener, PersonalCenterActivity.this), body);
    }

    private void MyImage(Float index) {
        //重新添加之前要清除之前的内容
        int num = LinearLayout.getChildCount();
        if (num >= 2) {
            for (int i = 0; i < num - 2; i++) {
                LinearLayout.removeViewAt(i + 2);
            }
        }

        int s = (int) (index * 10) % 10;
        int g = (int) (index % 10);
        switch (g) {
            case 1://花童
                userLv.setText("  花童  ");
                break;
            case 2://花学士
                userLv.setText("  花学士  ");
                break;
            case 3://花博士
                userLv.setText("  花博士  ");
                break;
            case 4://花仙
                userLv.setText("  花仙  ");
                break;
        }

        for (int j = 0; j < s; j++) {
            ImageView imageView = new ImageView(activity);
            android.widget.LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(android.widget.LinearLayout.LayoutParams.WRAP_CONTENT, android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);
            imageView.setLayoutParams(params);
            imageView.setImageResource(R.mipmap.vip_hua);
            LinearLayout.addView(imageView, 2);
        }
    }

    /**
     * 关注取消关注
     */
    @OnClick(R.id.add_attention)
    public void onAttention(View view) {
        switch (info.getFocusStatus()) {
            //关注状态 （当前操作用户相对于该用户的关注，1=已关注，2=未关注，3=不支持）
            case 1:
                cancelFoucs(userid);
                break;
            case 2:
                userAddAttention(userid);
                break;
            case 3:
                break;
        }


    }

    /**
     * 关注用户
     *
     * @param userId
     */
    public void userAddAttention(String userId) {

        mOnNextListener = new SubscriberOnNextListener<BaseRequestInfo>() {
            @Override
            public void onNext(BaseRequestInfo o) {
                Log.i("wan", "关注用户成功");
                addAttention.setBackground(getResources().getDrawable(R.drawable.oval_yellow_untransparent));
                addAttention.setText("已关注");
                addAttention.setTextColor(getResources().getColor(R.color.white));
                info.setFocusStatus(1);
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
        HttpMethods.getInstance().getUserAttention(new ProgressSubscriber<BaseRequestInfo>(mOnNextListener, activity), body);
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
                addAttention.setBackground(getResources().getDrawable(R.drawable.oval_yellow_transparent));
                addAttention.setText("+关注");
                addAttention.setTextColor(getResources().getColor(R.color.Orange));
                info.setFocusStatus(2);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onLogicError(String msg) {

            }
        }, activity), body);
    }

    @OnClick(R.id.back)
    public void onBack(View view) {
        finishActivity();
    }
}
