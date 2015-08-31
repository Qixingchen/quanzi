package com.tizi.quanzi.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by qixingchen on 15/8/31.
 * 组群详细信息
 * http://120.25.232.240:8080/group/findF?groupid=HTDM0048f4c2d7967c6f4011b69aaaa7af17b4e2&sign=ba5d601990b9c5e4e628d0139e7d2dcb&ts=1441010617&uid=HTDM004825b32141fe9c41f09846e85f0902f0bd
 */
public class GroupInfo {

    /**
     * groupNo :
     * icon : http://ac-hy5srahi.clouddn.com/fnwlHqNa7BPXdAfs8bZAFID.jpeg
     * groupName : 圈子
     * memlist : [{"id":"HTDM004825b32141fe9c41f09846e85f0902f0bd","icon":"http://ac-hy5srahi.clouddn.com/caLxbdJcpOapVD1UR2yoJvD.jpeg","sex":"1","name":"232"},{"id":"HTDM00480d82f1a557cf4e76a41de993de5a6b2c","icon":"http://ac-hy5srahi.clouddn.com/UZhfc3QzcnuRmF2zjNr3APD.jpeg","sex":"0","name":"2"}]
     * type : 0
     * success : true
     */

    @SerializedName("groupNo")
    public String groupNo;
    @SerializedName("icon")
    public String icon;
    @SerializedName("groupName")
    public String groupName;
    @SerializedName("type")
    public String type;
    @SerializedName("success")
    public boolean success;
    @SerializedName("memlist")
    public List<MemlistEntity> memlist;

    public static class MemlistEntity {
        /**
         * id : HTDM004825b32141fe9c41f09846e85f0902f0bd
         * icon : http://ac-hy5srahi.clouddn.com/caLxbdJcpOapVD1UR2yoJvD.jpeg
         * sex : 1
         * name : 232
         */

        @SerializedName("id")
        public String id;
        @SerializedName("icon")
        public String icon;
        @SerializedName("sex")
        public String sex;
        @SerializedName("name")
        public String name;
    }
}
