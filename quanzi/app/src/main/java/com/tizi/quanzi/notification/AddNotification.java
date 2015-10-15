package com.tizi.quanzi.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;

import com.tizi.quanzi.R;
import com.tizi.quanzi.app.App;
import com.tizi.quanzi.app.AppStaticValue;
import com.tizi.quanzi.dataStatic.GroupList;
import com.tizi.quanzi.model.ChatMessage;
import com.tizi.quanzi.model.GroupClass;
import com.tizi.quanzi.tool.StaticField;

import java.util.ArrayList;

/**
 * Created by qixingchen on 15/8/21.
 * 发布通知
 * todo 删除和点击动作冲突
 */
public class AddNotification {

    private static final String TAG = "通知";
    private static Context mContext;
    private static AddNotification mInstance;
    private static ArrayList<ChatMessage> chatMessageArrayList;
    private static NotificationManager mNotificationManager;
    SharedPreferences systemSetting;
    //通知设置
    private boolean needVibrate, needSound, needInAppNotifi;

    private AddNotification() {
        mContext = App.getApplication();
        chatMessageArrayList = new ArrayList<>();
        mNotificationManager = (NotificationManager) mContext.
                getSystemService(Context.NOTIFICATION_SERVICE);
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
    public void notifiClicked() {
        chatMessageArrayList.clear();
        mNotificationManager.cancel(1);
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
        if (chatMessageArrayList.size() == 1) {
            clickIntent.putExtra(StaticField.NotifiName.Conversation, chatMessageArrayList.get(0).ConversationId);
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
                .setWhen(chatMessageArrayList.get(0).create_time)
                .setVisibility(Notification.VISIBILITY_PRIVATE)
                .setNumber(chatMessageArrayList.size())
                .setLights(Color.WHITE, 1000, 500)
                .setAutoCancel(false)
                .setColor(mContext.getResources().getColor(R.color.colorPrimary));

        if (needSound && chatMessageArrayList.size() == 1) {
            mBuilder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
        }
        if (needVibrate) {
            mBuilder.setVibrate(new long[]{1000, 1000});
        }

        if (chatMessageArrayList.size() == 1) {
            ChatMessage chatMessage = chatMessageArrayList.get(0);
            mBuilder.setContentTitle(chatMessage.userName)
                    .setContentText(ChatMessage.getContentText(chatMessage));
            if (chatMessage.type == StaticField.ChatContantType.IMAGE) {
                NotificationCompat.BigPictureStyle pictureStyle = new NotificationCompat.BigPictureStyle();
                // TODO: 15/10/12 BigPictureStyle
            }
        } else {
            /*多条消息*/
            mBuilder.setContentTitle("圈子")
                    .setContentText("收到" + chatMessageArrayList.size() + "条消息");
            /*inbox*/
            NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
            inboxStyle.setBigContentTitle("圈子");
            for (ChatMessage chatMessage : chatMessageArrayList) {
                inboxStyle.addLine(ChatMessage.getContentText(chatMessage));
            }
            inboxStyle.setSummaryText("收到" + chatMessageArrayList.size() + "条消息");
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
    public void AddMessage(ChatMessage chatMessage) {
        if (App.isAppForeground && !needInAppNotifi) {
            return;
        }
        switch (chatMessage.ChatBothUserType) {
            case StaticField.ConvType.GROUP:

                GroupClass group = (GroupClass) GroupList.getInstance().getGroup(chatMessage.groupID);
                if (group != null && !group.getNeedNotifi()) {
                    return;
                }
                break;

            case StaticField.ConvType.twoPerson:
                if (!systemSetting.getBoolean(chatMessage.sender, true))
                    return;
                break;
            case StaticField.ConvType.BoomGroup:
                if (!systemSetting.getBoolean(chatMessage.groupID, true))
                    return;
                break;
            default:
                break;
        }

        chatMessageArrayList.add(chatMessage);
        setNotification();
    }


    public boolean isNeedVibrate() {
        return needVibrate;
    }

    public void setNeedVibrate(boolean needVibrate) {
        this.needVibrate = needVibrate;
        systemSetting.edit().putBoolean(StaticField.SystemSettingString.needVibrate, needVibrate).apply();
    }

    public boolean isNeedSound() {
        return needSound;
    }

    public void setNeedSound(boolean needSound) {
        this.needSound = needSound;
        systemSetting.edit().putBoolean(StaticField.SystemSettingString.needSound, needSound).apply();
    }

    public boolean isNeedInAppNotifi() {
        return needInAppNotifi;
    }

    public void setNeedInAppNotifi(boolean needInAppNotifi) {
        this.needInAppNotifi = needInAppNotifi;
        systemSetting.edit().putBoolean(StaticField.SystemSettingString.needInAppNotifi, needInAppNotifi).apply();
    }

    public void setSharedPreferences() {
        systemSetting = AppStaticValue.getNotifiPreferences();
        needVibrate = systemSetting.getBoolean(StaticField.SystemSettingString.needVibrate, true);
        needSound = systemSetting.getBoolean(StaticField.SystemSettingString.needSound, true);
        needInAppNotifi = systemSetting.getBoolean(StaticField.SystemSettingString.needInAppNotifi, true);
    }
}
