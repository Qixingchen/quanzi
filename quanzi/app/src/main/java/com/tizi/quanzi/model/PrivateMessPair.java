package com.tizi.quanzi.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.tizi.quanzi.tool.StaticField;

import java.io.Serializable;

/**
 * Created by qixingchen on 15/9/3.
 * 私信对信息
 */
public class PrivateMessPair implements Serializable, Parcelable {
    public String Name;
    public String Face;
    public String ID;
    public int Type;
    public String convId;
    //不要重置的项目
    public int UnreadCount;
    public String lastMess;
    public long lastMessTime;
    //如果是系统消息
    public String MessID;

    public static PrivateMessPair PriMessFromSystemMess(SystemMessage systemMessage) {
        PrivateMessPair privateMessPair = new PrivateMessPair();
        privateMessPair.Type = StaticField.PrivateMessOrSysMess.SysMess;
        privateMessPair.Name = systemMessage.getUser_name();
        privateMessPair.Face = systemMessage.getUser_icon();
        privateMessPair.ID = systemMessage.getId();
        privateMessPair.convId = systemMessage.getConvid();
        privateMessPair.MessID = systemMessage.getId();
        privateMessPair.lastMess = systemMessage.getContent();
        privateMessPair.lastMessTime = systemMessage.getCreate_time();
        privateMessPair.UnreadCount = (systemMessage.getStatus() == StaticField.SystemMessAttrName.statueCode.complete)
                ? 0 : 1;
        return privateMessPair;
    }

    //auto make

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.Name);
        dest.writeString(this.Face);
        dest.writeString(this.ID);
        dest.writeInt(this.Type);
        dest.writeString(this.convId);
        dest.writeInt(this.UnreadCount);
        dest.writeString(this.lastMess);
        dest.writeLong(this.lastMessTime);
    }

    public PrivateMessPair() {
    }

    protected PrivateMessPair(Parcel in) {
        this.Name = in.readString();
        this.Face = in.readString();
        this.ID = in.readString();
        this.Type = in.readInt();
        this.convId = in.readString();
        this.UnreadCount = in.readInt();
        this.lastMess = in.readString();
        this.lastMessTime = in.readLong();
    }

    public static final Parcelable.Creator<PrivateMessPair> CREATOR = new Parcelable.Creator<PrivateMessPair>() {
        public PrivateMessPair createFromParcel(Parcel source) {
            return new PrivateMessPair(source);
        }

        public PrivateMessPair[] newArray(int size) {
            return new PrivateMessPair[size];
        }
    };
}
