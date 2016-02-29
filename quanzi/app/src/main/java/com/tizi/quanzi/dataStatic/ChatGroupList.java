package com.tizi.quanzi.dataStatic;

import com.tizi.chatlibrary.model.group.ConvGroupAbs;
import com.tizi.chatlibrary.model.message.ChatMessage;
import com.tizi.chatlibrary.staticData.GroupList;
import com.tizi.quanzi.model.GroupClass;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qixingchen on 16/2/29.
 */
public class ChatGroupList {
    private static ChatGroupList mInstance;

    private List<GroupClass> privateList = new ArrayList<>();

    public ChatGroupList() {
        ArrayList groups = GroupList.getInstance().getGroupList();
        for (Object group : groups) {
            if (((ConvGroupAbs) group).getType() == ChatMessage.CONVERSATION_TYPE_FRIEND_GROUP) {
                privateList.add((ConvGroupAbs) group);
            }
        }

    }

    public static ChatGroupList getInstance() {
        if (mInstance == null) {
            synchronized (ChatGroupList.class) {
                if (mInstance == null) {
                    mInstance = new ChatGroupList();
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
