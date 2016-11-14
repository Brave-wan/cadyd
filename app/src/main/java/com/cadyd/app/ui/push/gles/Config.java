package com.cadyd.app.ui.push.gles;

import android.content.pm.ActivityInfo;

import com.qiniu.pili.droid.streaming.StreamingProfile;

/**
 * Created by jerikc on 15/12/8.
 */
public class Config {

    public static final boolean DEBUG_MODE = false;
    public static final boolean FILTER_ENABLED = false;
    public static final int ENCODING_LEVEL = StreamingProfile.VIDEO_ENCODING_HEIGHT_480;
    public static final int SCREEN_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;


    public static String SOCKETPATH = "114.55.58.18";
    public static int SOCKETPORT = 9002;

    public static final int SUCCESS_STATE = 0X12345;
    public static final int FAIL_STATE = 0X12378;
    public static final int SOCKETCONNECT = 0x300;
    public static final int SOCKETRESTART = 0x1000;
    public static final int SOCKETCHATMSG = 0x400;
    public static final int SOCKETCHATGIFT = 0x500;
    public static final int SOCKETUNCONNECT = 0x600;
    public static final int SOCKETCONCERN = 0x700;
    public static final int SOCKETUNCONCERN = 0x800;
    public static final int SOCKETCREATEROOM = 0X900;
    public static final int SOCKETSTOPPED = 0x11000;
    public static final int SOCKETINIT = 0X22222;
    public static final int MESSAGE_ID_RECONNECTING = 0x121;

    public static final int FILE = 0x1000168;
    public static final String EXTRA_PUBLISH_URL_PREFIX = "URL:";
    public static final String EXTRA_PUBLISH_JSON_PREFIX = "JSON:";
    public static String serverId = "";

    public static String ChannelId = "";
    public static String ConversationID = "";
    //直播悬浮框是否开启
    public static boolean isLive = false;

    public static final String VERSION_HINT = "v2.0.0";
    public static final String EXTRA_KEY_PUB_URL = "pub_url";
    public static final String HINT_ENCODING_ORIENTATION_CHANGED = "Encoding orientation had been changed. Stop streaming first and restart streaming will take effect";

}
