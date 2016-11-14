package com.cadyd.app.utils;

import android.util.Log;

import com.cadyd.app.MyApplication;
import com.cadyd.app.interfaces.SocketConnectListener;
import com.cadyd.app.model.SocketMode;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.net.URISyntaxException;


/**
 * @Description: ${todo}(这里用一句话描述这个方法的作用)}$
 * 作者：Jerry
 * 邮箱：185214487@qq.com
 */
public class SocketSingleton {

    private static SocketSingleton instance = new SocketSingleton();
    private Socket mSocket;
    private SocketConnectListener mSocketSataeListener;
    private static String TcpUrl;

    public void setOnSocketSataeListener(SocketConnectListener listener) {
        this.mSocketSataeListener = listener;
    }

    private SocketSingleton() {
    }

    public static SocketSingleton getInstance(String url) {
        TcpUrl = "http://" + url;
        return instance;
    }


    public Socket getSocket() {
        try {
            Log.i("wan", "tcp服务器地址---" + TcpUrl);
            mSocket = IO.socket(TcpUrl);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        socketConn();
        return mSocket;
    }

    //连接到Server
    private void socketConn() {
        mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        mSocket.connect();
    }


    /**
     * 主播开播建立链接
     *
     * @param ChannelID 房间id
     * @param msg       建立房间的消息
     * @return
     */

    public JSONObject getSocketJson(String ChannelID, String userName, String msg) {
        SocketMode socketMode = new SocketMode();
        JSONObject jsonObject = null;
        try {
            socketMode.setAnchorId(MyApplication.getInstance().getSignInModel().id);
            socketMode.setLiveUser(true);
            socketMode.setRoomId(ChannelID);
            socketMode.setHeadUrl(MyApplication.getInstance().getSignInModel().photo);
            socketMode.setSend(true);
            socketMode.setMessage(msg);
            socketMode.setUserName(userName);
            socketMode.setUserId(MyApplication.getInstance().getSignInModel().id);
            String json = new Gson().toJson(socketMode);
            Log.i("wan", "json-----" + json);
            jsonObject = new JSONObject(json);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonObject;
    }


    public JSONObject getUserSocketJson(String ChannelID, String userName, String msg) {
        SocketMode socketMode = new SocketMode();
        JSONObject jsonObject = null;
        try {
            socketMode.setAnchorId(MyApplication.getInstance().getSignInModel().id);
            socketMode.setLiveUser(false);
            socketMode.setRoomId(ChannelID);
            socketMode.setHeadUrl(MyApplication.getInstance().getSignInModel().photo);
            socketMode.setSend(true);
            socketMode.setMessage(msg);
            socketMode.setUserName(userName);
            socketMode.setUserId(MyApplication.getInstance().getSignInModel().id);
            String json = new Gson().toJson(socketMode);
            Log.i("wan", "userjson-----" + json);
            jsonObject = new JSONObject(json);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonObject;
    }


    /**
     * 销毁连接
     */
    public void disConnect() {
        Log.i("wan", "正在销毁连接");
        mSocket.off();
        mSocket.disconnect();
        mSocket.off(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.off(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
    }

    /**
     * 连接状态监听
     */
    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            if (mSocketSataeListener != null)
                mSocketSataeListener.onConnect();
        }
    };


}
