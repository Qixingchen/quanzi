package com.tizi.chatlibrary.action;

import com.tizi.chatlibrary.model.group.ConvGroupAbs;
import com.tizi.chatlibrary.model.message.ChatMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qixingchen on 16/2/25.
 * 获取消息
 */
public class GetMessages {

    /**
     * 获取群的所有消息
     *
     * @param groupID 群ID
     */
    public static List<ChatMessage> getGroupMessageByGroupID(String groupID) {
        List<ChatMessage> ans = new ArrayList<>();
        ConvGroupAbs group = DatabaseAction.quaryChatGroup(groupID);
        if (group != null) {
            ans = getGroupMessageByConvID(group.getConvId());
        }
        return ans;
    }

    /**
     * 获取群的所有消息
     *
     * @param convID 群的convID
     */
    public static List<ChatMessage> getGroupMessageByConvID(String convID) {
        return DatabaseAction.queryMessage(convID);

    }

    /**
     * 获取群的未读消息ID
     */
    public static List<String> getGroupUnreadMessID(String convID) {
        return DatabaseAction.quaryUnreadList(convID);
    }

}
