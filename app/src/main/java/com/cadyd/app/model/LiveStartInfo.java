package com.cadyd.app.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/10/12.
 */
public class LiveStartInfo implements Serializable {


    private ChatParameterBean chatParameter;
    private String conversationID;
    private int foucsCount;
    private String headImageUrl;

    private QiniuParameterBean qiniuParameter;
    private String roomCode;

    public ChatParameterBean getChatParameter() {
        return chatParameter;
    }

    public void setChatParameter(ChatParameterBean chatParameter) {
        this.chatParameter = chatParameter;
    }

    public String getConversationID() {
        return conversationID;
    }

    public void setConversationID(String conversationID) {
        this.conversationID = conversationID;
    }

    public int getFoucsCount() {
        return foucsCount;
    }

    public void setFoucsCount(int foucsCount) {
        this.foucsCount = foucsCount;
    }

    public String getHeadImageUrl() {
        return headImageUrl;
    }

    public void setHeadImageUrl(String headImageUrl) {
        this.headImageUrl = headImageUrl;
    }

    public QiniuParameterBean getQiniuParameter() {
        return qiniuParameter;
    }

    public void setQiniuParameter(QiniuParameterBean qiniuParameter) {
        this.qiniuParameter = qiniuParameter;
    }

    public String getRoomCode() {
        return roomCode;
    }

    public void setRoomCode(String roomCode) {
        this.roomCode = roomCode;
    }

    public static class ChatParameterBean implements Serializable {
        private String channelID;
        private String serverID;

        public String getChannelID() {
            return channelID;
        }

        public void setChannelID(String channelID) {
            this.channelID = channelID;
        }

        public String getServerID() {
            return serverID;
        }

        public void setServerID(String serverID) {
            this.serverID = serverID;
        }
    }

    public static class QiniuParameterBean implements Serializable {
        private String pushStream;

        public String getPushStream() {
            return pushStream;
        }

        public void setPushStream(String pushStream) {
            this.pushStream = pushStream;
        }
    }
}
