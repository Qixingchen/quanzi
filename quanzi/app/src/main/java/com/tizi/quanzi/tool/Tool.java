package com.tizi.quanzi.tool;

import android.os.Environment;

import com.avos.avoscloud.im.v2.AVIMMessage;
import com.tizi.quanzi.app.App;
import com.tizi.quanzi.model.ChatMessage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by qixingchen on 15/7/20.
 * 检查是否存在SDCard
 */
public class Tool {

    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    public static ChatMessage chatMessageFromAVMessage(AVIMMessage message) {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.messID = message.getMessageId();
        chatMessage.create_time = message.getTimestamp();
        // todo chatMessage.type
        chatMessage.receiptTimestamp = message.getReceiptTimestamp();
        chatMessage.status = message.getMessageStatus().getStatusCode();
        chatMessage.text = message.getContent();
        chatMessage.ConversationId = message.getConversationId();
        chatMessage.uid = App.getUserID();
        chatMessage.sender = message.getFrom();
        // todo chatMessage.local_path
        // todo chatMessage.url
        // todo chatMessage.voice_duration
        // TODO: 15/8/12 chatMessage.chatImage
        chatMessage.chatImage =
                "http://www.gravatar.com/avatar/6727fb208dd4a54b0eac56f8f6142cda?s=500";
        // TODO: 15/8/13  getMessageIOType不可用 为什么？
        chatMessage.isSelfSend = (message.getFrom().compareTo(App.getUserID()) == 0);
        chatMessage.isread = chatMessage.isSelfSend;
        if (chatMessage.isSelfSend){
            chatMessage.From = StaticField.ChatFrom.ME;
        }else {
            chatMessage.From = StaticField.ChatFrom.OTHER;
        }
        return chatMessage;
    }

    public static String timeStringFromUNIX(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINESE);
        String date = sdf.format(new Date(timestamp));
        return date;
    }

}
