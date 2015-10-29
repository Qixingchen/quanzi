package com.tizi.quanzi.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by qixingchen on 15/8/31.
 * 组群详细信息
 */
public class GroupAllInfo extends OnlySuccess {


    /**
     * memlist : [{"id":"HTDM0048c94f8a6d0e8543a3b7c7e5f3470ae359","icon":"http://ac-hy5srahi.clouddn.com/GoHGsMSbB9rHwSDju25hChB.jpeg","sex":"0","name":"ht"},{"id":"HTDM0048067b632765bd493f887eb213775ea52a","icon":"http://ac-hy5srahi.clouddn.com/Y6tC89EgGAJt0SqSR1EhHrB.jpeg","sex":"1","name":"11223344"}]
     * tagList : [{"tagName":"善良","tagId":"HTDM0048773c059f042343eeafe426bbf92b2a53","id":"HTDM0048dc32dc2ce6a840968b9df65d54fc9de0"},{"tagName":"安静","tagId":"HTDM0048773c059f042342eeafe426bbf92b2a52","id":"HTDM004856f993cfebae4017a3429ae44dea7528"}]
     * group : {"keyIndexCode":null,"icon":"http://ac-hy5srahi.clouddn.com/T20CM0M0RnxowNaszxO1R4C.jpeg","createTime":"20150909164555","groupName":"我写作业了","remark":null,"validation":"Y","convId":"55eff12260b22f0f9c74d28a","hold":"0","type":"0","id":"HTDM0048c2d550386dfb4a73b6bd74c6f14dec7c","groupNo":"","createUser":"HTDM0048c94f8a6d0e8543a3b7c7e5f3470ae359","dirty":false,"bg":null,"rowStatus":2,"notice":"我一直以为我写作业了"}
     */

    @SerializedName("group")
    public GroupEntity group;
    @SerializedName("memlist")
    public List<MemlistEntity> memlist;
    @SerializedName("tagList")
    public List<AllTags.TagsEntity> tagList;

    public static class GroupEntity {
        /**
         * keyIndexCode : null
         * icon : http://ac-hy5srahi.clouddn.com/T20CM0M0RnxowNaszxO1R4C.jpeg
         * createTime : 20150909164555
         * groupName : 我写作业了
         * remark : null
         * validation : Y
         * convId : 55eff12260b22f0f9c74d28a
         * hold : 0
         * type : 0
         * id : HTDM0048c2d550386dfb4a73b6bd74c6f14dec7c
         * groupNo :
         * createUser : HTDM0048c94f8a6d0e8543a3b7c7e5f3470ae359
         * dirty : false
         * bg : null
         * rowStatus : 2
         * notice : 我一直以为我写作业了
         */

        @SerializedName("keyIndexCode")
        public String keyIndexCode;
        @SerializedName("icon")
        public String icon;
        @SerializedName("createTime")
        public String createTime;
        @SerializedName("groupName")
        public String groupName;
        @SerializedName("remark")
        public String remark;
        @SerializedName("validation")
        public String validation;
        @SerializedName("convId")
        public String convId;
        @SerializedName("hold")
        public String hold;
        @SerializedName("type")
        public String type;
        @SerializedName("id")
        public String id;
        @SerializedName("groupNo")
        public String groupNo;
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

    public static class MemlistEntity {
        /**
         * id : HTDM0048c94f8a6d0e8543a3b7c7e5f3470ae359
         * icon : http://ac-hy5srahi.clouddn.com/GoHGsMSbB9rHwSDju25hChB.jpeg
         * sex : 0
         * name : ht
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
