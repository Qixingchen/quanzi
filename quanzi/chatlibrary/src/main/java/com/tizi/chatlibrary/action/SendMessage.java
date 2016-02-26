package com.tizi.chatlibrary.action;

import com.tizi.chatlibrary.model.message.ChatMessage;

/**
 * Created by qixingchen on 16/2/24.
 * <p/>
 * 发送消息后的调用
 */
public class SendMessage {

    /**
     * 消息发送成功后的调用
     */
    public static void onMessageSendOK(String tempID, ChatMessage chatMessage, String CONVERSATION_ID) {
        DatabaseAction.deleteMessage(tempID);
        DatabaseAction.addOrReplaceChatMessage(chatMessage);
    }

    /**
     * 消息发送失败后的调用
     */
    public static void onMessageSendError(String errorMessage, String CONVERSATION_ID,
                                          String tempID, ChatMessage chatMessage) {
        DatabaseAction.addOrReplaceChatMessage(chatMessage);
    }

    /**
     * 消息预发送后的调用
     */
    public static void onMessagePreSend(String CONVERSATION_ID,
                                        String tempID, ChatMessage chatMessage) {
        DatabaseAction.addOrReplaceChatMessage(chatMessage);
    }
}
