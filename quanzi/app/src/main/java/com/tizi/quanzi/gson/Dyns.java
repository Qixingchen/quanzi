package com.tizi.quanzi.gson;

import java.util.List;

/**
 * Created by qixingchen on 15/8/19.
 * 动态
 */
public class Dyns {


    /**
     * success : true
     * msg : null
     */
    public List<DynsEntity> dyns;
    public boolean success;
    public String msg;

    public static class DynsEntity {
        /**
         * content : 你好啊
         * commentNum : 5
         * createTime : 20150101101001
         * createUser : HTDM0048efd1df5cacd448e48f86775e454e1981
         * sex : 0
         * pics : [{"url":null}]
         * nickName : 1
         * zan : 1
         * dynid : aaa
         */
        public String content;
        public int commentNum;
        public String createTime;
        public String createUser;
        public String sex;
        public List<PicsEntity> pics;
        public String nickName;
        public int zan;
        public String dynid;

        public static class PicsEntity {
            /**
             * url : null
             */
            public String url;
        }
    }
}
