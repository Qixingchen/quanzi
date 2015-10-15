package com.tizi.quanzi.tool;

import android.app.Activity;

import com.avos.avoscloud.AVAnalytics;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.tizi.quanzi.log.Log;

/**
 * Created by qixingchen on 15/7/23.
 * 获取GMS状态
 */
public class GetGMSStatue {
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    /**
     * 获取是否有GMS
     *
     * @param mactivity Activity
     *
     * @return isHave
     */
    public static boolean haveGMS(Activity mactivity) {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(mactivity);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                //                GooglePlayServicesUtil.getErrorDialog(resultCode, mactivity,
                //                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
                Log.w("GMS状态", "Type Error : " + resultCode);
                AVAnalytics.onEvent(mactivity, "GMStatue", String.valueOf(resultCode));
                AVAnalytics.onEvent(mactivity, "GMStatue-" + String.valueOf(resultCode));
            } else {
                Log.i("GMS状态", "This device is not supported.");
                AVAnalytics.onEvent(mactivity, "GMStatue", "NotSupported");
                AVAnalytics.onEvent(mactivity, "GMStatue-" + "NotSupported");
            }
            return false;
        }
        Log.i("GMS状态", "This device is supported.");
        AVAnalytics.onEvent(mactivity, "GMStatue", "Supported!");
        AVAnalytics.onEvent(mactivity, "GMStatue-" + "Supported");
        return true;
    }
}
