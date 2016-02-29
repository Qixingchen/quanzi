package com.tizi.quanzi.model;

import com.tizi.chatlibrary.model.group.TempGroupClass;
import com.tizi.quanzi.gson.BoomGroup;
import com.tizi.quanzi.gson.GroupAllInfo;
import com.tizi.quanzi.tool.StaticField;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qixingchen on 15/10/10.
 * 碰撞群
 * 对应 Gson {@link BoomGroup}
 */
public class BoomGroupClass extends TempGroupClass {

    public List<GroupAllInfo.MemberEntity> groupMenber1;
    public List<GroupAllInfo.MemberEntity> groupMenber2;
    public boolean isGroup1MyGroup;
    // TODO: 15/10/10 add theme ID
    public String themeID;

    public static ArrayList<BoomGroupClass> getBoomGroupListFromBoomGroupGson(
            List<BoomGroup.GroupmatchEntity> boomGroups, String themeID) {
        ArrayList<BoomGroupClass> ans = new ArrayList<>();
        for (BoomGroup.GroupmatchEntity boomGroup : boomGroups) {
            ans.add(getBoomGroupFromBoomGroupGson(boomGroup, themeID));
        }
        return ans;
    }

    public static BoomGroupClass getBoomGroupFromBoomGroupGson(BoomGroup.GroupmatchEntity boomGroup, String themeID) {
        BoomGroupClass temp = new BoomGroupClass();
        temp.setConvId(boomGroup.convId);
        temp.setType(StaticField.ConvType.BOOM_GROUP);
        temp.setID(boomGroup.id);
        temp.setLastMess("");
        temp.setLastMessTime(0);
        temp.setGroupId1(boomGroup.groupId1);
        temp.setGroupId2(boomGroup.groupId2);
        temp.setGroupName1(boomGroup.groupName1);
        temp.setGroupName2(boomGroup.groupName2);
        temp.setIcon1(boomGroup.icon1);
        temp.setIcon2(boomGroup.icon2);
        temp.groupMenber1 = boomGroup.groupMenber1;
        temp.groupMenber2 = boomGroup.groupMenber2;
        temp.isGroup1MyGroup = isMyGroup(temp.getGroupId1());
        temp.themeID = themeID;
        return temp;
    }

}
