package com.tizi.quanzi.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.tizi.quanzi.tool.StaticField;

import java.io.Serializable;

/**
 * Created by qixingchen on 15/8/25.
 * 系统消息
 * 相关attr name {@link com.tizi.quanzi.tool.StaticField.SystemMessAttrName}
 */
public class SystemMessage implements Parcelable, Serializable {
    private String id; //消息ID
    private String convid;//会话id, 针对邀请加入某个圈子之类的，需要根据当前会话id加入
    private String user_id; //发送系统消息方
    private String user_icon;//发送系统消息方的头像，冗余，可以为空，如系统主动发送的消息可能会有个系统图标，此时这个为空。
    private String user_name;//发送系统消息的名称，冗余，可以为空，如a邀请b加入某个圈子，user_name = a。系统发送的可以为空或“客服小秘书”
    private String msg_type;//消息类型：系统公告、邀请加入圈子等消息类型
    private String content;//消息内容：如 xxx邀请您加入xx圈子
    private String remark;//消息中附带的邀请者信息：比如附带消息是“我是xxx，快来加入啊”
    private String link_url;//可能是链接消息，可以通过网址打开网站
    /** 系统消息标识 {@link com.tizi.quanzi.tool.StaticField.SystemMessAttrName.systemFlag} */
    private int sys_msg_flag;
    /** 处理状态 {@link StaticField.SystemMessAttrName.statueCode} */
    private int status;

    private boolean isread;//是否已读
    private String group_id;//邀请加入的圈子ID
    private long create_time;

    @Override
    public String toString() {
        return "SystemMessage{" +
                "id='" + id + '\'' +
                ", convid='" + convid + '\'' +
                ", user_id='" + user_id + '\'' +
                ", user_icon='" + user_icon + '\'' +
                ", user_name='" + user_name + '\'' +
                ", msg_type='" + msg_type + '\'' +
                ", content='" + content + '\'' +
                ", remark='" + remark + '\'' +
                ", link_url='" + link_url + '\'' +
                ", sys_msg_flag=" + sys_msg_flag +
                ", status=" + status +
                ", isread=" + isread +
                ", group_id='" + group_id + '\'' +
                ", create_time=" + create_time +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getConvid() {
        return convid;
    }

    public void setConvid(String convid) {
        this.convid = convid;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_icon() {
        return user_icon;
    }

    public void setUser_icon(String user_icon) {
        this.user_icon = user_icon;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getMsg_type() {
        return msg_type;
    }

    public void setMsg_type(String msg_type) {
        this.msg_type = msg_type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getLink_url() {
        return link_url;
    }

    public void setLink_url(String link_url) {
        this.link_url = link_url;
    }

    public int getSys_msg_flag() {
        return sys_msg_flag;
    }

    public void setSys_msg_flag(int sys_msg_flag) {
        this.sys_msg_flag = sys_msg_flag;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isread() {
        return isread;
    }

    public void setIsread(boolean isread) {
        this.isread = isread;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.convid);
        dest.writeString(this.user_id);
        dest.writeString(this.user_icon);
        dest.writeString(this.user_name);
        dest.writeString(this.msg_type);
        dest.writeString(this.content);
        dest.writeString(this.remark);
        dest.writeString(this.link_url);
        dest.writeInt(this.sys_msg_flag);
        dest.writeInt(this.status);
        dest.writeByte(isread ? (byte) 1 : (byte) 0);
        dest.writeString(this.group_id);
        dest.writeLong(this.create_time);
    }

    public SystemMessage() {
    }

    protected SystemMessage(Parcel in) {
        this.id = in.readString();
        this.convid = in.readString();
        this.user_id = in.readString();
        this.user_icon = in.readString();
        this.user_name = in.readString();
        this.msg_type = in.readString();
        this.content = in.readString();
        this.remark = in.readString();
        this.link_url = in.readString();
        this.sys_msg_flag = in.readInt();
        this.status = in.readInt();
        this.isread = in.readByte() != 0;
        this.group_id = in.readString();
        this.create_time = in.readLong();
    }

    public static final Parcelable.Creator<SystemMessage> CREATOR = new Parcelable.Creator<SystemMessage>() {
        public SystemMessage createFromParcel(Parcel source) {
            return new SystemMessage(source);
        }

        public SystemMessage[] newArray(int size) {
            return new SystemMessage[size];
        }
    };
}
