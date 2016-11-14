package com.cadyd.app.comm;

import android.content.Context;
import android.widget.Toast;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.wechat.friends.Wechat;

import org.wcy.common.utils.StringUtil;

import java.util.HashMap;

/**
 * Created by Administrator on 2016/5/23.
 */
public class MyShare {

    /**
     * 一键分享
     *
     * @param context
     * @param title
     * @param content
     * @param url
     * @param imgUrl
     */
    public static void showShare(final Context context, String title, String content, String url, String imgUrl) {
        ShareSDK.initSDK(context);
        ShareSDK.getPlatform(context, Wechat.NAME);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(StringUtil.hasText(title) == true ? title : "第一点");
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl(StringUtil.hasText(url) == true ? url : "http://www.cadyd.com");
        // text是分享文本，所有平台都需要这个字段
        oks.setText(content);
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        oks.setImageUrl(imgUrl);
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(StringUtil.hasText(url) == true ? url : "http://www.cadyd.com");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment(content);
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite("第一点");
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(StringUtil.hasText(url) == true ? url : "http://www.cadyd.com");
        // 将快捷分享的操作结果将通过OneKeyShareCallback回调
        oks.setCallback(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                Toast.makeText(context, "分享成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                Toast.makeText(context, "分享失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel(Platform platform, int i) {
                Toast.makeText(context, "分享取消", Toast.LENGTH_SHORT).show();
            }
        });
        // 启动分享GUI
        oks.show(context);
    }

    /**
     * 无界面分享
     *
     * @param context
     * @param title
     * @param content
     * @param url
     * @param imgUrl
     * @param platformToShare
     */
    public static void showShare(Context context, String title, String content, String url, String imgUrl, String platformToShare) {
        ShareSDK.initSDK(context);
        ShareSDK.getPlatform(context, Wechat.NAME);
        OnekeyShare oks = new OnekeyShare();
        oks.setPlatform(platformToShare);
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(StringUtil.hasText(title) == true ? title : "第一点");
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl(StringUtil.hasText(url) == true ? url : "http://www.cadyd.com");
        // text是分享文本，所有平台都需要这个字段
        oks.setText(content);
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        oks.setImageUrl(StringUtil.hasText(imgUrl) == true ? imgUrl : "http://pic33.nipic.com/20130916/3420027_192919547000_2.jpg");
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(StringUtil.hasText(url) == true ? url : "http://www.cadyd.com");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment(content);
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite("第一点");
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(StringUtil.hasText(url) == true ? url : "http://www.cadyd.com");
        // 启动分享GUI
        oks.show(context);
    }
}
