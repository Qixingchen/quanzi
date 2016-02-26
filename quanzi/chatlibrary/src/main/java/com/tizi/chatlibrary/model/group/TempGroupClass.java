package com.tizi.chatlibrary.model.group;

import android.databinding.Bindable;

/**
 * Created by qixingchen on 15/10/10.
 * 碰撞群
 */
public class TempGroupClass extends ConvGroupAbs {

    private String groupId1;
    private String groupId2;
    private String groupName1;
    private String groupName2;
    private String icon1;
    private String icon2;

    @Bindable
    public String getGroupId1() {
        return groupId1;
    }

    public void setGroupId1(String groupId1) {
        this.groupId1 = groupId1;
    }

    @Bindable
    public String getGroupId2() {
        return groupId2;
    }

    public void setGroupId2(String groupId2) {
        this.groupId2 = groupId2;
    }

    @Bindable
    public String getGroupName1() {
        return groupName1;
    }

    public void setGroupName1(String groupName1) {
        this.groupName1 = groupName1;
    }

    @Bindable
    public String getGroupName2() {
        return groupName2;
    }

    public void setGroupName2(String groupName2) {
        this.groupName2 = groupName2;
    }

    @Bindable
    public String getIcon1() {
        return icon1;
    }

    public void setIcon1(String icon1) {
        this.icon1 = icon1;
    }

    @Bindable
    public String getIcon2() {
        return icon2;
    }

    public void setIcon2(String icon2) {
        this.icon2 = icon2;
    }

    @Bindable
    public boolean isGroup1MyGroup() {
        return isMyGroup(groupId1);
    }
}
