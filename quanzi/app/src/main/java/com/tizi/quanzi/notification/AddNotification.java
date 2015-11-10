package com.tizi.quanzi.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.provider.Settings;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.tizi.quanzi.R;
import com.tizi.quanzi.app.App;
import com.tizi.quanzi.app.AppStaticValue;
import com.tizi.quanzi.dataStatic.BoomGroupList;
import com.tizi.quanzi.dataStatic.GroupList;
import com.tizi.quanzi.model.ChatMessage;
import com.tizi.quanzi.model.GroupClass;
import com.tizi.quanzi.model.SystemMessage;
import com.tizi.quanzi.tool.StaticField;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qixingchen on 15/8/21.
 * 发布通知
 * todo 删除和点击动作冲突
 */
public class AddNotification {

    private static final String TAG = "通知";
    private static Context mContext;
    private static AddNotification mInstance;
    private static List<NotifiContact> notifiContacts;
    private static NotificationManager mNotificationManager;
    SharedPreferences systemSetting;
    //通知设置
    private boolean needVibrate, needSound, needInAppNotifi;
    private boolean needNotifi, needPriMessNotifi, needZanNotifi, needSysNotifi;

    private AddNotification() {
        mContext = App.getApplication();
        notifiContacts = new ArrayList<>();
        mNotificationManager = (NotificationManager) mContext.
                getSystemService(Context.NOTIFICATION_SERVICE);
        systemSetting = AppStaticValue.getNotifiPreferences();
    }

    public static AddNotification getInstance() {
        if (mInstance == null) {
            synchronized (AddNotification.class) {
                if (mInstance == null) {
                    mInstance = new AddNotification();
                }
            }
        }
        return mInstance;
    }

    /**
     * 通知被点击
     * 清空List并取消通知
     */
    public void notifiClean() {
        notifiContacts.clear();
        mNotificationManager.cancel(1);
    }

    public void chatActivityOpened(String convID) {
        for (NotifiContact notifiContact : notifiContacts) {
            if (notifiContact.convID.compareTo(convID) == 0) {
                notifiClean();
                break;
            }
        }
    }

    /**
     * 根据目前的List构造通知列表
     */
    private void setNotification() {
        int mId = 1;//set Notifi ID

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(mContext);
        mBuilder = setBuildStyle(mBuilder);
        mBuilder = setBuildEvent(mBuilder);

        mNotificationManager.notify(mId, mBuilder.build());
    }

    /**
     * 设置通知的事件
     *
     * @param mBuilder 建立事件前的 NotificationCompat.Builder
     *
     * @return 事件建立完成的 NotificationCompat.Builder
     */
    private NotificationCompat.Builder setBuildEvent(NotificationCompat.Builder mBuilder) {

        /*intent*/
        /*delete*/
        Intent deleteIntent = new Intent(mContext.getString(R.string.NotificaitonEventIntent));
        deleteIntent.putExtra(StaticField.NotifiName.NotifiDelete, true);

        mBuilder.setDeleteIntent(PendingIntent.getBroadcast(
                mContext, 0, deleteIntent, PendingIntent.FLAG_UPDATE_CURRENT));

        /*click*/
        Intent clickIntent = new Intent(mContext.getString(R.string.NotificaitonEventIntent));
        clickIntent.putExtra(StaticField.NotifiName.NotifiClick, true);
        if (notifiContacts.size() == 1) {
            NotifiContact temp = notifiContacts.get(0);
            if (temp.type == NotifiContact.GROUP || temp.type == NotifiContact.PRI_MESS) {
                clickIntent.putExtra(StaticField.NotifiName.Conversation, notifiContacts.get(0).convID);
            }
        }
        mBuilder.setContentIntent(PendingIntent.getBroadcast(
                mContext, 0, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT));

        return mBuilder;
    }


    /**
     * 建立通知的样式与内容
     *
     * @param mBuilder 建立样式前的 NotificationCompat.Builder
     *
     * @return 样式建立完成的 NotificationCompat.Builder
     */
    private NotificationCompat.Builder setBuildStyle(NotificationCompat.Builder mBuilder) {
        mBuilder.setSmallIcon(R.mipmap.ic_launcher)
                .setCategory(Notification.CATEGORY_MESSAGE)
                .setWhen(notifiContacts.get(0).createTime)
                .setVisibility(Notification.VISIBILITY_PRIVATE)
                .setNumber(notifiContacts.size())
                .setLights(Color.WHITE, 1000, 500)
                .setAutoCancel(false)
                .setColor(mContext.getResources().getColor(R.color.colorPrimary));

        if (needSound && notifiContacts.size() == 1) {
            mBuilder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
        }
        if (needVibrate && notifiContacts.size() == 1) {
            mBuilder.setVibrate(new long[]{0, 400});
        }

        if (notifiContacts.size() == 1) {
            NotifiContact notifiContact = notifiContacts.get(0);
            mBuilder.setContentTitle(notifiContact.messFrom)
                    .setContentText(notifiContact.contact);
        } else {
            /*多条消息*/
            mBuilder.setContentTitle(mContext.getString(R.string.app_name))
                    .setContentText("收到" + notifiContacts.size() + "条消息");
            /*inbox*/
            NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
            inboxStyle.setBigContentTitle("圈子");
            for (NotifiContact notifiContact : notifiContacts) {
                inboxStyle.addLine(notifiContact.contact);
            }
            inboxStyle.setSummaryText("收到" + notifiContacts.size() + "条消息");
            mBuilder.setStyle(inboxStyle);
        }

        return mBuilder;
    }

    /**
     * 判断用户是否需要接收这条通知
     * 将消息添加进List
     * 并添加通知
     *
     * @param chatMessage 需要发布通知的消息
     */
    public void addMessage(ChatMessage chatMessage) {
        if (!needNotifi) {
            return;
        }
        if (App.isAppForeground && !needInAppNotifi) {
            return;
        }
        switch (chatMessage.ChatBothUserType) {
            case StaticField.ConvType.GROUP:

                GroupClass group = (GroupClass) GroupList.getInstance().getGroup(chatMessage.groupID);
                if (group == null || !group.getNeedNotifi()) {
                    return;
                }
                break;

            case StaticField.ConvType.twoPerson:
                if (!systemSetting.getBoolean(chatMessage.sender, true)) {
                    return;
                }
                break;

            case StaticField.ConvType.BoomGroup:
                if (!systemSetting.getBoolean(chatMessage.groupID, true)) {
                    return;
                }
                break;

            default:
                break;
        }
        addMessage(NotifiContact.fromMessage(chatMessage));
    }

    public void addMessage(SystemMessage systemMessage) {
        if (!needNotifi) {
            return;
        }
        int flag = systemMessage.sys_msg_flag;
        if (flag == StaticField.SystemMessAttrName.systemFlag.notice ||
                flag == StaticField.SystemMessAttrName.systemFlag.invitation ||
                flag == StaticField.SystemMessAttrName.systemFlag.kicked ||
                flag == StaticField.SystemMessAttrName.systemFlag.group_delete) {
            if (needSysNotifi) {
                addMessage(NotifiContact.sysMessage(systemMessage.content, systemMessage.create_time, "系统消息"));
            }
            return;
        }
        if (needZanNotifi) {
            addMessage(NotifiContact.sysMessage(systemMessage.reply_comment, systemMessage.create_time, systemMessage.content));
        }

    }

    private void addMessage(NotifiContact notifiContact) {
        if (notifiContact == null) {
            return;
        }
        notifiContacts.add(notifiContact);
        setNotification();
    }

    /**
     * 从偏好文件加载设置
     */
    public void setSharedPreferences() {
        needVibrate = systemSetting.getBoolean(StaticField.SystemSettingString.needVibrate, true);
        needSound = systemSetting.getBoolean(StaticField.SystemSettingString.needSound, true);
        needInAppNotifi = systemSetting.getBoolean(StaticField.SystemSettingString.needInAppNotifi, true);

        needNotifi = systemSetting.getBoolean(StaticField.SystemSettingString.needNotifi, true);
        needPriMessNotifi = systemSetting.getBoolean(StaticField.SystemSettingString.needPriMessNotifi, true);
        needZanNotifi = systemSetting.getBoolean(StaticField.SystemSettingString.needZanNotifi, true);
        needSysNotifi = systemSetting.getBoolean(StaticField.SystemSettingString.needSystemNotifi, true);

        if (!needNotifi) {
            notifiClean();
        }
    }

    static class NotifiContact {

        public static final int GROUP = 1;
        public static final int BOOM_GROUP = 4;
        public static final int PRI_MESS = 2;
        public static final int SYSTEM = 3;
        public int type;
        public String convID;
        public String contact;
        public String groupID;
        public long createTime;
        public String messFrom;//消息来源,用于显示标题

        public NotifiContact(@TYPE_DEF int type, String convID, String contact, String groupID, long createTime, String messFrom) {
            this.type = type;
            this.convID = convID;
            this.contact = contact;
            this.groupID = groupID;
            this.createTime = createTime;
            this.messFrom = messFrom;
        }

        public NotifiContact(int type, String contact, long createTime, String messFrom) {
            this.type = type;
            this.contact = contact;
            this.createTime = createTime;
            this.messFrom = messFrom;
        }

        public static NotifiContact groupMessage(String convID, String contact, String groupID, long createTime, String messFrom) {
            return new NotifiContact(NotifiContact.GROUP, convID, contact, groupID, createTime, messFrom);
        }

        public static NotifiContact boomGroupMessage(String convID, String contact, String groupID, long createTime, String messFrom) {
            return new NotifiContact(NotifiContact.BOOM_GROUP, convID, contact, groupID, createTime, messFrom);
        }

        public static NotifiContact priMessage(String convID, String contact, String userID, long createTime, String messFrom) {
            return new NotifiContact(NotifiContact.PRI_MESS, convID, contact, userID, createTime, messFrom);
        }

        public static NotifiContact sysMessage(String contact, long createTime, String messFrom) {
            return new NotifiContact(NotifiContact.SYSTEM, contact, createTime, messFrom);
        }

        @Nullable
        public static NotifiContact fromMessage(ChatMessage chatMessage) {
            String groupID;
            switch (chatMessage.ChatBothUserType) {

                case StaticField.ConvType.GROUP:
                    groupID = GroupList.getInstance().getGroupIDByConvID(chatMessage.ConversationId);
                    if (groupID.equals("")) {
                        return null;
                    }
                    return groupMessage(chatMessage.ConversationId, ChatMessage.getContentText(chatMessage),
                            groupID, chatMessage.create_time, chatMessage.userName);

                case StaticField.ConvType.twoPerson:
                    return priMessage(chatMessage.ConversationId, ChatMessage.getContentText(chatMessage),
                            chatMessage.sender, chatMessage.create_time, chatMessage.userName);

                case StaticField.ConvType.BoomGroup:
                    groupID = BoomGroupList.getInstance().getGroupIDByConvID(chatMessage.ConversationId);
                    if (groupID.equals("")) {
                        return null;
                    }
                    return groupMessage(chatMessage.ConversationId, ChatMessage.getContentText(chatMessage),
                            groupID, chatMessage.create_time, chatMessage.userName);

                default:
                    return null;
            }
        }

        @IntDef({GROUP, PRI_MESS, SYSTEM, BOOM_GROUP})
        public @interface TYPE_DEF {
        }

    }
}
