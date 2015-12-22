package com.tizi.quanzi.gson;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by qixingchen on 15/8/31.
 * 组群详细信息
 */
public class GroupAllInfo extends OnlySuccess {


    /**
     * keyIndexCode : null
     * createTime : 20151014140946
     * icon : https://dn-hy5srahi.qbox.me/3MgrgN7Zmu9bPrAgjjc5OvfVSo7Gc5un5B10GlRC.jpg
     * groupName : jt01111
     * convId : 561df1aaddb24819b7cd8547
     * remark : null
     * validation : Y
     * hold : 0
     * type : 0
     * groupNo :
     * id : HTDM00487c8a88dac5a54699aee8cb4fb5ff7ffe
     * createUser : HTDM004895dfca0c4c404097a8d3150df70b0d19
     * dirty : false
     * bg : null
     * rowStatus : 2
     * notice : 说说说说说说说说说说说说说说说说说说说说说说说说说说说说说说说说说说说说说说说说说说说说莫名其妙说说说说说说说说说说说说说说说说说说说说
     */

    @SerializedName("group")
    public GroupEntity group;
    /**
     * tags : null
     * id : HTDM00486dd5ad1afaa044658d118889c2bdc202
     * birthday : 2011-9-3
     * icon : https://dn-hy5srahi.qbox.me/KO0OTWdqBxTuvVriUYcyCKqn103yBkMsidk6X8tA.jpg
     * sex : 1
     * area : 福建省泉州市
     * name : 星辰
     * bg : https://dn-hy5srahi.qbox.me/zLRqm9uSg6HaQCD5EdxfXRnaimY3MuyiP4SydXbm.jpg
     * signature : 2333
     * 无图
     * high
     * 刚刚好
     * 刚刚好
     * <p/>
     * 刚旅途
     */

    @SerializedName("memlist")
    public List<MemberEntity> memlist;
    /**
     * tagName : 婚前恐惧
     * tagId : HTDM0048773c059f042340eeafe426bbf92aaa42
     * id : HTDM0000e409ad931a27475bb561db69bc58abc6
     */

    @SerializedName("tagList")
    public List<AllTags.TagsEntity> tagList;

    public static class GroupEntity {
        @SerializedName("keyIndexCode")
        public Object keyIndexCode;
        @SerializedName("createTime")
        public String createTime;
        @SerializedName("icon")
        public String icon;
        @SerializedName("groupName")
        public String groupName;
        @SerializedName("convId")
        public String convId;
        @SerializedName("remark")
        public String remark;
        @SerializedName("validation")
        public String validation;
        @SerializedName("hold")
        public String hold;
        @SerializedName("type")
        public int type;
        @SerializedName("groupNo")
        public String groupNo;
        @SerializedName("id")
        public String id;
        @SerializedName("createUser")
        public String createUser;
        @SerializedName("dirty")
        public boolean dirty;
        @SerializedName("bg")
        public String bg;
        @SerializedName("rowStatus")
        public int rowStatus;
        @SerializedName("notice")
        public String notice;
    }

    public static class MemberEntity implements Serializable {
        @SerializedName("tags")
        public Object tags;
        @SerializedName("id")
        public String id;
        @SerializedName("birthday")
        public String birthday;
        @SerializedName("icon")
        public String icon;
        @SerializedName("sex")
        public String sex;
        @SerializedName("area")
        public String area;
        @SerializedName("name")
        public String name;
        @SerializedName("bg")
        public String bg;
        @SerializedName("signature")
        public String signature;
    }
}
