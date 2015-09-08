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

    @Override
    public int getUnreadCount(String convID) {
        int ans = 0;
        for (PrivateMessPair privateMessPair : groupList) {
            if (privateMessPair.Type == StaticField.PrivateMessOrSysMess.SysMess) {
                SystemMessage systemMessage = DBAct.getInstance().quarySysMess(privateMessPair.MessID);
                ans += systemMessage.getStatus() == StaticField.SystemMessAttrName.statueCode.complete ? 0 : 1;
            } else if (privateMessPair.UnreadCount != 0) {
                ans++;
            }
        }
        return ans;
    }

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

    /**
     * 从数据库加载原有的消息
     */
    public void getGroupsFromDataBase() {
        List<SystemMessage> systemMessges = DBAct.getInstance().quaryAllSysMess();
        setGroupList(PrivateMessPair.PriMessesFromSystemMesses(systemMessges));
        // TODO: 15/9/8 私聊添加
    }
}
