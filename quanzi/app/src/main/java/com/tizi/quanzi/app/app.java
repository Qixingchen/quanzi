package com.tizi.quanzi.app;

/**
 * Created by qixingchen on 15/7/13.
 */

import android.app.Application;
import android.content.SharedPreferences;

import com.avos.avoscloud.AVCloud;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.tizi.quanzi.network.AutoLogin;
import com.tizi.quanzi.tool.StaticField;

public class App extends Application {
    private static final String TAG = "App";
    private static Application application;
    static SharedPreferences preferences;

    AVIMClient imClient;

    private static String UserToken = "";
    //todo 本地储存 UserAccount
    private static String UserID = "";
    private static String UserPhone = "";

    public static String getUserPhone() {
        return UserPhone;
    }

    public static void setUserPhone(String userPhone) {
        preferences.edit().putString(StaticField.TokenPreferences.USERPHONE, userPhone).apply();
        UserPhone = userPhone;
    }

    public static String getUserID() {
        return UserID;
    }

    public static void setUserID(String userID) {
        preferences.edit().putString(StaticField.TokenPreferences.USERID, userID).apply();
        UserID = userID;
    }

    public static String getUserToken() {
        return UserToken;
    }

    public static void setUserToken(String userToken) {
        preferences.edit().putString(StaticField.TokenPreferences.USERTOKEN, userToken).apply();
        UserToken = userToken;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        preferences = this.getSharedPreferences(StaticField.TokenPreferences.TOKENFILE,
                MODE_PRIVATE);
        UserID = preferences.getString(StaticField.TokenPreferences.USERID, "");
        UserToken = preferences.getString(StaticField.TokenPreferences.USERTOKEN, "");
        UserPhone = preferences.getString(StaticField.TokenPreferences.USERPHONE, "");
        application = this;
        AVOSCloud.initialize(this, "iz9otzx11p733n25vd54r6uho3rq1f5adfkcva1ttmsoecof",
                "q1r5y5f5mr6dhbdacphcrd9w2vnh8whgta1d91b8d9v39jxz");
        AVCloud.setProductionMode(false); //调用测试环境云代码
        AutoLogin.getInstance(application).makeOKListener().makeErrorListener().login();
    }

    public AVIMClient getImClient() {
        if (imClient == null) {
            imClient = AVIMClient.getInstance(UserID);
        }
        return imClient;
    }

    public void setImClient(String userID) {
        this.imClient = AVIMClient.getInstance(userID);
    }

    public void setImClient() {
        this.imClient = AVIMClient.getInstance(UserID);
    }

    public static Application getApplication() {
        return application;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }
}
