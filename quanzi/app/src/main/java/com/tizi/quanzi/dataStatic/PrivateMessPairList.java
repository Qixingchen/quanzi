package com.tizi.quanzi.dataStatic;

import com.tizi.chatlibrary.model.group.ConvGroupAbs;
import com.tizi.chatlibrary.model.message.ChatMessage;
import com.tizi.chatlibrary.staticData.GroupList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qixingchen on 16/2/29.
 * 私信消息组
 */
public class PrivateMessPairList {

    private static PrivateMessPairList mInstance;

    private List<ConvGroupAbs> privateList = new ArrayList<>();

    public PrivateMessPairList() {
        ArrayList groups = GroupList.getInstance().getGroupList();
        for (Object group : groups) {
            if (((ConvGroupAbs) group).getType() == ChatMessage.CONVERSATION_TYPE_TWO_PERSION) {
                privateList.add((ConvGroupAbs) group);
            }
        }

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

    public List<ConvGroupAbs> getPrivateList() {
        return privateList;
    }

    public int getAllUnreadCount() {
        int ans = 0;
        for (ConvGroupAbs groupAbs : privateList) {
            ans += groupAbs.getUnreadCount();
        }
        return ans;
    }
}
