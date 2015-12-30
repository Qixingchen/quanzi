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
        privateMessPair.setName(chatMessage.userName);
        privateMessPair.setFace(chatMessage.chatImage);
        privateMessPair.setID(chatMessage.sender);
        privateMessPair.setType(StaticField.PrivateMessOrSysMess.PrivateMess);
        privateMessPair.setConvId(chatMessage.ConversationId);
        privateMessPair.setLastMess(ChatMessage.getContentText(chatMessage));
        privateMessPair.setLastMessTime(chatMessage.create_time);

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
        privateMessPair.setName(otherUserInfo.userName);
        privateMessPair.setFace(otherUserInfo.icon);
        privateMessPair.setID(otherUserInfo.id);
        privateMessPair.setType(StaticField.PrivateMessOrSysMess.PrivateMess);
        privateMessPair.setConvId(convID);
        privateMessPair.setLastMess("");
        privateMessPair.setLastMessTime(0);

        return privateMessPair;
    }

}
