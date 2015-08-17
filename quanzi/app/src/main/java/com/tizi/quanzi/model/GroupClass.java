package com.tizi.quanzi.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.tizi.quanzi.gson.Login;

import java.io.Serializable;
import java.util.List;

/**
 * Created by qixingchen on 15/7/16.
 */
public class GroupClass implements Parcelable {
    public String groupName;
    public Uri groupFace;
    public String groupID;
    public String groupType;

    public static GroupClass[] getGroupByEntity(List<Login.GroupEntity> groupEntityList) {
        int length = groupEntityList.size();
        GroupClass[] groupClasses = new GroupClass[length];
        for (int i = 0; i < length; i++) {
            groupClasses[i] = getGroupByEntity(groupEntityList.get(i));
        }
        return groupClasses;
    }

    public GroupClass(String groupName, Uri groupFace, String groupID, String groupType) {
        this.groupName = groupName;
        this.groupFace = groupFace;
        this.groupID = groupID;
        this.groupType = groupType;
    }

    public static GroupClass getGroupByEntity(Login.GroupEntity groupEntity){
        GroupClass groupClass = new GroupClass();
        groupClass.groupID = groupEntity.getId();
        groupClass.groupName = groupEntity.getGroupName();
        groupClass.groupFace = Uri.parse(groupEntity.getIcon());
        groupClass.groupType = groupEntity.getType();
        return groupClass;
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
