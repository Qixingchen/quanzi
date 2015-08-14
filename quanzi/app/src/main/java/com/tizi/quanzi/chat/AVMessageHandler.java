package com.tizi.quanzi.chat;

import android.support.annotation.Nullable;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.AVIMMessageHandler;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.tizi.quanzi.app.App;
import com.tizi.quanzi.database.DBAct;
import com.tizi.quanzi.model.ChatMessage;
import com.tizi.quanzi.log.Log;
import com.tizi.quanzi.tool.Tool;

/**
 * Created by qixingchen on 15/8/12.
 * 普通消息接收，todo 等待删除
 */
public class AVMessageHandler extends AVIMMessageHandler {
    private static final String TAG = AVMessageHandler.class.getSimpleName();
    private static AVMessageHandler mInstance;

    public static AVMessageHandler getInstance() {
        if (mInstance == null) {
            synchronized (AVMessageHandler.class) {
                if (mInstance == null) {
                    mInstance = new AVMessageHandler();
                }
            }
        }
        return mInstance;
    }

    private AVMessageHandler() {
    }

    public void setOnMessage(@Nullable OnMessage onMessage) {
        this.onMessage = onMessage;
    }

    private OnMessage onMessage;

    // TODO: 15/8/12 更新UI
    public void onMessage(AVIMMessage message, AVIMConversation conversation, AVIMClient client) {

        ChatMessage chatMessage = ChatMessFormatFromAVIM.ChatMessageFromAVMessage((AVIMTypedMessage) message);
        DBAct.getInstance().addOrReplaceChatMessage(chatMessage);

        if (App.UI_CONVERSATION_ID.compareTo(message.getConversationId()) == 0) {
            if (onMessage != null) {
                onMessage.OnMessageGet(chatMessage);
            }
        }

        Log.d(TAG, conversation.getConversationId() + " 收到一条新消息：" + chatMessage.toString());
    }

    @Override
    public void onMessageReceipt(AVIMMessage message, AVIMConversation conversation, AVIMClient client) {
        super.onMessageReceipt(message, conversation, client);
        ChatMessage chatMessage = ChatMessFormatFromAVIM.ChatMessageFromAVMessage((AVIMTypedMessage) message);
        DBAct.getInstance().addOrReplaceChatMessage(chatMessage);
        //todo 消息被接受
        if (App.UI_CONVERSATION_ID.compareTo(message.getConversationId()) == 0) {
            if (onMessage != null) {
                onMessage.OnMyMessageSent(chatMessage);
            }
        }
        Log.d(TAG, conversation.getConversationId() + " 消息成功送达：" + chatMessage.toString());
    }


    //消息接收器回调接口
    public interface OnMessage {
        void OnMessageGet(ChatMessage chatMessage);

        void OnMyMessageSent(ChatMessage chatMessage);
    }

}
