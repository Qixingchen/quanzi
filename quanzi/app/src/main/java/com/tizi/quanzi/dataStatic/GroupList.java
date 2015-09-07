package com.tizi.quanzi.dataStatic;

import android.support.annotation.Nullable;

import com.tizi.quanzi.model.GroupClass;

import java.lang.ref.WeakReference;
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
    private ArrayList<OnChangeCallBack> onChangeCallBacks = new ArrayList<>();

    public GroupList addOnChangeCallBack(OnChangeCallBack onChangeCallBack) {

        onChangeCallBacks.add(onChangeCallBack);
        return mInstance;
    }

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

    public void callUpdate() {
        noticeAllCallBack();
    }

    @Nullable
    public GroupClass getGroup(String id) {
        synchronized (groupList) {
            for (GroupClass group : groupList) {
                if (group.groupID.compareTo(id) == 0) {
                    return group;
                }
            }
        }
        return null;
    }

    public String getGroupIDByConvID(String convID) {
        synchronized (groupList) {
            for (GroupClass group : groupList) {
                if (group.convId.compareTo(convID) == 0) {
                    return group.groupID;
                }
            }
        }
        return "";
    }

    public void setGroupList(List<GroupClass> newGroupList) {
        synchronized (groupList) {
            groupList.clear();
            groupList.addAll(newGroupList);
        }
        sort();
        noticeAllCallBack();
    }

    public void addGroup(GroupClass group) {
        synchronized (groupList) {
            groupList.add(group);
        }
        sort();
        noticeAllCallBack();
    }

    public boolean deleteGroup(String groupID) {
        synchronized (groupList) {
            for (GroupClass groupclass : groupList) {
                if (groupclass.groupID.compareTo(groupID) == 0) {
                    groupList.remove(groupclass);
                    noticeAllCallBack();
                    return true;
                }
            }
            return false;
        }
    }

    public void updateGroup(GroupClass group) {
        synchronized (groupList) {
            for (GroupClass groupclass : groupList) {
                if (groupclass.groupID.compareTo(group.groupID) == 0) {
                    groupList.remove(groupclass);
                    groupList.add(groupclass);
                }
            }
        }
        sort();
        noticeAllCallBack();
    }

    public void sort() {
        synchronized (groupList) {
            Collections.sort(groupList, new Comparator<GroupClass>() {
                @Override
                public int compare(GroupClass lhs, GroupClass rhs) {
                    if (lhs.lastMessTime == 0 && rhs.lastMessTime == 0) {
                        return 0;
                    }
                    if (lhs.lastMessTime == 0) {
                        return Integer.MAX_VALUE;
                    }
                    if (rhs.lastMessTime == 0) {
                        return Integer.MIN_VALUE;
                    }
                    return (int) -(lhs.lastMessTime - rhs.lastMessTime);
                }
            });
        }
    }

    public boolean updateGroupLastMess(String convID, String lastMess, long lastTime) {
        boolean isUpdated = false;
        synchronized (groupList) {
            for (GroupClass group : groupList) {
                if (group.convId.compareTo(convID) == 0) {
                    group.lastMess = lastMess;
                    group.lastMessTime = lastTime;
                    isUpdated = true;
                    break;
                }
            }
        }
        if (isUpdated) {
            sort();
            noticeAllCallBack();
        }
        return isUpdated;
    }

    private void noticeAllCallBack() {
        for (OnChangeCallBack cb : onChangeCallBacks) {
            cb.changed();
        }
    }

    public interface OnChangeCallBack {
        void changed();
    }
}
