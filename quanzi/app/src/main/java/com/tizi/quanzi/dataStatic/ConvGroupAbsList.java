package com.tizi.quanzi.dataStatic;

import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by qixingchen on 15/9/8.
 * 通讯组列表，例如群列表，私信、系统通知列表
 */
public abstract class ConvGroupAbsList<T extends ConvGroupAbs> {

    protected final ArrayList<T> groupList = new ArrayList<>();
    protected ArrayList<OnChangeCallBack> onChangeCallBacks = new ArrayList<>();

    public abstract int getUnreadCount(String convID);

    public ConvGroupAbsList addOnChangeCallBack(OnChangeCallBack onChangeCallBack) {

        onChangeCallBacks.add(onChangeCallBack);
        noticeAllCallBack();
        return this;
    }

    public ArrayList<T> getGroupList() {
        updateUnreadCount();
        return groupList;
    }

    public void callUpdate() {
        noticeAllCallBack();
    }

    @Nullable
    public T getGroup(String id) {
        synchronized (groupList) {
            for (T group : groupList) {
                if (group.ID.compareTo(id) == 0) {
                    group.UnreadCount = getUnreadCount(group.convId);
                    return group;
                }
            }
        }
        return null;
    }

    public String getGroupIDByConvID(String convID) {
        synchronized (groupList) {
            for (T group : groupList) {
                if (group.convId.compareTo(convID) == 0) {
                    return group.ID;
                }
            }
        }
        return "";
    }

    public void setGroupList(List<T> newGroupList) {
        synchronized (groupList) {
            groupList.clear();
            groupList.addAll(newGroupList);
            updateUnreadCount();
        }
        sort();
        noticeAllCallBack();
    }

    public void addGroup(T group) {
        synchronized (groupList) {
            groupList.add(group);
            group.UnreadCount = getUnreadCount(group.convId);
        }
        sort();
        noticeAllCallBack();
    }

    public boolean deleteGroup(String groupID) {
        synchronized (groupList) {
            for (T groupclass : groupList) {
                if (groupclass.ID.compareTo(groupID) == 0) {
                    groupList.remove(groupclass);
                    noticeAllCallBack();
                    return true;
                }
            }
            return false;
        }
    }

    public void updateGroup(T group) {
        synchronized (groupList) {
            for (T groupclass : groupList) {
                if (groupclass.ID.compareTo(group.ID) == 0) {
                    groupList.remove(groupclass);
                    groupList.add(group);
                    break;
                }
            }
        }
        sort();
        noticeAllCallBack();
    }

    public void sort() {
        synchronized (groupList) {
            Collections.sort(groupList, new Comparator<T>() {
                @Override
                public int compare(T lhs, T rhs) {
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
            for (T group : groupList) {
                if (group.convId.compareTo(convID) == 0) {
                    group.lastMess = lastMess;
                    group.lastMessTime = lastTime;
                    group.UnreadCount = getUnreadCount(group.convId);
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

    protected void updateUnreadCount() {
        for (T group : groupList) {
            group.UnreadCount = getUnreadCount(group.convId);
        }
    }

    protected void noticeAllCallBack() {
        for (OnChangeCallBack cb : onChangeCallBacks) {
            cb.changed();
        }
    }

    public interface OnChangeCallBack {
        void changed();
    }
}
