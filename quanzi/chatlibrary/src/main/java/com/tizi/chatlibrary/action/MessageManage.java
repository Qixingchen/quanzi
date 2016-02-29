package com.tizi.chatlibrary.action;

import com.tizi.chatlibrary.model.group.ConvGroupAbs;
import com.tizi.chatlibrary.model.message.ChatMessage;
import com.tizi.chatlibrary.model.message.CommentNotifyMessage;
import com.tizi.chatlibrary.model.message.SystemMessage;
import com.tizi.chatlibrary.staticData.GroupList;

import java.util.List;

/**
 * Created by qixingchen on 16/2/26.
 * <p/>
 * 消息的处理
 */
public class MessageManage {

    public static void onMessage(ChatMessage chatMessage) {
        DatabaseAction.addOrReplaceChatMessage(chatMessage);
        GroupList.getInstance().addUnreadMess(chatMessage);
    }

    public static void setMessageRead(ChatMessage chatMessage, boolean isRead) {
        chatMessage.setRead(isRead);
        DatabaseAction.addOrReplaceChatMessage(chatMessage);
        if (isRead) {
            GroupList.getInstance().removeUnreadMess(chatMessage);
        }
    }

    public static void setMessageRead(String messID, boolean isRead) {

        ChatMessage chatMessage = DatabaseAction.queryMessageByID(messID);
        if (chatMessage == null) {
            return;
        }
        chatMessage.setRead(isRead);
        DatabaseAction.addOrReplaceChatMessage(chatMessage);
        if (isRead) {
            GroupList.getInstance().removeUnreadMess(chatMessage);
        }
    }

    public static void setAllMessageASRead(String convID) {
        DatabaseAction.setAllAsRead(convID);
        GroupList.getInstance().removeAllUnread(convID);
    }

    public static void deleteMessage(ChatMessage chatMessage) {
        GroupList.getInstance().removeUnreadMess(chatMessage);
        DatabaseAction.deleteMessage(chatMessage.getMessID());

        ConvGroupAbs group = GroupList.getInstance().getGroupByConvID(chatMessage.getConversationId());
        if (group != null) {
            group.updateLastMess();
        }
    }

    public static void deleteAllMess(String convID) {
        setAllMessageASRead(convID);
        DatabaseAction.deleteAllMessage(convID);
        ConvGroupAbs group = GroupList.getInstance().getGroupByConvID(convID);
        if (group != null) {
            group.updateLastMess();
        }
    }

    public static void deleteAllSystemMess() {
        DatabaseAction.deleteAllSystemMessage();
    }

    public static void updateMess(ChatMessage chatMessage) {
        DatabaseAction.addOrReplaceChatMessage(chatMessage);
    }

    public static List<ChatMessage> queryMess(String convID, int start) {
        return DatabaseAction.queryMessage(convID, start);
    }

    public static List<ChatMessage> queryAllPhotoMess(String convID) {
        return DatabaseAction.quaryPhotoMess(convID);
    }

    public static List<SystemMessage> queryAllSystemMess() {
        return DatabaseAction.quaryAllSysMess();
    }

    public static int queryUnreadSystemMessCount() {
        return DatabaseAction.quaryUnreadSysMess();
    }

    public static int queryUnreadCommentCount() {
        return DatabaseAction.quaryUnreadComment();
    }

    public static List<CommentNotifyMessage> queryAllCommentNotifyMessage() {
        return DatabaseAction.quaryAllComment();
    }
}
