package com.tizi.quanzi.model;

import com.tizi.quanzi.tool.FriendTime;
import com.tizi.quanzi.tool.StaticField;
import com.tizi.quanzi.tool.Tool;

/**
 * Created by qixingchen on 15/8/12.
 * 聊天信息
 * 转换
 *
 * @see com.tizi.quanzi.chat.ChatMessFormatFromAVIM
 */
public class ChatMessage {

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
            contentText = "收到了一张图片";
        } else if (chatMessage.type == StaticField.ChatContantType.VOICE) {
            contentText = "收到了一段录音";
        } else if (chatMessage.type == StaticField.ChatContantType.VEDIO) {
            contentText = "收到了一段视频";
        } else if (chatMessage.type == StaticField.ChatContantType.Location) {
            contentText = "收到了一个位置";
        } else {
            contentText = chatMessage.text;
        }
        return contentText;
    }

    @Override
    public String toString() {
        return "ChatMessage{" +
                "type=" + type +
                ", status=" + status +
                ", From=" + From +
                ", ChatBothUserType=" + ChatBothUserType +
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
