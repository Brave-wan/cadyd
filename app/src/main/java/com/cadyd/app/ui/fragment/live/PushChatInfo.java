package com.cadyd.app.ui.fragment.live;

import java.util.UUID;

/**
 * @Description: ${todo}(这里用一句话描述这个方法的作用)}$
 * 作者：Jerry
 * 邮箱：185214487@qq.com
 */
public class PushChatInfo {
    private String commandType;
    private boolean liveUser;
    private UUID messageId;
    private UUID roomId;
    private int state;
    private String userId;
    private PersonterData data;
    public String getCommandType() {
        return commandType;
    }

    public void setCommandType(String commandType) {
        this.commandType = commandType;
    }

    public boolean isLiveUser() {
        return liveUser;
    }

    public void setLiveUser(boolean liveUser) {
        this.liveUser = liveUser;
    }

    public UUID getMessageId() {
        return messageId;
    }

    public void setMessageId(UUID messageId) {
        this.messageId = messageId;
    }

    public UUID getRoomId() {
        return roomId;
    }

    public void setRoomId(UUID roomId) {
        this.roomId = roomId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public PersonterData getData() {
        return data;
    }


    public void setData(PersonterData data) {
        this.data = data;
    }

    public static class PersonterData {
        private String headUrl;
        private String message;
        private UUID giftId;
        private int concernCount;
        private int totalCount;
        private String userId;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public UUID getGiftId() {
            return giftId;
        }

        public void setGiftId(UUID giftId) {
            this.giftId = giftId;
        }

        public int getConcernCount() {
            return concernCount;
        }

        public void setConcernCount(int concernCount) {
            this.concernCount = concernCount;
        }

        public int getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
        }

        public String getHeadUrl() {
            return headUrl;
        }

        public void setHeadUrl(String headUrl) {
            this.headUrl = headUrl;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        @Override
        public String toString() {
            return "PersonterData{" +
                    "headUrl='" + headUrl + '\'' +
                    ", message='" + message + '\'' +
                    '}';
        }
    }


    @Override
    public String toString() {
        return "PushChatInfo{" +
                "commandType='" + commandType + '\'' +
                ", liveUser=" + liveUser +
                ", messageId='" + messageId + '\'' +
                ", roomId='" + roomId + '\'' +
                ", state=" + state +
                ", userId='" + userId + '\'' +
                ", data=" + data +
                '}';
    }
}
