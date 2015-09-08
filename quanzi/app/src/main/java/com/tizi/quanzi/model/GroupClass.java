package com.tizi.quanzi.model;

import com.tizi.quanzi.dataStatic.ConvGroupAbs;
import com.tizi.quanzi.dataStatic.GroupList;
import com.tizi.quanzi.gson.GroupUserInfo;
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
        GroupClass groupOld = (GroupClass) GroupList.getInstance().getGroup(groupEntity.getId());
        groupClass.ID = groupEntity.getId();
        groupClass.Name = groupEntity.getGroupName();
        groupClass.Face = groupEntity.getIcon();
        groupClass.Type = groupEntity.getType();
        groupClass.Notice = groupEntity.getNotice();
        groupClass.convId = groupEntity.getConvId();
        groupClass.background = groupEntity.getBg();
        groupClass.validation = groupEntity.isValidation();
        groupClass.createUser = groupEntity.getCreateUser();
        if (groupOld != null) {
            groupClass.lastMess = groupOld.lastMess;
            groupClass.lastMessTime = groupOld.lastMessTime;
            groupClass.UnreadCount = groupOld.UnreadCount;
        } else {
            groupClass.lastMessTime = 0;
            groupClass.UnreadCount = 0;
        }

        return groupClass;
    }

    /**
     * 将 GroupUserInfo 转换为 GroupClass
     *
     * @param groupUserInfo 须转换的 GroupUserInfo {@link GroupUserInfo}
     *
     * @return 转换完成的 GroupClass
     */
    @Deprecated
    public static GroupClass getGroupByGroupUserInfo(GroupUserInfo groupUserInfo, String groupID,
                                                     String convId) {
        GroupClass groupClass = new GroupClass();
        groupClass.ID = groupID;
        groupClass.Name = groupUserInfo.groupName;
        groupClass.Face = groupUserInfo.icon;
        groupClass.Type = Integer.valueOf(groupUserInfo.type);
        groupClass.Notice = "";
        groupClass.convId = convId;
        // TODO: 15/9/3 信息不够！

        return groupClass;
    }

    public GroupClass() {
    }


}
