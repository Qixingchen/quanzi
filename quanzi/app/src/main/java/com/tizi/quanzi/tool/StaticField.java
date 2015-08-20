package com.tizi.quanzi.tool;

/**
 * Created by qixingchen on 15/7/23.
 * 静态值
 */
public class StaticField {

    /*消息来源*/
    public static class ChatFrom {
        public final static int ME = 0;
        public final static int GROUP = 1;
        public final static int OTHER = 2;
    }

    /*消息携带内容类型*/
    public static class ChatContantType {
        public final static int TEXT = -1;
        public final static int IMAGE = -2;
        public final static int VOICE = -3;
        public final static int VEDIO = -4;

    }

    /*本地储存信息字段*/
    public static class TokenPreferences {
        public final static String USERID = "USERID";
        public final static String USERTOKEN = "USERTOKEN";
        public final static String TOKENFILE = "TOKENFILE";
        public final static String PASSWORD = "PASSWORD";
        public final static String USERPHONE = "USERPHONE";
    }

    /*聊天室类型*/
    public static class ChatBothUserType {
        public final static int twoPerson = 0;
        public final static int GROUP = 1;
        public final static int BoomGroup = 2;
    }

    /*聊天信息附带参数字段*/
    public static class ChatMessAttrName {
        public final static String userIcon = "userIcon";
        public final static String userName = "userName";
        public final static String groupID = "groupId";
        public final static String userID = "userId";
        public final static String type = "type";
    }

    /*图片请求Code*/
    public static class ImageRequreCode {
        public static final int IMAGE_REQUEST_CODE = 0;//相册
        public static final int CAMERA_REQUEST_CODE = 1;//相机
        public static final int CUT_REQUEST_CODE = 2;//裁减
    }

}
