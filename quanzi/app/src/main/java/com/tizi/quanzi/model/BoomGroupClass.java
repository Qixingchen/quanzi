package com.tizi.quanzi.model;

import com.tizi.quanzi.dataStatic.ConvGroupAbs;
import com.tizi.quanzi.dataStatic.GroupList;
import com.tizi.quanzi.gson.BoomGroup;
import com.tizi.quanzi.tool.StaticField;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qixingchen on 15/10/10.
 * 碰撞群
 * 对应 Gson {@link BoomGroup}
 */
public class BoomGroupClass extends ConvGroupAbs {

    public String groupId1;
    public String groupId2;
    public String groupName1;
    public String groupName2;
    public String icon1;
    public String icon2;
    public List<BoomGroup.GroupmatchEntity.GrpmemEntity> groupMenber1;
    public List<BoomGroup.GroupmatchEntity.GrpmemEntity> groupMenber2;
    public boolean isGroup1MyGroup;
    // TODO: 15/10/10 add theme ID

    public static ArrayList<BoomGroupClass> getBoomGroupListFromBoomGroupGson(
            List<BoomGroup.GroupmatchEntity> boomGroups) {
        ArrayList<BoomGroupClass> ans = new ArrayList<>();
        for (BoomGroup.GroupmatchEntity boomGroup : boomGroups) {
            BoomGroupClass temp = new BoomGroupClass();
            temp.convId = boomGroup.convId;
            temp.Type = StaticField.ConvType.BoomGroup;
            temp.ID = boomGroup.id;
            temp.UnreadCount = 0;
            temp.lastMess = "";
            temp.lastMessTime = 0;
            temp.groupId1 = boomGroup.groupId1;
            temp.groupId2 = boomGroup.groupId2;
            temp.groupName1 = boomGroup.groupName1;
            temp.groupName2 = boomGroup.groupName2;
            temp.icon1 = boomGroup.icon1;
            temp.icon2 = boomGroup.icon2;
            temp.groupMenber1 = boomGroup.groupMenber1;
            temp.groupMenber2 = boomGroup.groupMenber2;
            temp.isGroup1MyGroup = isMyGroup(temp.groupId1);
            ans.add(temp);
        }
        return ans;
    }

    /**
     * 判断是不是自己的群
     *
     * @param GroupID 群号
     *
     * @return true：是自己的圈子
     */
    private static boolean isMyGroup(String GroupID) {
        return GroupList.getInstance().getGroup(GroupID) != null;
    }

}
