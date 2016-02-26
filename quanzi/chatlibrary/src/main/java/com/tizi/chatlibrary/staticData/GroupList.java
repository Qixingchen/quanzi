package com.tizi.chatlibrary.staticData;

import android.support.annotation.Nullable;

import com.tizi.chatlibrary.action.DatabaseAction;
import com.tizi.chatlibrary.action.GetMessages;
import com.tizi.chatlibrary.model.group.ConvGroupAbs;
import com.tizi.chatlibrary.model.message.ChatMessage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by qixingchen on 16/2/25.
 * 所有群的列表
 */
public class GroupList<T extends ConvGroupAbs> {

    private static GroupList mInstance;
    private final ArrayList<T> groupList = new ArrayList<>();

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

    /**
     * 初始化设置指定群的未读消息
     *
     * @param convID  需查询的组的ID
     * @param GroupID 需要查询的groupID
     */
    public void setUnreadMessage(String convID, String GroupID) {
        T group = getGroup(GroupID);
        if (group != null) {
            group.addUnreadMessageID(GetMessages.getGroupUnreadMessID(convID));
        }
    }


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
            setUnreadMessage(t.getConvId(), t.getID());
            DatabaseAction.addOrReplaceChatGroup(t);
        }

        sort();

    }

    /**
     * 向所有回调发布更新通知
     */
    public void callUpdate() {

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
                if (group.getID() == null) {
                    return null;
                }
                if (group.getID().compareTo(id) == 0) {
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
                if (group.getConvId().compareTo(convID) == 0) {
                    return group.getID();
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
                if (group.getConvId() != null && group.getConvId().equals(convID)) {
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
        if (getGroup(group.getID()) != null) {
            return;
        }
        synchronized (groupList) {
            groupList.add(group);
            setUnreadMessage(group.getConvId(), group.getID());
        }
        sort();

        DatabaseAction.addOrReplaceChatGroup(group);
    }

    /**
     * 删除一个组
     */
    public boolean deleteGroup(String groupID) {
        synchronized (groupList) {
            for (T groupclass : groupList) {
                if (groupclass.getID().compareTo(groupID) == 0) {
                    groupList.remove(groupclass);
                    DatabaseAction.deleteGroup(groupID);
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
                if (groupclass.getID().compareTo(group.getID()) == 0) {
                    groupList.remove(groupclass);
                    groupList.add(group);
                    break;
                }
            }
        }
        sort();

    }

    /**
     * 排序
     */
    public void sort() {
        synchronized (groupList) {
            Collections.sort(groupList, new Comparator<T>() {
                @Override
                public int compare(T lhs, T rhs) {
                    if (lhs.getLastMessTime() == 0 && rhs.getLastMessTime() == 0) {
                        return 0;
                    }
                    if (lhs.getLastMessTime() == 0) {
                        return Integer.MAX_VALUE;
                    }
                    if (rhs.getLastMessTime() == 0) {
                        return Integer.MIN_VALUE;
                    }
                    return (int) (rhs.getLastMessTime() - lhs.getLastMessTime());
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
                group.setLastMess(lastMess);
                group.setLastMessTime(lastTime);
                isUpdated = true;
            }
        }
        if (isUpdated) {
            sort();

        }
        return isUpdated;
    }

    /**
     * 添加未读消息
     *
     * @param message 要添加的未读消息
     *
     * @return 是否被添加了
     */
    public boolean addUnreadMess(ChatMessage message) {
        String convID = message.getConversationId();
        T group = getGroupByConvID(convID);
        if (group == null) {
            switch (message.getConversationType()) {
                case ChatMessage.CONVERSATION_TYPE_TWO_PERSION:
                    ConvGroupAbs groupAbs = new ConvGroupAbs(message.getSenderName(), message.getSenderIcon(),
                            message.getSenderID(), ChatMessage.CONVERSATION_TYPE_TWO_PERSION, message.getConversationId());
                    groupList.add((T) groupAbs);
                    group = (T) groupAbs;
                    break;
                default:
                    return false;
            }
        }

        group.setLastMess(message.getChatText());
        group.setLastMessTime(message.getCreateTime());

        return group.addUnreadMessageID(message.getMessID());
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
            return true;
        }
        return false;
    }

    public boolean removeUnreadMess(ChatMessage message) {
        return removeUnreadMessIDByConvID(message.getConversationId(), message.getMessID());
    }

    /**
     * 移除所有未读
     *
     * @param convID 群的convID
     *
     * @return 是否有对应的群
     */
    public boolean removeAllUnread(String convID) {
        T group = getGroupByConvID(convID);
        if (group != null) {
            group.removeAllUnread();
            return true;
        }
        return false;
    }

}
