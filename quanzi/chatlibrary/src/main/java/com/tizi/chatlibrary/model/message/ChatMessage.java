package com.tizi.chatlibrary.model.message;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.annotation.IntDef;

import java.io.Serializable;

/**
 * Created by qixingchen on 16/2/23.
 * 基础消息(带文本)
 */
public class ChatMessage extends BaseObservable implements Serializable {

    public static final int MESSAGE_TYPE_TEXT = 1;
    public static final int MESSAGE_TYPE_IMAGE = 2;
    public static final int MESSAGE_TYPE_VOICE = 3;
    public static final int MESSAGE_TYPE_VEDIO = 4;
    public static final int MESSAGE_TYPE_LOCATION = 5;
    public static final int MESSAGE_TYPE_NOTIFI = 6;

    public static final int STATUS_SENDING = 1;
    public static final int STATUS_SENT = 2;
    public static final int STATUS_RECEVIED = 3;
    public static final int STATUS_FAILED = 4;

    public static final int FROM_ME = 1;
    public static final int FROM_GROUP_FRIEND = 2;
    public static final int FROM_OTHER_USER = 3;
    public static final int FROM_SYSTEM = 4;

    public static final int CONVERSATION_TYPE_TWO_PERSION = 0;
    public static final int CONVERSATION_TYPE_FRIEND_GROUP = 1;
    public static final int CONVERSATION_TYPE_TEMP_GROUP = 2;
    public static final int CONVERSATION_TYPE_CHAT_ROOM = 3;
    public static final int CONVERSATION_TYPE_SYSTEM = 4;
    public static final int CONVERSATION_TYPE_DYN_COMMENT = 5;
    /**
     * 消息的类型
     *
     * @see messageTypeDef
     */
    private int messageType;
    /**
     * 消息的状态
     *
     * @see statusDef
     */
    private int status;
    /**
     * 消息来源
     *
     * @see messFromDef
     */
    private int from;
    /**
     * 对话类型
     *
     * @see conversationTypeDef
     */
    private int conversationType;
    private String messID;/*消息ID*/
    private String conversationId;/*会话ID*/
    private String senderID;/*发送者ID*/
    private String senderName;/*发送者名称*/
    private String senderIcon;/*发送者头像*/
    private String senderGroupID;/*发送者以哪个群为名义发的*/
    private String chatText;/*消息的文本*/
    private long createTime;/*消息产生时间*/
    private long receiptTime;/*消息被接受时间*/
    /**
     * 是否已读
     * <p/>
     * 本机发的永远返回已读
     */
    private boolean isRead;

    /**
     * 获取新的聊天消息,用于发送前的临时消息
     */
    public static void setChatMessage(ChatMessage chatMessage, String userID, int type, int ChatBothUserType,
                                      String text, String conversationId, String messID,
                                      long createTime, String senderName, String senderIcon,
                                      String groupID) {
        chatMessage.senderID = userID;
        chatMessage.isRead = true;
        chatMessage.messageType = type;
        chatMessage.conversationType = ChatBothUserType;
        chatMessage.chatText = text;
        chatMessage.conversationId = conversationId;
        chatMessage.messID = messID;
        chatMessage.createTime = createTime;
        chatMessage.status = STATUS_SENDING;
        chatMessage.from = FROM_ME;
        chatMessage.senderName = senderName;
        chatMessage.senderIcon = senderIcon;
        chatMessage.senderGroupID = groupID;
    }

    public static
    @ChatMessage.statusDef
    int getMessageStatusType(int avimType) {
        switch (avimType) {
            case STATUS_SENDING:
                return STATUS_SENDING;
            case STATUS_SENT:
                return STATUS_SENT;
            case STATUS_RECEVIED:
                return STATUS_RECEVIED;
            case STATUS_FAILED:
                return STATUS_FAILED;
            default:
                return STATUS_FAILED;
        }
    }

    public static
    @ChatMessage.conversationTypeDef
    int getMessageConversationType(int avimType) {
        switch (avimType) {
            case CONVERSATION_TYPE_TWO_PERSION:
                return CONVERSATION_TYPE_TWO_PERSION;
            case CONVERSATION_TYPE_FRIEND_GROUP:
                return CONVERSATION_TYPE_FRIEND_GROUP;
            case CONVERSATION_TYPE_CHAT_ROOM:
                return CONVERSATION_TYPE_CHAT_ROOM;
            case CONVERSATION_TYPE_SYSTEM:
                return CONVERSATION_TYPE_SYSTEM;
            case CONVERSATION_TYPE_TEMP_GROUP:
                return CONVERSATION_TYPE_TEMP_GROUP;
            default:
                return CONVERSATION_TYPE_TWO_PERSION;
        }
    }

    @Bindable
    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(@messageTypeDef int messageType) {
        this.messageType = messageType;
    }

    @Bindable
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Bindable
    public int getFrom() {
        return from;
    }

    public void setFrom(@messFromDef int from) {
        this.from = from;
    }

    @Bindable
    public int getConversationType() {
        return conversationType;
    }

    public void setConversationType(@conversationTypeDef int conversationType) {
        this.conversationType = conversationType;
    }

    @Bindable
    public String getMessID() {
        return messID;
    }

    public void setMessID(String messID) {
        this.messID = messID;
    }

    @Bindable
    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    @Bindable
    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    @Bindable
    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    @Bindable
    public String getChatText() {
        return chatText;
    }

    public void setChatText(String chatText) {
        this.chatText = chatText;
    }

    @Bindable
    public String getSenderIcon() {
        return senderIcon;
    }

    public void setSenderIcon(String senderIcon) {
        this.senderIcon = senderIcon;
    }

    @Bindable
    public String getSenderGroupID() {
        return senderGroupID;
    }

    public void setSenderGroupID(String senderGroupID) {
        this.senderGroupID = senderGroupID;
    }

    @Bindable
    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    @Bindable
    public long getReceiptTime() {
        return receiptTime;
    }

    public void setReceiptTime(long receiptTime) {
        this.receiptTime = receiptTime;
    }

    @Bindable
    /**
     * 返回是否为本机本用户所发,不是针对用户的所有登陆!
     * */
    public boolean getIsSelfSend() {
        return status != STATUS_RECEVIED;
    }

    @Bindable
    public boolean isRead() {
        return isRead | getIsSelfSend();
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    @IntDef({MESSAGE_TYPE_TEXT, MESSAGE_TYPE_IMAGE, MESSAGE_TYPE_VOICE, MESSAGE_TYPE_VEDIO,
            MESSAGE_TYPE_LOCATION, MESSAGE_TYPE_NOTIFI})
    public @interface messageTypeDef {
    }

    @IntDef({STATUS_SENDING, STATUS_SENT, STATUS_RECEVIED, STATUS_FAILED})
    public @interface statusDef {
    }

    @IntDef({FROM_ME, FROM_GROUP_FRIEND, FROM_OTHER_USER, FROM_SYSTEM})
    public @interface messFromDef {
    }

    @IntDef({CONVERSATION_TYPE_TWO_PERSION, CONVERSATION_TYPE_FRIEND_GROUP, CONVERSATION_TYPE_TEMP_GROUP,
            CONVERSATION_TYPE_CHAT_ROOM, CONVERSATION_TYPE_SYSTEM})
    public @interface conversationTypeDef {
    }
}
