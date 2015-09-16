package com.tizi.quanzi.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by qixingchen on 15/9/11.
 * 主题活动
 */
public class Theme extends OnlySuccess {


    /**
     * msg : null
     * success : true
     */

    @SerializedName("acts")
    public List<ActsEntity> acts;

    public static class ActsEntity {
        /**
         * id : HTDM004832d9e7d1827b48d89845204dc5139421
         * title : 脱单大作战
         * content : 帮闺蜜脱单，从这里开始
         * icon : http://ac-hy5srahi.clouddn.com/twOiaJCSL7MFFB3PIOKFOSA.jpeg
         * detailUrl : http://www.baidu.com
         * signNum : 1000
         * beginDate : null
         * endDate : null
         * beginTime : 22:00:00
         * endTime : 24:00:00
         */

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
        @SerializedName("signNum")
        public int participantsNum;
        @SerializedName("beginDate")
        public String beginDate;
        @SerializedName("endDate")
        public String endDate;
        @SerializedName("beginTime")
        public String beginTime;
        @SerializedName("endTime")
        public String endTime;
    }
}
