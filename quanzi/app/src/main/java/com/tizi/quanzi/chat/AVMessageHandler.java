package com.tizi.quanzi.chat;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.AVIMMessageHandler;
import com.tizi.quanzi.database.DBAct;
import com.tizi.quanzi.gson.ChatMessage;
import com.tizi.quanzi.log.Log;
import com.tizi.quanzi.tool.Tool;

/**
 * Created by qixingchen on 15/8/12.
 */
public class AVMessageHandler extends AVIMMessageHandler {
    private static final String TAG = AVMessageHandler.class.getSimpleName();

    // TODO: 15/8/12 更新UI
    public void onMessage(AVIMMessage message, AVIMConversation conversation, AVIMClient client) {

        ChatMessage chatMessage = Tool.chatMessageFromAVMessage(message);
        DBAct.getInstance().addChatMessage(chatMessage);

        Log.d("TAG", conversation.getConversationId() + " 收到一条新消息：" + chatMessage.toString());
    }

    @Override
    public void onMessageReceipt(AVIMMessage message, AVIMConversation conversation, AVIMClient client) {
        super.onMessageReceipt(message, conversation, client);
        ChatMessage chatMessage = Tool.chatMessageFromAVMessage(message);
        DBAct.getInstance().addChatMessage(chatMessage);
        //todo 消息被接受
    }
}
