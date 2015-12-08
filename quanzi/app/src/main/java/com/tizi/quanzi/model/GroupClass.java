package com.tizi.quanzi.model;

import com.tizi.quanzi.dataStatic.ConvGroupAbs;
import com.tizi.quanzi.dataStatic.GroupList;
import com.tizi.quanzi.gson.GroupAllInfo;
import com.tizi.quanzi.gson.GroupInviteAns;
import com.tizi.quanzi.gson.Login;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by qixingchen on 15/7/16.
 * 组群
 * 对应 Gson {@link com.tizi.quanzi.gson.Group}
 */
public class GroupClass extends ConvGroupAbs implements Serializable {
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
        groupClass.ID = groupEntity.id;
        groupClass.Name = groupEntity.groupName;
        groupClass.Face = groupEntity.icon;
        groupClass.Type = groupEntity.type;
        groupClass.Notice = groupEntity.notice;
        groupClass.convId = groupEntity.convId;
        groupClass.background = groupEntity.bg;
        groupClass.validation = (groupEntity.validation.compareTo("Y") == 0);
        groupClass.createUser = groupEntity.createUser;
        groupClass.memlist = groupEntity.memlist;
        if (groupOld != null) {
            groupClass.lastMess = groupOld.lastMess;
            groupClass.lastMessTime = groupOld.lastMessTime;
        } else {
            groupClass.lastMessTime = 0;
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
        groupClass.ID = groupans.id;
        groupClass.Name = groupans.groupName;
        groupClass.Face = groupans.icon;
        groupClass.Type = groupans.type;
        groupClass.Notice = groupans.notice;
        groupClass.convId = groupans.convId;
        groupClass.validation = true;
        groupClass.createUser = groupans.createUser;
        groupClass.lastMessTime = 0;
        groupClass.lastMess = "";
        groupClass.background = groupans.bg;
        return groupClass;
    }


}
