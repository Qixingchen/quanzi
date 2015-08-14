package com.tizi.quanzi.chat;

import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.AVIMReservedMessageType;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.avos.avoscloud.im.v2.messages.AVIMAudioMessage;
import com.avos.avoscloud.im.v2.messages.AVIMImageMessage;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.avos.avoscloud.im.v2.messages.AVIMVideoMessage;
import com.tizi.quanzi.app.App;
import com.tizi.quanzi.log.Log;
import com.tizi.quanzi.model.ChatMessage;
import com.tizi.quanzi.tool.StaticField;

/**
 * Created by qixingchen on 15/8/14.
 * 将AVIM消息类型转换为ChatMessage
 */
public class ChatMessFormatFromAVIM {

    public static ChatMessage ChatMessageFromAVMessage(AVIMTypedMessage message) {
        String TAG = "Tool类 消息类型转换";
        ChatMessage chatMessage = null;
        if (message.getMessageType() == AVIMReservedMessageType.TextMessageType.getType()) {
            chatMessage = textChatMessageFromAVMessage(message);
            Log.w(TAG, "文本消息:");
        } else if (message.getMessageType() == AVIMReservedMessageType.ImageMessageType.getType()) {
            chatMessage = imageChatMessageFromAVMessage(message);
            Log.w(TAG, "图片消息。");
        } else if (message.getMessageType() == AVIMReservedMessageType.AudioMessageType.getType()) {
            chatMessage = voiceChatMessageFromAVMessage(message);
            Log.w(TAG, "音频消息。");
        } else if (message.getMessageType() == AVIMReservedMessageType.VideoMessageType.getType()) {
            chatMessage = vedioChatMessageFromAVMessage(message);
            Log.w(TAG, "视频消息");
        } else {
            chatMessage = textChatMessageFromAVMessage(message);
            Log.e(TAG, "未知类型消息");
        }
        Log.w(TAG, chatMessage.toString());
        return chatMessage;
    }

    private static ChatMessage textChatMessageFromAVMessage(AVIMMessage message) {
        ChatMessage chatMessage = mainMessageInfoFromAvimMessage(message);
        chatMessage.type = StaticField.chatType.TEXT;
        // todo chatMessage.local_path
        // todo chatMessage.url
        // todo chatMessage.voice_duration
        return chatMessage;
    }

    private static ChatMessage imageChatMessageFromAVMessage(AVIMMessage message) {
        ChatMessage chatMessage = mainMessageInfoFromAvimMessage(message);
        chatMessage.type = StaticField.chatType.IMAGE;
        chatMessage.url = ((AVIMImageMessage) message).getFileUrl();
        return chatMessage;
    }

    private static ChatMessage voiceChatMessageFromAVMessage(AVIMMessage message) {
        ChatMessage chatMessage = mainMessageInfoFromAvimMessage(message);
        chatMessage.type = StaticField.chatType.VOICE;
        chatMessage.url = ((AVIMAudioMessage) message).getFileUrl();
        chatMessage.voice_duration = ((AVIMAudioMessage) message).getDuration();
        return chatMessage;
    }

    private static ChatMessage vedioChatMessageFromAVMessage(AVIMMessage message) {
        ChatMessage chatMessage = mainMessageInfoFromAvimMessage(message);
        chatMessage.type = StaticField.chatType.VEDIO;
        chatMessage.url = ((AVIMVideoMessage) message).getFileUrl();
        chatMessage.voice_duration = ((AVIMVideoMessage) message).getDuration();
        return chatMessage;
    }

    private static ChatMessage mainMessageInfoFromAvimMessage(AVIMMessage message) {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.messID = message.getMessageId();
        chatMessage.create_time = message.getTimestamp();
        chatMessage.receiptTimestamp = message.getReceiptTimestamp();
        chatMessage.status = message.getMessageStatus().getStatusCode();
        chatMessage.text = message.getContent();
        chatMessage.ConversationId = message.getConversationId();
        chatMessage.uid = App.getUserID();
        chatMessage.sender = message.getFrom();
        chatMessage.chatImage = (String)
                ((AVIMTextMessage) message).getAttrs().get("userIcon");
        chatMessage.userName = (String)
                ((AVIMTextMessage) message).getAttrs().get("userName");
        // TODO: 15/8/13  getMessageIOType不可用 为什么？
        chatMessage.isSelfSend = (message.getFrom().compareTo(App.getUserID()) == 0);
        chatMessage.isread = chatMessage.isSelfSend;
        if (chatMessage.isSelfSend) {
            chatMessage.From = StaticField.ChatFrom.ME;
        } else {
            chatMessage.From = StaticField.ChatFrom.OTHER;
        }
        return chatMessage;
    }

}
