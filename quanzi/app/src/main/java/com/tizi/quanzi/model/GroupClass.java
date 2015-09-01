package com.tizi.quanzi.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.tizi.quanzi.dataStatic.GroupList;
import com.tizi.quanzi.database.DBAct;
import com.tizi.quanzi.gson.Login;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by qixingchen on 15/7/16.
 * 组群
 * 对应 Gson {@link com.tizi.quanzi.gson.Group}
 */
public class GroupClass implements Parcelable, Serializable {
    public String groupName;
    public Uri groupFace;
    public String groupID;
    public String groupType;
    public String Notice;
    public String convId;
    public String background;
    public boolean validation;
    public String createUser;
    public String groupNo;
    //不要重置的项目
    public boolean needFlushMess;
    public int UnreadCount;
    public String lastMess;
    public long lastMessTime;// TODO: 15/9/1

    public GroupClass(String groupName, Uri groupFace, String groupID, String groupType, String notice, String convId, String background, boolean validation, String createUser, String groupNo, boolean needFlushMess) {
        this.groupName = groupName;
        this.groupFace = groupFace;
        this.groupID = groupID;
        this.groupType = groupType;
        Notice = notice;
        this.convId = convId;
        this.background = background;
        this.validation = validation;
        this.createUser = createUser;
        this.groupNo = groupNo;
        this.needFlushMess = needFlushMess;
    }

    /**
     * 将 List<Login.GroupEntity> 转换为 ArrayList<GroupClass>
     *
     * @param groupEntityList 须转换的 List<Login.GroupEntity>
     *
     * @return 转换完成的 ArrayList<GroupClass>
     */
    public static ArrayList<GroupClass> getGroupArrayListByEntityList(List<Login.GroupEntity> groupEntityList) {
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
        GroupClass groupOld = GroupList.getInstance().getGroup(groupEntity.getId());
        groupClass.groupID = groupEntity.getId();
        groupClass.groupName = groupEntity.getGroupName();
        groupClass.groupFace = Uri.parse(groupEntity.getIcon());
        groupClass.groupType = groupEntity.getType();
        groupClass.Notice = groupEntity.getNotice();
        groupClass.convId = groupEntity.getConvId();
        groupClass.background = groupEntity.getBg();
        groupClass.validation = groupEntity.getValidation();
        groupClass.createUser = groupEntity.getCreateUser();
        groupClass.groupNo = groupEntity.getGroupNo();
        if (groupOld != null) {
            groupClass.lastMess = groupOld.lastMess;
            groupClass.lastMessTime = groupOld.lastMessTime;
            groupClass.UnreadCount = groupOld.UnreadCount;
            groupClass.needFlushMess = groupOld.needFlushMess;
        } else {
            groupClass.lastMessTime = 0;
            groupClass.UnreadCount = 0;
            groupClass.needFlushMess = true;
        }

        return groupClass;
    }

    public GroupClass() {
    }


    //    Parcelable


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
        dest.writeString(this.Notice);
        dest.writeString(this.convId);
        dest.writeString(this.background);
        dest.writeByte(validation ? (byte) 1 : (byte) 0);
        dest.writeString(this.createUser);
        dest.writeString(this.groupNo);
        dest.writeInt(this.UnreadCount);
        dest.writeString(this.lastMess);
    }

    protected GroupClass(Parcel in) {
        this.groupName = in.readString();
        this.groupFace = in.readParcelable(Uri.class.getClassLoader());
        this.groupID = in.readString();
        this.groupType = in.readString();
        this.Notice = in.readString();
        this.convId = in.readString();
        this.background = in.readString();
        this.validation = in.readByte() != 0;
        this.createUser = in.readString();
        this.groupNo = in.readString();
        this.UnreadCount = in.readInt();
        this.lastMess = in.readString();
    }

    public static final Creator<GroupClass> CREATOR = new Creator<GroupClass>() {
        public GroupClass createFromParcel(Parcel source) {
            return new GroupClass(source);
        }

        public GroupClass[] newArray(int size) {
            return new GroupClass[size];
        }
    };
}
