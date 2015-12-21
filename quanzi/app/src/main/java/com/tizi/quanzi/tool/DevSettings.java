package com.tizi.quanzi.tool;

import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;

import com.tizi.quanzi.app.App;

/**
 * Created by qixingchen on 15/12/21.
 */
public class DevSettings {

    private static SharedPreferences devSettings = PreferenceManager.getDefaultSharedPreferences(App.getApplication());

    public static boolean compatWeixinShare() {
        return isDevMode() &&
                devSettings.getBoolean(StaticField.SystemSettingString.compactWeixinShare, false);
    }

    public static boolean disallowBaiduLocation() {
        return isDevMode() &&
                devSettings.getBoolean(StaticField.SystemSettingString.disallowBaiduLocation, false);
    }

    private static boolean isDevMode() {
        return devSettings.getBoolean(StaticField.SystemSettingString.devMode, false);
    }

}
