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

    /**
     * 获取未读数量
     *
     * @param convID 需查询的组的ID
     *
     * @return 未读数量
     */
    public abstract int getUnreadCount(String convID);

    /**
     * 添加数据变更时的回调
     * 添加后会立即调用一次
     */
    public ConvGroupAbsList addOnChangeCallBack(OnChangeCallBack onChangeCallBack) {

        onChangeCallBacks.add(onChangeCallBack);
        noticeAllCallBack();
        return this;
    }

    /**
     * 获取组列表
     */
    public ArrayList<T> getGroupList() {
        updateUnreadCount();
        return groupList;
    }

    /**
     * 重设整个组
     */
    public void setGroupList(List<T> newGroupList) {
        synchronized (groupList) {
            groupList.clear();
            groupList.addAll(newGroupList);
            updateUnreadCount();
        }
        sort();
        noticeAllCallBack();
    }

    /**
     * 向所有回调发布更新通知
     */
    public void callUpdate() {
        noticeAllCallBack();
    }

    /**
     * 获取组
     *
     * @param id 组ID
     *
     * @return 组
     */
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

    /**
     * 获取组的ConvID
     */
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

    /**
     * 添加一个组,如果同 ID 已存在，则忽略
     */
    public void addGroup(T group) {
        if (getGroup(group.ID) == null) {
            return;
        }
        synchronized (groupList) {
            groupList.add(group);
            group.UnreadCount = getUnreadCount(group.convId);
        }
        sort();
        noticeAllCallBack();
    }

    /**
     * 删除一个组
     */
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

    /**
     * 更新组，条件是ID相同
     */
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

    /**
     * 排序
     */
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

    /**
     * 更新最后一条消息的信息
     */
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

    /**
     * 更新未读数量
     */
    protected void updateUnreadCount() {
        for (T group : groupList) {
            group.UnreadCount = getUnreadCount(group.convId);
        }
    }

    /**
     * 通知所有回调
     */
    protected void noticeAllCallBack() {
        for (OnChangeCallBack cb : onChangeCallBacks) {
            cb.changed();
        }
    }

    public interface OnChangeCallBack {
        void changed();
    }
}
