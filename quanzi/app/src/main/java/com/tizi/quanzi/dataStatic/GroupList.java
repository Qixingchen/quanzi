package com.tizi.quanzi.dataStatic;

import android.support.annotation.Nullable;

import com.tizi.quanzi.gson.Group;
import com.tizi.quanzi.model.GroupClass;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by qixingchen on 15/9/1.
 * 全局变量组，用于储存群组消息
 */
public class GroupList {
    private final static ArrayList<GroupClass> groupList = new ArrayList<>();
    private static GroupList mInstance;

    public static GroupList getInstance() {
        if (mInstance == null) {
            synchronized (GroupList.class) {
                if (mInstance == null) {
                    mInstance = new GroupList();
                }
            }
        }
        return mInstance;
    }

    private GroupList() {
    }

    public ArrayList<GroupClass> getGroupList() {
        return groupList;
    }

    @Nullable
    public GroupClass getGroup(String id) {
        synchronized (groupList) {
            for (GroupClass group : groupList) {
                if (group.groupNo.compareTo(id) == 0) {
                    return group;
                }
            }
        }
        return null;
    }

    public void setGroupList(List<GroupClass> newGroupList) {
        synchronized (groupList) {
            groupList.clear();
            groupList.addAll(newGroupList);
        }
        sort();
    }

    public void addGroup(GroupClass group) {
        synchronized (groupList) {
            groupList.add(group);
        }
        sort();
    }

    public boolean deleteGroup(GroupClass group) {
        synchronized (groupList) {
            for (GroupClass groupclass : groupList) {
                if (groupclass.groupNo.compareTo(group.groupID) == 0) {
                    groupList.remove(groupclass);
                    return true;
                }
            }
            return false;
        }
    }

    public void updateGroup(GroupClass group) {
        synchronized (groupList) {
            for (GroupClass groupclass : groupList) {
                if (groupclass.groupNo.compareTo(group.groupID) == 0) {
                    groupList.remove(groupclass);
                    groupList.add(groupclass);
                }
            }
        }
        sort();
    }

    public void sort() {
        synchronized (groupList) {
            Collections.sort(groupList, new Comparator<GroupClass>() {
                @Override
                public int compare(GroupClass lhs, GroupClass rhs) {
                    return (int) (lhs.lastMessTime - rhs.lastMessTime);
                }
            });
        }
    }

    public boolean updateGroupLastMess(String convID, String lastMess, long lastTime) {
        synchronized (groupList) {
            for (GroupClass group : groupList) {
                if (group.convId.compareTo(convID) == 0) {
                    group.lastMess = lastMess;
                    group.lastMessTime = lastTime;
                    return true;
                }
            }
        }
        return false;
    }

}
