package com.tizi.quanzi.model;

import com.tizi.quanzi.dataStatic.ConvGroupAbs;
import com.tizi.quanzi.gson.OtherUserInfo;
import com.tizi.quanzi.tool.StaticField;

/**
 * Created by qixingchen on 15/9/3.
 * 私信对信息
 */
public class PrivateMessPair extends ConvGroupAbs {
    //如果是系统消息
    public SystemMessage systemMessage;

    public static PrivateMessPair newPrivatePair(ChatMessage chatMessage) {
        PrivateMessPair privateMessPair = new PrivateMessPair();
        privateMessPair.Name = chatMessage.userName;
        privateMessPair.Face = chatMessage.chatImage;
        privateMessPair.ID = chatMessage.sender;
        privateMessPair.Type = StaticField.PrivateMessOrSysMess.PrivateMess;
        privateMessPair.convId = chatMessage.ConversationId;
        privateMessPair.UnreadCount = 1;
        privateMessPair.lastMess = ChatMessage.getContentText(chatMessage);
        privateMessPair.lastMessTime = chatMessage.create_time;

        return privateMessPair;
    }

    /**
     * 新建私信列表
     *
     * @param otherUserInfo 对方信息
     * @param convID        convID
     *
     * @return PrivateMessPair
     */
    public static PrivateMessPair newPrivatePair(OtherUserInfo otherUserInfo, String convID) {
        PrivateMessPair privateMessPair = new PrivateMessPair();
        privateMessPair.Name = otherUserInfo.userName;
        privateMessPair.Face = otherUserInfo.icon;
        privateMessPair.ID = otherUserInfo.id;
        privateMessPair.Type = StaticField.PrivateMessOrSysMess.PrivateMess;
        privateMessPair.convId = convID;
        privateMessPair.UnreadCount = 0;
        privateMessPair.lastMess = "";
        privateMessPair.lastMessTime = 0;

        return privateMessPair;
    }

}
