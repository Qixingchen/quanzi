package com.tizi.quanzi.model;

import android.net.Uri;

import com.tizi.quanzi.gson.Login;

import java.io.Serializable;
import java.util.List;

/**
 * Created by qixingchen on 15/7/16.
 */
public class GroupClass implements Serializable {
    public String groupName;
    public Uri groupFace;
    public String groupID;
    public String groupType;

    public static GroupClass[] getGroupByInent(String[] ids, String[] icons,
                                               String[] groupNames, String[] types) {
        int length = ids.length;
        GroupClass[] groupClasses = new GroupClass[length];
        for (int i = 0; i < length; i++) {
            groupClasses[i] = new GroupClass();
            groupClasses[i].groupID = ids[i];
            groupClasses[i].groupName = groupNames[i];
            groupClasses[i].groupFace = Uri.parse(icons[i]);
            groupClasses[i].groupType = types[i];
        }
        return groupClasses;
    }

    public static GroupClass[] getGroupByEntity(List<Login.GroupEntity> groupEntityList) {
        int length = groupEntityList.size();
        GroupClass[] groupClasses = new GroupClass[length];
        for (int i = 0; i < length; i++) {
            groupClasses[i] = new GroupClass();
            groupClasses[i].groupID = groupEntityList.get(i).getId();
            groupClasses[i].groupName = groupEntityList.get(i).getGroupName();
            groupClasses[i].groupFace = Uri.parse(groupEntityList.get(i).getIcon());
            groupClasses[i].groupType = groupEntityList.get(i).getType();
        }
        return groupClasses;
    }
}
