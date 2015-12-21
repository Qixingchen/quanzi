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
        public final static int NOTIFI = 3;
    }

    /*消息携带内容类型*/
    public static class ChatContantType {
        public final static int TEXT = -1;
        public final static int IMAGE = -2;
        public final static int VOICE = -3;
        public final static int VEDIO = -4;
        public final static int Location = -5;
        public final static int Notifi = -9;
    }

    /*本地储存信息字段*/
    public static class Preferences {
        public final static String USERID = "USERID";
        public final static String USERTOKEN = "USERTOKEN";
        public final static String TOKENFILE = "TOKENFILE";
        public final static String PASSWORD = "PASSWORD";
        public final static String USERPHONE = "USERPHONE";
        public final static String AllowAppUpDate = "AllowAppUpDate";
        public final static String LastAppUpDateTime = "LastAppUpDateTime";
    }

    /*聊天室类型*/
    public static class ConvType {
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
        public final static String type = "type";//单人群聊boom
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
        public final static String SYS_MSG_FLAG = "sysMsgFlag";

        public final class MessTypeCode {
            public final static String System_mess = "S";
            public final static String normal_mess = "C";
        }

        //状态：如邀请加入圈子，状态有未处理0、已处理1。当未处理时显示“同意、拒绝”按钮
        public final class statueCode {
            public final static int complete = 1;
            public final static int notComplete = 0;
        }

        //系统消息标识：0：系统消息、1：邀请加入圈子、2：拒绝加入圈子、3:圈子解散
        // 4:被踢出圈子
        public final class systemFlag {
            public final static int notice = 0;
            public final static int invitation = 1;
            public final static int reject = 2;
            public final static int group_delete = 3;
            public final static int kicked = 4;
            public final static int group_change_name = 5;
            public final static int dyn_comment = 6;
            public final static int jiong_pic = 7;
        }
    }

    public static class DynNoticeAttrName {

        public final static String reply_comment_id = "reply_comment_id"; //回复评论id
        public final static String reply_comment = "reply_comment"; // 回复评论内容
        public final static String reply_userid = "reply_userid"; // 回复评论人id
        public final static String reply_username = "reply_username"; // 回复评论人名称

        public final static String dynid = "dynid"; // 动态id
        public final static String dyn_content = "dyn_content"; //动态内容
        public final static String dyn_icon = "dyn_icon";
        public final static String dyn_create_userid = "dyn_create_userid"; // 动态发布人id
        public final static String dyn_create_username = "dyn_create_username"; //动态发布人（圈子）
    }

    /*申请权限ID*/
    /*0~255 在这被使用*/
    public static class PermissionRequestCode {

        /*图像相关:1~100 十位为奇数的是裁剪*/
        public final static int chat_insert_photo = 1;
        public final static int userInfoSetFragment_user_face_photo = 2;
        public final static int userInfoSetFragment_user_face_photo_crop = 12;
        public final static int new_group_face_photo = 3;
        public final static int new_group_face_photo_crop = 13;
        public final static int QuanziIntroduceFragment_group_face = 4;
        public final static int QuanziIntroduceFragment_group_face_crop = 14;
        public final static int register_user_face = 5;
        public final static int register_user_face_crop = 15;
        public final static int send_dyn = 6;
        public final static int user_back_ground = 7;
        public final static int user_back_ground_crop = 17;


        public final static int CHAT_RECORD_AUDIO = 101;
        public final static int userInfoSetFragment_location = 102;
        public final static int addContactUsersInQuanziZone = 103;
        public final static int addContactUsersInNewGroup = 104;
        public final static int saveImageToExternalStorage = 105;
        public final static int shareImage = 106;
        public final static int QrCodeScan = 107;

        public final static int QrCodeScan_QuestCode = 0x0000c0de;
        public final static int GroupDynInfo_QuestCode = 0x0000c0df;

        public static boolean isPermissionRequest(int code) {
            return code >= 0 && code <= 255;
        }

        public static boolean isImagePermissionEvent(int code) {
            return code > 0 && code <= 100;
        }
    }

    /*数量限制*/
    public static class Limit {
        public static final int MessageLimit = 1000;
        public static final int FlushMaxTimes = 10;
        public static final int DynamicLimit = 50;
        public static final int ContactLimit = 100;
        public static final int MAX_QUANZI = 10;
        public static final int SIGN_CODE_COUNTDOWN = 10;
        public static final int MAX_MEMBER_IN_GROUP = 10;
        public static final int DYN_IMAGE_SIZE = 9;
        public static final int IMAGE_WIDTH = 1440;
        public static final int IMAGE_QUALITY = 75;
    }

    /*系统消息还是私聊*/
    public static class PrivateMessOrSysMess {
        public final static int PrivateMess = 1;
        public final static int SysMess = 2;
    }

    /*游客的账号和密码*/
    public static class GuestUser {
        public final static String Account = "guest";
        public final static String PassWord = "1a100d2c0dab19ce3ceb5881a0a1fdaad01296d7554868d4430e7d73762f379e";
    }

    /*图片查看器*/
    public static class GalleryPara {
        public final static String pics = "pics";
        public final static String nowPosition = "now";
    }

    /*APP Name*/
    public static class AppName {
        public final static String AppEngName = "GiMi";
        public final static String AppZHName = "基蜜";
    }

    /*Intent 名称*/
    public static class IntentName {
        public static final String OtherUserInfo = "userInfo";
    }

    /*系统设置字段*/
    public static class SystemSettingString {
        public static final String needVibrate = "notifications_vibrate";
        public static final String needSound = "notifications_sound";
        public static final String needInAppNotifi = "notifications_in_app";
        public static final String needNotifi = "notifications";
        public static final String needPriMessNotifi = "notifications_pri_mess";
        public static final String needZanNotifi = "notifications_zan";
        public static final String needSystemNotifi = "notifications_system";
        public static final String disallowBaiduLocation = "disallow_baidu_location";
        public static final String compactWeixinShare = "compact_weixin_share";
        public static final String devMode = "dev_mode";
    }
}
