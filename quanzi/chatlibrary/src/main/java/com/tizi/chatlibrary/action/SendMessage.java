package com.tizi.chatlibrary.action;

import com.tizi.chatlibrary.model.message.ChatMessage;

/**
 * Created by qixingchen on 16/2/24.
 * <p/>
 * 发送消息
 */
public class SendMessage {

    private SendMess mSendMess;

    public void setSendMess(SendMess mSendMess) {
        this.mSendMess = mSendMess;
    }

    /**
     * 消息发送成功
     */
    public void onMessageSendOK(String tempID, ChatMessage chatMessage, String CONVERSATION_ID) {
        DatabaseAction.deleteMessage(tempID);
        DatabaseAction.addOrReplaceChatMessage(chatMessage);
    }

    /**
     * 消息发送失败
     */
    public void onMessageSendError(String errorMessage, String CONVERSATION_ID,
                                   String tempID, ChatMessage chatMessage) {
        DatabaseAction.addOrReplaceChatMessage(chatMessage);
    }

    /**
     * 消息预发送
     */
    public void onMessagePreSend(String CONVERSATION_ID,
                                 String tempID, ChatMessage chatMessage) {
        DatabaseAction.addOrReplaceChatMessage(chatMessage);
    }

    public void SendMessage(ChatMessage chatMessage) {
        if (mSendMess != null) {
            mSendMess.sendMess(chatMessage);
        }
    }

    public interface SendMess {
        void sendMess(ChatMessage chatMessage);
    }
}
