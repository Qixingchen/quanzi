package com.tizi.quanzi.dataStatic;

import com.tizi.quanzi.database.DBAct;
import com.tizi.quanzi.model.SystemMessage;
import com.tizi.quanzi.model.SystemMessagePair;
import com.tizi.quanzi.otto.BusProvider;

import java.util.HashSet;
import java.util.List;

/**
 * Created by qixingchen on 15/10/22.
 * 系统消息组
 */
public class SystemMessageList extends ConvGroupAbsList<SystemMessagePair> {

    private static SystemMessageList mInstance;
    private HashSet<String> unreadMessageIDSet = new HashSet<>();

    public static SystemMessageList getInstance() {
        if (mInstance == null) {
            synchronized (PrivateMessPairList.class) {
                if (mInstance == null) {
                    mInstance = new SystemMessageList();
                }
            }
        }
        return mInstance;
    }

    /**
     * 从数据库加载原有的消息
     */
    public void getGroupsFromDataBase() {
        List<SystemMessage> systemMessages = DBAct.getInstance().quaryAllSysMess();
        setGroupList(SystemMessagePair.SysMessPairsFromSystemMesses(systemMessages));
    }

    /**
     * 设置未读消息
     *
     * @param convID  需查询的组的ID
     * @param GroupID 需要查询的groupID
     */
    @Override
    public void setUnreadMessage(String convID, String GroupID) {
        unreadMessageIDSet.addAll(DBAct.getInstance().quaryAllUnreadSysMess());
    }

    /**
     * 通知所有回调
     */
    @Override
    protected void noticeAllCallBack() {
        try {
            BusProvider.getInstance().post(this);
        } catch (Exception ignore) {
        }
    }

    /**
     * 获取未读总数量
     */
    public int getAllUnreadCount() {
        return unreadMessageIDSet.size();
    }

    public boolean removeUnreadMess(String messID) {
        if (unreadMessageIDSet.remove(messID)) {
            noticeAllCallBack();
            return true;
        }
        return false;
    }

    public boolean addUnreadMess(String messID) {
        if (unreadMessageIDSet.add(messID)) {
            noticeAllCallBack();
            return true;
        }
        return false;
    }

    public void removeAllUnreadMess() {
        unreadMessageIDSet.clear();
        noticeAllCallBack();

    }
}
