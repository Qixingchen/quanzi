package com.tizi.quanzi.dataStatic;

import android.support.annotation.Nullable;

import com.tizi.quanzi.database.DBAct;
import com.tizi.quanzi.model.ChatMessage;

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

    /**
     * 设置未读消息
     *
     * @param convID  需查询的组的ID
     * @param GroupID 需要查询的groupID
     */
    public abstract void setUnreadMessage(String convID, String GroupID);


    /**
     * 获取组列表
     */
    public ArrayList<T> getGroupList() {
        return groupList;
    }

    /**
     * 重设整个组
     */
    public void setGroupList(List<T> newGroupList) {
        synchronized (groupList) {
            groupList.clear();
            groupList.addAll(newGroupList);
        }
        for (T t : groupList) {
            setUnreadMessage(t.convId, t.ID);
            DBAct.getInstance().addOrReplaceChatGroup(t);
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
        if (id == null) {
            return null;
        }
        synchronized (groupList) {
            for (T group : groupList) {
                if (group.ID == null) {
                    return null;
                }
                if (group.ID.compareTo(id) == 0) {
                    return group;
                }
            }
        }
        return null;
    }

    /**
     * 从组的ConvID获得groupID
     */
    @Deprecated
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
     * 从组的ConvID获得group
     */
    @Nullable
    public T getGroupByConvID(String convID) {
        synchronized (groupList) {
            for (T group : groupList) {
                if (group.convId != null && group.convId.equals(convID)) {
                    return group;
                }
            }
        }
        return null;
    }

    /**
     * 添加一个组,如果同 ID 已存在，则忽略
     */
    public void addGroup(T group) {
        if (getGroup(group.ID) != null) {
            return;
        }
        setUnreadMessage(group.convId, group.ID);
        synchronized (groupList) {
            groupList.add(group);
            setUnreadMessage(group.convId, group.ID);
        }
        sort();
        noticeAllCallBack();
        DBAct.getInstance().addOrReplaceChatGroup(group);
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
            T group = getGroupByConvID(convID);
            if (group != null) {
                group.lastMess = lastMess;
                group.lastMessTime = lastTime;
                isUpdated = true;
            }
        }
        if (isUpdated) {
            sort();
            noticeAllCallBack();
        }
        return isUpdated;
    }

    /**
     * 添加未读消息
     *
     * @param convID convID
     * @param messID 未读的messID
     *
     * @return 是否被添加了
     */
    public boolean addUnreadMessIDByConvID(String convID, String messID) {
        T group = getGroupByConvID(convID);
        if (group != null && group.addUnreadMessageID(messID)) {
            noticeAllCallBack();
            return true;
        }
        return false;
    }

    public boolean addUnreadMess(ChatMessage message) {
        return addUnreadMessIDByConvID(message.ConversationId, message.messID);
    }

    /**
     * 移除未读消息
     *
     * @param convID convID
     * @param messID 需要被移除的messID
     *
     * @return 是否被移除了
     */
    public boolean removeUnreadMessIDByConvID(String convID, String messID) {
        T group = getGroupByConvID(convID);
        if (group != null && group.removeUnreadMessad(messID)) {
            noticeAllCallBack();
            return true;
        }
        return false;
    }

    public boolean removeUnreadMess(ChatMessage message) {
        return removeUnreadMessIDByConvID(message.ConversationId, message.messID);
    }

    /**
     * 移除所有未读
     *
     * @param convID
     *
     * @return 是否有对应的群
     */
    public boolean removeAllUnread(String convID) {
        T group = getGroupByConvID(convID);
        if (group != null) {
            group.removeAllUnread();
            noticeAllCallBack();
            return true;
        }
        return false;
    }

    /**
     * 在抽象类移除未读消息
     *
     * @return {@code false} 如果所有的返回都是false
     *
     * @see #removeUnreadMessIDByConvID(String, String)
     */
    public boolean removeUnreadMessIDByConvIDFromAbs(String convID, String messID) {
        if (GroupList.getInstance().removeUnreadMessIDByConvID(convID, messID)) {
            return true;
        }
        if (BoomGroupList.getInstance().removeUnreadMessIDByConvID(convID, messID)) {
            return true;
        }
        if (PrivateMessPairList.getInstance().removeUnreadMessIDByConvID(convID, messID)) {
            return true;
        }
        return false;
    }

    /**
     * 在抽象类移除未读消息
     *
     * @return {@code false} 如果所有的返回都是false
     *
     * @see #removeUnreadMess(ChatMessage)
     */
    public boolean removeUnreadMessFromAbs(ChatMessage message) {
        if (GroupList.getInstance().removeUnreadMess(message)) {
            return true;
        }
        if (BoomGroupList.getInstance().removeUnreadMess(message)) {
            return true;
        }
        if (PrivateMessPairList.getInstance().removeUnreadMess(message)) {
            return true;
        }
        return false;
    }

    /**
     * 在抽象类移除所有未读消息
     *
     * @return {@code false} 如果所有的返回都是false
     *
     * @see #removeAllUnread(String)
     */
    public boolean removeAllUnreadFromAbs(String ConvID) {
        if (GroupList.getInstance().removeAllUnread(ConvID)) {
            return true;
        }
        if (BoomGroupList.getInstance().removeAllUnread(ConvID)) {
            return true;
        }
        if (PrivateMessPairList.getInstance().removeAllUnread(ConvID)) {
            return true;
        }
        return false;
    }

    /**
     * 在抽象类添加未读消息
     *
     * @return {@code false} 如果所有的返回都是false
     *
     * @see #addUnreadMess(ChatMessage)
     */
    public boolean addUnreadMessFromAbs(ChatMessage message) {
        if (GroupList.getInstance().addUnreadMess(message)) {
            return true;
        }
        if (BoomGroupList.getInstance().addUnreadMess(message)) {
            return true;
        }
        if (PrivateMessPairList.getInstance().addUnreadMess(message)) {
            return true;
        }
        return false;
    }

    /**
     * 通知所有回调
     */
    protected abstract void noticeAllCallBack();

}
