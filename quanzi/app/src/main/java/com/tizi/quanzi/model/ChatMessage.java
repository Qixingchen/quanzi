package com.tizi.quanzi.model;

/**
 * Created by qixingchen on 15/8/12.
 * 聊天信息
 */
public class ChatMessage {

    public int type, status, From;
    public String text, ConversationId, uid, sender, local_path, url, voice_duration, messID, chatImage;
    public long create_time, receiptTimestamp;
    public boolean isread, isSelfSend;

    @Override
    public String toString() {
        return "ChatsMessage{" +
                "type=" + type +
                ", status=" + status +
                ", From=" + From +
                ", text='" + text + '\'' +
                ", ConversationId='" + ConversationId + '\'' +
                ", uid='" + uid + '\'' +
                ", sender='" + sender + '\'' +
                ", local_path='" + local_path + '\'' +
                ", url='" + url + '\'' +
                ", voice_duration='" + voice_duration + '\'' +
                ", messID='" + messID + '\'' +
                ", create_time=" + create_time +
                ", receiptTimestamp=" + receiptTimestamp +
                ", isread=" + isread +
                ", isSelfSend=" + isSelfSend +
                '}';
    }
}
