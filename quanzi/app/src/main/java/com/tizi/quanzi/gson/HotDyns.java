package com.tizi.quanzi.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by qixingchen on 15/9/11.
 * 热门动态
 */
public class HotDyns {


    /**
     * msg : null
     * success : true
     */

    @SerializedName("msg")
    public String msg;
    @SerializedName("success")
    public boolean success;
    @SerializedName("dyns")
    public List<DynsEntity> dyns;

    public static class DynsEntity {
        /**
         * commentNum : 0
         * content : 终于知道为什么家里的水用得这麽快了！[泪流满面] #爆笑gif图#
         * createTime : 20150902141007
         * icon : http://ac-hy5srahi.clouddn.com/caLxbdJcpOapVD1UR2yoJvD.jpeg
         * pics : [{"url":"http://ww3.sinaimg.cn/square/79a00895jw1evnyubouqpg205008whdt.gif"},{"url":"http://ac-hy5srahi.clouddn.com/n3XnNwNVqhsQw2jaZXQ9VD9vE4hGdruKYpp8PAK9.jpg"},{"url":"http://ac-hy5srahi.clouddn.com/12gUbWb5YIneFU6kGd1f7oy4jOqiNbEAVdlikBES.jpg"},{"url":"http://ac-hy5srahi.clouddn.com/12gUbWb5YIneFU6kGd1f7oy4jOqiNbEAVdlikBES.jpg"}]
         * sex : 1
         * createUser : HTDM004825b32141fe9c41f09846e85f0902f0bd
         * zan : 0
         * nickName : 232
         * dynid : HTDM00481e29d26dcc084c2eb664fd0e26806f4e
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
             * url : http://ww3.sinaimg.cn/square/79a00895jw1evnyubouqpg205008whdt.gif
             */

            @SerializedName("url")
            public String url;
        }
    }
}
