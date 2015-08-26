package com.tizi.quanzi.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.tizi.quanzi.tool.StaticField;
import com.tizi.quanzi.ui.ChatActivity;
import com.tizi.quanzi.ui.main.MainActivity;

/**
 * Created by qixingchen on 15/8/21.
 * 通知的事件广播接收器
 */
public class NotifiReceiver extends BroadcastReceiver {
    private static final String TAG = "NotifiReceiver";

    /**
     * 通知点击或划去广播
     *
     * @param context 上下文
     * @param intent  广播附带的intent
     *
     * @see AddNotification
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        boolean isDelete = intent.getBooleanExtra(StaticField.NotifiName.NotifiDelete, false);
        boolean isClick = intent.getBooleanExtra(StaticField.NotifiName.NotifiClick, false);
        AddNotification.getInstance().notifiClicked();
        if (isDelete) {
            return;
        }
        if (isClick) {
            String Conversation = intent.getStringExtra(StaticField.NotifiName.Conversation);
            if (Conversation != null) {
                Intent conversationIntent = new Intent(context, ChatActivity.class);
                conversationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(conversationIntent);
            } else {
                Intent MainIntent = new Intent(context, MainActivity.class);
                MainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(MainIntent);
            }
        }
    }
}
