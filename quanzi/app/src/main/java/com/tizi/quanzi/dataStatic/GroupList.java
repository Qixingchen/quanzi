package com.tizi.quanzi.dataStatic;

import com.tizi.quanzi.database.DBAct;
import com.tizi.quanzi.gson.Login;
import com.tizi.quanzi.model.ChatMessage;
import com.tizi.quanzi.model.GroupClass;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qixingchen on 15/9/1.
 * 全局变量组，用于储存群组消息
 */
public class GroupList extends ConvGroupAbsList<GroupClass> {

    private static GroupList mInstance;

    public static ConvGroupAbsList getInstance() {
        if (mInstance == null) {
            synchronized (GroupList.class) {
                if (mInstance == null) {
                    mInstance = new GroupList();
                }
            }
        }
        return mInstance;
    }

    @Override
    public int getUnreadCount(String convID) {
        return DBAct.getInstance().quaryUnreadCount(convID);
    }

    /**
     * 使用 groupEntityList 设置 groupList
     *
     * @param groupEntityList GroupEntity {@link com.tizi.quanzi.gson.Login.GroupEntity}  的List
     */
    public void setGroupListByLoginGroup(List<Login.GroupEntity> groupEntityList) {

        //set group
        ArrayList<GroupClass> groupClassArrayList = GroupClass.
                getGroupArrayListByEntityList(groupEntityList);

        for (GroupClass groupClass : groupClassArrayList) {
            ChatMessage chatMessage = DBAct.getInstance().queryNewestMessage(groupClass.convId);
            if (chatMessage != null) {
                groupClass.lastMessTime = chatMessage.create_time;
                groupClass.lastMess = ChatMessage.getContentText(chatMessage);
            }
            groupClass.setNeedNotifiFromPrefer();
        }

        GroupList.getInstance().setGroupList(groupClassArrayList);
    }
}
