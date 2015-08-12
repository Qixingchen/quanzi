package com.tizi.quanzi.tool;

/**
 * Created by qixingchen on 15/7/23.
 */
public class StaticField {

    public static class ChatFrom {
        public final static int ME = 0;
        public final static int GROUP = 1;
        public final static int OTHER = 2;
    }

    public static class chatType {
        public final static int TEXT = -1;
        public final static int IMAGE = -2;
        public final static int VOICE = -3;
        public final static int VEDIO = -4;

    }

    public static class TokenPreferences {
        public final static String USERID = "USERID";
        public final static String USERTOKEN = "USERTOKEN";
        public final static String TOKENFILE = "TOKENFILE";
        public final static String PASSWORD = "PASSWORD";
        public final static String USERPHONE = "USERPHONE";
    }

}
