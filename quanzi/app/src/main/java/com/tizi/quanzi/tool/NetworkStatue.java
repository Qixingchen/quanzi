package com.tizi.quanzi.tool;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.tizi.quanzi.app.App;

/**
 * Created by qixingchen on 15/12/11.
 */
public class NetworkStatue {
    public static boolean isWifi() {
        final ConnectivityManager connMgr = (ConnectivityManager)
                App.getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);

        final android.net.NetworkInfo wifi =
                connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        return wifi.isAvailable() && wifi.getDetailedState() == NetworkInfo.DetailedState.CONNECTED;
    }
}
