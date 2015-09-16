package com.tizi.quanzi.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by qixingchen on 15/8/19.
 * 动态
 */
public class Dyns extends OnlySuccess {


    /**
     * msg : null
     * success : true
     */
    @SerializedName("dyns")
    public List<DynsEntity> dyns;

    public static class DynsEntity {
        /**
         * commentNum : 0
         * content : jy
         * createTime : 20150908145122
         * icon : http://ac-hy5srahi.clouddn.com/DfoMrTPn9NMvAFtP5nPLLwA.jpeg
         * pics : [{"url":"http://ac-hy5srahi.clouddn.com/9AEsakFeTW4fJTiHnrH5JnD.jpeg"},{"url":"http://ac-hy5srahi.clouddn.com/VX7EO9O7OHRQABsh0fOJIwD.jpeg"},{"url":"http://ac-hy5srahi.clouddn.com/xlWdQaFfyXl2BEFrvaIrMLB.jpeg"}]
         * sex : 0
         * createUser : HTDM0048e1347f922f5d4d6c837ab27d407c1859
         * zan : 0
         * nickName : 炸鸡腿
         * dynid : HTDM0048abb3908b636e431c96589bcbc1901e61
         */

        @SerializedName("commentNum")
        public int commentNum;
        @SerializedName("content")
        public String content;
        @SerializedName("createTime")
        public String createTime;
        @SerializedName("icon")
        public String icon;
        @SerializedName("sex")
        public String sex;
        @SerializedName("createUser")
        public String createUser;
        @SerializedName("zan")
        public int zan;
        @SerializedName("nickName")
        public String nickName;
        @SerializedName("dynid")
        public String dynid;
        @SerializedName("pics")
        public List<PicsEntity> pics;

        public static class PicsEntity {
            /**
             * url : http://ac-hy5srahi.clouddn.com/9AEsakFeTW4fJTiHnrH5JnD.jpeg
             */

            @SerializedName("url")
            public String url;
        }
    }
}
