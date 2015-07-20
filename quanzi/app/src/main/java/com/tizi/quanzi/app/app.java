package com.tizi.quanzi.app;

/**
 * Created by qixingchen on 15/7/13.
 */

import android.app.Application;
import android.content.Intent;

import com.avos.avoscloud.AVCloud;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.tizi.quanzi.ui.MainActivity;

public class App extends Application {
    private static final String TAG = "App";
    private static Application application;

    private static String UserTken = "";
    //todo 本地储存 UserAccount
    private static String UserID = "18059237967";

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
        AVOSCloud.initialize(this, "iz9otzx11p733n25vd54r6uho3rq1f5adfkcva1ttmsoecof",
                "q1r5y5f5mr6dhbdacphcrd9w2vnh8whgta1d91b8d9v39jxz");
        AVIMClient imClient = AVIMClient.getInstance(UserID);
        AVCloud.setProductionMode(false); //调用测试环境云代码
        imClient.open(new AVIMClientCallback() {
            @Override
            public void done(AVIMClient client, AVException e) {
                if (null != e) {
                    // 出错了，可能是网络问题无法连接 LeanCloud 云端，请检查网络之后重试。
                    // 此时聊天服务不可用。
                    e.printStackTrace();
                }
            }
        });
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
