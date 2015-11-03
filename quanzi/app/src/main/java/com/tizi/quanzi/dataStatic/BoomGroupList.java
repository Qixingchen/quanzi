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
     * 获取未读数量
     *
     * @param convID 需查询的组的ID
     *
     * @return 未读数量
     */
    @Override
    public int getUnreadCount(String convID, String groupID) {
        return DBAct.getInstance().quaryUnreadCount(convID);
    }

    /**
     * 获取所有未读数量
     */
    public int getAllUnreadCount() {
        int ans = 0;
        for (BoomGroupClass boom : groupList) {
            ans += boom.UnreadCount;
        }
        return ans;
    }

    /**
     * 通知所有回调
     */
    @Override
    protected void noticeAllCallBack() {
        BusProvider.getInstance().post(this);
    }
}
