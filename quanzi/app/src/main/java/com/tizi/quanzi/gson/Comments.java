package com.tizi.quanzi.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by qixingchen on 15/9/25.
 */
public class Comments extends OnlySuccess {

    @SerializedName("comments")
    public List<CommentsEntity> comments;

    public static class CommentsEntity {
        /**
         * content : Gggg
         * id : HTDM0000ee5a363623cf4c8a83eec3ba24df0d45
         * createTime : 20150925151935
         * createUser : HTDM004825b32141fe9c41f09846e85f0902f0bd
         * createUserName : 账号1
         * dynamicId : HTDM0048f6f8aad48fd341b2943ed74d2bc3b72e
         * userIcon : http://ac-hy5srahi.clouddn.com/caLxbdJcpOapVD1UR2yoJvD.jpeg
         * senderId : HTDM004825b32141fe9c41f09846e85f0902f0bd
         * atUserName : 2
         * atUserId : HTDM00480d82f1a557cf4e76a41de993de5a6b2c
         * replyId : HTDM0000914fa70c3efc428eb8463c9e868a9f16
         * dr : 0
         */

        @SerializedName("content")
        public String content;
        @SerializedName("id")
        public String id;
        @SerializedName("createTime")
        public String createTime;
        @SerializedName("createUser")
        public String createUser;
        @SerializedName("createUserName")
        public String createUserName;
        @SerializedName("dynamicId")
        public String dynamicId;
        @SerializedName("userIcon")
        public String userIcon;
        @SerializedName("senderId")
        public String senderId;
        @SerializedName("atUserName")
        public String atUserName;
        @SerializedName("atUserId")
        public String atUserId;
        @SerializedName("replyId")
        public String replyId;
        @SerializedName("dr")
        public int dr;
    }
}
