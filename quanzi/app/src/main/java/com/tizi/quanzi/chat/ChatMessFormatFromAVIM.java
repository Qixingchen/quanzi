package com.tizi.quanzi.chat;

import com.avos.avoscloud.AVGeoPoint;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.AVIMReservedMessageType;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.avos.avoscloud.im.v2.messages.AVIMAudioMessage;
import com.avos.avoscloud.im.v2.messages.AVIMImageMessage;
import com.avos.avoscloud.im.v2.messages.AVIMLocationMessage;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.avos.avoscloud.im.v2.messages.AVIMVideoMessage;
import com.tizi.quanzi.app.App;
import com.tizi.quanzi.log.Log;
import com.tizi.quanzi.model.ChatMessage;
import com.tizi.quanzi.model.SystemMessage;
import com.tizi.quanzi.tool.StaticField;

/**
 * Created by qixingchen on 15/8/14.
 * 将AVIM消息类型转换为ChatMessage 或systemMess
 */
public class ChatMessFormatFromAVIM {

    /**
     * 将 AVIMTypedMessage 转换为 ChatMessage
     *
     * @param message 需要转换的 AVIMTypedMessage
     *
     * @return ChatMessage {@link ChatMessage}
     *
     * @throws ClassFormatError 消息为系统消息
     */
    public static ChatMessage ChatMessageFromAVMessage(AVIMTypedMessage message) throws ClassFormatError {
        String TAG = "Tool类 消息类型转换";
        ChatMessage chatMessage;
        if (message.getMessageType() == AVIMReservedMessageType.TextMessageType.getType()) {
            /*判断是否为系统消息*/
            String type = (String) ((AVIMTextMessage) message).getAttrs().get(
                    StaticField.ChatMessAttrName.IS_SYS_MESS);
            try {
                if (type.compareTo(StaticField.SystemMessAttrName.MessTypeCode.System_mess) == 0) {
                    throw new ClassFormatError("Is System Message");
                }
            } catch (Exception ignored) {//旧信息无此项目
            }
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
        } else if (message.getMessageType() == AVIMReservedMessageType.LocationMessageType.getType()) {
            chatMessage = locationChatMessageFromAVMessage(message);
            Log.w(TAG, "地理消息");
        } else {
            chatMessage = textChatMessageFromAVMessage(message);
            Log.e(TAG, "未知类型消息");
        }
        Log.w(TAG, chatMessage.toString());
        return chatMessage;
    }

    /**
     * 将 AVIMTypedMessage 转换为 SystemMessage
     *
     * @param message 需要转换的 AVIMTypedMessage
     *
     * @return SystemMessage {@link SystemMessage}
     */
    public static SystemMessage SysMessFromAVMess(AVIMTypedMessage message) {
        SystemMessage systemMessage = new SystemMessage();
        AVIMTextMessage textMessage = (AVIMTextMessage) message;
        systemMessage.setId(textMessage.getMessageId());
        try {
            systemMessage.setConvid((String) textMessage.getAttrs().get(
                    StaticField.SystemMessAttrName.JOIN_CONV_ID));
        } catch (Exception ignored) {
        }
        systemMessage.setUser_id((String) textMessage.getAttrs().get(
                StaticField.ChatMessAttrName.userID));
        systemMessage.setUser_icon((String) textMessage.getAttrs().get(
                StaticField.ChatMessAttrName.userIcon));
        systemMessage.setUser_name((String) textMessage.getAttrs().get(
                StaticField.ChatMessAttrName.userName));
        systemMessage.setMsg_type((String) textMessage.getAttrs().get(
                StaticField.ChatMessAttrName.IS_SYS_MESS));
        systemMessage.setContent(textMessage.getText());
        systemMessage.setRemark((String) textMessage.getAttrs().get(
                StaticField.SystemMessAttrName.REMARK));
        systemMessage.setLink_url((String) textMessage.getAttrs().get(
                StaticField.SystemMessAttrName.LINK_URL));
        systemMessage.setSys_msg_flag((int) textMessage.getAttrs().get(
                StaticField.SystemMessAttrName.SYS_MSG_FLAG));
        systemMessage.setStatus(0);
        systemMessage.setIsread(false);
        systemMessage.setGroup_id((String) textMessage.getAttrs().get(
                StaticField.ChatMessAttrName.groupID));
        systemMessage.setCreate_time(textMessage.getTimestamp());
        return systemMessage;
    }

    /*转换位置消息*/
    private static ChatMessage locationChatMessageFromAVMessage(AVIMMessage message) {
        ChatMessage chatMessage = mainMessageInfoFromAvimMessage(message);
        chatMessage.type = StaticField.ChatContantType.TEXT;
        AVGeoPoint avGeoPoint = ((AVIMLocationMessage) message).getLocation();
        chatMessage.text = ((AVIMLocationMessage) message).getText();
        chatMessage.chatImage = (String)
                ((AVIMLocationMessage) message).getAttrs().get(StaticField.ChatMessAttrName.userIcon);
        chatMessage.userName = (String)
                ((AVIMLocationMessage) message).getAttrs().get(StaticField.ChatMessAttrName.userName);
        chatMessage.groupID = (String)
                ((AVIMLocationMessage) message).getAttrs().get(StaticField.ChatMessAttrName.groupID);
        // todo chatMessage.local_path
        // todo chatMessage.url
        // todo chatMessage.voice_duration
        return chatMessage;
    }

    /*转换文本消息*/
    private static ChatMessage textChatMessageFromAVMessage(AVIMMessage message) {
        ChatMessage chatMessage = mainMessageInfoFromAvimMessage(message);
        chatMessage.type = StaticField.ChatContantType.TEXT;
        chatMessage.text = ((AVIMTextMessage) message).getText();
        chatMessage.chatImage = (String)
                ((AVIMTextMessage) message).getAttrs().get(StaticField.ChatMessAttrName.userIcon);
        chatMessage.userName = (String)
                ((AVIMTextMessage) message).getAttrs().get(StaticField.ChatMessAttrName.userName);
        chatMessage.groupID = (String)
                ((AVIMTextMessage) message).getAttrs().get(StaticField.ChatMessAttrName.groupID);
        // todo chatMessage.local_path
        // todo chatMessage.url
        // todo chatMessage.voice_duration
        return chatMessage;
    }

    /*转换图片消息*/
    private static ChatMessage imageChatMessageFromAVMessage(AVIMMessage message) {
        ChatMessage chatMessage = mainMessageInfoFromAvimMessage(message);
        chatMessage.type = StaticField.ChatContantType.IMAGE;
        chatMessage.url = ((AVIMImageMessage) message).getFileUrl();
        chatMessage.chatImage = (String)
                ((AVIMImageMessage) message).getAttrs().get(StaticField.ChatMessAttrName.userIcon);
        chatMessage.userName = (String)
                ((AVIMImageMessage) message).getAttrs().get(StaticField.ChatMessAttrName.userName);
        chatMessage.groupID = (String)
                ((AVIMImageMessage) message).getAttrs().get(StaticField.ChatMessAttrName.groupID);
        chatMessage.imageWeight = ((AVIMImageMessage) message).getWidth();
        chatMessage.imageHeight = ((AVIMImageMessage) message).getHeight();
        return chatMessage;
    }

    /*转换音频消息*/
    private static ChatMessage voiceChatMessageFromAVMessage(AVIMMessage message) {
        ChatMessage chatMessage = mainMessageInfoFromAvimMessage(message);
        chatMessage.type = StaticField.ChatContantType.VOICE;
        chatMessage.url = ((AVIMAudioMessage) message).getFileUrl();
        chatMessage.voice_duration = ((AVIMAudioMessage) message).getDuration();
        chatMessage.chatImage = (String)
                ((AVIMAudioMessage) message).getAttrs().get(StaticField.ChatMessAttrName.userIcon);
        chatMessage.userName = (String)
                ((AVIMAudioMessage) message).getAttrs().get(StaticField.ChatMessAttrName.userName);
        chatMessage.groupID = (String)
                ((AVIMAudioMessage) message).getAttrs().get(StaticField.ChatMessAttrName.groupID);
        return chatMessage;
    }

    /*转换视频消息*/
    private static ChatMessage vedioChatMessageFromAVMessage(AVIMMessage message) {
        ChatMessage chatMessage = mainMessageInfoFromAvimMessage(message);
        chatMessage.type = StaticField.ChatContantType.VEDIO;
        chatMessage.url = ((AVIMVideoMessage) message).getFileUrl();
        chatMessage.voice_duration = ((AVIMVideoMessage) message).getDuration();
        chatMessage.chatImage = (String)
                ((AVIMVideoMessage) message).getAttrs().get(StaticField.ChatMessAttrName.userIcon);
        chatMessage.userName = (String)
                ((AVIMVideoMessage) message).getAttrs().get(StaticField.ChatMessAttrName.userName);
        chatMessage.groupID = (String)
                ((AVIMVideoMessage) message).getAttrs().get(StaticField.ChatMessAttrName.groupID);
        return chatMessage;
    }

    /*通用信息转换*/

    /**
     * @param message 需要转换的 AVIMMessage
     *
     * @return 部分信息已经填充的 ChatMessage
     *
     * @see ChatMessage
     */
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
        // TODO: 15/8/13  getMessageIOType不可用 为什么？
        chatMessage.isSelfSend = (message.getFrom().compareTo(App.getUserID()) == 0);
        // TODO: 15/8/17 isread
        chatMessage.isread = chatMessage.isSelfSend;
        if (chatMessage.isSelfSend) {
            chatMessage.From = StaticField.ChatFrom.ME;
        } else {
            chatMessage.From = StaticField.ChatFrom.OTHER;
        }
        return chatMessage;
        // TODO: 15/8/17  ChatBothUserType
        // ChatBothUserType,;

    }

}
