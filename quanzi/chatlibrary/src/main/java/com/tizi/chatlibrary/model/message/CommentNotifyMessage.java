package com.tizi.chatlibrary.model.message;

import android.databinding.Bindable;

/**
 * Created by qixingchen on 16/2/26.
 */
public class CommentNotifyMessage extends ChatMessage {

    /*动态通知*/
    private String reply_comment_id; //回复评论id
    private String reply_comment; // 回复评论内容
    private String reply_userid; // 回复评论人id
    private String reply_username; // 回复评论人名称
    private String dynid; // 动态id
    private String dyn_content; //动态内容
    private String dyn_icon;
    private String dyn_create_userid; // 动态发布人id
    private String dyn_create_username; //动态发布人（圈子）

    @Bindable
    public String getReply_comment_id() {
        return reply_comment_id;
    }

    public void setReply_comment_id(String reply_comment_id) {
        this.reply_comment_id = reply_comment_id;
    }

    @Bindable
    public String getReply_comment() {
        return reply_comment;
    }

    public void setReply_comment(String reply_comment) {
        this.reply_comment = reply_comment;
    }

    @Bindable
    public String getReply_userid() {
        return reply_userid;
    }

    public void setReply_userid(String reply_userid) {
        this.reply_userid = reply_userid;
    }

    @Bindable
    public String getReply_username() {
        return reply_username;
    }

    public void setReply_username(String reply_username) {
        this.reply_username = reply_username;
    }

    @Bindable
    public String getDynid() {
        return dynid;
    }

    public void setDynid(String dynid) {
        this.dynid = dynid;
    }

    @Bindable
    public String getDyn_content() {
        return dyn_content;
    }

    public void setDyn_content(String dyn_content) {
        this.dyn_content = dyn_content;
    }

    @Bindable
    public String getDyn_icon() {
        return dyn_icon;
    }

    public void setDyn_icon(String dyn_icon) {
        this.dyn_icon = dyn_icon;
    }

    @Bindable
    public String getDyn_create_userid() {
        return dyn_create_userid;
    }

    public void setDyn_create_userid(String dyn_create_userid) {
        this.dyn_create_userid = dyn_create_userid;
    }

    @Bindable
    public String getDyn_create_username() {
        return dyn_create_username;
    }

    public void setDyn_create_username(String dyn_create_username) {
        this.dyn_create_username = dyn_create_username;
    }
}
