package com.tizi.chatlibrary.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.annotation.IntDef;

import java.io.Serializable;

/**
 * Created by qixingchen on 16/2/23.
 * 消息的抽象类
 */
public abstract class ChatMessageAbs extends BaseObservable implements Serializable {

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
    private long createTime;/*消息产生时间*/
    private long receiptTime;/*消息被接受时间*/

    /**
     * 是否已读
     * <p/>
     * 本机发的永远返回已读
     */
    private boolean isRead;

    public static final int MESSAGE_TYPE_TEXT = 0x1;
    public static final int MESSAGE_TYPE_IMAGE = 0x2;
    public static final int MESSAGE_TYPE_VOICE = 0x4;
    public static final int MESSAGE_TYPE_VEDIO = 0x8;
    public static final int MESSAGE_TYPE_LOCATION = 0x10;
    public static final int MESSAGE_TYPE_NOTIFI = 0x20;

    @IntDef({MESSAGE_TYPE_TEXT, MESSAGE_TYPE_IMAGE, MESSAGE_TYPE_VOICE, MESSAGE_TYPE_VEDIO,
            MESSAGE_TYPE_LOCATION, MESSAGE_TYPE_NOTIFI})
    public @interface messageTypeDef {
    }

    public static final int STATUS_RECEVIED = 3;
    public static final int STATUS_SENDING = 1;
    public static final int STATUS_SENT = 2;
    public static final int STATUS_FAILED = 4;

    @IntDef({STATUS_SENDING, STATUS_SENT, STATUS_RECEVIED, STATUS_FAILED})
    public @interface statusDef {
    }

    public static final int FROM_ME = 1;
    public static final int FROM_GROUP_FRIEND = 2;
    public static final int FROM_OTHER_USER = 3;
    public static final int FROM_SYSTEM = 4;

    @IntDef({FROM_ME, FROM_GROUP_FRIEND, FROM_OTHER_USER, FROM_SYSTEM})
    public @interface messFromDef {
    }

    public static final int CONVERSATION_TYPE_TWO_PERSION = 1;
    public static final int CONVERSATION_TYPE_FRIEND_GROUP = 2;
    public static final int CONVERSATION_TYPE_CHAT_ROOM = 3;

    @IntDef({CONVERSATION_TYPE_TWO_PERSION, CONVERSATION_TYPE_FRIEND_GROUP, CONVERSATION_TYPE_CHAT_ROOM})
    public @interface conversationTypeDef {
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

    public void setStatus(@statusDef int status) {
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
    public String getSenderIcon() {
        return senderIcon;
    }

    public void setSenderIcon(String senderIcon) {
        this.senderIcon = senderIcon;
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

    public boolean isRead() {
        return isRead | getIsSelfSend();
    }

    public void setRead(boolean read) {
        isRead = read;
    }
}
