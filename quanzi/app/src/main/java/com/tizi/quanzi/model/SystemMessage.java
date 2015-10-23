package com.tizi.quanzi.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.tizi.quanzi.tool.StaticField;

import java.io.Serializable;

/**
 * Created by qixingchen on 15/8/25.
 * 系统消息
 * 相关attr name {@link com.tizi.quanzi.tool.StaticField.SystemMessAttrName}
 * {@link com.tizi.quanzi.tool.StaticField.DynNoticeAttrName}
 */
public class SystemMessage implements Serializable, Parcelable {
    public static final Parcelable.Creator<SystemMessage> CREATOR = new Parcelable.Creator<SystemMessage>() {
        public SystemMessage createFromParcel(Parcel source) {
            return new SystemMessage(source);
        }

        public SystemMessage[] newArray(int size) {
            return new SystemMessage[size];
        }
    };
    public String id; //消息ID
    public String convid;//会话id, 针对邀请加入某个圈子之类的，需要根据当前会话id加入
    public String user_id; //发送系统消息方
    public String user_icon;//发送系统消息方的头像，冗余，可以为空，如系统主动发送的消息可能会有个系统图标，此时这个为空。
    public String user_name;//发送系统消息的名称，冗余，可以为空，如a邀请b加入某个圈子，user_name = a。系统发送的可以为空或“客服小秘书”
    public String msg_type;//消息类型：系统公告、邀请加入圈子等消息类型
    public String content;//消息内容：如 xxx邀请您加入xx圈子
    public String remark;//消息中附带的邀请者信息：比如附带消息是“我是xxx，快来加入啊”
    public String link_url;//可能是链接消息，可以通过网址打开网站
    /** 系统消息标识 {@link com.tizi.quanzi.tool.StaticField.SystemMessAttrName.systemFlag} */
    public int sys_msg_flag;
    /** 处理状态 {@link StaticField.SystemMessAttrName.statueCode} */
    public int status;
    public boolean isread;//是否已读
    public String group_id;//邀请加入的圈子ID
    public long create_time;
    /*动态通知*/
    public String reply_comment_id; //回复评论id
    public String reply_comment; // 回复评论内容
    public String reply_userid; // 回复评论人id
    public String reply_username; // 回复评论人名称
    public String dynid; // 动态id
    public String dyn_content; //动态内容
    public String dyn_icon;
    public String dyn_create_userid; // 动态发布人id
    public String dyn_create_username; //动态发布人（圈子）

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
        this.reply_comment_id = in.readString();
        this.reply_comment = in.readString();
        this.reply_userid = in.readString();
        this.reply_username = in.readString();
        this.dynid = in.readString();
        this.dyn_content = in.readString();
        this.dyn_icon = in.readString();
        this.dyn_create_userid = in.readString();
        this.dyn_create_username = in.readString();
    }

    public String getConvid() {
        return convid;
    }

    public void setConvid(String convid) {
        this.convid = convid;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setIsread(boolean isread) {
        this.isread = isread;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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
        dest.writeString(this.reply_comment_id);
        dest.writeString(this.reply_comment);
        dest.writeString(this.reply_userid);
        dest.writeString(this.reply_username);
        dest.writeString(this.dynid);
        dest.writeString(this.dyn_content);
        dest.writeString(this.dyn_icon);
        dest.writeString(this.dyn_create_userid);
        dest.writeString(this.dyn_create_username);
    }
}
