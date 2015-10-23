package com.tizi.quanzi.dataStatic;

import com.tizi.quanzi.database.DBAct;
import com.tizi.quanzi.model.SystemMessage;
import com.tizi.quanzi.model.SystemMessagePair;
import com.tizi.quanzi.otto.BusProvider;

import java.util.List;

/**
 * Created by qixingchen on 15/10/22.
 * 系统消息组
 */
public class SystemMessageList extends ConvGroupAbsList<SystemMessagePair> {

    private static SystemMessageList mInstance;

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
     * 获取未读数量
     *
     * @param convID 需查询的组的ID
     *
     * @return 未读数量
     */
    @Override
    public int getUnreadCount(String convID, String groupID) {
        SystemMessagePair pair = getGroup(groupID);
        if (pair == null) {
            return 0;
        }
        return pair.systemMessage.isread ? 0 : 1;
    }

    /**
     * 通知所有回调
     */
    @Override
    protected void noticeAllCallBack() {
        BusProvider.getInstance().post(this);
    }

    /**
     * 获取未读总数量
     */
    public int getAllUnreadCount() {
        int ans = 0;
        synchronized (groupList) {
            for (SystemMessagePair pair : groupList) {
                if (!pair.systemMessage.isread) {
                    ans += 1;
                }
            }
        }
        return ans;
    }
}
