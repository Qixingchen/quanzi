package com.tizi.quanzi.model;

import com.tizi.chatlibrary.model.group.ConvGroupAbs;
import com.tizi.chatlibrary.model.message.ChatMessage;
import com.tizi.chatlibrary.staticData.GroupList;
import com.tizi.quanzi.gson.GroupAllInfo;
import com.tizi.quanzi.gson.GroupInviteAns;
import com.tizi.quanzi.gson.Login;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qixingchen on 15/7/16.
 * 组群
 * 对应 Gson {@link com.tizi.quanzi.gson.Group}
 */
public class GroupClass extends ConvGroupAbs {
    public String Notice;
    public String background;
    public boolean validation;
    public String createUser;
    public List<GroupAllInfo.MemberEntity> memlist;

    public GroupClass() {
    }

    /**
     * 将 List<Login.GroupEntity> 转换为 ArrayList<GroupClass>
     *
     * @param groupEntityList 须转换的 List<Login.GroupEntity>
     *
     * @return 转换完成的 ArrayList<GroupClass>
     */
    public static ArrayList<GroupClass> getGroupArrayListByEntityList(List<Login.GroupEntity> groupEntityList) {
        ArrayList<GroupClass> groupClassArrayList = new ArrayList<>();
        for (Login.GroupEntity groupEntity : groupEntityList) {
            groupClassArrayList.add(GroupClass.getGroupByEntity(groupEntity));
        }
        return groupClassArrayList;
    }

    /**
     * 将 Login.GroupEntity 转换为 GroupClass
     *
     * @param groupEntity 须转换的 Login.GroupEntity
     *
     * @return 转换完成的 GroupClass
     */
    public static GroupClass getGroupByEntity(Login.GroupEntity groupEntity) {
        GroupClass groupClass = new GroupClass();
        GroupClass groupOld = (GroupClass) GroupList.getInstance().getGroup(groupEntity.id);
        groupClass.setID(groupEntity.id);
        groupClass.setName(groupEntity.groupName);
        groupClass.setFace(groupEntity.icon);
        groupClass.setType(ChatMessage.CONVERSATION_TYPE_FRIEND_GROUP);
        groupClass.Notice = groupEntity.notice;
        groupClass.setConvId(groupEntity.convId);
        groupClass.background = groupEntity.bg;
        groupClass.validation = (groupEntity.validation.compareTo("Y") == 0);
        groupClass.createUser = groupEntity.createUser;
        groupClass.memlist = groupEntity.memlist;
        if (groupOld != null) {
            groupClass.setLastMess(groupOld.getLastMess());
            groupClass.setLastMessTime(groupOld.getLastMessTime());
        } else {
            groupClass.setLastMessTime(0);
        }

        return groupClass;
    }

    /**
     * 将 GroupAllInfo 转换为 GroupClass
     *
     * @param groupInviteAns 须转换的 GroupInviteAns {@link GroupInviteAns}
     *
     * @return 转换完成的 GroupClass
     */
    public static GroupClass getGroupByGroupInviteAns(GroupInviteAns groupInviteAns) {
        GroupClass groupClass = new GroupClass();
        Login.GroupEntity groupans = groupInviteAns.groups.get(0);
        groupClass.setID(groupans.id);
        groupClass.setName(groupans.groupName);
        groupClass.setFace(groupans.icon);
        groupClass.setType(ChatMessage.CONVERSATION_TYPE_FRIEND_GROUP);
        groupClass.Notice = groupans.notice;
        groupClass.setConvId(groupans.convId);
        groupClass.validation = true;
        groupClass.createUser = groupans.createUser;
        groupClass.setLastMessTime(0);
        groupClass.setLastMess("");
        groupClass.background = groupans.bg;
        return groupClass;
    }


}
