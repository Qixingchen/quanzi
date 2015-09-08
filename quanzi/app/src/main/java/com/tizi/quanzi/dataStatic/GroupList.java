package com.tizi.quanzi.dataStatic;

import com.tizi.quanzi.database.DBAct;
import com.tizi.quanzi.model.GroupClass;

/**
 * Created by qixingchen on 15/9/1.
 * 全局变量组，用于储存群组消息
 */
public class GroupList extends ConvGroupAbsList<GroupClass> {

    private static GroupList mInstance;

    @Override
    public int getUnreadCount(String convID) {
        return DBAct.getInstance().quaryUnreadCount(convID);
    }

    public static ConvGroupAbsList getInstance() {
        if (mInstance == null) {
            synchronized (GroupList.class) {
                if (mInstance == null) {
                    mInstance = new GroupList();
                }
            }
        }
        return mInstance;
    }
}
