package com.tizi.quanzi.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.multidex.MultiDex;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.PushService;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMMessageManager;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.facebook.stetho.Stetho;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.picasso.Picasso;
import com.tizi.quanzi.BuildConfig;
import com.tizi.quanzi.chat.MutiTypeMsgHandler;
import com.tizi.quanzi.chat.MyAVIMClientEventHandler;
import com.tizi.quanzi.chat.MyAVIMConversationEventHandler;
import com.tizi.quanzi.log.Log;
import com.tizi.quanzi.network.LoginAndUserAccount;
import com.tizi.quanzi.ui.login.LoginActivity;


/**
 * Created by qixingchen on 15/7/13.
 * Application
 */
public class App extends Application implements Application.ActivityLifecycleCallbacks {
    private static final String TAG = "App";
    public static AppStaticValue appStaticValue;
    public static boolean isAppForeground;
    private static Application application;

    public static Application getApplication() {
        return application;
    }

    /**
     * 应用初始化
     * 初始化AVIM
     */
    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        appStaticValue = new AppStaticValue();
        //泄露监视器
        if (BuildConfig.DEBUG) {
            LeakCanary.install(this);
        }

        registerActivityLifecycleCallbacks(this);

        //LC 注册
        AVOSCloud.initialize(this, "hy5srahijnj9or45ufraqg9delstj8dlz47pj3kfhwjog372",
                "70oc8gv1nlf9nvz0gxokpmb2jyjiuhavdc022isv6zz7nwk2");

        //LC 分析
        AVAnalytics.setAnalyticsEnabled(true);
        AVAnalytics.enableCrashReport(this, true);


        //LC推送
        AVInstallation.getCurrentInstallation().saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    Log.i(TAG, AVInstallation.getCurrentInstallation().getInstallationId());
                } else {
                    Log.w(TAG, "AVInstallation注册失败" + e.getMessage());
                }
            }
        });
        PushService.subscribe(this, "public", LoginActivity.class);
        PushService.setDefaultPushCallback(this, LoginActivity.class);

        //LC实时通讯
        AVIMClient.setClientEventHandler(MyAVIMClientEventHandler.getInstance());
        AVIMMessageManager.setConversationEventHandler(new MyAVIMConversationEventHandler());
        AVIMMessageManager.registerMessageHandler(AVIMTypedMessage.class, MutiTypeMsgHandler.getInstance());

        //LC调试日志
        //        if (BuildConfig.BUILD_TYPE.equals("debug")) {
        //            AVOSCloud.setDebugLogEnabled(true);
        //        }

        //facebook Stetho
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(
                                Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(
                                Stetho.defaultInspectorModulesProvider(this))
                        .build());

        //picasso
        if (BuildConfig.DEBUG) {
            Picasso.with(getApplicationContext()).setIndicatorsEnabled(true);
        }

        if (!LoginAndUserAccount.getNewInstance().loginFromPrefer()) {
            Intent log_in = new Intent(application, LoginActivity.class);
            log_in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(log_in);
        }

    }

    /**
     * Building Apps with Over 65K Methods
     * todo https://developer.android.com/intl/zh-cn/tools/help/proguard.html
     */
    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }


    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        isAppForeground = true;
    }

    @Override
    public void onActivityPaused(Activity activity) {
        isAppForeground = false;
    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }
}
