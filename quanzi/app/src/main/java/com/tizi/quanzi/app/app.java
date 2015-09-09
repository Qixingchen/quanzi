package com.tizi.quanzi.app;

import android.app.Application;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMMessageManager;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.facebook.stetho.Stetho;
import com.tizi.quanzi.chat.MutiTypeMsgHandler;
import com.tizi.quanzi.chat.MyAVIMClientEventHandler;
import com.tizi.quanzi.chat.MyAVIMConversationEventHandler;


/**
 * Created by qixingchen on 15/7/13.
 * Application
 */
public class App extends Application {
    private static final String TAG = "App";
    private static Application application;
    public static AppStaticValue appStaticValue;


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
        //LeakCanary.install(this);

        AVAnalytics.setAnalyticsEnabled(false);
        AVOSCloud.initialize(this, "hy5srahijnj9or45ufraqg9delstj8dlz47pj3kfhwjog372",
                "70oc8gv1nlf9nvz0gxokpmb2jyjiuhavdc022isv6zz7nwk2");
        //AVAnalytics.enableCrashReport(this, true);


        AVIMClient.setClientEventHandler(new MyAVIMClientEventHandler());
        AVIMMessageManager.setConversationEventHandler(new MyAVIMConversationEventHandler());

        AVIMMessageManager.registerMessageHandler(AVIMTypedMessage.class, MutiTypeMsgHandler.getInstance());

        //facebook Stetho
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(
                                Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(
                                Stetho.defaultInspectorModulesProvider(this))
                        .build());

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
