package com.tizi.quanzi.app;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.tizi.quanzi.database.DataBaseHelper;
import com.tizi.quanzi.log.Log;
import com.tizi.quanzi.tool.StaticField;

import java.lang.ref.WeakReference;
import java.util.HashMap;

/**
 * Created by qixingchen on 15/9/8.
 * 与应用启动相关的静态值
 */
public class AppStaticValue {

    private static final String TAG = AppStaticValue.class.getSimpleName();
    //当前聊天窗口ID
    public static String UI_CONVERSATION_ID = "";
    //时间差
    public static long timeAddtion = 0;
    private static HashMap<String, WeakReference<Activity>> mActivitys = new HashMap<>();
    private static AVIMClient imClient = null;
    private static Application application;
    //偏好储存
    private static SharedPreferences notifiPreferences;
    private static SharedPreferences preferences;
    private static String UserToken = "";
    private static String UserID = "";
    private static String UserPhone = "";
    //数据库
    private static DataBaseHelper db;
    private static SQLiteDatabase db1;
    private static boolean isImclientOpen = false;

    public AppStaticValue() {
        application = App.getApplication();
        preferences = application.getSharedPreferences(StaticField.Preferences.TOKENFILE,
                App.MODE_PRIVATE);
        UserID = preferences.getString(StaticField.Preferences.USERID, "");
        UserToken = preferences.getString(StaticField.Preferences.USERTOKEN, "");
        UserPhone = preferences.getString(StaticField.Preferences.USERPHONE, "");

        notifiPreferences = application.getSharedPreferences(UserID + "Notifi",
                App.MODE_PRIVATE);

        if (UserID.compareTo("") != 0) {
            setDataBaseHelper(UserID);
        }
    }

    /* 设置数据库*/
    public static void setDataBaseHelper(String userID) {
        db = new DataBaseHelper(App.getApplication(), userID, null, 1);
        db1 = db.getWritableDatabase();
    }

    public static SQLiteDatabase getDatabase() {
        return db1;
    }

    public static String getUserPhone() {
        return UserPhone;
    }

    public static void setUserPhone(String userPhone) {
        preferences.edit().putString(StaticField.Preferences.USERPHONE, userPhone).apply();
        UserPhone = userPhone;
    }

    public static String getUserID() {
        return UserID;
    }

    public static void setUserID(String userID) {
        preferences.edit().putString(StaticField.Preferences.USERID, userID).apply();
        UserID = userID;
    }

    public static String getUserToken() {
        return UserToken;
    }

    public static void setUserToken(String userToken) {
        preferences.edit().putString(StaticField.Preferences.USERTOKEN, userToken).apply();
        UserToken = userToken;
    }

    /*偏好*/
    public static String getStringPrefer(String Name) {
        return preferences.getString(Name, "");
    }

    public static void setStringPrefer(String name, String vaule) {
        preferences.edit().putString(name, vaule).apply();
    }

    public static boolean getBoolenPrefer(String Name) {
        return preferences.getBoolean(Name, false);
    }

    public static void setBoolenPrefer(String name, boolean vaule) {
        preferences.edit().putBoolean(name, vaule).apply();
    }

    public static int getIntPrefer(String Name, int defaultValue) {
        return preferences.getInt(Name, defaultValue);
    }

    public static void setIntPrefer(String name, int vaule) {
        preferences.edit().putInt(name, vaule).apply();
    }

    public static SharedPreferences getNotifiPreferences() {
        return notifiPreferences;
    }


    /**
     * 设置Activity引用
     *
     * @param activity 新增的activity
     */
    public static synchronized void setActivitys(Activity activity) {
        WeakReference<Activity> reference = new WeakReference<Activity>(activity);
        mActivitys.put(activity.getClass().getSimpleName(), reference);
    }

    public static Activity getActivity(String ActivityName) {
        return mActivitys.get(ActivityName).get();
    }

    /**
     * 获取新的AVIMClient 并将旧的关闭
     *
     * @param userID 用户ID
     *
     * @return AVIMClient
     */
    public static AVIMClient getNewImClient(String userID) {
        if (imClient != null) {
            imClient.close(null);
        }
        imClient = AVIMClient.getInstance(userID);

        imClient.open(new AVIMClientCallback() {
            @Override
            public void done(AVIMClient avimClient, AVIMException e) {
                if (null != e) {
                    Log.e(TAG, "AVIMClient链接失败");
                    isImclientOpen = false;
                    e.printStackTrace();
                } else {
                    Log.i(TAG, "AVIMClient链接成功");
                    isImclientOpen = true;
                }
            }
        });
        return imClient;
    }

    public static AVIMClient getImClient() {
        if (imClient == null) {
            imClient = getNewImClient(UserID);
        }
        if (!isImclientOpen) {
            imClient.open(new AVIMClientCallback() {
                @Override
                public void done(AVIMClient avimClient, AVIMException e) {
                    if (null != e) {
                        Log.e(TAG, "AVIMClient链接失败");
                        isImclientOpen = false;
                        e.printStackTrace();
                    } else {
                        Log.i(TAG, "AVIMClient链接成功");
                        isImclientOpen = true;
                    }
                }
            });
        }


        return imClient;
    }

}
