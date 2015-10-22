package com.tizi.quanzi.dataStatic;

import com.tizi.quanzi.database.DBAct;
import com.tizi.quanzi.model.BoomGroupClass;

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
}
