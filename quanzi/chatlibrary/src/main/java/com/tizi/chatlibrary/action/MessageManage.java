package com.tizi.chatlibrary.action;

import com.tizi.chatlibrary.model.group.ConvGroupAbs;
import com.tizi.chatlibrary.model.message.ChatMessage;
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

    public static List<ChatMessage> queryMess(String convID, int start) {
        return DatabaseAction.queryMessage(convID, start);
    }
}
