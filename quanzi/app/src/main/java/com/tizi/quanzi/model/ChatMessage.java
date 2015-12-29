package com.tizi.quanzi.model;

import com.avos.avoscloud.im.v2.AVIMMessage;
import com.tizi.quanzi.app.AppStaticValue;
import com.tizi.quanzi.dataStatic.MyUserInfo;
import com.tizi.quanzi.gson.Login;
import com.tizi.quanzi.tool.FriendTime;
import com.tizi.quanzi.tool.StaticField;
import com.tizi.quanzi.tool.Tool;
import com.tizi.quanzi.tool.ZipPic;

import java.io.Serializable;

/**
 * Created by qixingchen on 15/8/12.
 * 聊天信息
 * 转换
 *
 * @see com.tizi.quanzi.chat.ChatMessFormatFromAVIM
 */
public class ChatMessage implements Serializable {

    public int type, status, From, ChatBothUserType, imageHeight, imageWeight;
    public String text, ConversationId, uid, sender,
            local_path, url, messID, chatImage, userName, groupID;
    public long create_time, receiptTimestamp;
    public boolean isread, isSelfSend;
    public double voice_duration;

    /**
     * 生成内容文字
     * todo add name
     *
     * @param chatMessage 需要发布的消息
     *
     * @return 可阅读的消息内容
     */
    public static String getContentText(ChatMessage chatMessage) {
        String contentText;
        if (chatMessage.type == StaticField.ChatContantType.IMAGE) {
            contentText = "图片";
        } else if (chatMessage.type == StaticField.ChatContantType.VOICE) {
            contentText = "录音";
        } else if (chatMessage.type == StaticField.ChatContantType.VEDIO) {
            contentText = "视频";
        } else if (chatMessage.type == StaticField.ChatContantType.Location) {
            contentText = "位置";
        } else {
            contentText = chatMessage.text;
        }
        return contentText;
    }

    /**
     * 获取新的聊天消息,用于发送前的临时消息
     */
    private static ChatMessage getChatMessage(
            int type, int ChatBothUserType, String text, String conversationId,
            String local_path, String messID, double voice_duration) {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.sender = AppStaticValue.getUserID();
        chatMessage.isread = true;
        chatMessage.type = type;
        chatMessage.ChatBothUserType = ChatBothUserType;
        chatMessage.text = text;
        chatMessage.ConversationId = conversationId;
        chatMessage.local_path = local_path;
        chatMessage.messID = messID;
        chatMessage.create_time = Tool.getBeijinTime();
        chatMessage.voice_duration = voice_duration;
        chatMessage.status = AVIMMessage.AVIMMessageStatus.AVIMMessageStatusSending.getStatusCode();
        chatMessage.From = StaticField.ChatFrom.ME;
        chatMessage.uid = AppStaticValue.getUserID();
        Login.UserEntity user = MyUserInfo.getInstance().getUserInfo();
        if (user != null) {
            chatMessage.chatImage = user.getIcon();
            chatMessage.userName = user.getUserName();
        }
        chatMessage.isSelfSend = true;
        return chatMessage;
    }

    public static ChatMessage getTextChatMessage(int ChatBothUserType, String text,
                                                 String conversationId, String messID) {
        return getChatMessage(StaticField.ChatContantType.TEXT, ChatBothUserType, text,
                conversationId, null, messID, 0);
    }

    public static ChatMessage getImageChatMessage(int chatBothUserType, String conversationId, String local_path,
                                                  String messID) {
        ChatMessage chatMessage = getChatMessage(StaticField.ChatContantType.IMAGE, chatBothUserType, "", conversationId, local_path,
                messID, 0);

        int[] imagesize = ZipPic.getImageSize(local_path);

        chatMessage.imageWeight = imagesize[0];
        chatMessage.imageHeight = imagesize[1];

        return chatMessage;
    }

    public static ChatMessage getVoiceChatMessage(int chatBothUserType, String conversationId, String local_path,
                                                  String messID, double voice_duration) {
        return getChatMessage(StaticField.ChatContantType.VOICE, chatBothUserType, "",
                conversationId, local_path, messID, voice_duration);
    }

    @Override
    public String toString() {
        return "ChatMessage{" +
                "type=" + type +
                ", status=" + status +
                ", From=" + From +
                ", conv_type=" + ChatBothUserType +
                ", imageHeight=" + imageHeight +
                ", imageWeight=" + imageWeight +
                ", text='" + text + '\'' +
                ", ConversationId='" + ConversationId + '\'' +
                ", uid='" + uid + '\'' +
                ", sender='" + sender + '\'' +
                ", local_path='" + local_path + '\'' +
                ", url='" + url + '\'' +
                ", messID='" + messID + '\'' +
                ", chatImage='" + chatImage + '\'' +
                ", userName='" + userName + '\'' +
                ", groupID='" + groupID + '\'' +
                ", create_time=" + create_time + "  " + FriendTime.timeStringFromUNIX(create_time) +
                ", receiptTimestamp=" + receiptTimestamp +
                ", isread=" + isread +
                ", isSelfSend=" + isSelfSend +
                ", voice_duration=" + voice_duration +
                '}';
    }
}
