package com.tizi.quanzi.model;

import com.tizi.quanzi.tool.FriendTime;
import com.tizi.quanzi.tool.Tool;

/**
 * Created by qixingchen on 15/8/12.
 * 聊天信息
 */
public class ChatMessage {

    public int type, status, From, ChatBothUserType, imageHeight, imageWeight;
    public String text, ConversationId, uid, sender,
            local_path, url, messID, chatImage, userName, groupID;
    public long create_time, receiptTimestamp;
    public boolean isread, isSelfSend;
    public double voice_duration;

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
