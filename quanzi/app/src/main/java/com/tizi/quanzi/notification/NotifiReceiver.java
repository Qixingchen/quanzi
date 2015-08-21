package com.tizi.quanzi.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.tizi.quanzi.log.Log;

/**
 * Created by qixingchen on 15/8/21.
 */
public class NotifiReceiver extends BroadcastReceiver {
    private static final String TAG = "NotifiReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e(TAG, "收到广播了！");
    }
}
