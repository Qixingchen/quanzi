package com.tizi.quanzi.dataStatic;

import com.tizi.quanzi.database.DBAct;
import com.tizi.quanzi.model.PrivateMessPair;
import com.tizi.quanzi.model.SystemMessage;
import com.tizi.quanzi.tool.StaticField;

import java.util.List;

/**
 * Created by qixingchen on 15/9/3.
 * 私信对列表
 */
public class PrivateMessPairList extends ConvGroupAbsList<PrivateMessPair> {

    private static PrivateMessPairList mInstance;

    public static PrivateMessPairList getInstance() {
        if (mInstance == null) {
            synchronized (PrivateMessPairList.class) {
                if (mInstance == null) {
                    mInstance = new PrivateMessPairList();
                }
            }
        }
        return mInstance;
    }

    @Override
    public int getUnreadCount(String convID) {
        // TODO: 15/9/9 get Unread count
        if (convID == null || convID.compareTo("") == 0) {
            return 0;
        } else {
            return DBAct.getInstance().quaryUnreadCount(convID);
        }
    }

    /**
     * 从数据库加载原有的消息
     */
    public void getGroupsFromDataBase() {
        List<SystemMessage> systemMessges = DBAct.getInstance().quaryAllSysMess();
        setGroupList(PrivateMessPair.PriMessesFromSystemMesses(systemMessges));
        // TODO: 15/9/8 私聊添加
    }

    /**
     * 获取未读数量，每组最多按1计算
     */
    public int getAllUnreadCount() {
        int ans = 0;
        synchronized (groupList) {
            for (PrivateMessPair pair : groupList) {
                if (pair.Type == StaticField.PrivateMessOrSysMess.SysMess) {
                    ans += pair.systemMessage.isread() ? 0 : 1;
                } else {
                    ans += pair.UnreadCount == 0 ? 0 : 1;
                }
            }
        }
        return ans;
    }
}
