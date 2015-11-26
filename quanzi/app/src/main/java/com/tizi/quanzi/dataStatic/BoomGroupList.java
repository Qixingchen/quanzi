package com.tizi.quanzi.dataStatic;

import com.tizi.quanzi.database.DBAct;
import com.tizi.quanzi.model.BoomGroupClass;
import com.tizi.quanzi.otto.BusProvider;

/**
 * Created by qixingchen on 15/10/10.
 * 全局变量组，碰撞群列表
 */
public class BoomGroupList extends ConvGroupAbsList<BoomGroupClass> {

    private static BoomGroupList mInstance;

    public static BoomGroupList getInstance() {
        if (mInstance == null) {
            synchronized (BoomGroupList.class) {
                if (mInstance == null) {
                    mInstance = new BoomGroupList();
                }
            }
        }
        return mInstance;
    }


    /**
     * 获取所有未读数量
     */
    public int getAllUnreadCount() {
        int ans = 0;
        for (BoomGroupClass boom : groupList) {
            ans += boom.getUnreadCount();
        }
        return ans;
    }

    /**
     * 设置未读消息
     *
     * @param convID  需查询的组的ID
     * @param GroupID 需要查询的groupID
     */
    @Override
    public void setUnreadMessage(String convID, String GroupID) {
        BoomGroupClass boom = getGroup(GroupID);
        if (boom != null) {
            boom.addUnreadMessageID(DBAct.getInstance().quaryUnreadList(convID));
        }
    }

    /**
     * 通知所有回调
     */
    @Override
    protected void noticeAllCallBack() {
        BusProvider.getInstance().post(this);
    }
}
