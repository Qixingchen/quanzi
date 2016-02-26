package com.tizi.chatlibrary.model.message;

import android.databinding.Bindable;
import android.support.annotation.IntDef;

/**
 * Created by qixingchen on 16/2/26.
 */
public class SystemMessage extends ChatMessage {

    public final static int notice = 0;
    public final static int invitation = 1;
    public final static int reject = 2;
    public final static int group_delete = 3;
    public final static int kicked = 4;
    public final static int group_change_name = 5;
    public final static int dyn_comment = 6;
    public final static int jiong_pic = 7;

    public final static int complete = 1;
    public final static int notComplete = 0;

    private String joinConvid;//加入的会话id, 针对邀请加入某个圈子之类的，需要根据当前会话id加入
    private String joinGroupID;//邀请加入的圈子ID
    private String sendUserID; //发送系统消息方
    private String sendUserIcon;//发送系统消息方的头像，冗余，可以为空，如系统主动发送的消息可能会有个系统图标，此时这个为空。
    private String sendUsername;//发送系统消息的名称，冗余，可以为空，如a邀请b加入某个圈子，user_name = a。系统发送的可以为空或“客服小秘书”
    private String content;//消息内容：如 xxx邀请您加入xx圈子
    private String remark;//消息中附带的邀请者信息：比如附带消息是“我是xxx，快来加入啊”
    private String link_url;//可能是链接消息，可以通过网址打开网站
    /** 系统消息类型 {@link systemMsgType} */
    private int sys_msg_Type;
    /** 处理状态 {@link completeStatueCode} */
    private int completeStatue;

    public static
    @SystemMessage.systemMsgType
    int getSystemMsgType(int type) {
        switch (type) {
            case notice:
                return notice;
            case invitation:
                return invitation;
            case reject:
                return reject;
            case group_delete:
                return group_delete;
            case kicked:
                return kicked;
            case group_change_name:
                return group_change_name;
            case jiong_pic:
                return jiong_pic;
            default:
                return notice;
        }
    }

    @Bindable
    public String getJoinConvid() {
        return joinConvid;
    }

    public void setJoinConvid(String joinConvid) {
        this.joinConvid = joinConvid;
    }

    @Bindable
    public String getJoinGroupID() {
        return joinGroupID;
    }

    public void setJoinGroupID(String joinGroupID) {
        this.joinGroupID = joinGroupID;
    }

    @Bindable
    public String getSendUserID() {
        return sendUserID;
    }

    public void setSendUserID(String sendUserID) {
        this.sendUserID = sendUserID;
    }

    @Bindable
    public String getSendUserIcon() {
        return sendUserIcon;
    }

    public void setSendUserIcon(String sendUserIcon) {
        this.sendUserIcon = sendUserIcon;
    }

    @Bindable
    public String getSendUsername() {
        return sendUsername;
    }

    public void setSendUsername(String sendUsername) {
        this.sendUsername = sendUsername;
    }

    @Bindable
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Bindable
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Bindable
    public String getLink_url() {
        return link_url;
    }

    public void setLink_url(String link_url) {
        this.link_url = link_url;
    }

    @Bindable
    public int getSys_msg_Type() {
        return sys_msg_Type;
    }

    public void setSys_msg_Type(@systemMsgType int sys_msg_Type) {
        this.sys_msg_Type = sys_msg_Type;
    }

    @Bindable
    public int getCompleteStatue() {
        return completeStatue;
    }

    public void setCompleteStatue(@completeStatueCode int completeStatue) {
        this.completeStatue = completeStatue;
    }

    @IntDef({notice, invitation, reject, group_delete, kicked, group_change_name, jiong_pic})
    public @interface systemMsgType {
    }

    @IntDef({complete, notComplete})
    public @interface completeStatueCode {
    }

}
