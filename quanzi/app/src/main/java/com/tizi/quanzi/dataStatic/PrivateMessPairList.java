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

    /**
     * 设置未读消息
     *
     * @param convID  需查询的组的ID
     * @param GroupID 需要查询的groupID
     */
    @Override
    public void setUnreadMessage(String convID, String GroupID) {
        PrivateMessPair pair = getGroup(GroupID);
        if (pair != null) {
            pair.addUnreadMessageID(DBAct.getInstance().quaryUnreadList(convID));
        }
    }

    @Override
    public void addGroup(PrivateMessPair group) {
        super.addGroup(group);
        DBAct.getInstance().addOrReplacePriMessPair(group);
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
            if (pair.getID() == null || pair.getID().equals("")) {
                return;
            }
            ChatMessage message = DBAct.getInstance().queryNewestMessage(pair.getConvId());
            if (message != null) {
                pair.setLastMess(ChatMessage.getContentText(message));
                pair.setLastMessTime(message.create_time);
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
                ans += pair.getUnreadCount() == 0 ? 0 : 1;
            }
        }
        return ans;
    }
}
