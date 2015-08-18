package com.tizi.quanzi.app;

/**
 * Created by qixingchen on 15/7/13.
 */

import android.app.Application;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMMessageManager;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.tizi.quanzi.chat.MutiTypeMsgHandler;
import com.tizi.quanzi.chat.MyAVIMClientEventHandler;
import com.tizi.quanzi.chat.MyAVIMConversationEventHandler;
import com.tizi.quanzi.database.DataBaseHelper;
import com.tizi.quanzi.log.Log;
import com.tizi.quanzi.tool.StaticField;

public class App extends Application {
    private static final String TAG = "App";
    private static Application application;

    private static AVIMClient imClient = null;

    //偏好储存
    private static SharedPreferences preferences;
    private static String UserToken = "";
    //todo 本地储存 UserAccount
    private static String UserID = "";
    private static String UserPhone = "";

    //当前聊天窗口ID
    public static String UI_CONVERSATION_ID = "";


    //数据库
    public static void setDataBaseHelper(String userID) {
        db = new DataBaseHelper(application, userID, null, 1);
        db1 = db.getWritableDatabase();
    }

    private static DataBaseHelper db;

    public static SQLiteDatabase getDatabase() {
        return db1;
    }

    private static SQLiteDatabase db1;

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
        //泄露监视器
        //LeakCanary.install(this);
        preferences = this.getSharedPreferences(StaticField.TokenPreferences.TOKENFILE,
                MODE_PRIVATE);
        UserID = preferences.getString(StaticField.TokenPreferences.USERID, "");
        UserToken = preferences.getString(StaticField.TokenPreferences.USERTOKEN, "");
        UserPhone = preferences.getString(StaticField.TokenPreferences.USERPHONE, "");
        application = this;
        AVOSCloud.initialize(this, "hy5srahijnj9or45ufraqg9delstj8dlz47pj3kfhwjog372",
                "70oc8gv1nlf9nvz0gxokpmb2jyjiuhavdc022isv6zz7nwk2");
        //AVAnalytics.enableCrashReport(this, true);
        AVAnalytics.setAnalyticsEnabled(false);

        AVIMClient.setClientEventHandler(new MyAVIMClientEventHandler());
        AVIMMessageManager.setConversationEventHandler(new MyAVIMConversationEventHandler());

        AVIMMessageManager.registerMessageHandler(AVIMTypedMessage.class, MutiTypeMsgHandler.getInstance());

    }

    public static AVIMClient getImClient() {
        if (imClient == null) {
            imClient = AVIMClient.getInstance(UserID);
            imClient.open(new AVIMClientCallback() {
                @Override
                public void done(AVIMClient avimClient, AVException e) {
                    if (null != e) {
                        Log.e(TAG, "AVIMClient链接失败");
                        e.printStackTrace();
                    } else {
                        Log.w(TAG, "AVIMClient链接成功");
                    }
                }
            });
        }
        return imClient;
    }

    public static AVIMClient getNewImClient(String userID) {
        imClient = AVIMClient.getInstance(userID);
        imClient.open(new AVIMClientCallback() {
            @Override
            public void done(AVIMClient avimClient, AVException e) {
                if (null != e) {
                    Log.e(TAG, "AVIMClient链接失败");
                    e.printStackTrace();
                } else {
                    Log.w(TAG, "AVIMClient链接成功");
                }
            }
        });
        return imClient;
    }

    public static void setImClient(String userID) {
        App.imClient = AVIMClient.getInstance(userID);
    }

    public static void setImClient() {
        App.imClient = AVIMClient.getInstance(UserID);
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
