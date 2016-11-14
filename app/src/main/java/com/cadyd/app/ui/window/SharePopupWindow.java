package com.cadyd.app.ui.window;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.cadyd.app.R;
import com.cadyd.app.api.ApiClient;
import com.cadyd.app.asynctask.retrofitmode.BaseRequestInfo;
import com.cadyd.app.asynctask.retrofitmode.HttpMethods;
import com.cadyd.app.asynctask.retrofitmode.ProgressSubscriber;
import com.cadyd.app.asynctask.retrofitmode.SubscriberOnNextListener;
import com.cadyd.app.comm.MyShare;
import com.cadyd.app.model.ShareInfo;
import com.cadyd.app.utils.Util;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;
import okhttp3.RequestBody;

/**
 * Created by rance on 2016/10/5.
 * 自定义分享界面
 */
public class SharePopupWindow extends PopupWindow implements View.OnClickListener{
    @Bind(R.id.pop_share_sina)
    TextView popShareSina;
    @Bind(R.id.pop_share_wechat)
    TextView popShareWechat;
    @Bind(R.id.pop_share_friend)
    TextView popShareFriend;
    @Bind(R.id.pop_share_qq)
    TextView popShareQq;
    @Bind(R.id.pop_share_qzone)
    TextView popShareQzone;
    @Bind(R.id.pop_share_link)
    TextView popShareLink;
    @Bind(R.id.pop_share_cancel)
    TextView popShareCancel;
    private Context mContext;
    private ShareInfo shareInfo;
    private String shareType;
    private String shareid;
    private int liveType;

    public SharePopupWindow(Context mContext, String shareType, String shareid, int liveType) {
        this.mContext = mContext;
        this.shareType = shareType;
        this.shareid = shareid;
        this.liveType = liveType;
        View view = LayoutInflater.from(mContext).inflate(R.layout.pop_share, null);
        setContentView(view);
        initView(view);
    }

    private void initView(View view) {
        popShareSina = (TextView) view.findViewById(R.id.pop_share_sina);
        popShareSina.setOnClickListener(this);
        popShareWechat = (TextView) view.findViewById(R.id.pop_share_wechat);
        popShareWechat.setOnClickListener(this);
        popShareFriend = (TextView) view.findViewById(R.id.pop_share_friend);
        popShareFriend.setOnClickListener(this);
        popShareQq = (TextView) view.findViewById(R.id.pop_share_qq);
        popShareQq.setOnClickListener(this);
        popShareQzone = (TextView) view.findViewById(R.id.pop_share_qzone);
        popShareQzone.setOnClickListener(this);
        popShareLink = (TextView) view.findViewById(R.id.pop_share_link);
        popShareLink.setOnClickListener(this);
        popShareCancel = (TextView) view.findViewById(R.id.pop_share_cancel);
        popShareCancel.setOnClickListener(this);

        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(Util.getScreenHeight(mContext) - Util.getStatusHeight(mContext));
        setOutsideTouchable(true);
        setFocusable(false);
    }

    /**
     * 显示popwindow
     *
     * @param view
     */
    public void showPop(View view) {
        showAtLocation(view, Gravity.BOTTOM, 0, 0);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pop_share_sina:
                shareHttp(SinaWeibo.NAME);
                dismiss();
                break;
            case R.id.pop_share_wechat:
                shareHttp(Wechat.NAME);
                dismiss();
                break;
            case R.id.pop_share_friend:
                shareHttp(WechatMoments.NAME);
                dismiss();
                break;
            case R.id.pop_share_qq:
                shareHttp(QQ.NAME);
                dismiss();
                break;
            case R.id.pop_share_qzone:
                shareHttp(QZone.NAME);
                dismiss();
                break;
            case R.id.pop_share_link:
                shareHttp(null);
                dismiss();
                break;
            case R.id.pop_share_cancel:
                dismiss();
                break;
        }
    }

    /**
     * 分享
     */
    public void shareHttp(final String shareName) {
        Map<Object, Object> map = new HashMap<>();
        map.put("shareType", shareType);
        map.put("param1", shareid);
        if (liveType != -1){
            map.put("param2", liveType);
        }
        RequestBody key = ApiClient.getRequestBody(map);
        HttpMethods.getInstance().getShareInfo(new ProgressSubscriber<BaseRequestInfo<ShareInfo>>(new SubscriberOnNextListener<BaseRequestInfo<ShareInfo>>() {
            @Override
            public void onNext(BaseRequestInfo<ShareInfo> o) {
                shareInfo = o.getData();
                if (!TextUtils.isEmpty(shareName)){
                    MyShare.showShare(mContext, shareInfo.getTitle(), shareInfo.getContent(), shareInfo.getClickUrl(), shareInfo.getPictureUrl(), shareName);
                } else {
                    copyText(mContext, shareInfo.getClickUrl());
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onLogicError(String msg) {

            }
        }, mContext), key);
    }

    /**
     * 复制文本到剪贴板
     *
     * @param context 上下文
     * @param text    文本
     */
    public static void copyText(Context context, CharSequence text) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        clipboard.setPrimaryClip(ClipData.newPlainText("text", text));
        Toast.makeText(context, "链接复制成功", Toast.LENGTH_SHORT).show();
    }

}
