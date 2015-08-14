package com.tizi.quanzi.model;

/**
 * Created by qixingchen on 15/8/12.
 * 聊天信息
 */
public class ChatMessage {

    public int type, status, From;
    public String text, ConversationId, uid, sender, local_path, url, messID, chatImage, userName;
    public long create_time, receiptTimestamp;
    public boolean isread, isSelfSend;
    public double voice_duration;

    @Override
    public String toString() {
        return "ChatMessage{" +
                "type=" + type +
                ", status=" + status +
                ", From=" + From +
                ", text='" + text + '\'' +
                ", ConversationId='" + ConversationId + '\'' +
                ", uid='" + uid + '\'' +
                ", sender='" + sender + '\'' +
                ", url='" + url + '\'' +
                ", messID='" + messID + '\'' +
                ", chatImage='" + chatImage + '\'' +
                ", userName='" + userName + '\'' +
                ", create_time=" + create_time +
                ", receiptTimestamp=" + receiptTimestamp +
                ", isread=" + isread +
                ", isSelfSend=" + isSelfSend +
                ", voice_duration=" + voice_duration +
                '}';
    }
}
