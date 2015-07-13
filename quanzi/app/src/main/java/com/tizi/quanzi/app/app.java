package com.tizi.quanzi.app;

/**
 * Created by qixingchen on 15/7/13.
 */
import android.app.Application;

public class App extends Application {
    private static final String TAG = "App";
    private static Application application;

    private static String UserTken = "";
    //todo 本地储存 UserAccount
    private static String UserID = "";

    public static String getUserID() {
        return UserID;
    }

    public static void setUserID(String userID) {
        UserID = userID;
    }

    public static String getUserTken() {
        return UserTken;
    }

    public static void setUserTken(String userTken) {
        UserTken = userTken;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
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
