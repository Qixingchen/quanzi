package com.tizi.quanzi.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by qixingchen on 15/9/11.
 * 主题活动
 */
public class Theme extends OnlySuccess {


    /**
     * id : HTDM004832d9e7d1827b48d89845204dc5139421
     * title : 脱单大作战
     * content : #帮基蜜脱单#从这里开始~!
     * icon : http://ac-hy5srahi.clouddn.com/c815654724ba000f.png
     * detailUrl : http://v.xiumi.us/stage/v3/1ZhdA/4165144
     * template : 1
     * signNum : 1004
     * beginDate : null
     * endDate : null
     * adUrl : null
     * beginTime : 12:00:00
     * endTime : 16:00:00
     */

    @SerializedName("acts")
    public List<ActsEntity> acts;

    public static class ActsEntity {
        @SerializedName("id")
        public String id;
        @SerializedName("title")
        public String title;
        @SerializedName("content")
        public String content;
        @SerializedName("icon")
        public String icon;
        @SerializedName("detailUrl")
        public String detailUrl;
        @SerializedName("template")
        public int template;
        @SerializedName("signNum")
        public int signNum;
        @SerializedName("beginDate")
        public Object beginDate;
        @SerializedName("endDate")
        public Object endDate;
        @SerializedName("adUrl")
        public Object adUrl;
        @SerializedName("beginTime")
        public String beginTime;
        @SerializedName("endTime")
        public String endTime;
    }
}
