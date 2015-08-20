package com.tizi.quanzi.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.tizi.quanzi.gson.Login;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by qixingchen on 15/7/16.
 * 组群
 * 对应 Gson
 *
 * @see com.tizi.quanzi.gson.Group
 */
public class GroupClass implements Parcelable {
    public String groupName;
    public Uri groupFace;
    public String groupID;
    public String groupType;

    /**
     * 将 List<Login.GroupEntity> 转换为 ArrayList<GroupClass>
     *
     * @param groupEntityList 须转换的 List<Login.GroupEntity>
     *
     * @return 转换完成的 ArrayList<GroupClass>
     */
    public static ArrayList<GroupClass> getGroupArrayListByEntityList(
            List<Login.GroupEntity> groupEntityList) {
        ArrayList<GroupClass> groupClassArrayList = new ArrayList<>();
        for (Login.GroupEntity groupEntity : groupEntityList) {
            groupClassArrayList.add(GroupClass.getGroupByEntity(groupEntity));
        }
        return groupClassArrayList;
    }

    /**
     * 将 Login.GroupEntity 转换为 GroupClass
     *
     * @param groupEntity 须转换的 Login.GroupEntity
     *
     * @return 转换完成的 GroupClass
     */
    public static GroupClass getGroupByEntity(Login.GroupEntity groupEntity) {
        GroupClass groupClass = new GroupClass();
        groupClass.groupID = groupEntity.getId();
        groupClass.groupName = groupEntity.getGroupName();
        groupClass.groupFace = Uri.parse(groupEntity.getIcon());
        groupClass.groupType = groupEntity.getType();
        return groupClass;
    }

    public GroupClass(String groupName, Uri groupFace, String groupID, String groupType) {
        this.groupName = groupName;
        this.groupFace = groupFace;
        this.groupID = groupID;
        this.groupType = groupType;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.groupName);
        dest.writeParcelable(this.groupFace, 0);
        dest.writeString(this.groupID);
        dest.writeString(this.groupType);
    }

    public GroupClass() {
    }

    protected GroupClass(Parcel in) {
        this.groupName = in.readString();
        this.groupFace = in.readParcelable(Uri.class.getClassLoader());
        this.groupID = in.readString();
        this.groupType = in.readString();
    }

    public static final Parcelable.Creator<GroupClass> CREATOR = new Parcelable.Creator<GroupClass>() {
        public GroupClass createFromParcel(Parcel source) {
            return new GroupClass(source);
        }

        public GroupClass[] newArray(int size) {
            return new GroupClass[size];
        }
    };


}
