package com.tizi.quanzi.dataStatic;

import com.tizi.quanzi.database.DBAct;
import com.tizi.quanzi.model.ChatMessage;
import com.tizi.quanzi.model.PrivateMessPair;
import com.tizi.quanzi.otto.BusProvider;

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
    public void addGroup(PrivateMessPair group) {
        super.addGroup(group);
        DBAct.getInstance().addOrReplacePriMessPair(group);
    }

    @Override
    public int getUnreadCount(String convID, String groupID) {
        if (convID == null || convID.compareTo("") == 0) {
            return 0;
        } else {
            return DBAct.getInstance().quaryUnreadCount(convID);
        }
    }

    /**
     * 通知所有回调
     */
    @Override
    protected void noticeAllCallBack() {
        BusProvider.getInstance().post(this);
    }

    /**
     * 从数据库加载原有的消息
     */
    public void getGroupsFromDataBase() {
        List<PrivateMessPair> temp = DBAct.getInstance().quaryAllPrivateMessPair();
        for (PrivateMessPair pair : temp) {
            if (pair.ID == null || pair.ID.equals("")) {
                return;
            }
            ChatMessage message = DBAct.getInstance().queryNewestMessage(pair.convId);
            if (message != null) {
                pair.lastMess = ChatMessage.getContentText(message);
                pair.lastMessTime = message.create_time;
            }
        }
        setGroupList(temp);
    }

    /**
     * 获取未读数量，每组最多按1计算
     */
    public int getAllUnreadCount() {
        int ans = 0;
        synchronized (groupList) {
            for (PrivateMessPair pair : groupList) {
                ans += pair.UnreadCount == 0 ? 0 : 1;
            }
        }
        return ans;
    }
}
