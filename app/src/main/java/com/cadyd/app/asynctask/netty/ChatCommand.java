package com.cadyd.app.asynctask.netty;

/**
 * @Description: ${todo}(这里用一句话描述这个方法的作用)}$
 * 作者：Jerry
 * 邮箱：185214487@qq.com
 */
public class ChatCommand {
    private String  CommandType;

    public String getCommandType() {
        return CommandType;
    }

    public void setCommandType(String commandType) {
        CommandType = commandType;
    }

    @Override
    public String toString() {
        return "ChatCommand{" +
                "CommandType='" + CommandType + '\'' +
                '}';
    }
}
