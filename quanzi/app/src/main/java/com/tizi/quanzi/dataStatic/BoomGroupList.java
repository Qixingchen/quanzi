package com.tizi.quanzi.dataStatic;

import com.tizi.quanzi.model.BoomGroupClass;
import com.tizi.quanzi.otto.BusProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qixingchen on 15/10/10.
 * 全局变量组，碰撞群列表
 */
public class BoomGroupList {

    private static BoomGroupList mInstance;

    private List<BoomGroupClass> groupList;

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
     * 获取某一个活动的groups
     */
    public List<BoomGroupClass> getThemeBoomGroup(String themeID) {
        List<BoomGroupClass> ans = new ArrayList<>();
        for (BoomGroupClass boom : groupList) {
            if (boom.themeID.equals(themeID)) {
                ans.add(boom);
            }
        }
        return ans;
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
     * 通知所有回调
     */
    protected void noticeAllCallBack() {
        BusProvider.getInstance().post(this);
    }
}
