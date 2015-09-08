package com.tizi.quanzi.app;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.im.v2.AVIMClient;
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

    public AppStaticValue() {
        application = App.getApplication();
        preferences = application.getSharedPreferences(StaticField.TokenPreferences.TOKENFILE,
                App.MODE_PRIVATE);
        UserID = preferences.getString(StaticField.TokenPreferences.USERID, "");
        UserToken = preferences.getString(StaticField.TokenPreferences.USERTOKEN, "");
        UserPhone = preferences.getString(StaticField.TokenPreferences.USERPHONE, "");
        if (UserID.compareTo("") != 0) {
            setDataBaseHelper(UserID);
            // TODO: 15/8/21 fix crash
            //getNewImClient(UserID);
        }
    }

    private static final String TAG = AppStaticValue.class.getSimpleName();
    private static HashMap<String, WeakReference<Activity>> mActivitys = new HashMap<>();
    private static AVIMClient imClient = null;
    private static Application application;

    //偏好储存
    private static SharedPreferences preferences;
    private static String UserToken = "";
    //todo 本地储存 UserAccount
    private static String UserID = "";
    private static String UserPhone = "";

    //当前聊天窗口ID
    public static String UI_CONVERSATION_ID = "";

    //数据库
    private static DataBaseHelper db;
    private static SQLiteDatabase db1;

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

    public static String getPrefer(String Name) {
        return preferences.getString(Name, "");
    }

    public static void setPrefer(String name, String vaule) {
        preferences.edit().putString(name, vaule).apply();
    }

    public static void setUserToken(String userToken) {
        preferences.edit().putString(StaticField.TokenPreferences.USERTOKEN, userToken).apply();
        UserToken = userToken;
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

    public static AVIMClient getImClient() {
        if (imClient == null) {
            imClient = getNewImClient(UserID);
        }
        return imClient;
    }

}
