package com.tizi.quanzi.tool;

import android.app.Activity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.tizi.quanzi.log.Log;

/**
 * Created by qixingchen on 15/7/23.
 */
public class GetGMSStatue {
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    public static boolean haveGCM(Activity mactivity) {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(mactivity);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, mactivity,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.w("gcm状态", "This device is not supported.");
            }
            return false;
        }
        return true;
    }
}
