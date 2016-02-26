package com.tizi.chatlibrary.action;

import com.tizi.chatlibrary.model.message.ChatMessage;
import com.tizi.chatlibrary.staticData.GroupList;

/**
 * Created by qixingchen on 16/2/26.
 * <p/>
 * 收到消息后的处理
 */
public class ReceiveMessage {

    public static void onMessage(ChatMessage chatMessage) {
        DatabaseAction.addOrReplaceChatMessage(chatMessage);
        GroupList.getInstance().addUnreadMess(chatMessage);
    }
}
