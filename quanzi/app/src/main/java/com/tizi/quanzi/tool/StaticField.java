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
        public final static int Location = -5;
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
        public final static String type = "type";//单人群聊
        public final static String IS_SYS_MESS = "msgType";// 消息类型 ： S：系统消息， C：非系统消息
    }

    /*图片请求Code*/
    public static class ImageRequreCode {
        public static final int IMAGE_REQUEST_CODE = 0;//相册
        public static final int CAMERA_REQUEST_CODE = 1;//相机
        public static final int CUT_REQUEST_CODE = 2;//裁减
    }

    /*通知Intent*/
    public static class NotifiName {
        public final static String NotifiDelete = "NotifiDelete";
        public final static String NotifiClick = "NotifiClick";
        public final static String Conversation = "Conversation";
    }

    /*系统消息attr名称*/
    public static class SystemMessAttrName {

        public final static String REMARK = "remark";// 邀请加人时可能会有附加信息
        public final static String JOIN_CONV_ID = "joinConvId";// 用于邀请加人时
        public final static String LINK_URL = "linkUrl";// 链接：可以是图片链接或网页等
        /** 系统消息标识 {@link com.tizi.quanzi.model.SystemMessage} */
        public final static String SYS_MSG_FLAG = "sysMsgFlag";

        public final class MessTypeCode {
            public final static String System_mess = "S";
            public final static String normal_mess = "C";
        }

        public final class systemFlag {
            public final static int notice = 0;
            public final static int invitation = 1;
            public final static int reject = 2;
            public final static int group_delete = 3;
        }
    }

    /*申请权限ID*/
    public static class PermissionRequestCode {
        public final static int WRITE_EXTERNAL_STORAGE = 0;
        public final static int READ_EXTERNAL_STORAGE = 1;
    }

    /*消息刷新数量*/
    public static class MessageQueryLimit {
        public static final int Limit = 1000;
        public static final int FlushMaxTimes = 10;
    }
}
