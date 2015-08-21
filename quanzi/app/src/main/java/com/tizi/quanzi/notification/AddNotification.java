package com.tizi.quanzi.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;

import com.tizi.quanzi.R;
import com.tizi.quanzi.app.App;
import com.tizi.quanzi.model.ChatMessage;
import com.tizi.quanzi.tool.StaticField;
import com.tizi.quanzi.ui.ChatActivity;
import com.tizi.quanzi.ui.MainActivity;

import java.util.ArrayList;

/**
 * Created by qixingchen on 15/8/21.
 * 发布通知
 */
public class AddNotification {

    private static Context mContext;
    private static AddNotification mInstance;
    private static ArrayList<ChatMessage> chatMessageArrayList;
    private static NotificationManager mNotificationManager;

    private static final String TAG = "通知";

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

    private AddNotification() {
        mContext = App.getApplication();
        chatMessageArrayList = new ArrayList<>();
        mNotificationManager = (NotificationManager) mContext.
                getSystemService(Context.NOTIFICATION_SERVICE);
    }

    /**
     * 通知被点击
     * 清空List
     */
    public void notifiClicked() {
        chatMessageArrayList.clear();
    }

    /**
     * 根据目前的List构造通知列表
     */

    private void setNotification() {
//        int mId = 1;//set Notifi ID
//
//
//        NotificationCompat.Builder mBuilder =
//                new NotificationCompat.Builder(mContext);
//        mBuilder = setBuildStyle(mBuilder);
//
//        Intent resultIntent;
//        TaskStackBuilder stackBuilder = TaskStackBuilder.create(mContext);
//        if (chatMessageArrayList.size() == 1) {
//
//            resultIntent = new Intent(mContext, ChatActivity.class);
//            resultIntent.putExtra("conversation", chatMessage.ConversationId);
//            //导航
//            stackBuilder.addParentStack(ChatActivity.class);
//
//        } else {
//
//
//            resultIntent = new Intent(mContext, MainActivity.class);
//            //导航
//            stackBuilder.addParentStack(MainActivity.class);
//        }
//
//        stackBuilder.addNextIntent(resultIntent);
//
//        PendingIntent resultPendingIntent =
//                stackBuilder.getPendingIntent(
//                        0,
//                        PendingIntent.FLAG_UPDATE_CURRENT
//                );
//        mBuilder.setContentIntent(resultPendingIntent)
//                .setContentIntent(PendingIntent.getBroadcast(
//                        mContext, 0, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT));
//        mNotificationManager.notify(mId, mBuilder.build());
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
        Intent deleteIntent = new Intent(mContext.getString(R.string.NotificaitonEventIntent));
        deleteIntent.putExtra(StaticField.NotifiName.NotifiDelete, true);
        Intent clickIntent = new Intent(mContext.getString(R.string.NotificaitonEventIntent));
        clickIntent.putExtra(StaticField.NotifiName.NotifiClick, true);

        mBuilder.setDeleteIntent(PendingIntent.getBroadcast(
                mContext, 0, deleteIntent, PendingIntent.FLAG_UPDATE_CURRENT));

        return mBuilder;
    }


    /**
     * 建立通知的样式
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
                .setAutoCancel(false);

        if (chatMessageArrayList.size() == 1) {
            ChatMessage chatMessage = chatMessageArrayList.get(0);
            mBuilder.setContentTitle(chatMessage.userName)
                    .setContentText(getContentText(chatMessage));
        } else {
            /*多条消息*/
            mBuilder.setContentTitle("圈子")
                    .setContentText("收到" + chatMessageArrayList.size() + "条消息");
            /*inbox*/
            NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
            inboxStyle.setBigContentTitle("圈子");
            for (ChatMessage chatMessage : chatMessageArrayList) {
                inboxStyle.addLine(getContentText(chatMessage));
            }
            inboxStyle.setSummaryText("收到" + chatMessageArrayList.size() + "条消息");
            mBuilder.setStyle(inboxStyle);
        }

        return mBuilder;
    }

    /**
     * 将消息添加进List
     * 并添加通知
     *
     * @param chatMessage 需要发布通知的消息
     */
    public void AddMessage(ChatMessage chatMessage) {
        chatMessageArrayList.add(chatMessage);
        setNotification();
    }

    /**
     * 生成内容文字
     * todo add name
     *
     * @param chatMessage 需要发布的消息
     *
     * @return 可阅读的消息内容
     */
    private String getContentText(ChatMessage chatMessage) {
        String contentText;
        if (chatMessage.type == StaticField.ChatContantType.IMAGE) {
            contentText = "收到了一张图片";
        } else if (chatMessage.type == StaticField.ChatContantType.VOICE) {
            contentText = "收到了一段录音";
        } else if (chatMessage.type == StaticField.ChatContantType.VEDIO) {
            contentText = "收到了一段视频";
        } else if (chatMessage.type == StaticField.ChatContantType.Location) {
            contentText = "收到了一个位置";
        } else {
            contentText = chatMessage.text;
        }
        return contentText;
    }


}
